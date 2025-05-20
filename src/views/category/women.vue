<template>
  <div class="category-page">
    <div class="background-image"></div>
    <nav-header></nav-header>
    <div class="main-content">
      <category-nav></category-nav>
      <div class="content-wrapper">
        <div class="category-header">
          <h1>女装</h1>
          <div class="category-breadcrumb">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>女装</el-breadcrumb-item>
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
            <div v-for="product in filteredProducts" :key="product.id" class="product-card">
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
                  <el-button type="primary" :style="btnStyle" @click="router.push(`/product/${product.id}`)">立即购买</el-button>
                  <button class="add-cart-btn" @click="addToCart(product, $event)">加入购物车</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <app-footer></app-footer>
    <side-toolbar></side-toolbar>
  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import NavHeader from '@/components/layout/NavHeader.vue'
import CategoryNav from '@/components/business/CategoryNav.vue'
import AppFooter from '@/components/layout/Footer.vue'
import SideToolbar from '@/components/layout/SideToolbar.vue'
import { useCartStore } from '@/stores/cart'
const router = useRouter()
const sortBy = ref('default')
const cartStore = useCartStore()
const products = ref([
  { id: 401, name: '优衣库女装连衣裙', description: '简约时尚，舒适面料', price: 199, originalPrice: 299, image: '/Images/uniqlo-dress.jpg', sales: 600 },
])
const filteredProducts = computed(() => {
  let filtered = products.value
  switch (sortBy.value) {
    case 'price-asc': filtered = filtered.slice().sort((a, b) => a.price - b.price); break
    case 'price-desc': filtered = filtered.slice().sort((a, b) => b.price - a.price); break
    case 'sales': filtered = filtered.slice().sort((a, b) => (b.sales || 0) - (a.sales || 0)); break
  }
  return filtered
})
const btnStyle = {
  background: 'rgb(237, 198, 231)',
  borderColor: 'rgb(237, 198, 231)',
  color: 'rgb(255, 255, 255)',
  fontWeight: 'bold',
  letterSpacing: '2px'
}
function addToCart(product, event) {
  event.stopPropagation();
  cartStore.addToCart(product, 1)
}
</script>
<style scoped>
@import './index.vue';
</style> 