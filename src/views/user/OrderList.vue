<template>
  <div class="order-list">
    <h2>我的订单</h2>
    
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="全部订单" name="all" />
      <el-tab-pane label="待付款" name="unpaid" />
      <el-tab-pane label="待发货" name="unshipped" />
      <el-tab-pane label="已完成" name="completed" />
      <el-tab-pane label="已取消" name="cancelled" />
    </el-tabs>

    <div class="order-items">
      <el-card v-for="order in filteredOrders" :key="order.id" class="order-item">
        <div class="order-header">
          <span class="order-no">订单号：{{ order.orderNo }}</span>
          <span class="order-status">{{ getStatusText(order.status) }}</span>
        </div>
        
        <div class="order-content">
          <div class="product-info">
            <img :src="order.goodsImage || '/default.png'" :alt="order.productName" class="product-image">
            <div class="product-details">
              <h3>{{ order.productName }}</h3>
              <p class="specs">{{ order.specs }}</p>
              <p class="quantity">数量：{{ order.quantity }}</p>
            </div>
          </div>
          
          <div class="order-amount">
            <p class="price">¥{{ order.amount.toFixed(2) }}</p>
          </div>
        </div>

        <div class="order-footer">
          <span class="order-time">下单时间：{{ formatDate(order.createTime) }}</span>
          <div class="order-actions">
            <el-button 
              v-if="order.status === 'unpaid'" 
              type="primary" 
              size="small"
              @click="handlePay(order)"
            >
              立即付款
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getOrderList } from '@/api/order'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()
const activeTab = ref('all')
const orders = ref([])

const statusMap = {
  unpaid: '待付款',
  paid: '已支付',
  cancelled: '已取消',
  unshipped: '待发货',
  completed: '已完成'
}

const formatStatus = (status) => statusMap[status] || status

const formatDate = (date) => date ? new Date(date).toLocaleString() : '--'

const filteredOrders = computed(() => {
  if (activeTab.value === 'all') return orders.value
  return orders.value.filter(order => order.status === activeTab.value)
})

const loadOrders = async () => {
  const res = await getOrderList(userStore.userInfo.id)
  const data = res.data?.data || res.data
  orders.value = data.list || []
}

const handleTabClick = () => {
  // 这里只需重新过滤，不需要重新请求
}

const handlePay = (order) => {
  router.push(`/order/payment/${order.id}`)
}

onMounted(loadOrders)
</script>

<style scoped>
.order-list {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.order-item {
  margin-bottom: 20px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.order-status {
  color: #ff6b6b;
}

.order-content {
  display: flex;
  padding: 20px 0;
}

.product-info {
  display: flex;
  flex: 1;
}

.product-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  margin-right: 20px;
}

.product-details {
  flex: 1;
}

.product-details h3 {
  margin: 0 0 10px;
  font-size: 16px;
}

.specs {
  color: #666;
  margin: 5px 0;
}

.order-amount {
  text-align: right;
  min-width: 100px;
}

.price {
  color: #ff6b6b;
  font-size: 18px;
  font-weight: bold;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #eee;
}

.order-time {
  color: #999;
}
</style>