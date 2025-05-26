import axios from 'axios'

// 创建axios实例
const instance = axios.create({
    baseURL: 'http://127.0.0.1:8080',  // 你的后端地址
    timeout: 5000,
    withCredentials: true  // 允许跨域携带cookie
})

// 请求拦截器
instance.interceptors.request.use(
    config => {
        // 从localStorage获取token
        const token = localStorage.getItem('token')
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// 响应拦截器
instance.interceptors.response.use(
    response => {
        return response
    },
    error => {
        if (error.response?.status === 401) {
            // token过期或无效，清除token并跳转到登录页
            localStorage.removeItem('token')
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)

export default instance 