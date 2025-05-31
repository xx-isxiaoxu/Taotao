<template>
  <div class="auth-container">
    <div class="logo">
      <img src="/logo2.png" alt="Logo">
    </div>
    <div class="auth-box">
      <h2>注册淘淘账号</h2>
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef">
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" placeholder="用户名">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="确认密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="registerForm.phone" placeholder="手机号">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="submit-btn" :loading="loading" @click="handleRegister">
            {{ loading ? '注册中...' : '注册' }}
          </el-button>
        </el-form-item>
      </el-form>
      <div class="auth-footer">
        <span>已有账号？</span>
        <router-link to="/login">立即登录</router-link>
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
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  phone: ''
})

const validatePass2 = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3到20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validatePass2, trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await userStore.registerUser({
          username: registerForm.username,
          password: registerForm.password,
          phone: registerForm.phone
        })
        ElMessage.success('注册成功')
        router.push('/home')
      } catch (error) {
        ElMessage.error(error.message)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.auth-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url('/background.png') no-repeat center center;
  background-size: cover;
}

.auth-box {
  width: 400px;
  padding: 30px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.logo {
  text-align: center;
  margin-bottom: 20px;
}

.logo img {
  height: 40px;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.verify-code {
  display: flex;
  gap: 10px;
}

.verify-code .el-button {
  flex-shrink: 0;
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
  text-align: center;
}

.auth-footer a {
  color: #ff69b4;
  text-decoration: none;
  margin-left: 5px;
}
</style>