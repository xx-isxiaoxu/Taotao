<template>
  <div class="hot-products">
    <div class="section-header">
      <h2>热门推荐</h2>
      <el-button text @click="router.push('/hot')" :style="btnStyle">查看更多</el-button>
    </div>
    
    <div class="products-grid">
      <div v-for="product in products" 
           :key="product.id" 
           class="product-card"
           @click="handleProductClick(product)">
        <img :src="product.gpicture" :alt="product.gname">
        <h3>{{ product.gname }}</h3>
        <p class="description">{{ product.gdetails }}</p>
        <div class="price">
          <span class="current">¥{{ product.gprice }}</span>
          <span class="original" v-if="product.original_price">¥{{ product.original_price }}</span>
        </div>
        <div class="product-actions">
          <el-button type="primary" :style="btnStyle" @click.stop="handleBuyNow(product, $event)">
            立即购买
          </el-button>
          <button class="add-cart-btn" @click="addToCart(product, $event)">
            加入购物车
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'

const router = useRouter()
const cartStore = useCartStore()

const props = defineProps({
  products: {
    type: Array,
    default: () => []
  }
})

const btnStyle = {
  background: 'rgb(237, 198, 231)',
  borderColor: 'rgb(237, 198, 231)',
  color: 'rgb(255, 255, 255)',
  fontWeight: 'bold',
  letterSpacing: '2px'
}

function recordClick(productId) {
  const key = 'hotProductClicks';
  let clicks = JSON.parse(localStorage.getItem(key) || '{}');
  clicks[productId] = (clicks[productId] || 0) + 1;
  localStorage.setItem(key, JSON.stringify(clicks));
}

function handleProductClick(product) {
  recordClick(product.id);
  router.push(`/product/${product.id}`);
}

function handleBuyNow(product, event) {
  event.stopPropagation();
  recordClick(product.id);
  router.push(`/product/${product.id}`);
}

function addToCart(product, event) {
  event.stopPropagation();
  recordClick(product.id);
  cartStore.addToCart(product, 1);
}
</script>

<style scoped>
.hot-products {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.product-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
}

.product-card img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
}

.product-card h3 {
  margin: 10px 0;
  font-size: 16px;
}

.description {
  color: #666;
  font-size: 14px;
}

.price {
  margin-top: 10px;
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
  background: #f6f4fa;
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
</style>