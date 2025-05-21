<template>
  <div class="user-orders">
    <el-tabs v-model="activeStatus" @tab-click="handleStatusChange">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="未支付" name="unpaid">
        <template #label>
          <el-badge :value="unpaidCount" :max="99" v-if="unpaidCount > 0" style="margin-right: 4px;">
            <span>未支付</span>
          </el-badge>
          <span v-else>未支付</span>
        </template>
      </el-tab-pane>
      <el-tab-pane label="待发货" name="unshipped" />
      <el-tab-pane label="待收货" name="shipped" />
      <el-tab-pane label="已完成" name="completed" />
    </el-tabs>

    <el-table :data="orderList" style="width: 100%">
      <el-table-column label="商品图片" width="100">
        <template #default="{ row }">
          <img :src="row.image" alt="商品图片" style="width:60px;height:60px;object-fit:cover;" v-if="row.image"/>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" width="180" />
      <el-table-column prop="specs" label="规格" width="180">
        <template #default="{ row }">
          <span v-if="row.specs">{{ Object.entries(row.specs).map(([k, v]) => `${k}:${v}`).join('，') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="订单金额" width="120">
        <template #default="{ row }">
          <span v-if="row && row.amount !== undefined">¥{{ Number(row.amount).toFixed(2) }}</span>
          <span v-else>¥0.00</span>
        </template>
      </el-table-column>
      <el-table-column prop="quantity" label="数量" width="80" />
      <el-table-column prop="status" label="订单状态" width="120">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="剩余支付时间" width="140">
        <template #default="{ row }">
          <span v-if="row.status === 'unpaid'">
            <span v-if="countdownMap[row.id] > 0">
              {{ Math.floor(countdownMap[row.id] / 60) }}分{{ countdownMap[row.id] % 60 }}秒
            </span>
            <span v-else>已超时</span>
          </span>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="320">
        <template #default="{ row }">
          <div class="order-action-btns">
            <el-button type="primary" @click="showDetail(row)">查看详情</el-button>
            <el-button 
              v-if="row.status === 'unpaid'" 
              type="success" 
              @click="handlePay(row)"
            >
              去支付
            </el-button>
            <el-button 
              type="danger" 
              @click="handleDelete(row)"
            >
              删除
            </el-button>
            <el-button
              v-if="row.status === 'shipped'"
              type="info"
              @click="handleLogistics(row)"
            >
              查看物流
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog v-model="detailDialogVisible" title="订单详情" width="500px">
      <div v-if="currentOrder">
        <p><b>订单编号：</b>{{ currentOrder.orderNo }}</p>
        <p><b>下单时间：</b>{{ formatDate(currentOrder.createTime) }}</p>
        <p><b>收货人：</b>{{ getReceiverName(currentOrder) }}</p>
        <p><b>联系电话：</b>{{ getReceiverPhone(currentOrder) }}</p>
        <p><b>收货地址：</b>{{ getReceiverAddress(currentOrder) }}</p>
        <el-divider />
        <p><b>商品名称：</b>{{ currentOrder.name }}</p>
        <p><b>规格：</b>
          <span v-if="currentOrder.specs">{{ Object.entries(currentOrder.specs).map(([k, v]) => `${k}:${v}`).join('，') }}</span>
        </p>
        <p><b>数量：</b>{{ currentOrder.quantity }}</p>
        <p><b>订单金额：</b>¥{{ Number(currentOrder.amount).toFixed(2) }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useOrderStore } from '@/stores/order'
import { useUserStore } from '@/stores/user'
import { ElMessageBox, ElMessage } from 'element-plus'
import { codeToText } from 'element-china-area-data'

const router = useRouter()
const activeStatus = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const orderStore = useOrderStore()
const userStore = useUserStore()
const orderList = computed(() => {
  if (activeStatus.value === 'all') {
    return orderStore.orders
  }
  if (activeStatus.value === 'unpaid') {
    // 显示未支付和已取消订单
    return orderStore.orders.filter(order => order.status === 'unpaid' || order.status === 'cancelled')
  }
  // 其它tab只显示对应状态
  return orderStore.orders.filter(order => order.status === activeStatus.value)
})

const detailDialogVisible = ref(false)
const currentOrder = ref(null)

const countdownMap = ref({}) // { [orderId]: 剩余秒数 }

let timer = null
const updateCountdowns = () => {
  const now = Date.now()
  orderStore.orders.forEach(order => {
    if (order.status === 'unpaid') {
      const left = Math.floor((order.expireTime - now) / 1000)
      countdownMap.value[order.id] = left > 0 ? left : 0
      if (left <= 0) {
        orderStore.cancelOrder(order.id)
      }
    }
  })
}

onMounted(() => {
  updateCountdowns()
  timer = setInterval(updateCountdowns, 1000)
  orderStore.orders.forEach((order, idx) => {
    if (order.status === 'paid') {
      // 偶数设为待发货，奇数设为已发货
      order.status = idx % 2 === 0 ? 'unshipped' : 'shipped'
    }
  })
})
onUnmounted(() => {
  if (timer) clearInterval(timer)
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString()
}

const getStatusType = (status) => {
  const statusMap = {
    unpaid: 'warning',
    unshipped: 'info',
    shipped: 'primary',
    completed: 'success'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    unpaid: '待付款',
    unshipped: '待发货',
    shipped: '待收货',
    completed: '已完成'
  }
  return statusMap[status] || status
}

const handleStatusChange = () => {
  // TODO: 实现状态切换逻辑
  loadOrders()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadOrders()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadOrders()
}

const showDetail = (row) => {
  currentOrder.value = row
  detailDialogVisible.value = true
}

const handlePay = (row) => {
  router.push(`/order/payment/${row.id}`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定要删除该订单吗？删除后无法恢复！',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    orderStore.removeOrder(row.id)
    ElMessage.success('订单已删除')
  }).catch(() => {})
}

const handleLogistics = (row) => {
  // 获取收货地址中文
  const address = userStore.userInfo?.addressList?.find(a => a.id == row.addressId)
  if (!address) {
    ElMessage.error('找不到该订单的收货地址信息')
    return
  }
  const regionText = address.region?.map(code => codeToText[code]).join(' ') || ''
  const fullAddress = `${regionText} ${address.address || ''}`
  router.push({ 
    path: '/order/logistics-tracking', 
    query: { orderId: row.id, to: fullAddress }
  })
}

// 获取收货人信息（假设userInfo.addressList里有id和name/phone/address）
const getReceiverName = (order) => {
  const address = userStore.userInfo?.addressList?.find(a => a.id == order.addressId)
  return address ? address.name : ''
}
const getReceiverPhone = (order) => {
  const address = userStore.userInfo?.addressList?.find(a => a.id == order.addressId)
  return address ? address.phone : ''
}
const getReceiverAddress = (order) => {
  const address = userStore.userInfo?.addressList?.find(a => a.id == order.addressId)
  if (!address) return ''
  // region转中文
  const regionText = address.region.map(code => codeToText[code]).join(' ')
  return `${regionText} ${address.address}`
}

const loadOrders = () => {
  // TODO: 实现订单加载逻辑
}

const unpaidCount = computed(() => orderStore.orders.filter(order => order.status === 'unpaid').length)
</script>

<style scoped>
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.order-action-btns {
  display: flex;
  flex-wrap: wrap;
  gap: 8px; /* 按钮间距 */
  align-items: center;
  justify-content: flex-start;
}
</style>