import request from '@/utils/request'

// 创建订单
export function createOrder(data) {
  return request.post('/api/order/create', data)
}

// 查询订单列表
export function getOrderList(userId, pageNum = 1, pageSize = 10) {
  return request.get('/api/order/list', { params: { userId, pageNum, pageSize } })
}

// 查询订单详情
export function getOrderDetail(orderId) {
  return request.get(`/api/order/detail/${orderId}`)
}

// 支付订单
export function payOrder(orderId) {
  return request.post(`/api/order/pay/${orderId}`)
}

// 取消订单
export function cancelOrder(orderId) {
  return request.post(`/api/order/cancel/${orderId}`)
}

// 删除订单
export function deleteOrder(orderId) {
  return request.delete(`/api/order/delete/${orderId}`)
}
