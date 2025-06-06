<template>
  <div class="order-confirm">
    <!-- 顶部导航栏-->
    <div class="detail-nav">
      <div class="nav-content">
        <div class="nav-left">
          <div class="logo">
            <img src="/logo1.png" alt="淘淘商城">
          </div>
        </div>
        <div class="nav-right">
          <router-link to="/" class="nav-item">
            <el-icon><HomeFilled /></el-icon>
            首页
          </router-link>
          <router-link to="/cart" class="nav-item">
            <el-badge :value="cartCount" class="cart-badge">
              <el-icon><ShoppingCart /></el-icon>
              购物车
            </el-badge>
          </router-link>
          <router-link to="/user" class="nav-item">
            <el-icon><User /></el-icon>
            个人中心
          </router-link>
        </div>
      </div>
    </div>
    <div class="confirm-container">
      <h2>确认订单</h2>
      <!-- 收货地址选择 -->
      <div class="address-section">
        <h2>收货地址</h2>
        <el-radio-group v-model="selectedAddressId">
          <el-radio
            v-for="address in addressList"
            :key="address.id"
            :value="address.id"
          >
            {{ address.receiver }} {{ address.phone }}
            {{ codeToText[address.province] || address.province }}
            {{ codeToText[address.city] || address.city }}
            {{ codeToText[address.district] || address.district }}
            {{ address.detail }}
          </el-radio>
        </el-radio-group>
      </div>

      <!-- 商品清单 -->
      <div class="product-list">
        <h2>商品清单</h2>
        <el-table :data="products" style="width: 100%; margin: 20px 0;">
          <el-table-column label="图片" width="80">
            <template #default="{ row }">
              <img :src="row.image" alt="商品图片" style="width:60px;height:60px;object-fit:cover;" />
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

      <!-- 优惠券选择 -->
      <div class="coupon-section">
        <h2>优惠券</h2>
        <el-select v-model="selectedCoupon" placeholder="请选择优惠券">
          <el-option
            v-for="coupon in availableCoupons"
            :key="coupon.id"
            :label="coupon.name"
            :value="coupon.id"
          />
        </el-select>
      </div>

      <!-- 订单金额 -->
      <div class="order-amount">
        <div class="amount-item">
          <span>商品总额：</span>
          <span>¥{{ totalAmount.toFixed(2) }}</span>
        </div>
        <div class="amount-item">
          <span>优惠金额：</span>
          <span>-¥{{ discountAmount.toFixed(2) }}</span>
        </div>
        <div class="amount-item total">
          <span>应付金额：</span>
          <span class="price">¥{{ payAmount.toFixed(2) }}</span>
        </div>
      </div>

      <!-- 提交订单按钮 -->
      <div class="submit-order">
        <el-button type="primary" size="large" @click="submitOrder">提交订单</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useOrderStore } from '@/stores/order'
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/stores/cart'
import { HomeFilled, ShoppingCart, User } from '@element-plus/icons-vue'
import { codeToText } from 'element-china-area-data'
import { createOrder } from '@/api/order'
import { getAddressList } from '@/api/address'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const orderStore = useOrderStore()
const cartStore = useCartStore()

// 收货地址列表
const addressList = ref([])
const selectedAddressId = ref('')

// 判断来源
let products = ref(null)

if (route.query.productId) {
  // 立即购买
  products.value = [{
    id: route.query.productId,
    goodsName: route.query.name,
    goodsPrice: Number(route.query.price),
    quantity: Number(route.query.quantity),
    specs: JSON.parse(route.query.specs),
    image: route.query.image
  }]
} else {
  // 购物车结算
  const items = localStorage.getItem('checkoutItems')
  if (items) {
    // 兼容处理，确保每个商品都有 image 字段
    products.value = JSON.parse(items).map(item => ({
      ...item,
      image: item.image || '' // 没有图片时给个空字符串或默认图片
    }))
  } else {
    ElMessage.error('没有可结算的商品')
    router.push('/cart')
  }
}

// 优惠券
const availableCoupons = ref([
  { id: 1, name: '满1000减100', amount: 100, condition: 1000 },
  { id: 2, name: '满2000减200', amount: 200, condition: 2000 }
])
const selectedCoupon = ref('')

// 计算金额
const totalAmount = computed(() => {
  if (Array.isArray(products.value)) {
    return products.value.reduce((sum, item) => sum + item.goodsPrice * item.quantity, 0)
  } else if (products.value) {
    return products.value.goodsPrice * products.value.quantity
  }
  return 0
})

const discountAmount = computed(() => {
  if (!selectedCoupon.value) return 0
  const coupon = availableCoupons.value.find(c => c.id === selectedCoupon.value)
  return coupon && totalAmount.value >= coupon.condition ? coupon.amount : 0
})

const payAmount = computed(() => {
  return totalAmount.value - discountAmount.value
})

// 提交订单
const submitOrder = async () => {
  if (!selectedAddressId.value) {
    ElMessage.warning('请选择收货地址')
    return
  }
  try {
    const address = addressList.value.find(a => a.id === selectedAddressId.value)
    const orderData = {
      orderNo: 'NO' + Date.now(),
      userId: userStore.userInfo.id,
      totalAmount: totalAmount.value,
      status: 'unpaid',
      address: `${codeToText[address.province] || address.province}${codeToText[address.city] || address.city}${codeToText[address.district] || address.district}${address.detail}`,
      receiver: address.receiver,
      phone: address.phone,
      remark: '',
      orderItemList: products.value.map(item => ({
        goodsId: item.id,
        goodsName: item.goodsName,
        goodsImage: item.image,
        goodsPrice: item.goodsPrice,
        quantity: item.quantity,
        specs: typeof item.specs === 'object' ? JSON.stringify(item.specs) : item.specs
      }))
    }
    const res = await createOrder(orderData)
    if (res.data && res.data.data && res.data.data.id) {
      // 订单创建成功，清空购物车已结算商品
      const selectedItems = JSON.parse(localStorage.getItem('checkoutItems') || '[]')
      const ids = selectedItems.map(item => item.id)
      await cartStore.removeItemsByIds(ids)
      localStorage.removeItem('checkoutItems')
      router.push(`/order/payment/${res.data.data.id}`)
    } else {
      ElMessage.error('创建订单失败')
    }
  } catch (error) {
    ElMessage.error('创建订单失败')
  }
}

const cartCount = computed(() => cartStore.cartCount)

// 格式化 region 编码为中文
const formatRegion = (regionArr) => {
  if (!regionArr || !Array.isArray(regionArr)) return ''
  return regionArr.map(code => codeToText[code] || code).join(' ')
}

const formatSpecs = (specs) => {
  if (!specs) return ''
  let obj = specs
  if (typeof specs === 'string') {
    try {
      obj = JSON.parse(specs)
    } catch {
      // 不是JSON字符串，直接返回
      return specs
    }
  }
  if (typeof obj === 'object') {
    return Object.entries(obj).map(([k, v]) => `${k}: ${v}`).join('，')
  }
  return obj
}

onMounted(async () => {
  if (userStore.userInfo && userStore.userInfo.id) {
    const res = await getAddressList(userStore.userInfo.id)
    if (res.data) {
      addressList.value = res.data
    }
  }
})
</script>

<style scoped>
.order-confirm {
  padding-top: 60px;
  padding-left: 20px;
  padding-right: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.confirm-container {
  max-width: 1200px;
  margin: 0 auto;
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.address-section,
.product-list,
.coupon-section {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #ebeef5;
}

h2 {
  margin-bottom: 16px;
  font-size: 18px;
  font-weight: 600;
}

.address-item {
  padding: 8px 0;
}

.receiver {
  margin-bottom: 8px;
}

.receiver span {
  margin-right: 16px;
}

.product-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
}

.amount-item {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 8px;
}

.total {
  font-size: 16px;
  font-weight: 600;
}

.price {
  color: #ff4949;
  font-size: 20px;
}

.submit-order {
  text-align: right;
  margin-top: 24px;
}

.detail-nav {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 60px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.logo img {
  height: 50px;
  display: block;
}

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.nav-left {
  display: flex;
  align-items: center;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 24px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #333;
  text-decoration: none;
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 20px;
  transition: all 0.3s;
}

.nav-item:hover {
  background: rgba(0, 0, 0, 0.05);
  color: #9E8CB9;
}

.nav-item .el-icon {
  font-size: 18px;
}
</style>