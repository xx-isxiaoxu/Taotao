<template>
  <div class="carousel-section">
    <el-carousel 
      :interval="3000" 
      height="400px"
      arrow="always"
      indicator-position="outside"
      :autoplay="true"
      trigger="click"
      @change="handleChange">
      <el-carousel-item v-for="item in carouselItems" :key="item.id">
        <div class="carousel-item">
          <img :src="item.image" :alt="item.title" class="carousel-image">
          <div class="carousel-content">
            <h2>{{ item.title }}</h2>
            <p>{{ item.description }}</p>
            <el-button type="primary" @click="handleClick(item)">立即查看</el-button>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const carouselItems = ref([
  {
    id: 1,
    title: '新品上市',
    description: '全新产品系列，限时特惠',
    image: '/Images/banner1.jpg',
    link: '/product/1'
  },
  {
    id: 2,
    title: '618大促',
    description: '全场商品低至5折',
    image: '/Images/banner2.jpg',
    link: '/activity/618'
  },
  {
    id: 3,
    title: '品质生活',
    description: '精选好物，品质之选',
    image: '/Images/banner3.jpg',
    link: '/quality'
  }
])

const handleClick = (item) => {
  router.push(item.link)
}

const handleChange = (index) => {
  console.log('当前轮播图索引：', index)
}
</script>

<style scoped>
.carousel-section {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.carousel-item {
  position: relative;
  height: 100%;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.carousel-content {
  position: absolute;
  bottom: 40px;
  left: 40px;
  color: #fff;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
  z-index: 1;
}

.carousel-content h2 {
  font-size: 32px;
  margin-bottom: 10px;
}

.carousel-content p {
  font-size: 18px;
  margin-bottom: 20px;
}

/* 自定义轮播图指示器样式 */
:deep(.el-carousel__indicators) {
  bottom: 20px;
}

:deep(.el-carousel__button) {
  width: 30px;
  height: 3px;
  border-radius: 3px;
  background-color: rgba(255, 255, 255, 0.7);
}

:deep(.el-carousel__indicator.is-active .el-carousel__button) {
  background-color: #fff;
}

/* 自定义轮播图箭头样式 */
:deep(.el-carousel__arrow) {
  background-color: rgba(0, 0, 0, 0.3);
  border-radius: 50%;
  width: 44px;
  height: 44px;
}

:deep(.el-carousel__arrow:hover) {
  background-color: rgba(0, 0, 0, 0.5);
}

:deep(.el-carousel__arrow i) {
  font-size: 20px;
}
</style>