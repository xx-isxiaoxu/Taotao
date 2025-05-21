import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getLogisticsInfo } from '@/api/logistics'

export const useLogisticsStore = defineStore('logistics', {
  state: () => ({
    logisticsInfo: null
  }),
  actions: {
    async fetchLogistics(orderId, toAddress) {
      console.log('获取物流信息，参数:', {
        orderId,
        toAddress
      })
      const res = await getLogisticsInfo(orderId, toAddress)
      this.logisticsInfo = res
    }
  }
})
