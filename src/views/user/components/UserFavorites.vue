<template>
  <div class="favorites-container">
    <div v-if="collects.length === 0" class="empty-state">
      <el-empty description="暂无收藏商品" />
    </div>
    
    <div v-else class="favorites-grid">
      <el-card v-for="item in collects" :key="item.id" class="favorite-item" shadow="hover">
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
            <div class="collect-time">收藏时间：{{ formatDate(item.collectTime) }}</div>
          </div>
        </router-link>
        <div class="item-actions">
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

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString()
}
</script>

<style scoped>
.favorites-container {
  padding: 20px;
}

.empty-state {
  margin-top: 40px;
}

.favorites-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.favorite-item {
  transition: transform 0.3s;
}

.favorite-item:hover {
  transform: translateY(-5px);
}

.product-link {
  text-decoration: none;
  color: inherit;
  display: block;
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
  overflow: hidden;
  text-overflow: ellipsis;
  max-height: 2.8em; /* 约等于两行文字的高度 */
  line-height: 1.4em;
  position: relative;
  padding-right: 1em;
}

.product-name::after {
  content: '...';
  position: absolute;
  right: 0;
  bottom: 0;
  background: #fff;
  padding-left: 0.5em;
}

.product-desc {
  margin: 8px 0;
  font-size: 14px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  max-height: 2.8em;
  line-height: 1.4em;
  position: relative;
  padding-right: 1em;
}

.product-desc::after {
  content: '...';
  position: absolute;
  right: 0;
  bottom: 0;
  background: #fff;
  padding-left: 0.5em;
}

.product-price {
  color: #ff69b4;
  font-size: 18px;
  font-weight: bold;
  margin: 8px 0;
}

.collect-time {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

.item-actions {
  padding: 10px 14px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .favorites-grid {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  }
}
</style> 