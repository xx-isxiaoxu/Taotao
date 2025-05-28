<template>
  <div class="user-orders">
    <h2>我的订单</h2>
    <el-tabs v-model="activeTab" @tab-click="loadOrders">
      <el-tab-pane label="全部订单" name="all" />
      <el-tab-pane label="待付款" name="unpaid" />
      <el-tab-pane label="待发货" name="unshipped" />
      <el-tab-pane label="已完成" name="completed" />
      <el-tab-pane label="已取消" name="cancelled" />
    </el-tabs>
    <el-table :data="filteredOrders" style="width: 100%">
      <el-table-column prop="orderNo" label="订单编号" />
      <el-table-column prop="totalAmount" label="金额" />
      <el-table-column prop="status" label="状态" :formatter="formatStatus" />
      <el-table-column prop="createTime" label="下单时间" :formatter="formatDate" />
      <el-table-column label="剩余支付时间">
        <template #default="{ row }">
          <span v-if="row.status === 'unpaid' && row.expireTime">
            <span v-if="row._countdown > 0">
              {{ Math.floor(row._countdown / 60) }}分{{ row._countdown % 60 }}秒
            </span>
            <span v-else>已超时</span>
          </span>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button type="primary" @click="viewDetail(row)">详情</el-button>
          <el-button v-if="row.status === 'unpaid'" @click="pay(row)">去支付</el-button>
          <el-button type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next"
      @size-change="loadOrders"
      @current-change="loadOrders"
    />
    <el-dialog v-model="detailDialogVisible" title="订单详情" width="700px" :close-on-click-modal="false">
      <div v-if="currentOrder">
        <div style="margin-bottom: 16px;">
          <b>订单编号：</b>{{ currentOrder.orderNo }}<br>
          <b>下单时间：</b>{{ formatDate(currentOrder.createTime) }}<br>
          <b>收货人：</b>{{ currentOrder.receiver }}<br>
          <b>收货地址：</b>{{ currentOrder.address }}<br>
          <b>联系电话：</b>{{ currentOrder.phone }}
        </div>
        <el-table :data="currentOrder.orderItemList" style="width: 100%">
          <el-table-column label="图片" width="80">
            <template #default="{ row }">
              <img :src="row.goodsImage" alt="商品图片" style="width:60px;height:60px;object-fit:cover;" />
            </template>
          </el-table-column>
          <el-table-column prop="goodsName" label="商品名称" />
          <el-table-column prop="specs" label="规格">
            <template #default="{ row }">
              <span>{{ formatSpecs(row.specs) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="goodsPrice" label="单价" />
          <el-table-column prop="quantity" label="数量" />
          <el-table-column label="小计">
            <template #default="{ row }">
              ¥{{ (row.goodsPrice * row.quantity).toFixed(2) }}
            </template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { getOrderList, getOrderDetail } from '@/api/order'
import { useRouter } from 'vue-router'
import { deleteOrder } from '@/api/order'
import { ElMessageBox, ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const timer = ref(null)
const detailDialogVisible = ref(false)
const currentOrder = ref(null)
const activeTab = ref('all')

const statusMap = {
  unpaid: '待付款',
  paid: '已支付',
  cancelled: '已取消',
  unshipped: '待发货',
  completed: '已完成'
}

const formatStatus = (row, column, cellValue) => {
  return statusMap[cellValue] || cellValue
}

const formatDate = (row, column, cellValue) => {
  let value = cellValue
  if (typeof row === 'string' && !column && !cellValue) value = row
  if (!value) return '--'
  let dateStr = value.replace(' ', 'T').replace(/\.\d+$/, '')
  let date = new Date(dateStr)
  if (isNaN(date.getTime())) return value
  return date.toLocaleString()
}

const formatSpecs = (specs) => {
  if (!specs) return ''
  let obj = specs
  if (typeof specs === 'string') {
    try {
      obj = JSON.parse(specs)
    } catch {
      return specs
    }
  }
  if (typeof obj === 'object') {
    return Object.entries(obj).map(([k, v]) => `${k}: ${v}`).join('，')
  }
  return obj
}

const loadOrders = async () => {
  const res = await getOrderList(userStore.userInfo.id, currentPage.value, pageSize.value)
  // 兼容后端返回格式
  const data = res.data?.data || res.data
  orders.value = data.list || []
  total.value = data.total || 0
}

const viewDetail = async (row) => {
  const res = await getOrderDetail(row.id)
  currentOrder.value = res.data?.data || res.data
  detailDialogVisible.value = true
}

const pay = (row) => {
  router.push(`/order/payment/${row.id}`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该订单吗？', '提示', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteOrder(row.id)
    ElMessage.success('删除成功')
    loadOrders()
  }).catch(() => {})
}

const updateCountdowns = () => {
  const now = Date.now()
  orders.value.forEach(order => {
    if (order.status === 'unpaid' && order.expireTime) {
      order._countdown = Math.floor((order.expireTime - now) / 1000)
    }
  })
}

const filteredOrders = computed(() => {
  if (activeTab.value === 'all') return orders.value
  return orders.value.filter(order => order.status === activeTab.value)
})

const getStatusText = (status) => {
  return statusMap[status] || status
}

onMounted(() => {
  loadOrders().then(() => {
    updateCountdowns()
    timer.value = setInterval(updateCountdowns, 1000)
  })
})

onUnmounted(() => {
  if (timer.value) clearInterval(timer.value)
})
</script>

<style scoped>
.el-dialog__body {
  background: #f8f9fa;
}
.el-table img {
  border-radius: 8px;
  border: 1px solid #eee;
}
.el-table .el-table__cell {
  vertical-align: middle;
}
.order-items {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}
.order-item {
  width: calc(33.33% - 16px);
  padding: 16px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
.order-status {
  font-weight: bold;
  margin-bottom: 16px;
}
</style>