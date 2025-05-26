<template>
  <div class="search-result">
    <!-- 背景图层 -->
    <div class="background-image"></div>
    
    <div class="content-wrapper">
      <div class="search-header">
        <el-button
          class="back-home-btn"
          type="primary"
          :style="btnStyle"
          @click="router.push('/home')"
          plain
        >
          返回首页
        </el-button>
        <h2>搜索结果：{{ keyword }}</h2>
        <div class="result-count" v-if="products.length">
          共找到 {{ products.length }} 个相关商品
        </div>
      </div>

      <div v-if="products.length" class="products-grid">
        <div v-for="item in products" :key="item.gid" class="product-card">
          <div class="product-image">
            <img :src="item.gpicture" :alt="item.gname" />
          </div>
          <div class="product-info">
            <h3 class="product-name">{{ item.gname }}</h3>
            <p class="product-desc">{{ item.gdetails }}</p>
            <div class="product-price">¥{{ item.gprice }}</div>
            <div class="product-actions">
              <el-button type="primary" :style="btnStyle" @click="router.push(`/product/${item.gid}`)">
                立即购买
              </el-button>
              <el-button type="info" plain :style="btnStyle" @click="router.push(`/product/${item.gid}`)">
                查看详情
              </el-button>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="empty-result">
        <el-empty description="未找到相关商品" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { searchGoods } from '@/api/good'

const route = useRoute()
const router = useRouter()
const keyword = ref(route.query.keyword || '')
const products = ref([])

const btnStyle = {
  background: '#9E8CB9',
  color: '#fff',
  border: 'none',
  padding: '5px 15px',
  borderRadius: '15px',
  transition: 'background 0.3s, color 0.3s, transform 0.2s',
  '&:hover': {
    background: '#9E8CB9',
    color: '#fff',
    transform: 'scale(1.045) translateY(-6px) rotateZ(-0.5deg)',
    boxShadow: '0 8px 32px rgba(158,140,185,0.18)'
  }
}

onMounted(async () => {
  if (!keyword.value) return
  const res = await searchGoods({ gname: keyword.value, pageNum: 1, pageSize: 20 })
  if (res.data && res.data.data && res.data.data.list) {
    products.value = res.data.data.list
  }
})
</script>

<style scoped>
.search-result {
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

.content-wrapper {
  flex: 1;
  max-width: 1400px;
  width: 90vw;
  margin: 48px auto;
  padding: 48px 32px;
  background: rgba(255,255,255,0.96);
  border-radius: 32px;
  box-shadow: 0 8px 48px rgba(0,0,0,0.10);
  position: relative;
  z-index: 1;
}

.search-header {
  margin-bottom: 32px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f0f0f0;
  display: flex;
  align-items: center;
}

.search-header h2 {
  color: #333;
  font-size: 28px;
  margin-bottom: 8px;
  margin: 0;
}

.result-count {
  color: #666;
  font-size: 16px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 32px;
}

.product-card {
  border: 1px solid #eee;
  border-radius: 16px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  transition: transform 0.3s cubic-bezier(.34,1.56,.64,1), box-shadow 0.3s;
}

.product-card:hover {
  transform: scale(1.045) translateY(-6px) rotateZ(-0.5deg);
  box-shadow: 0 8px 32px rgba(158,140,185,0.18);
  z-index: 2;
}

.product-image {
  position: relative;
  padding-top: 100%;
}

.product-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 20px;
}

.product-name {
  font-size: 18px;
  color: #333;
  margin-bottom: 8px;
  font-weight: 600;
}

.product-desc {
  color: #666;
  font-size: 14px;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-price {
  color: #9E8CB9;
  font-size: 24px;
  font-weight: bold;
  margin: 12px 0;
}

.product-actions {
  display: flex;
  gap: 12px;
  margin-top: 16px;
}

.empty-result {
  padding: 48px 0;
  text-align: center;
}

.back-home-btn {
  margin-right: 18px;
  vertical-align: middle;
  font-size: 15px;
  padding: 4px 18px;
  border-radius: 16px;
  background: #fff;
  color: #9E8CB9;
  border: 1.5px solid #9E8CB9;
  transition: background 0.2s, color 0.2s;
}

.back-home-btn:hover {
  background: #9E8CB9;
  color: #fff;
}

@media (max-width: 1920px) {
  .content-wrapper {
    max-width: 98vw;
    margin: 24px auto;
    padding: 24px;
    border-radius: 18px;
  }
  
  .products-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 16px;
  }
}
</style>
