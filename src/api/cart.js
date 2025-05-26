import request from '@/utils/request' // 你的 axios 封装

// 查询用户所有购物车项（分页）
export function getCartList(userId, pageNum = 1, pageSize = 10) {
  return request.get('/api/cart/all', {
    params: { userId, pageNum, pageSize }
  })
}

// 新增购物车项
export function addCart(data) {
  return request.post('/api/cart/insert', data)
}

// 更新购物车项
export function updateCart(data) {
  return request.put('/api/cart/update', data)
}

// 购物车商品数量+1
export function addOneToCart(userId, goodsId, specs) {
  return request.put('/api/cart/addone', null, {
    params: { userId, goodsId, specs }
  })
}

// 购物车商品数量-1
export function subOneFromCart(userId, goodsId, specs) {
  return request.put('/api/cart/subone', null, {
    params: { userId, goodsId, specs }
  })
}

// 删除单个购物车项
export function deleteCartItem(id) {
  return request.delete(`/api/cart/deleteone/${id}`)
}

// 清空用户购物车
export function clearCart(userId) {
  return request.delete(`/api/cart/deleteall/${userId}`)
}

// 结算购物车
export function payCart(userId) {
  return request.post(`/api/cart/pay/${userId}`)
}
