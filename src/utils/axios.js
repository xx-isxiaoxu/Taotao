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
            // 调试用：打印请求头
            console.log('请求头 Authorization:', config.headers['Authorization'])
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// 响应拦截器
instance.interceptors.response.use(
    response => response,
    async error => {
        if (error.response?.status === 401 && error.response.data.message === 'token过期了！') {
            const refreshToken = localStorage.getItem('refreshToken')
            if (refreshToken) {
                // 调用刷新 token 接口
                const res = await axios.post('/api/auth/refreshToken', { refreshToken })
                if (res.data?.data?.token) {
                    localStorage.setItem('token', res.data.data.token)
                    // 重新发起原请求
                    error.config.headers['Authorization'] = `Bearer ${res.data.data.token}`
                    return instance(error.config)
                }
            }
            // 刷新失败，跳转登录
            localStorage.removeItem('token')
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)

export default instance 