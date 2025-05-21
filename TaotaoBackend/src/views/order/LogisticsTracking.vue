<template>
  <div class="logistics-tracking-page">
    <div class="logistics-header">
      <el-button type="primary" @click="goBack" class="back-btn">返回</el-button>
      <h2>物流跟踪</h2>
      <div class="address-info">
        <span>发货地：</span><span class="address">{{ info?.from }}</span>
        <span style="margin: 0 12px;">→</span>
        <span>收货地：</span><span class="address">{{ info?.to }}</span>
      </div>
      <div class="order-status">
        <span class="status-label">当前状态：</span>
        <span class="status-value" :class="statusClass">{{ info?.status || '--' }}</span>
        <span class="current-node" v-if="info?.current">（{{ info.current }}）</span>
      </div>
    </div>
    
    <!-- 添加加载状态 -->
    <el-skeleton :loading="loading" animated :rows="10" v-if="!info">
      <template #template>
        <div style="padding: 14px">
          <el-skeleton-item variant="h3" style="width: 50%" />
          <el-skeleton-item variant="text" style="margin-top: 16px; width: 80%" />
          <el-skeleton-item variant="text" style="margin-top: 16px; width: 60%" />
        </div>
      </template>
    </el-skeleton>

    <!-- 添加错误提示 -->
    <el-alert
      v-if="error"
      :title="error"
      type="error"
      :closable="false"
      show-icon
      style="margin-bottom: 20px"
    />

    <logistics-map v-if="info" :from="info.from" :to="info.to" />
    <logistics-tracker v-if="info" :steps="info.steps" />
  </div>
</template>

<script setup>
import { onMounted, computed, ref, watch } from 'vue'  // 添加 watch 导入
import { useRoute, useRouter } from 'vue-router'
import { useLogisticsStore } from '@/stores/logistics'
import LogisticsMap from '@/components/business/logistics/LogisticsMap.vue'
import LogisticsTracker from '@/components/business/logistics/LogisticsTracker.vue'

const route = useRoute()
const router = useRouter()
const logisticsStore = useLogisticsStore()

const loading = ref(true)
const error = ref('')

onMounted(async () => {
  try {
    loading.value = true
    const toAddress = route.query.to
    const orderId = route.query.orderId
    
    // 添加路由参数检查
    console.log('路由参数:', {
      orderId,
      toAddress
    })
    
    if (!orderId) {
      error.value = '订单ID不能为空'
      return
    }
    
    await logisticsStore.fetchLogistics(orderId, toAddress)
  } catch (err) {
    error.value = '获取物流信息失败：' + err.message
  } finally {
    loading.value = false
  }
})

const info = computed(() => logisticsStore.logisticsInfo)

// 添加 info 监听
watch(() => info.value, (newVal) => {
  console.log('物流信息更新:', newVal)
}, { immediate: true })

const statusClass = computed(() => {
  if (!info.value) return ''
  if (info.value.status === '运输中') return 'status-in-transit'
  if (info.value.status === '已签收') return 'status-finished'
  return ''
})

const goBack = () => {
  window.history.length > 1 ? router.back() : router.push('/user?tab=orders')
}
</script>

<style scoped>
.logistics-tracking-page {
  max-width: 900px;
  margin: 40px auto;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 4px 32px rgba(255, 192, 203, 0.10);
  padding: 32px 24px 24px 24px;
  min-height: 600px;
}
.logistics-header {
  margin-bottom: 32px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
h2 {
  color: #ff69b4;
  font-size: 28px;
  margin-bottom: 12px;
}
.order-status {
  font-size: 18px;
  color: #333;
}
.status-label {
  color: #ff69b4;
  font-weight: bold;
}
.status-value.status-in-transit {
  color: #ff69b4;
  font-weight: bold;
}
.status-value.status-finished {
  color: #43a047;
  font-weight: bold;
}
.current-node {
  color: #9e8cb9;
  margin-left: 8px;
}
.back-btn {
  margin-bottom: 16px;
  background: #ff69b4;
  border-color: #ff69b4;
}
.address-info {
  margin-bottom: 12px;
  font-size: 16px;
  color: #9e8cb9;
}
.address {
  font-weight: bold;
  color: #ff69b4;
}

/* 添加新样式 */
.el-skeleton {
  padding: 20px;
  background: #fff;
  border-radius: 16px;
  margin-bottom: 32px;
}

.el-alert {
  border-radius: 8px;
}
</style>
