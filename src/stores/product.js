import { defineStore } from 'pinia'
import { getGoodsList } from '@/api/goods'

export const useProductStore = defineStore('product', {
  state: () => ({
    productDetail: null,
    productList: []
  }),
  actions: {
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