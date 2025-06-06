<template>
  <div class="payment-page">
    <div class="payment-container" v-loading="loading">
      <h1>订单支付</h1>
      
      <div class="order-info">
        <div class="amount">
          支付金额：<span class="price">¥{{ (orderInfo.totalAmount || 0).toFixed(2) }}</span>
        </div>
        <div class="order-no">
          订单编号：{{ orderInfo.orderNo || '--' }}
        </div>
      </div>

      <div class="payment-extra-actions" style="text-align:center;margin-bottom:20px;">
        <el-button @click="goBack">返回</el-button>
        <el-button type="danger" @click="handleCancelOrder">取消订单</el-button>
      </div>

      <div class="payment-methods">
        <h2>选择支付方式</h2>
        <el-radio-group v-model="paymentMethod">
          <el-radio value="alipay">
            <img src="@/assets/payment/alipay.png" alt="支付宝" class="payment-icon">
            支付宝
          </el-radio>
          <el-radio value="wechat">
            <img src="@/assets/payment/wechat.png" alt="微信支付" class="payment-icon">
            微信支付
          </el-radio>
        </el-radio-group>
      </div>

      <div class="payment-action">
        <el-button type="primary" size="large" @click="pay">
          立即支付
        </el-button>
      </div>

      <div class="countdown" v-if="orderInfo.status === 'unpaid'">
        剩余支付时间：{{ Math.floor(countdown / 60) }}分{{ countdown % 60 }}秒
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getOrderDetail, reqAliPay } from '@/api/order'
import { useOrderStore } from '@/stores/order'

const route = useRoute()
const router = useRouter()
const orderStore = useOrderStore()

const orderInfo = ref({})
const paymentMethod = ref('alipay')
const loading = ref(false)
const countdown = ref(0)
let timer = null

const startCountdown = () => {
  if (!orderInfo.value.expireTime) return
  timer = setInterval(() => {
    const now = Date.now()
    const left = Math.floor((orderInfo.value.expireTime - now) / 1000)
    countdown.value = left > 0 ? left : 0
    if (countdown.value <= 0) {
      clearInterval(timer)
      handleCancelOrder()
    }
  }, 1000)
}

const fetchOrderDetail = async () => {
  const orderId = route.params.orderId
  if (!orderId) {
    ElMessage.error('订单ID不存在')
    router.push('/')
    return
  }
  loading.value = true
  try {
    const res = await getOrderDetail(orderId)
    const order = res.data?.data || res.data
    if (order && order.id) {
      orderInfo.value = order
      if (orderInfo.value.status === 'unpaid') {
        startCountdown()
      }
    } else {
      ElMessage.error('订单不存在')
      router.push('/')
    }
  } catch (error) {
    ElMessage.error('获取订单信息失败')
    router.push('/')
  } finally {
    loading.value = false
  }
}

onMounted(fetchOrderDetail)
onUnmounted(() => {
  if (timer) clearInterval(timer)
})

const pay = async () => {
  if (!orderInfo.value.id) return
  if (paymentMethod.value !== 'alipay') {
    ElMessage.warning('目前仅支持支付宝沙箱支付')
    return
  }
  try {
    const data = { id: orderInfo.value.id }
    const res = await reqAliPay(data)
    console.log('后端返回:', res.data)
    const html = res.data

    const blob = new Blob([html], { type: 'text/html' })
    const url = URL.createObjectURL(blob)
    window.open(url, '_self')
  } catch (e) {
    ElMessage.error('拉起支付宝支付失败')
  }
}

const goBack = () => {
  router.back()
}

const handleCancelOrder = async () => {
  if (!orderInfo.value.id) {
    ElMessage.error('订单信息不完整')
    return
  }
  try {
    orderStore.cancelOrder(orderInfo.value.id)
    ElMessage.success('订单已取消')
    goToUserOrders()
  } catch (e) {
    ElMessage.error('取消订单失败')
  }
}

const goToUserOrders = () => {
  router.push({ path: '/user', query: { tab: 'orders' } })
}
</script>

<style scoped>
.payment-page {
  padding: 40px 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.payment-container {
  max-width: 800px;
  margin: 0 auto;
  background: #fff;
  padding: 32px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: center;
  margin-bottom: 32px;
}

.order-info {
  text-align: center;
  margin-bottom: 40px;
}

.amount {
  font-size: 16px;
  margin-bottom: 16px;
}

.price {
  color: #ff4949;
  font-size: 24px;
  font-weight: bold;
}

.order-no {
  color: #666;
}

.payment-extra-actions {
  margin-bottom: 20px;
}

h2 {
  margin-bottom: 20px;
}

.payment-icon {
  width: 24px;
  height: 24px;
  margin-right: 8px;
  vertical-align: middle;
}

.el-radio {
  display: block;
  height: 40px;
  line-height: 40px;
  margin-bottom: 16px;
}

.payment-action {
  text-align: center;
}
</style>