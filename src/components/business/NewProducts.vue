<template>
  <div class="new-products">
    <div class="section-header">
      <h2>新品上架</h2>
      <div class="tabs">
        <span v-for="tab in tabs" 
              :key="tab.id"
              :class="['tab', { active: activeTab === tab.id }]"
              @click="activeTab = tab.id">
          {{ tab.name }}
        </span>
      </div>
    </div>
    
    <div class="products-grid">
      <div v-for="product in products" 
           :key="product.gid" 
           class="product-card">
        <div class="product-image">
          <img :src="product.gpicture" :alt="product.gname">
          <div class="new-tag">新品</div>
        </div>
        <div class="product-info">
          <h3>{{ product.gname }}</h3>
          <p>{{ product.gdetails }}</p>
          <div class="price">¥{{ product.gprice }}</div>
          <el-button type="primary" :style="btnStyle" @click="router.push(`/product/${product.gid}`)">
            立即购买
          </el-button>
          <el-button type="info" plain style="margin-left: 10px;" :style="btnStyle" @click="router.push(`/product/${product.gid}`)">
            更多
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const activeTab = ref(1)

const props = defineProps({
  products: {
    type: Array,
    default: () => []
  }
})

const tabs = ref([
  { id: 1, name: '全部' },
  { id: 2, name: '电脑办公' },
  { id: 3, name: '手机数码' },
  { id: 4, name: '家用电器' },
  { id: 5, name: '服装鞋包' },
  { id: 6, name: '美妆个护' }
])

const filteredProducts = computed(() => {
  if (activeTab.value === 1) return props.products.slice(0, 6)
  return props.products.filter(p => p.category === activeTab.value)
})

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
</script>

<style scoped>
.new-products {
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

.tabs {
  display: flex;
  gap: 8px;
}

.tab {
  cursor: pointer;
  padding: 5px 15px;
  border-radius: 15px;
  transition: background 0.3s, color 0.3s, transform 0.2s;
}

.tab.active {
  background: #9E8CB9;
  color: #fff;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.product-card {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  transition: transform 0.3s cubic-bezier(.34,1.56,.64,1), box-shadow 0.3s;
}

.product-card:hover {
  transform: scale(1.045) translateY(-6px) rotateZ(-0.5deg);
  box-shadow: 0 8px 32px rgba(255,107,129,0.18);
  z-index: 2;
}

.product-image {
  position: relative;
}

.product-image img {
  width: 100%;
  height: 250px;
  object-fit: cover;
}

.new-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  background: #9E8CB9;
  color: #fff;
  padding: 4px 8px;
  border-radius: 4px;
}

.product-info {
  padding: 15px;
}

.price {
  color: #9E8CB9;
  font-size: 24px;
  font-weight: bold;
  margin: 10px 0;
}
</style>