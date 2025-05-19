<template>
  <div class="statistics-container">
    <el-row :gutter="24" class="chart-row">
      <!-- 热销商品排行 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>热销商品排行</span>
            </div>
          </template>
          <div ref="hotSalesChart" class="chart"></div>
        </el-card>
      </el-col>
      
      <!-- 商品类别占比 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>商品类别占比</span>
            </div>
          </template>
          <div ref="categoryChart" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 平均订单金额趋势 -->
    <el-row style="margin-top: 20px">
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>平均订单金额趋势</span>
              <el-radio-group v-model="timeRange" size="small">
                <el-radio-button :value="'week'">近一周</el-radio-button>
                <el-radio-button label="month">近一月</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="orderTrendChart" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick, onBeforeUnmount, computed } from 'vue'
import * as echarts from 'echarts'
import { useProductStore } from '@/stores/product'
import { useOrderStore } from '@/stores/order'

const hotSalesChart = ref(null)
const categoryChart = ref(null)
const orderTrendChart = ref(null)
const timeRange = ref('week')

let hotSalesInstance = null
let categoryInstance = null
let orderTrendInstance = null

const productStore = useProductStore()
const orderStore = useOrderStore()

// 获取商品点击数据
const getProductClicks = () => {
  const clicks = JSON.parse(localStorage.getItem('hotProductClicks') || '{}')
  const products = productStore.productList
  return Object.entries(clicks)
    .map(([id, count]) => ({
      id: Number(id),
      count,
      name: products.find(p => p.id === Number(id))?.name || `商品${id}`
    }))
    .sort((a, b) => b.count - a.count)
    .slice(0, 5)
}

// 获取商品类别统计
const getCategoryStats = () => {
  const products = productStore.productList
  const stats = products.reduce((acc, product) => {
    acc[product.category] = (acc[product.category] || 0) + 1
    return acc
  }, {})
  return Object.entries(stats).map(([category, count]) => ({
    name: category,
    value: count
  }))
}

// 获取订单金额趋势
const getOrderTrend = (type = 'week') => {
  const now = new Date()
  const days = type === 'week' ? 7 : 30
  const dates = Array.from({ length: days }, (_, i) => {
    const date = new Date(now)
    date.setDate(date.getDate() - i)
    return date.toISOString().split('T')[0]
  }).reverse()

  const paidOrders = orderStore.orders.filter(order => 
    order.status !== 'unpaid' && order.status !== 'cancelled'
  )

  const dailyAmounts = dates.map(date => {
    const dayOrders = paidOrders.filter(order => 
      order.payTime?.split('T')[0] === date
    )
    if (dayOrders.length === 0) return 0
    const total = dayOrders.reduce((sum, order) => sum + Number(order.amount), 0)
    return Math.round(total / dayOrders.length)
  })

  return {
    dates: type === 'week' 
      ? ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      : dates.map(d => d.slice(5)), // 只显示月-日
    amounts: dailyAmounts
  }
}

// 初始化热销商品图表
const initHotSalesChart = () => {
  if (!hotSalesChart.value) return
  if (hotSalesInstance) hotSalesInstance.dispose()
  
  const hotProducts = getProductClicks()
  hotSalesInstance = echarts.init(hotSalesChart.value)
  hotSalesInstance.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    grid: {
      left: 60,
      right: 30,
      top: 30,
      bottom: 30,
      containLabel: true
    },
    xAxis: {
      type: 'value',
      axisLabel: { fontSize: 14 }
    },
    yAxis: {
      type: 'category',
      data: hotProducts.map(p => p.name),
      axisLabel: { fontSize: 14 }
    },
    series: [{
      name: '点击次数',
      type: 'bar',
      data: hotProducts.map(p => p.count),
      itemStyle: { color: '#ff69b4' }
    }]
  })
}

// 初始化商品类别占比图表
const initCategoryChart = () => {
  if (!categoryChart.value) return
  if (categoryInstance) categoryInstance.dispose()
  
  const categoryStats = getCategoryStats()
  categoryInstance = echarts.init(categoryChart.value)
  categoryInstance.setOption({
    tooltip: { trigger: 'item' },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
      itemWidth: 18,
      itemHeight: 14,
      textStyle: { fontSize: 14 }
    },
    series: [{
      name: '商品类别',
      type: 'pie',
      radius: '60%',
      center: ['40%', '50%'],
      data: categoryStats,
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      },
      itemStyle: {
        color: function(params) {
          const colors = ['#ff69b4', '#ff9a9e', '#fad0c4', '#ffd1ff', '#ffc3a0']
          return colors[params.dataIndex % colors.length]
        }
      }
    }]
  })
}

// 初始化订单金额趋势图表
const initOrderTrendChart = () => {
  if (!orderTrendChart.value) return
  if (orderTrendInstance) orderTrendInstance.dispose()
  
  const trend = getOrderTrend(timeRange.value)
  orderTrendInstance = echarts.init(orderTrendChart.value)
  orderTrendInstance.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: trend.dates
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value} 元'
      }
    },
    series: [{
      name: '平均订单金额',
      type: 'line',
      smooth: true,
      data: trend.amounts,
      itemStyle: { color: '#ff69b4' },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(255, 105, 180, 0.3)' },
          { offset: 1, color: 'rgba(255, 105, 180, 0.1)' }
        ])
      }
    }]
  })
}

const resizeCharts = () => {
  hotSalesInstance && hotSalesInstance.resize()
  categoryInstance && categoryInstance.resize()
  orderTrendInstance && orderTrendInstance.resize()
}

// 监听时间范围变化
watch(timeRange, (newValue) => {
  // 这里可以根据选择的时间范围重新获取数据
  initOrderTrendChart()
})

onMounted(() => {
  nextTick(() => {
    initHotSalesChart()
    initCategoryChart()
    initOrderTrendChart()
    window.addEventListener('resize', resizeCharts)
  })
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeCharts)
  hotSalesInstance && hotSalesInstance.dispose()
  categoryInstance && categoryInstance.dispose()
  orderTrendInstance && orderTrendInstance.dispose()
})
</script>

<style scoped>
.statistics-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.chart-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(255, 192, 203, 0.1);
  padding: 24px 16px;
  min-height: 420px;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #333;
}

.chart {
  height: 340px;
  min-width: 400px;
  width: 100%;
  margin: 0 auto;
  /* 保证居中 */
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-radio-group {
  margin-left: 20px;
}
</style>