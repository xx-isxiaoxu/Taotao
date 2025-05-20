<template>
  <div class="cart-view">
    <!-- 背景图层 -->
    <div class="background-image"></div>
    
    <!-- 顶部导航 -->
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
            返回首页
          </router-link>
          <router-link to="/user" class="nav-item">
            <el-icon><User /></el-icon>
            个人中心
          </router-link>
        </div>
      </div>
    </div>

    <div class="cart-container">
      <div class="cart-header">
        <h2 class="page-title">我的购物车</h2>
        <div class="cart-stats">
          <span class="stat-item">
            <el-icon><ShoppingCart /></el-icon>
            共 {{ cartStore.cartCount }} 件商品
          </span>
          <span class="stat-item">
            <el-icon><Timer /></el-icon>
            最近更新：{{ lastUpdateTime }}
          </span>
        </div>
      </div>
      
      <div v-if="cartStore.cartItems.length === 0" class="empty-cart">
        <el-empty description="购物车是空的">
          <template #image>
            <el-image src="/empty-cart.png" style="width: 200px" />
          </template>
          <el-button type="primary" @click="goShopping">去购物</el-button>
        </el-empty>
      </div>
      
      <div v-else class="cart-content">
        <div class="cart-items">
          <div class="cart-header-row">
            <div class="col-check">
              <el-checkbox v-model="selectAll" @change="handleSelectAll">全选</el-checkbox>
            </div>
            <div class="col-info">商品信息</div>
            <div class="col-price">单价</div>
            <div class="col-quantity">数量</div>
            <div class="col-total">小计</div>
            <div class="col-action">操作</div>
          </div>
          
          <div v-for="item in cartStore.cartItems" :key="`${item.id}-${JSON.stringify(item.specs)}`" class="cart-item">
            <div class="col-check">
              <el-checkbox v-model="item.selected" @change="handleItemSelect" />
            </div>
            <div class="col-info">
              <div class="item-main">
                <img :src="item.image" :alt="item.name" class="item-image">
                <div class="item-details">
                  <div class="item-name">{{ item.name }}</div>
                  <div class="item-specs" v-if="Object.keys(item.specs).length">
                    {{ formatSpecs(item.specs) }}
                  </div>
                  <div class="item-time">加入时间：{{ formatDate(item.addTime) }}</div>
                </div>
              </div>
            </div>
            <div class="col-price">
              <span class="price-symbol">¥</span>
              <span class="price-value">{{ item.price }}</span>
            </div>
            <div class="col-quantity">
              <el-input-number 
                v-model="item.quantity" 
                :min="1" 
                :max="99"
                @change="(val) => updateQuantity(item, val)"
              />
            </div>
            <div class="col-total">
              <span class="price-symbol">¥</span>
              <span class="price-value">{{ (item.price * item.quantity).toFixed(2) }}</span>
            </div>
            <div class="col-action">
              <el-button type="danger" @click="removeItem(item)">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </div>
        </div>
        
        <div class="cart-footer">
          <div class="footer-left">
            <el-checkbox v-model="selectAll" @change="handleSelectAll">全选</el-checkbox>
            <el-button type="text" @click="clearCart">清空购物车</el-button>
          </div>
          <div class="footer-right">
            <div class="selected-info">
              已选择 <span class="highlight">{{ selectedCount }}</span> 件商品
            </div>
            <div class="total-info">
              总计: <span class="price">¥{{ cartStore.selectedTotalPrice }}</span>
            </div>
            <el-button type="primary" size="large" @click="handleCheckout" :disabled="selectedCount === 0">
              结算
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-section">
          <h3>关于我们</h3>
          <ul>
            <li>公司简介</li>
            <li>联系我们</li>
            <li>招贤纳士</li>
          </ul>
        </div>
        <div class="footer-section">
          <h3>客户服务</h3>
          <ul>
            <li>帮助中心</li>
            <li>配送说明</li>
            <li>退换货政策</li>
          </ul>
        </div>
        <div class="footer-section">
          <h3>关注我们</h3>
          <ul>
            <li>微信公众号</li>
            <li>新浪微博</li>
          </ul>
        </div>
        <div class="footer-section">
          <h3>联系方式</h3>
          <ul>
            <li>客服电话：400-123-4567</li>
            <li>工作时间：周一至周日 9:00-21:00</li>
            <li>邮箱：service@taotaoshangcheng.com</li>
          </ul>
        </div>
      </div>
      <div class="footer-bottom">
        <p>© 2024 淘淘商城 版权所有 | ICP备案号：粤ICP备12345678号</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCartStore } from '@/stores/cart'
import { HomeFilled, User, ShoppingCart, Timer, Delete } from '@element-plus/icons-vue'

const router = useRouter()
const cartStore = useCartStore()
const selectAll = ref(false)

// 格式化规格信息
const formatSpecs = (specs) => {
  return Object.entries(specs)
    .map(([name, value]) => `${name}: ${value}`)
    .join(', ')
}

// 格式化日期
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 获取最后更新时间
const lastUpdateTime = computed(() => {
  if (cartStore.cartItems.length === 0) return '暂无更新'
  const latestItem = cartStore.cartItems.reduce((latest, current) => {
    return new Date(current.addTime) > new Date(latest.addTime) ? current : latest
  })
  return formatDate(latestItem.addTime)
})

const selectedCount = computed(() => {
  return cartStore.cartItems.filter(item => item.selected).length
})

const handleSelectAll = (val) => {
  cartStore.toggleSelectAll(val)
}

const handleItemSelect = () => {
  selectAll.value = cartStore.cartItems.length > 0 && cartStore.cartItems.every(item => item.selected)
}

// 页面加载时同步一次
onMounted(() => {
  handleItemSelect()
})

const updateQuantity = (item, quantity) => {
  cartStore.updateQuantity(item.id, item.specs, quantity)
}

const removeItem = (item) => {
  ElMessageBox.confirm(
    '确定要删除这个商品吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    cartStore.removeFromCart(item.id, item.specs)
    ElMessage.success('商品已删除')
  }).catch(() => {})
}

const clearCart = () => {
  ElMessageBox.confirm(
    '确定要清空购物车吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    cartStore.clearCart()
    ElMessage.success('购物车已清空')
  }).catch(() => {})
}

const goShopping = () => {
  router.push('/')
}

const handleCheckout = () => {
  const selectedItems = cartStore.getSelectedItems()
  if (selectedItems.length === 0) {
    ElMessage.warning('请选择要结算的商品')
    return
  }
  // 临时存储到localStorage
  localStorage.setItem('checkoutItems', JSON.stringify(selectedItems))
  router.push('/order/confirm')
}
</script>

<style scoped>
.cart-view {
  min-height: 100vh;
  background: #f5f6f7;
  position: relative;
}

.background-image {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: url('/Images/background.png') center center/cover no-repeat;
  opacity: 0.15;
  z-index: 0;
  pointer-events: none;
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

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.logo img {
  height: 36px;
  display: block;
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

.cart-container {
  max-width: 1200px;
  margin: 80px auto 20px;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 1;
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.page-title {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.cart-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  font-size: 14px;
}

.empty-cart {
  padding: 40px 0;
  text-align: center;
}

.cart-header-row {
  display: grid;
  grid-template-columns: 80px 3fr 1fr 1fr 1fr 1fr;
  padding: 16px 0;
  border-bottom: 1px solid #eee;
  font-weight: bold;
  background: #f8f9fa;
  border-radius: 4px;
}

.cart-item {
  display: grid;
  grid-template-columns: 80px 3fr 1fr 1fr 1fr 1fr;
  padding: 20px 0;
  border-bottom: 1px solid #eee;
  align-items: center;
  transition: all 0.3s;
}

.cart-item:hover {
  background: #f8f9fa;
}

.item-main {
  display: flex;
  align-items: center;
  gap: 12px;
}

.item-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.item-details {
  flex: 1;
}

.item-name {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}

.item-specs {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.item-time {
  font-size: 12px;
  color: #999;
}

.price-symbol {
  color: #9E8CB9;
  font-size: 14px;
}

.price-value {
  color: #9E8CB9;
  font-size: 18px;
  font-weight: bold;
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
  margin-top: 20px;
  border-top: 1px solid #eee;
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.selected-info {
  font-size: 14px;
}

.highlight {
  color: #9E8CB9;
  font-weight: bold;
}

.total-info {
  font-size: 16px;
}

.price {
  color: #9E8CB9;
  font-weight: bold;
  font-size: 24px;
  margin-left: 8px;
}

.footer {
  background: #2d3a4b;
  color: #fff;
  padding: 40px 0 20px;
  margin-top: 40px;
  position: relative;
  z-index: 1;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 40px;
  padding: 0 20px;
}

.footer-section h3 {
  color: #fff;
  margin-bottom: 16px;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section li {
  color: #cfd8dc;
  margin-bottom: 8px;
  font-size: 14px;
}

.footer-section li:hover {
  color: #9E8CB9;
  cursor: pointer;
  transition: color 0.2s;
}

.footer-bottom {
  text-align: center;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #445069;
  color: #cfd8dc;
  font-size: 13px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .cart-header-row,
  .cart-item {
    grid-template-columns: 60px 2fr 1fr 1fr 1fr;
  }
  
  .col-total {
    display: none;
  }
  
  .footer-content {
    grid-template-columns: 1fr;
    gap: 20px;
  }
}
</style> 