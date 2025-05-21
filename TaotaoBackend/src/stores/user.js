import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo')) || {
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
      this.userInfo = {
        username: '',
        nickname: '',
        phone: '',
        email: '',
        createTime: '',
        avatar: '',
        addressList: []
      }
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    },

    updateUserInfo(info) {
      this.userInfo = { ...this.userInfo, ...info }
      localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
    },
  }
})