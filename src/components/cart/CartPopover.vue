<template>
  <div class="cart-popover">
    <div v-if="cartStore.cartItems.length === 0" class="empty-cart">
      <el-empty description="购物车是空的" />
    </div>
    <div v-else class="cart-items">
      <div v-for="item in cartStore.cartItems" :key="`${item.id}-${JSON.stringify(item.specs)}`" class="cart-item">
        <img :src="item.image" :alt="item.name" class="item-image">
        <div class="item-info">
          <div class="item-name">{{ item.name }}</div>
          <div class="item-specs" v-if="Object.keys(item.specs).length">
            {{ formatSpecs(item.specs) }}
          </div>
          <div class="item-price">¥{{ item.price }}</div>
          <div class="item-quantity">
            <el-input-number 
              v-model="item.quantity" 
              :min="1" 
              :max="99"
              size="small"
              @change="(val) => updateQuantity(item, val)"
            />
          </div>
        </div>
        <el-button 
          type="danger" 
          size="small" 
          circle
          @click="removeItem(item)"
        >
          <el-icon><Delete /></el-icon>
        </el-button>
      </div>
      <div class="cart-footer">
        <div class="total">
          总计: <span class="price">¥{{ cartStore.totalPrice }}</span>
        </div>
        <el-button type="primary" @click="goToCart">去结算</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Delete } from '@element-plus/icons-vue'
import { useCartStore } from '@/stores/cart'

const router = useRouter()
const cartStore = useCartStore()

const formatSpecs = (specs) => {
  return Object.entries(specs)
    .map(([name, value]) => `${name}: ${value}`)
    .join(', ')
}

const updateQuantity = (item, quantity) => {
  cartStore.updateQuantity(item.id, item.specs, quantity)
}

const removeItem = (item) => {
  cartStore.removeFromCart(item.id, item.specs)
}

const goToCart = () => {
  router.push('/cart')
}
</script>

<style scoped>
.cart-popover {
  width: 360px;
  max-height: 400px;
  overflow-y: auto;
  padding: 16px;
}

.empty-cart {
  padding: 20px 0;
}

.cart-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px;
  border-bottom: 1px solid #eee;
}

.item-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.item-info {
  flex: 1;
}

.item-name {
  font-size: 14px;
  margin-bottom: 4px;
}

.item-specs {
  font-size: 12px;
  color: #666;
  margin: 4px 0;
}

.item-price {
  color: #9E8CB9;
  font-weight: bold;
  margin-bottom: 4px;
}

.cart-footer {
  margin-top: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.total {
  font-size: 16px;
}

.price {
  color: #9E8CB9;
  font-weight: bold;
  font-size: 20px;
}
</style> 