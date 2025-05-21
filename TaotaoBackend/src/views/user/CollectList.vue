<template>
  <div class="collect-list">
    <h2>我的收藏</h2>
    
    <div v-if="collects.length === 0" class="empty-state">
      <el-empty description="暂无收藏商品" />
    </div>
    
    <div v-else class="collect-grid">
      <div v-for="item in collects" :key="item.id" class="collect-item">
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <router-link :to="`/product/${item.id}`" class="product-link">
            <el-image 
              :src="item.image" 
              fit="cover"
              class="product-image"
            />
            <div class="product-info">
              <h3 class="product-name">{{ item.name }}</h3>
              <p class="product-desc">{{ item.description }}</p>
              <div class="product-price">¥{{ item.price }}</div>
            </div>
          </router-link>
          <div class="collect-actions">
            <el-button 
              type="danger" 
              size="small"
              @click="removeCollect(item.id)"
            >
              取消收藏
            </el-button>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useCollectStore } from '@/stores/collect'
import { ElMessage } from 'element-plus'

const collectStore = useCollectStore()
const collects = computed(() => collectStore.getCollects())

const removeCollect = (productId) => {
  collectStore.removeCollect(productId)
  ElMessage.success('已取消收藏')
}
</script>

<style scoped>
.collect-list {
  padding: 20px;
}

.empty-state {
  margin-top: 40px;
}

.collect-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.collect-item {
  transition: transform 0.3s;
}

.collect-item:hover {
  transform: translateY(-5px);
}

.product-link {
  text-decoration: none;
  color: inherit;
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 14px;
}

.product-name {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.product-desc {
  margin: 8px 0;
  font-size: 14px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-clamp: 2;
  display: -moz-box;
  -moz-line-clamp: 2;
  -moz-box-orient: vertical;
  display: box;
  box-orient: vertical;
}

.product-price {
  color: #ff69b4;
  font-size: 18px;
  font-weight: bold;
}

.collect-actions {
  padding: 10px 14px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
}
</style> 