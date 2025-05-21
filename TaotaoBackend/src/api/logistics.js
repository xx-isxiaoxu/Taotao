// 获取物流信息
export const getLogisticsInfo = (orderId, toAddress = '') => {
  // return axios.get(`/api/logistics/${orderId}`)
  // mock:
  return Promise.resolve({
    orderId,
    status: '运输中',
    from: '北京市朝阳区望京SOHO',
    to: toAddress || '上海市浦东新区张江高科',
    current: '上海转运中心',
    steps: [
      { time: '2024-05-13 10:00', desc: '包裹已揽收' },
      { time: '2024-05-14 08:00', desc: '到达北京转运中心' },
      { time: '2024-05-15 12:00', desc: '到达上海转运中心' }
    ]
  })
}

