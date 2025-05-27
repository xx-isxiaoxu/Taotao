import { defineStore } from 'pinia'
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCartList, addCart, updateCart, deleteCartItem, clearCart, payCart } from '@/api/cart'
import { useUserStore } from '@/stores/user'

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

export const useCartStore = defineStore('cart', {
  state: () => ({
    cartItems: [],
    totalPrice: 0
  }),
  getters: {
    cartCount(state) {
      // 统计所有商品的数量总和
      return state.cartItems.reduce((sum, item) => sum + Number(item.quantity), 0)
    }
  },
  actions: {
    getUserIdOrWarn() {
      const userStore = useUserStore()
      const userId = userStore.userInfo?.id
      if (!userId) {
        ElMessage.warning('请先登录')
        throw new Error('未登录')
      }
      return userId
    },
    async fetchCart() {
      const userId = this.getUserIdOrWarn()
      const res = await getCartList(userId)
      this.cartItems = res.data?.data?.list || []
      this.totalPrice = this.cartItems.reduce((sum, item) => sum + item.goodsPrice * item.quantity, 0)
    },
    async addToCart(product, quantity = 1, specsObj = {}) {
      const userId = this.getUserIdOrWarn()
      const specsStr = JSON.stringify(specsObj)
      const data = {
        userId,
        goodsId: product.id,
        goodsName: product.name,
        goodsImage: product.image || (product.images && product.images[0]),
        goodsPrice: product.price,
        specs: specsStr,
        quantity
      }
      const res = await addCart(data)
      await this.fetchCart()
      return res
    },
    async updateQuantity(id, specs, quantity) {
      const userId = this.getUserIdOrWarn()
      const item = this.cartItems.find(i => i.id === id && i.specs === specs)
      if (!item) return
      const newItem = { ...item, quantity }
      const res = await updateCart(newItem)
      await this.fetchCart()
      return res
    },
    async removeFromCart(id) {
      const userId = this.getUserIdOrWarn()
      const res = await deleteCartItem(id)
      await this.fetchCart()
      return res
    },
    async clearCart() {
      const userId = this.getUserIdOrWarn()
      const res = await clearCart(userId)
      await this.fetchCart()
      return res
    },
    async payCart() {
      const userId = this.getUserIdOrWarn()
      const res = await payCart(userId)
      await this.fetchCart()
      return res
    }
  }
}) 