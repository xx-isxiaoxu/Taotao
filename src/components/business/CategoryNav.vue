<template>
  <div class="category-nav">
    <h3 class="nav-title">商品分类</h3>
    <ul class="category-list">
      <li v-for="category in categories" 
          :key="category.id" 
          class="category-item"
          @mouseenter="handleMouseEnter(category)"
          @mouseleave="handleMouseLeave">
        <div class="category-main">
          <i :class="category.icon"></i>
          {{ category.name }}
        </div>
        
        <!-- 子分类弹出层 -->
        <div class="sub-categories" v-show="activeCategory === category.id">
          <div v-for="sub in category.children" 
               :key="sub.id" 
               class="sub-category"
               @click="handleCategoryClick(sub)">
            <img :src="sub.image" :alt="sub.name">
            <span>{{ sub.name }}</span>
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const activeCategory = ref(null)
//分类数据结构
const categories = ref([
  {
    id: 1,
    name: '电脑办公',
    icon: 'el-icon-monitor',
    children: [
      { id: 101, name: '笔记本', image: '/Images/desktop.png' },
      { id: 102, name: '台式机', image: '/Images/computer.png' },
      { id: 103, name: '平板电脑', image: '/Images/tablet.png' },
      { id: 104, name: '显示器', image: '/Images/monitor.png' },
      { id: 105, name: '办公设备', image: '/Images/office.png' }
    ]
  },
  {
    id: 2,
    name: '手机数码',
    icon: 'el-icon-mobile',
    children: [
      { id: 201, name: '手机', image: '/Images/phone.png' },
      { id: 202, name: '相机', image: '/Images/camera.png' },
      { id: 203, name: '耳机', image: '/Images/headphone.png' },
      { id: 204, name: '智能手表', image: '/Images/smartwatch.png' },
      { id: 205, name: '配件', image: '/Images/accessories.png' }
    ]
  },
  {
    id: 3,
    name: '家用电器',
    icon: 'el-icon-refrigerator',
    children: [
      { id: 301, name: '电视', image: '/Images/tv.png' },
      { id: 302, name: '冰箱', image: '/Images/fridge.png' },
      { id: 303, name: '洗衣机', image: '/Images/washer.png' },
      { id: 304, name: '空调', image: '/Images/ac.png' },
      { id: 305, name: '厨房电器', image: '/Images/kitchen.png' }
    ]
  },
  {
    id: 4,
    name: '服装鞋包',
    icon: 'el-icon-shopping-bag',
    children: [
      { id: 401, name: '女装', image: '/Images/women.png' },
      { id: 402, name: '男装', image: '/Images/men.png' },
      { id: 403, name: '鞋靴', image: '/Images/shoes.png' },
      { id: 404, name: '箱包', image: '/Images/bags.png' },
      { id: 405, name: '配饰', image: '/Images/fashion.png' }
    ]
  },
  {
    id: 5,
    name: '美妆个护',
    icon: 'el-icon-present',
    children: [
      { id: 501, name: '护肤', image: '/Images/skincare.png' },
      { id: 502, name: '彩妆', image: '/Images/makeup.png' },
      { id: 503, name: '洗护', image: '/Images/personal.png' },
      { id: 504, name: '香水', image: '/Images/perfume.png' },
      { id: 505, name: '美妆工具', image: '/Images/tools.png' }
    ]
  }
])
//鼠标悬停显示子分类
const handleMouseEnter = (category) => {
  activeCategory.value = category.id
}
//点击分类跳转
const handleMouseLeave = () => {
  activeCategory.value = null
}

const handleCategoryClick = (category) => {
  router.push({ name: 'Category', params: { id: category.id } })
}
</script>

<style scoped>
.category-nav {
  width: 240px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.nav-title {
  padding: 15px 20px;
  font-size: 16px;
  border-bottom: 1px solid #eee;
}

.category-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-item {
  position: relative;
  padding: 12px 20px;
  cursor: pointer;
}

.category-item:hover {
  background-color: #f5f5f5;
}

.category-main {
  display: flex;
  align-items: center;
  gap: 10px;
}

.category-main i {
  font-size: 16px;
  color: #9E8CB9;
}

.sub-categories {
  position: absolute;
  left: 100%;
  top: 0;
  width: 600px;
  min-height: 300px;
  background: #fff;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  z-index: 10;
}

.sub-category {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: transform 0.3s;
}

.sub-category:hover {
  transform: translateY(-3px);
}

.sub-category img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
}

.sub-category span {
  font-size: 14px;
  color: #333;
}
</style>