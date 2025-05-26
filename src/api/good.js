import axios from 'axios'

const BASE_URL = 'http://127.0.0.1:8080'

export function getGoodsList(params) {
  return axios.get(`${BASE_URL}/api/goods/all`, { params })
}

// 获取热门商品（8个）
export function getHotGoods() {
  return axios.get(`${BASE_URL}/api/goods/hot`)
}

// 获取新品上架（6个）
export function getNewGoods() {
  return axios.get(`${BASE_URL}/api/goods/new`)
}

// 商品搜索
export function searchGoods(params) {
  return axios.get(`${BASE_URL}/api/goods/search`, { params })
}
