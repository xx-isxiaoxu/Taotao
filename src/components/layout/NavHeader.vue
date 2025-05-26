<template>
  <div class="nav-header">
    <div class="header-content">
      <div class="logo">
        <img src="/logo1.png" alt="淘淘商城">
      </div>
      <div class="search">
        <el-input
          v-model="searchText"
          placeholder="搜索商品"
          class="search-input"
          @focus="handleInputFocus"
          @blur="handleInputBlur"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
        <div v-if="showSuggest && suggestList.length" class="search-suggest">
          <div v-for="(item, idx) in suggestList" :key="item.id" class="suggest-item" @mousedown.prevent="handleSuggestClick(item)">
            <span class="suggest-index">{{ idx + 1 }}</span>
            <img :src="item.image" class="suggest-img" />
            <span class="suggest-name">{{ item.name }}</span>
            <span class="suggest-price">¥{{ item.price }}</span>
          </div>
        </div>
      </div>
      <div class="nav-actions">
        <el-popover
          placement="bottom"
          :width="360"
          trigger="hover"
          popper-class="cart-popover"
        >
          <template #reference>
            <div class="nav-item">
              <el-badge :value="cartCount" class="cart-badge">
                <el-icon><ShoppingCart /></el-icon>
              </el-badge>
              购物车
            </div>
          </template>
          <CartPopover />
        </el-popover>
        <router-link :to="{ path: '/user', query: { tab: 'orders' } }" class="nav-item">
          <el-icon><User /></el-icon>
          个人订单
        </router-link>
        <router-link to="/user" class="nav-item">
          <el-icon><User /></el-icon>
          个人中心
        </router-link>
      </div>
      <div class="nav-welcome" v-if="username">
        <span class="welcome">{{ username }}，欢迎回来</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { Search, ShoppingCart, User } from '@element-plus/icons-vue'
import CartPopover from '../cart/CartPopover.vue'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { searchGoods } from '@/api/good'
import { ElMessage } from 'element-plus'

// 热门推荐商品数据（与HotProducts.vue保持一致）
const hotProducts = [
  {
    id: 1,
    name: '小新Pro16',
    description: '16英寸大屏轻薄本',
    price: 5999,
    originalPrice: 6999,
    image: '/Images/xiaoxin-pro16.jpg',
    category: '电脑办公'
  },
  {
    id: 2,
    name: 'iPhone 15 Pro',
    description: '苹果旗舰手机，A17芯片',
    price: 8999,
    originalPrice: 9999,
    image: '/Images/iphone15pro.jpg',
    category: '手机数码'
  },
  {
    id: 3,
    name: '索尼降噪耳机',
    description: '无线蓝牙，主动降噪',
    price: 1299,
    originalPrice: 1599,
    image: '/Images/sony-headphone.jpg',
    category: '手机数码'
  },
  {
    id: 4,
    name: '美的变频空调',
    description: '一级能效，智能控制',
    price: 2999,
    originalPrice: 3599,
    image: '/Images/midea-ac.jpg',
    category: '家用电器'
  },
  {
    id: 5,
    name: '海尔双门冰箱',
    description: '大容量，节能静音',
    price: 2399,
    originalPrice: 2699,
    image: '/Images/haier-fridge.jpg',
    category: '家用电器'
  },
  {
    id: 6,
    name: '阿迪达斯运动鞋',
    description: '舒适透气，时尚百搭',
    price: 499,
    originalPrice: 699,
    image: '/Images/adidas-shoes.jpg',
    category: '服装鞋包'
  },
  {
    id: 7,
    name: '兰蔻小黑瓶精华',
    description: '修护肌肤，焕发光彩',
    price: 780,
    originalPrice: 980,
    image: '/Images/lancome-serum.jpg',
    category: '美妆个护'
  },
  {
    id: 8,
    name: '优衣库女装连衣裙',
    description: '简约时尚，舒适面料',
    price: 199,
    originalPrice: 299,
    image: '/Images/uniqlo-dress.jpg',
    category: '服装鞋包'
  }
]

const searchText = ref('')
const searchResults = ref([])
const cartStore = useCartStore()
const userStore = useUserStore()
const cartCount = computed(() => cartStore.cartCount)
const username = computed(() => userStore.userInfo?.nickname || userStore.userInfo?.username || '')
const router = useRouter()

// 搜索建议逻辑
const showSuggest = ref(false)

function getClickCount(productId) {
  const key = 'hotProductClicks';
  let clicks = JSON.parse(localStorage.getItem(key) || '{}');
  return clicks[productId] || 0;
}
function recordClick(productId) {
  const key = 'hotProductClicks';
  let clicks = JSON.parse(localStorage.getItem(key) || '{}');
  clicks[productId] = (clicks[productId] || 0) + 1;
  localStorage.setItem(key, JSON.stringify(clicks));
}
const suggestList = computed(() => {
  const keyword = searchText.value.trim();
  let sorted = [...hotProducts].sort((a, b) => getClickCount(b.id) - getClickCount(a.id));
  if (!keyword) {
    // 没有输入时，直接推荐点击次数最多的前10个商品
    return sorted.slice(0, 10);
  }
  let result = sorted.filter(p => p.name.includes(keyword));
  if (result.length < 10) {
    result = result.concat(sorted.filter(p => !result.includes(p) && p.description.includes(keyword)));
  }
  return result.slice(0, 10);
});

function handleInputFocus() {
  showSuggest.value = true
}
function handleInputBlur() {
  setTimeout(() => { showSuggest.value = false }, 200) // 延迟隐藏，便于点击
}
function handleSuggestClick(product) {
  showSuggest.value = false
  searchText.value = product.name
  recordClick(product.id)
  router.push(`/product/${product.id}`)
}
async function handleSearch() {
  const keyword = searchText.value.trim()
  if (!keyword) return
  // 跳转到搜索结果页，并把关键词作为 query 传递
  router.push({ path: '/search', query: { keyword } })
}
</script>

<style scoped>
.nav-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1800px;
  margin: 0 auto;
  padding: 18px 40px;
  display: flex;
  align-items: center;
  gap: 48px;
  min-height: 100px;
}

.logo img {
  height: 50px;
  display: block;
}

.search {
  flex: 1;
  max-width: 500px;
  position: relative;
}

.nav-actions {
  display: flex;
  gap: 24px;
  align-items: center;
}

.nav-welcome {
  margin-left: auto;
  min-width: 180px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #9E8CB9;
  text-decoration: none;
}

.nav-item:hover {
  color: #ff6b81;
}

.cart-badge :deep(.el-badge__content) {
  background-color: #9E8CB9;
}

.welcome {
  font-size: 18px;
  color: #ed6ea0;
  font-weight: bold;
  margin-left: 16px;
}

.search-suggest {
  position: absolute;
  left: 0;
  top: 48px;
  width: 100%;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(158,140,185,0.18);
  z-index: 10;
  padding: 8px 0;
}
.suggest-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 18px;
  cursor: pointer;
  transition: background 0.2s;
}
.suggest-item:hover {
  background: #f6f4fa;
}
.suggest-img {
  width: 36px;
  height: 36px;
  object-fit: cover;
  border-radius: 4px;
}
.suggest-name {
  flex: 1;
  font-size: 15px;
  color: #333;
}
.suggest-price {
  color: #9E8CB9;
  font-weight: bold;
  font-size: 15px;
}
.suggest-index {
  display: inline-block;
  width: 22px;
  text-align: center;
  font-size: 15px;
  color: #9E8CB9;
  font-weight: bold;
}
</style>