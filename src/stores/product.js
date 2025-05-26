import { defineStore } from 'pinia'
import { getGoodsList } from '@/api/good'

export const useProductStore = defineStore('product', {
  state: () => ({
    productDetail: null,
    productList: []
  }),
  actions: {
    async getProductDetail(id) {
      try {
        const product = this.productList.find(p => p.id === parseInt(id))
        if (product) {
          this.productDetail = product
        } else {
          throw new Error('商品不存在')
        }
      } catch (error) {
        throw new Error('获取商品详情失败')
      }
    },
    async getProductList(pageNum = 1, pageSize = 10) {
      const res = await getGoodsList({ pageNum, pageSize })
      if (res.data && res.data.data && Array.isArray(res.data.data.list)) {
        this.productList = res.data.data.list
      return this.productList
      }
      return []
    }
  }
})