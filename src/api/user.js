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

// 发送手机验证码
export function sendCode(phone) {
  return request.post('/api/auth/sendCode', { phone })
}

// 手机验证码登录
export function loginByPhone(data) {
  return request.post('/api/auth/loginByPhone', data)
}
