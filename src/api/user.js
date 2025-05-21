import axios from 'axios'

// 设置后端基础地址
const BASE_URL = 'http://127.0.0.1:8080'

// 注册接口
export function register(data) {
  return axios.post(`${BASE_URL}/user/register`, data)
}

// 登录接口
export function login(data) {
  return axios.post(`${BASE_URL}/user/login`, data)
}
