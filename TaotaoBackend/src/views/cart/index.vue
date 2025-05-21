<script setup>
// ... 其他导入
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { ElMessage } from 'element-plus'

const router = useRouter()
const cartStore = useCartStore()

// 处理结算
const handleCheckout = () => {
  const selectedItems = cartStore.getSelectedItems()
  if (selectedItems.length === 0) {
    ElMessage.warning('请选择要结算的商品')
    return
  }
  // 检查所有商品是否都选择了规格（无论商品本身是否有规格字段）
  const hasNoSpecs = selectedItems.some(item => !item.specs || Object.keys(item.specs).length === 0)
  if (hasNoSpecs) {
    ElMessage.warning('请先选择商品规格')
    // 跳转到第一个未选择规格的商品详情页
    const firstNoSpec = selectedItems.find(item => !item.specs || Object.keys(item.specs).length === 0)
    if (firstNoSpec) {
      router.push(`/product/${firstNoSpec.id}`)
    }
    return
  }
  // 所有商品都选择了规格，允许结算
  router.push('/order/confirm')
}
</script> 