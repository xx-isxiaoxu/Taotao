import { defineStore } from 'pinia'
import { ref, computed, watch } from 'vue'

// 从localStorage加载评价数据
const loadReviewsFromStorage = () => {
  try {
    const savedReviews = localStorage.getItem('productReviews')
    return savedReviews ? JSON.parse(savedReviews) : []
  } catch (error) {
    console.error('加载评价数据失败:', error)
    return []
  }
}

// 保存评价数据到localStorage
const saveReviewsToStorage = (reviews) => {
  try {
    localStorage.setItem('productReviews', JSON.stringify(reviews))
  } catch (error) {
    console.error('保存评价数据失败:', error)
  }
}

export const useReviewStore = defineStore('review', () => {
  const reviews = ref(loadReviewsFromStorage())
  
  // 监听评价数据变化，自动保存到localStorage
  watch(reviews, (newReviews) => {
    saveReviewsToStorage(newReviews)
  }, { deep: true })
  
  // 获取指定商品的评价
  const getProductReviews = (productId) => {
    return reviews.value.filter(review => review.productId === productId)
  }
  
  // 添加评价
  const addReview = (review) => {
    reviews.value.unshift({
      ...review,
      id: Date.now(),
      time: new Date().toISOString()
    })
  }
  
  // 删除评价
  const removeReview = (reviewId) => {
    const index = reviews.value.findIndex(review => review.id === reviewId)
    if (index > -1) {
      reviews.value.splice(index, 1)
    }
  }
  
  return {
    reviews,
    getProductReviews,
    addReview,
    removeReview
  }
}) 