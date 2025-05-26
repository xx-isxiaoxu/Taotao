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
import { addCart } from '@/api/cart'
import { useUserStore } from '@/stores/user'
import { computed, onMounted } from 'vue'

const props = defineProps({
  product: {
    type: Object,
    required: true
  }
})

const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()
const userId = computed(() => userStore.userInfo?.id)

onMounted(() => {
  cartStore.fetchCart()
})

// 购物车列表：cartStore.cartItems
// 总价：cartStore.totalPrice

const updateQuantity = (item, quantity) => {
  cartStore.updateQuantity(item.id, item.specs, quantity)
}
const removeItem = (item) => {
  cartStore.removeFromCart(item.id)
}
const clearAll = () => {
  cartStore.clearCart()
}

// 处理加入购物车
const handleAddToCart = async () => {
  // 检查是否选择了所有必要的规格
  const missingSpecs = props.product.specs?.filter(spec => !selectedSpecs.value[spec.name])
  if (missingSpecs?.length > 0) {
    ElMessage.warning('请选择商品规格')
    return
  }
  if (!userId.value) {
    ElMessage.warning('请先登录')
    return
  }
  // 组装 specs 字段为 JSON 字符串
  const specsStr = JSON.stringify(selectedSpecs.value)
  const data = {
    userId: userId.value,
    goodsId: props.product.id,
    goodsName: props.product.name,
    goodsImage: props.product.image || productImages.value[0],
    goodsPrice: props.product.price,
    specs: specsStr,
    quantity: quantity.value
  }
  const res = await addCart(data)
  if (res.data && res.data.code === 200) {
    ElMessage.success('成功加入购物车！')
    // 可选：刷新购物车列表
    // fetchCart()
  } else {
    ElMessage.error(res.data?.message || '加入购物车失败')
  }
}
</script> 