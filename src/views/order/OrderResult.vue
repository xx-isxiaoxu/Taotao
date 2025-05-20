<template>
  <div class="result-page">
    <div class="result-container">
      <el-result
        icon="success"
        title="支付成功"
        sub-title="感谢您的购买！"
      >
        <template #extra>
          <div class="order-info" v-if="orderInfo">
            <p>订单号：{{ orderInfo.orderNo || '--' }}</p>
            <p>支付金额：¥{{ (orderInfo.amount || 0).toFixed(2) }}</p>
            <p>支付时间：{{ formatDate(orderInfo.payTime) }}</p>
          </div>
          <el-button type="primary" @click="viewOrder">查看订单</el-button>
          <el-button @click="goHome">返回首页</el-button>
        </template>
      </el-result>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useOrderStore } from '@/stores/order'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const orderStore = useOrderStore()
const orderInfo = ref({
  amount: 0,
  orderNo: '',
  payTime: new Date(),
  id: null
})

onMounted(async () => {
  const orderId = route.params.orderId
  if (!orderId) {
    ElMessage.error('订单ID不存在')
    router.push('/')
    return
  }

  try {
    const order = await orderStore.getOrderDetail(orderId)
    if (order) {
      orderInfo.value = order
    } else {
      ElMessage.error('订单不存在')
      router.push('/')
    }
  } catch (error) {
    console.error('获取订单信息失败:', error)
    ElMessage.error('获取订单信息失败')
    router.push('/')
  }
})

const formatDate = (date) => {
  if (!date) return '--'
  return new Date(date).toLocaleString()
}

const viewOrder = () => {
  router.push('/user/orders')
}

const goHome = () => {
  router.push('/')
}
</script>

<style scoped>
.result-page {
  padding: 40px 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.result-container {
  max-width: 600px;
  margin: 0 auto;
  background: #fff;
  padding: 32px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.order-info {
  text-align: center;
  margin: 20px 0;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 4px;
}

.order-info p {
  margin: 8px 0;
  color: #666;
}
</style>