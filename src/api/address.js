import request from '@/utils/request'

// 获取用户地址列表
export function getAddressList(userId) {
  return request.get(`/api/address/user/${userId}`)
}

// 新增地址
export function addAddress(data) {
  return request.post('/api/address', data)
}

// 更新地址
export function updateAddress(id, data) {
  return request.put(`/api/address/${id}`, data)
}

// 删除地址
export function deleteAddress(id) {
  return request.delete(`/api/address/${id}`)
}

// 设置默认地址
export function setDefaultAddress(userId, addressId) {
  return request.put(`/api/address/default/${userId}/${addressId}`)
}
