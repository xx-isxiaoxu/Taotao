import request from '@/utils/axios'

const BASE_URL = ''

export function getGoodsList(params) {
  return request.get(`/api/goods/all`, { params })
}

// 获取热门商品（8个）
export function getHotGoods() {
  return request.get(`/api/goods/hot`)
}

// 获取新品上架（6个）
export function getNewGoods() {
  return request.get(`/api/goods/new`)
}

// 商品搜索
export function searchGoods(params) {
  return request.get(`/api/goods/search`, { params })
}

// 获取商品详情（根据商品ID）
export function getGoodsDetailByGoodsId(goodsId) {
  return request.get(`/api/goodsDetail/byGoodsId/${goodsId}`)
}

export function getFullProductDetail(goodsId) {
  return request.get(`/api/goods/fullDetail/${goodsId}`)
}

export const getGoodsDetail = getGoodsDetailByGoodsId
