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
      <div v-for="product in filteredProducts" 
           :key="product.id" 
           class="product-card">
        <div class="product-image">
          <img :src="product.image" :alt="product.name">
          <div class="new-tag">新品</div>
        </div>
        <div class="product-info">
          <h3>{{ product.name }}</h3>
          <p>{{ product.description }}</p>
          <div class="price">¥{{ product.price }}</div>
          <el-button type="primary" :style="btnStyle" @click="router.push(`/product/${product.id}`)">
            立即购买
          </el-button>
          <el-button type="info" plain style="margin-left: 10px;" :style="btnStyle" @click="router.push(`/product/${product.id}`)">
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

const tabs = ref([
  { id: 1, name: '全部' },
  { id: 2, name: '电脑办公' },
  { id: 3, name: '手机数码' },
  { id: 4, name: '家用电器' },
  { id: 5, name: '服装鞋包' },
  { id: 6, name: '美妆个护' }
])

const products = ref([
  {
    id: 1,
    name: '拯救者Y9000P',
    description: '搭载最新第13代处理器',
    price: 8999,
    image: '/Images/y9000p.jpg',
    category: 2
  },
  {
    id: 2,
    name: '华为Mate60',
    description: '全新麒麟芯片，5G旗舰',
    price: 6999,
    image: '/Images/mate60.jpg',
    category: 3
  },
  {
    id: 3,
    name: '戴森吸尘器V12',
    description: '无线手持，强劲吸力',
    price: 3299,
    image: '/Images/dyson-v12.jpg',
    category: 4
  },
  {
    id: 4,
    name: '优衣库男士T恤',
    description: '舒适纯棉，夏日必备',
    price: 99,
    image: '/Images/uniqlo-tshirt.jpg',
    category: 5
  },
  {
    id: 5,
    name: 'SK-II神仙水',
    description: '焕活肌肤，明星单品',
    price: 1080,
    image: '/Images/sk2.jpg',
    category: 6
  },
  {
    id: 6,
    name: '小米平板6',
    description: '高刷大屏，影音娱乐',
    price: 2499,
    image: '/Images/xiaomi-pad6.jpg',
    category: 2
  },
  {
    id: 7,
    name: '索尼微单A7M4',
    description: '全画幅，专业摄影',
    price: 15999,
    image: '/Images/sony-a7m4.jpg',
    category: 3
  },
  {
    id: 8,
    name: '美的电饭煲',
    description: '智能预约，IH加热',
    price: 499,
    image: '/Images/midea-cooker.jpg',
    category: 4
  },
  {
    id: 9,
    name: '耐克运动短裤',
    description: '透气速干，运动必备',
    price: 129,
    image: '/Images/nike-shorts.jpg',
    category: 5
  },
  {
    id: 10,
    name: '雅诗兰黛小棕瓶',
    description: '修护精华，紧致焕亮',
    price: 850,
    image: '/Images/estee-lauder.jpg',
    category: 6
  }
])

const filteredProducts = computed(() => {
  if (activeTab.value === 1) return products.value.slice(0, 6)
  return products.value.filter(p => p.category === activeTab.value)
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