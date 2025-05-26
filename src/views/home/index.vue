<template>
  <div class="home">
    <!-- 背景图层 -->
    <div class="background-image"></div>
    <!-- 顶部导航栏组件 -->
    <nav-header></nav-header>
    
    <div class="main-content">
      <!-- 商品分类导航 -->
      <category-nav></category-nav>
      
      <!-- 主要内容区域 -->
      <div class="content-wrapper">
        <!-- 轮播广告区 -->
        <carousel-section></carousel-section>
        
        <!-- 热门商品推荐 -->
        <hot-products :products="hotProducts" />
        
        <!-- 新品上架 -->
        <new-products :products="newProducts" />
        
        <!-- 促销活动专区 -->
        <promotion-section></promotion-section>
      </div>
    </div>

    <!-- 页脚 -->
    <app-footer></app-footer>

    <!-- 侧边工具栏 -->
    <side-toolbar></side-toolbar>
  </div>
</template>

<script setup>
import NavHeader from '../../components/layout/NavHeader.vue'
import CategoryNav from '../../components/business/CategoryNav.vue'
import CarouselSection from '../../components/business/CarouselSection.vue'
import HotProducts from '../../components/business/HotProducts.vue'
import NewProducts from '../../components/business/NewProducts.vue'
import PromotionSection from '../../components/business/PromotionSection.vue'
import AppFooter from '../../components/layout/Footer.vue'
import SideToolbar from '../../components/layout/SideToolbar.vue'
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import { useProductStore } from '@/stores/product'
import { getHotGoods, getNewGoods } from '@/api/good'

const router = useRouter()
const productStore = useProductStore()
const products = ref([])
const hotProducts = ref([])
const newProducts = ref([])

onMounted(async () => {
  products.value = await productStore.getProductList(1, 10)
  const res = await getHotGoods()
  if (res.data && res.data.data) {
    hotProducts.value = res.data.data
  }
  const newRes = await getNewGoods()
  if (newRes.data && newRes.data.data) {
    newProducts.value = newRes.data.data
  }
})

const goToDetail = (productId) => {
  router.push('/user/orders')
}
</script>

<style scoped>
.home {
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