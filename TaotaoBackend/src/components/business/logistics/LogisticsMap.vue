<template>
  <div class="logistics-map">
    <div id="amap-container" class="map-container"></div>
  </div>
</template>

<script setup>
import { onMounted, watch, ref, onUnmounted } from 'vue'

const props = defineProps({
  from: String, // 发货地地址
  to: String    // 收货地地址
})

const map = ref(null)
const polyline = ref(null)

const initAMap = () => {
  return new Promise((resolve, reject) => {
    console.log('检查高德地图API:', {
      AMap: window.AMap,
      plugins: window.AMap?.plugin
    })
    if (window.AMap) {
      window.AMap.plugin(['AMap.Geocoder', 'AMap.Driving'], () => {
        console.log('AMap.Geocoder:', window.AMap.Geocoder)
        resolve(window.AMap)
      })
    } else {
      reject(new Error('高德地图API未加载'))
    }
  })
}

// 在 props 声明后立即添加监听
watch(() => props.from, (newVal) => {
  console.log('发货地址变化:', newVal)
}, { immediate: true })

watch(() => props.to, (newVal) => {
  console.log('收货地址变化:', newVal)
}, { immediate: true })

// 添加地址格式化函数
const formatAddress = (address) => {
  if (!address) return ''
  // 移除多余空格，确保地址连续
  return address.replace(/\s+/g, '')
}

const renderRoute = async () => {
  try {
    // 在最开始就打印详细的调试信息
    console.log('renderRoute 开始执行，参数：', {
      from: props.from,
      to: props.to,
      mapContainer: document.getElementById('amap-container'),
      AMapLoaded: !!window.AMap
    })

    if (!props.from || !props.to) {
      console.error('地址参数无效:', { from: props.from, to: props.to })
      return
    }

    await initAMap()
    
    // 确保清除之前的地图实例
    if (map.value) {
      map.value.destroy()
    }
    
    // 创建新的地图实例
    map.value = new window.AMap.Map('amap-container', {
      zoom: 6,
      center: [116.397428, 39.90923],
      viewMode: '2D'
    })

    // 修改 driving 实例的创建位置和配置
    const driving = new window.AMap.Driving({
      map: map.value,           // 将地图实例传入
      panel: false,             // 不显示路线详情面板
      showTraffic: true,        // 显示路况信息
      hideMarkers: false,       // 显示起终点图标
      policy: window.AMap.DrivingPolicy.LEAST_TIME  // 最快路线
    })

    // 使用Promise处理异步操作
    const getLocation = (address) => {
      const formattedAddress = formatAddress(address)
      console.log('格式化后的地址:', formattedAddress)
      
      return new Promise((resolve, reject) => {
        const geocoder = new window.AMap.Geocoder({ city: "全国" })
        if (!geocoder) {
          console.error('Geocoder 实例创建失败')
          reject(new Error('Geocoder 实例创建失败'))
          return
        }
        let called = false
        const timer = setTimeout(() => {
          if (!called) {
            called = true
            reject(new Error('Geocoder 超时无响应'))
          }
        }, 5000)
        geocoder.getLocation(formattedAddress, (status, result) => {
          if (called) return
          called = true
          clearTimeout(timer)
          console.log('地址解析回调:', { address: formattedAddress, status, result })
          if (status === 'complete' && result.geocodes.length) {
            const location = result.geocodes[0].location
            console.log('解析到的坐标:', location)
            resolve(location)
          } else {
            console.error('地址解析失败:', { address: formattedAddress, status, result })
            reject(new Error(`地址解析失败：${formattedAddress}`))
          }
        })
      })
    }

    // 规划路线
    const [fromLngLat, toLngLat] = await Promise.all([
      getLocation(props.from),
      getLocation(props.to)
    ])

    // 使用经纬度数组格式进行路线规划
    driving.search(
      [fromLngLat.lng, fromLngLat.lat],
      [toLngLat.lng, toLngLat.lat],
      (status, result) => {
        console.log('路线规划详细结果:', { status, result, fromLngLat, toLngLat })
        
        if (status === 'complete' && result.routes && result.routes.length) {
          const path = result.routes[0].steps.reduce((arr, step) => {
            return arr.concat(step.path)
          }, [])

          // 创建新的路线
          polyline.value = new window.AMap.Polyline({
            path: path,
            strokeColor: '#ff69b4',  // 粉色路线
            strokeWeight: 6,         // 线条宽度
            strokeOpacity: 0.8,      // 透明度
            strokeStyle: 'solid',    // 实线
            zIndex: 100             // 确保路线在最上层
          })

          // 将路线添加到地图
          map.value.add(polyline.value)
          
          // 调整视野以包含所有点
          map.value.setFitView([polyline.value])
        } else {
          alert('路线规划失败，status: ' + status + '，message: ' + (result && result.info))
          console.error('路线规划失败:', { status, result })
        }
      }
    )
  } catch (error) {
    console.error('路线规划详细错误:', error)
  }
}

onMounted(() => {
  if (props.from && props.to) {
    renderRoute()
  }
})

watch([() => props.from, () => props.to], () => {
  if (props.from && props.to) {
    renderRoute()
  }
})

onUnmounted(() => {
  if (map.value) {
    map.value.destroy()
  }
})
</script>

<style scoped>
.logistics-map {
  width: 100%;
  height: 400px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(255, 192, 203, 0.15);
  margin-bottom: 32px;
  background: #fff0f6;
}

.map-container {
  width: 100%;
  height: 100%;
}
</style>
