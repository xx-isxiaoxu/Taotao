import { defineStore } from 'pinia'
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

// 从localStorage加载购物车数据
const loadCartFromStorage = () => {
  try {
    const savedCart = localStorage.getItem('cart')
    return savedCart ? JSON.parse(savedCart) : []
  } catch (error) {
    console.error('加载购物车数据失败:', error)
    return []
  }
}

// 保存购物车数据到localStorage
const saveCartToStorage = (cart) => {
  try {
    localStorage.setItem('cart', JSON.stringify(cart))
  } catch (error) {
    console.error('保存购物车数据失败:', error)
  }
}

export const useCartStore = defineStore('cart', () => {
  const cartItems = ref(loadCartFromStorage())
  const router = useRouter()
  
  // 监听购物车数据变化，自动保存到localStorage
  watch(cartItems, (newCart) => {
    saveCartToStorage(newCart)
  }, { deep: true })
  
  // 添加商品到购物车
  const addToCart = (product, quantity = 1, specs = {}) => {
    // 检查商品是否有规格选项
    if (product.specs && product.specs.length > 0) {
      // 检查是否选择了所有必要的规格
      const missingSpecs = product.specs.filter(spec => !specs[spec.name])
      if (missingSpecs.length > 0) {
        return false // 返回 false 表示需要选择规格
      }
    }

    const existingItem = cartItems.value.find(item => 
      item.id === product.id && 
      JSON.stringify(item.specs) === JSON.stringify(specs)
    )
    
    if (existingItem) {
      existingItem.quantity += quantity
    } else {
      cartItems.value.push({
        id: product.id,
        name: product.name,
        price: product.price,
        image: product.image || product.images?.[0],
        quantity,
        specs,
        selected: false,
        addTime: new Date().toISOString(),
        hasSpecs: product.specs && product.specs.length > 0
      })
    }
    return true // 返回 true 表示成功添加
  }
  
  // 检查商品是否需要选择规格
  const needsSpecs = (product) => {
    return product.specs && product.specs.length > 0
  }
  
  // 检查购物车中的商品是否都有规格
  const allItemsHaveSpecs = () => {
    return cartItems.value.every(item => {
      if (!item.hasSpecs) return true // 如果商品本身没有规格选项，返回 true
      return item.specs && Object.keys(item.specs).length > 0
    })
  }
  
  // 获取选中的商品
  const getSelectedItems = () => {
    return cartItems.value.filter(item => item.selected)
  }
  
  // 计算购物车商品总数
  const cartCount = computed(() => {
    return cartItems.value.reduce((total, item) => total + item.quantity, 0)
  })
  
  // 计算购物车总价
  const totalPrice = computed(() => {
    return cartItems.value.reduce((total, item) => {
      return total + item.price * item.quantity
    }, 0).toFixed(2)
  })
  
  // 计算选中商品的总价
  const selectedTotalPrice = computed(() => {
    return cartItems.value
      .filter(item => item.selected)
      .reduce((total, item) => total + item.price * item.quantity, 0)
      .toFixed(2)
  })
  
  // 更新购物车商品数量
  const updateQuantity = (itemId, specs, quantity) => {
    const item = cartItems.value.find(item => 
      item.id === itemId && 
      JSON.stringify(item.specs) === JSON.stringify(specs)
    )
    if (item) {
      item.quantity = quantity
    }
  }
  
  // 从购物车移除商品
  const removeFromCart = (itemId, specs) => {
    const index = cartItems.value.findIndex(item => 
      item.id === itemId && 
      JSON.stringify(item.specs) === JSON.stringify(specs)
    )
    if (index > -1) {
      cartItems.value.splice(index, 1)
    }
  }
  
  // 清空购物车
  const clearCart = () => {
    cartItems.value = []
  }
  
  // 选择/取消选择所有商品
  const toggleSelectAll = (selected) => {
    cartItems.value.forEach(item => {
      item.selected = selected
    })
  }
  
  const checkoutItems = ref([])
  
  const removeOrder = (orderId) => {
    const idx = checkoutItems.value.findIndex(o => o.id === orderId)
    if (idx !== -1) {
      checkoutItems.value.splice(idx, 1)
    }
  }
  
  const handleAddToCart = () => {
    router.push(`/product/${product.id}`)
    ElMessage.info('请先选择商品规格')
  }
  
  const handleCheckout = () => {
    const selectedItems = cartStore.getSelectedItems()
    if (selectedItems.length === 0) {
      ElMessage.warning('请选择要结算的商品')
      return
    }
    // 检查所有商品是否都选择了规格
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
    localStorage.setItem('checkoutItems', JSON.stringify(selectedItems))
    router.push('/order/confirm')
  }
  
  return {
    cartItems,
    cartCount,
    totalPrice,
    selectedTotalPrice,
    addToCart,
    getSelectedItems,
    needsSpecs,
    allItemsHaveSpecs,
    updateQuantity,
    removeFromCart,
    clearCart,
    toggleSelectAll,
    checkoutItems,
    removeOrder,
    handleAddToCart,
    handleCheckout
  }
}) 