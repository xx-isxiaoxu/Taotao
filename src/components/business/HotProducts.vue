<template>
  <div class="hot-products">
    <div class="section-header">
      <h2>热门推荐</h2>
      <el-button text @click="router.push('/hot')" :style="btnStyle">查看更多</el-button>
    </div>
    
    <div class="products-grid">
      <div v-for="product in hotProducts" 
           :key="product.id" 
           class="product-card"
           @click="handleProductClick(product)">
        <img :src="product.image" :alt="product.name">
        <h3>{{ product.name }}</h3>
        <p class="description">{{ product.description }}</p>
        <div class="price">
          <span class="current">¥{{ product.price }}</span>
          <span class="original" v-if="product.originalPrice">¥{{ product.originalPrice }}</span>
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

const hotProducts = ref([
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
])

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