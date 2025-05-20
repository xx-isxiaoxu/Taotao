import { defineStore } from 'pinia'
import { ref, computed, watch } from 'vue'

// 从localStorage加载收藏数据
const loadCollectsFromStorage = () => {
  try {
    const savedCollects = localStorage.getItem('collects')
    return savedCollects ? JSON.parse(savedCollects) : []
  } catch (error) {
    console.error('加载收藏数据失败:', error)
    return []
  }
}

// 保存收藏数据到localStorage
const saveCollectsToStorage = (collects) => {
  try {
    localStorage.setItem('collects', JSON.stringify(collects))
  } catch (error) {
    console.error('保存收藏数据失败:', error)
  }
}

export const useCollectStore = defineStore('collect', () => {
  const collects = ref(loadCollectsFromStorage())
  
  // 监听收藏数据变化，自动保存到localStorage
  watch(collects, (newCollects) => {
    saveCollectsToStorage(newCollects)
  }, { deep: true })
  
  // 检查商品是否已收藏
  const isCollected = (productId) => {
    return collects.value.some(item => item.id === productId)
  }
  
  // 添加收藏
  const addCollect = (product) => {
    if (!isCollected(product.id)) {
      collects.value.push({
        ...product,
        collectTime: new Date().toISOString()
      })
    }
  }
  
  // 取消收藏
  const removeCollect = (productId) => {
    const index = collects.value.findIndex(item => item.id === productId)
    if (index > -1) {
      collects.value.splice(index, 1)
    }
  }
  
  // 获取收藏列表
  const getCollects = () => {
    return collects.value
  }
  
  return {
    collects,
    isCollected,
    addCollect,
    removeCollect,
    getCollects
  }
}) 