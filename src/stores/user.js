import { defineStore } from 'pinia'
import { register, login, sendCode, loginByPhone as loginByPhoneApi } from '@/api/user'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo')) || {
      id: null,
      username: '',
      nickname: '',
      phone: '',
      email: '',
      createTime: '',
      avatar: '',
      addressList: []
    },
    addressForm: {
      region: [],
      address: '',
      name: '',
      phonePrefix: '+86',
      phone: '',
      isDefault: false,
      id: null
    }
  }),
  persist: true,
  
  actions: {
    // 设置token
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    
    // 设置用户信息
    setUserInfo(userInfo) {
      this.userInfo = userInfo
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    
    // 修改登录方法
    async login(username, password) {
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          const users = JSON.parse(localStorage.getItem('users')) || []
          const user = users.find(u => u.username === username)
          
          if (!user) {
            reject(new Error('用户不存在'))
            return
          }
          
          if (user.password !== password) {
            reject(new Error('密码错误'))
            return
          }
          
          // 生成token (实际项目中应该由后端生成)
          const token = 'token_' + Math.random().toString(36).substr(2)
          
          // 保存token和用户信息
          this.setToken(token)
          this.setUserInfo(user)
          
          resolve({ token, user })
        }, 1000)
      })
    },
    
    // 注册
    async register(username, password) {
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          const users = JSON.parse(localStorage.getItem('users')) || []
          
          // 检查用户名是否已存在
          if (users.some(u => u.username === username)) {
            reject(new Error('用户名已存在'))
            return
          }
          
          // 创建新用户
          const newUser = {
            id: Date.now(),
            username,
            password,
            createTime: new Date().toISOString()
          }
          
          users.push(newUser)
          localStorage.setItem('users', JSON.stringify(users))
          
          // 注册成功后自动登录
          this.login(username, password)
            .then(resolve)
            .catch(reject)
        }, 1000)
      })
    },
    
    // 退出登录
    logout() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    },

    updateUserInfo(info) {
      this.userInfo = { ...this.userInfo, ...info }
      localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
    },

    async registerUser(userData) {
      try {
        const res = await register(userData)
        // 处理返回数据，比如保存token、用户信息等
        return res.data
      } catch (err) {
        throw err
      }
    },

    async loginUser(userData) {
      try {
        const res = await login(userData)
        if (res.data && res.data.data && res.data.data.token) {
          this.setToken(res.data.data.token)
          // 兼容后端返回
          const userInfo = {
            id: res.data.data.id || res.data.data.userId, // 这里要有 id 字段
            username: res.data.data.username || res.data.data.userName || userData.username
          }
          this.setUserInfo(userInfo)
        }
        return res.data
      } catch (err) {
        throw err
      }
    },

    async loginByPhone(phone, code) {
      try {
        const res = await loginByPhoneApi({ phone, code })
        // 兼容扁平结构和 data 包裹结构
        const data = res.data.data || res.data
        if (data && data.token) {
          this.setToken(data.token)
          const userInfo = {
            id: data.id || data.userId,
            username: data.username || data.userName || phone,
            phone: data.phone || phone
          }
          this.setUserInfo(userInfo)
        }
        return res.data
      } catch (err) {
        throw err
      }
    },

    async handleSendCode(phone) {
      try {
        const res = await sendCode(phone)
        if (res.data.success) {
          // 开发环境下，直接显示验证码
          ElMessage.success('验证码已发送：' + res.data.code)
          // 生产环境下，不显示code
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (err) {
        throw err
      }
    }
  }
})