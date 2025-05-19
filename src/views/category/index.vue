<template>
  <div class="category-page">
    <!-- 背景图层 -->
    <div class="background-image"></div>
    
    <!-- 顶部导航栏组件 -->
    <nav-header></nav-header>
    
    <div class="main-content">
      <!-- 商品分类导航 -->
      <category-nav></category-nav>
      
      <!-- 主要内容区域 -->
      <div class="content-wrapper">
        <div class="category-header">
          <h1>{{ categoryName }}</h1>
          <div class="category-breadcrumb">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ categoryName }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </div>

        <div class="category-content">
          <div class="filter-section">
            <el-select v-model="sortBy" placeholder="排序方式" class="sort-select">
              <el-option label="默认排序" value="default" />
              <el-option label="价格从低到高" value="price-asc" />
              <el-option label="价格从高到低" value="price-desc" />
              <el-option label="销量优先" value="sales" />
            </el-select>
          </div>

          <div class="products-grid">
            <div v-for="product in filteredProducts" 
                 :key="product.id" 
                 class="product-card">
              <div class="product-image" @click="router.push(`/product/${product.id}`)">
                <img :src="product.image" :alt="product.name">
              </div>
              <div class="product-info">
                <h3>{{ product.name }}</h3>
                <p class="description">{{ product.description }}</p>
                <div class="price">
                  <span class="current">¥{{ product.price }}</span>
                  <span class="original" v-if="product.originalPrice">¥{{ product.originalPrice }}</span>
                </div>
                <div class="product-actions">
                  <el-button type="primary" :style="btnStyle" @click="router.push(`/product/${product.id}`)">
                    立即购买
                  </el-button>
                  <button class="add-cart-btn" @click="addToCart(product, $event)">
                    加入购物车
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 页脚 -->
    <app-footer></app-footer>

    <!-- 侧边工具栏 -->
    <side-toolbar></side-toolbar>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import NavHeader from '@/components/layout/NavHeader.vue'
import CategoryNav from '@/components/business/CategoryNav.vue'
import AppFooter from '@/components/layout/Footer.vue'
import SideToolbar from '@/components/layout/SideToolbar.vue'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const router = useRouter()
const sortBy = ref('default')
const cartStore = useCartStore()

// 替换products为包含所有分类的商品
const products = ref([
  // 笔记本
  { id: 1, name: '拯救者Y9000P', description: '搭载最新第13代处理器', price: 8999, originalPrice: 9999, image: '/Images/y9000p.jpg', category: 101, sales: 1000 },
  { id: 2, name: 'ThinkPad X1 Carbon', description: '轻薄商务本', price: 12999, originalPrice: 13999, image: '/Images/ThinkPad X1 Carbon.jpg', category: 101, sales: 800 },
  { id: 3, name: 'MacBook Pro 14', description: 'M2芯片，专业创作', price: 14999, originalPrice: 15999, image: '/Images/macbook-pro.jpg', category: 101, sales: 1200 },
  { id: 4, name: '华为MateBook X Pro', description: '全面屏轻薄本', price: 9999, originalPrice: 10999, image: '/Images/matebook-x.jpg', category: 101, sales: 900 },
  // 手机
  { id: 5, name: 'iPhone 15 Pro', description: '苹果旗舰手机，A17芯片', price: 8999, originalPrice: 9999, image: '/Images/iphone15pro.jpg', category: 201, sales: 1200 },
  // 电视
  { id: 6, name: '小米电视4A', description: '4K超高清智能电视', price: 2999, originalPrice: 3999, image: '/Images/tv.png', category: 301, sales: 800 },
  // 女装
  { id: 7, name: '优衣库女装连衣裙', description: '简约时尚，舒适面料', price: 199, originalPrice: 299, image: '/Images/uniqlo-dress.jpg', category: 401, sales: 600 },
])

// 分类id与名称映射
const categoryMap = {
  101: '笔记本',
  201: '手机',
  301: '电视',
  401: '女装',
}
//根据路由参数获取分类名称
const categoryName = computed(() => {
  const categoryId = parseInt(route.params.id)
  return categoryMap[categoryId] || '商品列表'
})
//根据路由参数获取分类id
const filteredProducts = computed(() => {
  const categoryId = parseInt(route.params.id)
  let filtered = products.value.filter(p => p.category === categoryId)
  //根据排序方式排序
  switch (sortBy.value) {
    case 'price-asc':
      filtered.sort((a, b) => a.price - b.price)
      break
    case 'price-desc':
      filtered.sort((a, b) => b.price - a.price)
      break
    case 'sales':
      filtered.sort((a, b) => b.sales - a.sales)
      break
  }
  
  return filtered
})

const btnStyle = {
  background: '#edc6e7',
  borderColor: '#edc6e7',
  color: '#fff',
  fontWeight: 'bold',
  letterSpacing: '2px'
}

function addToCart(product, event) {
  event.stopPropagation();
  cartStore.addToCart(product, 1)
}
</script>

<style scoped>
.category-page {
  min-height: 100vh;
  position: relative;
  overflow-x: hidden;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  background: #f5f6f7;
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

.main-content {
  display: flex;
  max-width: 1800px;
  margin: 0 auto;
  padding: 48px 0 0 0;
  gap: 48px;
  flex: 1;
  position: relative;
  z-index: 1;
}

.content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
  background: rgba(255,255,255,0.96);
  border-radius: 32px;
  box-shadow: 0 8px 48px rgba(0,0,0,0.10);
  padding: 48px 48px 32px 48px;
}

.category-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  background: #f8f9fb;
  padding: 18px 24px 10px 24px;
  border-radius: 18px 18px 0 0;
  border-bottom: 1.5px solid #f0e6f7;
  margin-bottom: 18px;
  box-shadow: 0 2px 8px rgba(237,198,231,0.06);
}

.category-header h1 {
  font-size: 28px;
  font-weight: bold;
  color: #9E8CB9;
  margin: 0;
  letter-spacing: 2px;
}

.category-breadcrumb {
  flex-shrink: 0;
  font-size: 14px;
  color: #999;
}

.category-breadcrumb :deep(.el-breadcrumb__item) {
  font-size: 14px;
}

.category-breadcrumb :deep(.el-breadcrumb__inner) {
  color: #888;
}

.category-breadcrumb :deep(.el-breadcrumb__inner.is-link) {
  color: #9E8CB9;
  font-weight: bold;
}

.filter-section {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-end;
}

.sort-select {
  width: 200px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.product-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
}

.product-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 15px;
}

.product-info h3 {
  margin: 0 0 10px 0;
  font-size: 16px;
}

.description {
  color: #666;
  font-size: 14px;
  margin-bottom: 10px;
}

.price {
  margin: 10px 0;
}

.current {
  color: #9E8CB9;
  font-size: 20px;
  font-weight: bold;
}

.original {
  color: #999;
  text-decoration: line-through;
  margin-left: 10px;
}

.product-actions {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.add-cart-btn {
  background: #fff0f6;
  color: #9E8CB9;
  border: 1.5px solid #9E8CB9;
  border-radius: 16px;
  padding: 6px 18px;
  font-size: 15px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.2s cubic-bezier(.34,1.56,.64,1);
  box-shadow: 0 2px 8px rgba(158,140,185,0.08);
}

.add-cart-btn:hover {
  background: #9E8CB9;
  color: #fff;
  transform: scale(1.06) translateY(-2px);
  box-shadow: 0 8px 24px rgba(158,140,185,0.18);
}

@media (max-width: 1920px) {
  .main-content {
    max-width: 98vw;
    padding: 24px 0 0 0;
    gap: 24px;
  }
  .content-wrapper {
    padding: 24px 8px 16px 8px;
    gap: 12px;
    border-radius: 18px;
  }
}
</style> 