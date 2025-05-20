import { defineStore } from 'pinia'
import { ref } from 'vue'
import { nanoid } from 'nanoid'

export const useOrderStore = defineStore('order', () => {
  const orders = ref([])

  const createOrder = (orderData) => {
    const now = Date.now();
    const order = {
      id: nanoid(),
      orderNo: 'NO' + now,
      ...orderData,
      status: 'unpaid',
      createTime: new Date(now).toISOString(),
      payTime: null,
      cancelTime: null,
      expireTime: now + 3 * 60 * 1000 // 3分钟后过期
    }
    orders.value.unshift(order)
    return order.id
  }

  const payOrder = ({ orderId }) => {
    const order = orders.value.find(o => o.id === orderId)
    if (order) {
      order.status = 'paid'
      order.payTime = new Date().toISOString()
    }
    console.log('payOrder called with orderId:', orderId, 'orders:', orders.value)
  }

  const cancelOrder = (orderId) => {
    const order = orders.value.find(o => o.id === orderId)
    if (order) {
      order.status = 'cancelled'
      order.cancelTime = new Date().toISOString()
    }
  }

  const getOrderDetail = (orderId) => {
    return orders.value.find(o => o.id === orderId)
  }

  const removeOrder = (orderId) => {
    const idx = orders.value.findIndex(o => o.id === orderId)
    if (idx !== -1) {
      orders.value.splice(idx, 1)
    }
  }

  return {
    orders,
    createOrder,
    payOrder,
    cancelOrder,
    getOrderDetail,
    removeOrder
  }
}, {
  persist: true
})
