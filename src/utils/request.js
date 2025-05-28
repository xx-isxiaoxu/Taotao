import axios from 'axios'

const request = axios.create({
  baseURL: 'http://127.0.0.1:8080', // 根据你的后端实际地址调整
  timeout: 10000
})

// 可选：请求/响应拦截器
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers['Authorization'] = token
  }
  return config
})

export default request
