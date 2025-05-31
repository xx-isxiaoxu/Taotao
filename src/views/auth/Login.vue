<template>
  <div class="auth-container">
    <div class="logo">
      <img src="/logo2.png" alt="Logo">
    </div>
    <div class="auth-box">
      <h2>登录淘淘账号</h2>
      <el-tabs v-model="loginType">
        <el-tab-pane label="账号密码登录" name="account"></el-tab-pane>
        <el-tab-pane label="手机验证码登录" name="phone"></el-tab-pane>
      </el-tabs>
      <el-form v-if="loginType === 'account'" :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item prop="username">   
          <el-input v-model="loginForm.username" placeholder="用户名">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            class="submit-btn" 
            :loading="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>
      <el-form v-else :model="phoneForm" :rules="phoneRules" ref="phoneFormRef">
        <el-form-item prop="phone">
          <el-input v-model="phoneForm.phone" placeholder="手机号">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input v-model="phoneForm.code" placeholder="验证码" style="width: 60%;">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
          <el-button :disabled="countdown > 0" @click="sendCode" style="margin-left: 10px;">
            {{ countdown > 0 ? countdown + 's后重试' : '获取验证码' }}
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            class="submit-btn" 
            :loading="loading"
            @click="handlePhoneLogin"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>
      <div class="auth-footer">
        <router-link to="/register">注册账号</router-link>
        <a href="#">忘记密码?</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loginFormRef = ref(null)
const phoneFormRef = ref(null)
const loading = ref(false)
const loginType = ref('account')

const loginForm = reactive({
  username: '',
  password: ''
})

const phoneForm = reactive({
  phone: '',
  code: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3到20个字符之间', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]+$/, message: '用户名只能包含字母、数字、下划线和中文', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ]
}

const phoneRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
}

// 验证码倒计时
const countdown = ref(0)
let timer = null
const sendCode = async () => {
  if (!phoneForm.phone || !/^1[3-9]\d{9}$/.test(phoneForm.phone)) {
    ElMessage.error('请输入正确的手机号')
    return
  }
  try {
    await userStore.handleSendCode(phoneForm.phone)
    // 这里开发环境下会弹出验证码
  } catch (err) {
    ElMessage.error(err.message || '发送验证码失败')
  }
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    const res = await userStore.loginUser({
      username: loginForm.username,
      password: loginForm.password
    })
    console.log('登录返回', res)
    ElMessage.success('登录成功')
    router.push('/home')
  } catch (error) {
    ElMessage.error(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}

const handlePhoneLogin = async () => {
  if (!phoneFormRef.value) return
  try {
    await phoneFormRef.value.validate()
    loading.value = true
    await userStore.loginByPhone(phoneForm.phone, phoneForm.code)
    ElMessage.success('登录成功')
    router.push('/home')
  } catch (error) {
    ElMessage.error(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: url('/background.png') no-repeat center center;
  background-size: cover;
  position: relative; /* 添加相对定位 */
}

.auth-box {
  width: 400px;
  padding: 30px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  position: relative; /* 确保z-index生效 */
  z-index: 10;  /* 增加z-index值，确保高于logo */
}

.logo {
  position: absolute;
  top: -80px; /* 调整logo向上偏移 */
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
  z-index: 1;  /* 降低logo的z-index值，确保低于auth-box */
}

.logo img {
  height: 500px;
  width: auto;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.submit-btn {
  width: 100%;
  background-color: #ff69b4;
  border-color: #ff69b4;
}

.submit-btn:hover {
  background-color: #ff5ba7;
  border-color: #ff5ba7;
}

.auth-footer {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
}

.auth-footer a {
  color: #ff69b4;
  text-decoration: none;
}
</style>