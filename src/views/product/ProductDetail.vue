<template>
  <div class="product-detail">
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

    <!-- 商品详情内容 -->
    <div class="detail-container">
      <!-- 左侧商品图片 -->
      <div class="product-gallery">
        <el-carousel
          class="gallery-carousel"
          indicator-position="outside"
          height="400px"
          :autoplay="false"
          arrow="always"
        >
          <el-carousel-item v-for="(img, idx) in productImages" :key="img">
            <el-image 
              :src="getFullImageUrl(img)"
              :preview-src-list="productImages.map(getFullImageUrl)"
              :initial-index="idx"
              fit="contain"
              class="carousel-image"
            />
          </el-carousel-item>
        </el-carousel>
      </div>
      
      <!-- 右侧商品信息 -->
      <div class="product-info">
        <h1>{{ product.name }}</h1>
        <p class="description">{{ product.description }}</p>
        
        <div class="price-section">
          <div class="current-price">¥{{ product.price }}</div>
          <div class="original-price" v-if="product.originalPrice">
            原价：<span>¥{{ product.originalPrice }}</span>
          </div>
        </div>

        <!-- 规格选择 -->
        <div class="specs-section" v-if="product.specs && product.specs.length">
          <h3>规格选择</h3>
          <div v-for="spec in product.specs" :key="spec.name" class="spec-group">
            <div class="spec-title">{{ spec.name }}</div>
            <div class="spec-options">
              <el-radio-group v-model="selectedSpecs[spec.name]">
                <el-radio 
                  v-for="option in spec.options" 
                  :key="option"
                  :value="option"
                >
                  {{ option }}
                </el-radio>
              </el-radio-group>
            </div>
          </div>
        </div>

        <!-- 数量选择 -->
        <div class="quantity-section">
          <h3>购买数量</h3>
          <el-input-number 
            v-model="quantity" 
            :min="1" 
            :max="99"
            size="large"
          />
        </div>

        <!-- 按钮组 -->
        <div class="action-buttons">
          <el-button type="primary" size="large" @click="addToCart">加入购物车</el-button>
          <el-button type="danger" size="large" @click="buyNow">立即购买</el-button>
          <el-button 
            :type="isCollected ? 'success' : 'default'" 
            size="large" 
            @click="toggleCollect"
          >
            <el-icon><Star /></el-icon>
            {{ isCollected ? '已收藏' : '收藏' }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- 商品详情 -->
    <div class="detail-content">
      <el-tabs>
        <el-tab-pane label="商品详情">
          <div class="rich-text-content" v-html="product.detailHtml"></div>
        </el-tab-pane>
        <el-tab-pane label="用户评价" name="reviews">
          <!-- 评价入口 -->
          <div class="review-actions" v-if="userStore.userInfo?.username">
            <el-button type="primary" @click="showReviewForm = true">
              写评价
            </el-button>
          </div>
          
          <!-- 评价表单 -->
          <el-dialog
            v-model="showReviewForm"
            title="写评价"
            width="50%"
          >
            <review-form
              :product-id="product.id"
              @submit-success="handleReviewSubmit"
            />
          </el-dialog>
          
          <!-- 评价列表 -->
          <div class="reviews-list">
            <div v-if="productReviews.length === 0" class="empty-reviews">
              <el-empty description="暂无评价" />
            </div>
            <div v-else class="review-item" v-for="review in productReviews" :key="review.id">
              <el-avatar :src="review.userAvatar">{{ review.userName?.charAt(0) }}</el-avatar>
              <div class="review-content">
                <div class="review-header">
                  <span class="username">{{ review.userName }}</span>
                  <el-rate v-model="review.rating" disabled />
                </div>
                <p class="review-text">{{ review.content }}</p>
                <div class="review-images" v-if="review.images?.length">
                  <el-image 
                    v-for="img in review.images"
                    :key="img"
                    :src="img"
                    :preview-src-list="review.images"
                    class="review-image"
                  />
                </div>
                <span class="review-time">{{ formatDate(review.time) }}</span>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { HomeFilled, ShoppingCart, User, Star } from '@element-plus/icons-vue'
import { useProductStore } from '@/stores/product'
import { useCartStore } from '@/stores/cart'
import { useOrderStore } from '@/stores/order'
import { useCollectStore } from '@/stores/collect'
import { useReviewStore } from '@/stores/review'
import { useUserStore } from '@/stores/user'
import ReviewForm from './components/ReviewForm.vue'
import { getFullProductDetail } from '@/api/goods'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const cartCount = computed(() => cartStore.cartCount)
const quantity = ref(1)
const orderStore = useOrderStore()
const orderList = computed(() => orderStore.orders)

const collectStore = useCollectStore()
const isCollected = computed(() => collectStore.isCollected(product.value.id))

const reviewStore = useReviewStore()
const userStore = useUserStore()
const showReviewForm = ref(false)

const productImages = ref([])

// 商品数据
const product = ref({})

// 获取商品评价
const productReviews = computed(() => {
  return reviewStore.getProductReviews(product.value.id)
})

// 选中的规格（改为对象形式）
const selectedSpecs = ref({})

// 初始化选中规格
const initSelectedSpecs = () => {
  if (product.value?.specs) {
    product.value.specs.forEach(spec => {
      // 检查spec.options是否存在且不为空
      if (spec.options && spec.options.length > 0) {
        selectedSpecs.value[spec.name] = spec.options[0]
      } else {
        // 如果options不存在或为空，设置一个默认值
        selectedSpecs.value[spec.name] = ''
      }
    })
  }
}

// 加入购物车
const addToCart = async () => {
  try {
    await cartStore.addToCart(product.value, quantity.value, selectedSpecs.value)
  ElMessage.success('成功加入购物车！')
  } catch (e) {
    ElMessage.error(e.message || '加入购物车失败')
  }
}

// 立即购买
const buyNow = () => {
  // 检查规格
  const missingSpecs = product.value.specs?.filter(spec => !selectedSpecs.value[spec.name])
  if (missingSpecs?.length > 0) {
    ElMessage.warning('请选择商品规格')
    return
  }
  // 跳转并传递商品信息
  router.push({
    path: '/order/confirm',
    query: {
      productId: product.value.id,
      name: product.value.name,
      price: product.value.price,
      quantity: quantity.value,
      specs: JSON.stringify(selectedSpecs.value),
      image: product.value.image || product.value.images?.[0]
    }
  })
}

// 收藏/取消收藏
const toggleCollect = () => {
  if (isCollected.value) {
    collectStore.removeCollect(product.value.id)
    ElMessage.success('已取消收藏')
  } else {
    collectStore.addCollect({
      id: product.value.id,
      name: product.value.name,
      price: product.value.price,
      image: product.value.image || productImages.value[0],
      description: product.value.description
    })
    ElMessage.success('收藏成功')
  }
}

const productStore = useProductStore()

// 修改loadProductDetail函数，添加更多的错误处理
const loadProductDetail = async (id) => {
  try {
    await productStore.getProductDetail(id)
    if (productStore.productDetail) {
      product.value = {
        id: productStore.productDetail.gid,
        name: productStore.productDetail.gname,
        price: productStore.productDetail.gprice,
        description: productStore.productDetail.gdesc,
        image: productStore.productDetail.gpicture,
        ...productStore.productDetail,
        specs: productStore.productDetail.specs ? JSON.parse(productStore.productDetail.specs) : [],
        params: productStore.productDetail.params ? JSON.parse(productStore.productDetail.params) : [],
        images: productStore.productDetail.images ? JSON.parse(productStore.productDetail.images) : [],
        detailHtml: productStore.productDetail.detailHtml
      }
      if (productStore.productDetail.images) {
        productImages.value = productStore.productDetail.images
      }
      // 确保product.value.specs存在
      if (!product.value.specs) {
        product.value.specs = []
      }
      // 初始化规格选择
      initSelectedSpecs()
    } else {
      ElMessage.error('商品不存在')
      router.push('/home')
    }
  } catch (error) {
    ElMessage.error('获取商品详情失败')
    console.error('获取商品详情失败:', error)
  }
}

onMounted(async () => {
  const goodsId = route.params.id
  try {
    const res = await getFullProductDetail(goodsId)
    if (res.data && res.data.data) {
      const goods = res.data.data.goods || {}
      const detail = res.data.data.detail || {}
      product.value = {
        id: goods.gid || detail.goodsId || detail.id,
        name: goods.gname,
        price: goods.gprice,
        image: goods.gpicture,
        description: goods.gdetails,
        detailHtml: detail.detailHtml || '',
        specs: detail.specs ? JSON.parse(detail.specs) : [],
        params: detail.params ? JSON.parse(detail.params) : [],
        images: detail.images
          ? (typeof detail.images === 'string'
              ? JSON.parse(detail.images)
              : detail.images)
          : []
      }
      productImages.value = product.value.images
    } else {
      ElMessage.error('商品详情不存在')
    }
  } catch (e) {
    ElMessage.error('获取商品详情失败')
    console.error(e)
  }
})

const viewOrder = () => {
  router.push({ path: '/user', query: { tab: 'orders' } })
}

// 处理评价提交成功
const handleReviewSubmit = () => {
  showReviewForm.value = false
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString()
}

const BASE_URL = 'http://127.0.0.1:8080'

function getFullImageUrl(img) {
  if (!img) return ''
  if (img.startsWith('http')) return img
  return img.startsWith('/') ? BASE_URL + img : BASE_URL + '/' + img
}
</script>

<style scoped>
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

.product-detail {
  min-height: 100vh;
  background: #f5f6f7;
  position: relative;
  padding-top: 60px;
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

.detail-container {
  max-width: 1200px;
  margin: 20px auto;
  display: flex;
  gap: 24px;
  background: #fff;
  padding: 0;  /* 移除内边距 */
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  position: relative;
  z-index: 1;
}

.product-gallery {
  width: 100%;
  max-width: 500px;
  margin: 0 auto;
  /* 可根据页面布局调整 */
}

.gallery-carousel {
  width: 100%;
  height: 400px;
  /* 可根据需要自适应高度 */
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  cursor: pointer;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
}

@media (max-width: 768px) {
  .gallery-carousel {
    height: 220px;
  }
}

.product-info {
  flex: 1;
  padding: 24px 32px;
  border-left: 1px solid #f0f0f0;
}

.detail-content {
  max-width: 1200px;
  margin: 20px auto;
  background: #fff;
  padding: 24px 8px;
  border-radius: 18px;
  box-shadow: 0 4px 32px rgba(0,0,0,0.08);
  position: relative;
  z-index: 1;
}

h1 {
  font-size: 24px;
  margin-bottom: 16px;
}

.description {
  color: #666;
  margin-bottom: 24px;
}

.price-section {
  margin-bottom: 32px;
}

.current-price {
  font-size: 32px;
  color: #9E8CB9;
  font-weight: bold;
}

.original-price {
  color: #999;
  text-decoration: line-through;
  margin-top: 8px;
}

.specs-section,
.quantity-section {
  margin-bottom: 32px;
}

.spec-options {
  margin-top: 16px;
}

.action-buttons {
  display: flex;
  gap: 16px;
  margin-top: 40px;
}

/* 添加购物车按钮样式覆盖 */
/* 主题色统一 */
:deep(.el-button--primary) {
  background-color: #ff69b4;
  border-color: #ff69b4;
}

:deep(.el-button--primary:hover) {
  background-color: #ff85c0;
  border-color: #ff85c0;
}

:deep(.el-button--success) {
  background-color: #67c23a;
  border-color: #67c23a;
}

:deep(.el-button--success:hover) {
  background-color: #85ce61;
  border-color: #85ce61;
}

.rich-text-content {
  padding: 20px;
}

.reviews-list {
  padding: 20px 0;
}

.review-item {
  display: flex;
  gap: 16px;
  padding: 20px 0;
  border-bottom: 1px solid #eee;
}

.review-content {
  flex: 1;
}

.review-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 8px;
}

.username {
  font-weight: bold;
}

.review-text {
  margin: 8px 0;
  color: #333;
}

.review-images {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

.review-image {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  object-fit: cover;
}

.review-time {
  color: #999;
  font-size: 12px;
}

.spec-group {
  margin-bottom: 20px;
}

.spec-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
}

.spec-options {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

:deep(.el-radio) {
  margin-right: 16px;
  margin-bottom: 8px;
}

:deep(.el-radio__label) {
  font-size: 14px;
}

.review-actions {
  margin-bottom: 20px;
  text-align: right;
}

.empty-reviews {
  padding: 40px 0;
}
</style>