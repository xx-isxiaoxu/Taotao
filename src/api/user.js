import request from '@/utils/axios'

// 设置后端基础地址
const BASE_URL = 'http://127.0.0.1:8080'

// 注册接口
export function register(data) {
  return request.post('/user/register', data)
}

// 登录接口
export function login(data) {
  return request.post('/user/login', data)
}
