<template>
  <div class="product-card">
    <!-- ... 其他内容 ... -->
    <div class="product-actions">
      <el-button 
        type="primary" 
        class="add-cart-btn"
        @click="handleAddToCart"
      >
        加入购物车
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { ElMessage } from 'element-plus'

const props = defineProps({
  product: {
    type: Object,
    required: true
  }
})

const router = useRouter()
const cartStore = useCartStore()

// 处理加入购物车
const handleAddToCart = () => {
  // 检查商品是否有规格
  if (props.product.specs && props.product.specs.length > 0) {
    // 如果有规格，跳转到商品详情页
    router.push(`/product/${props.product.id}`)
    ElMessage.info('请选择商品规格')
  } else {
    // 如果没有规格，直接加入购物车
    const success = cartStore.addToCart(props.product, 1, {})
    if (success) {
      ElMessage.success('成功加入购物车')
    }
  }
}
</script> 