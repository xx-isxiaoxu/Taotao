<template>
  <div class="user-profile">
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" class="profile-form">
      <el-form-item label="用户名">
        <el-input v-model="form.username" disabled />
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="form.nickname" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSave">保存修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const form = ref({
  username: '',
  nickname: '',
  phone: '',
  email: ''
})

const rules = {
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }]
}

const formRef = ref(null)

const initForm = () => {
  form.value = {
    username: userStore.userInfo?.username || '',
    nickname: userStore.userInfo?.nickname || '',
    phone: userStore.userInfo?.phone || '',
    email: userStore.userInfo?.email || ''
  }
}
initForm()
watch(() => userStore.userInfo, initForm, { deep: true })

const handleSave = () => {
  formRef.value.validate((valid) => {
    if (!valid) return
    userStore.updateUserInfo({
      nickname: form.value.nickname,
      phone: form.value.phone,
      email: form.value.email
    })
    ElMessage.success('保存成功')
  })
}
</script>

<style scoped>
.profile-form {
  max-width: 500px;
  margin: 100px auto;
}

.el-button--primary {
  background-color: #ff69b4;
  border-color: #ff69b4;
}
.el-button--primary:hover {
  background-color: #ff85c0;
  border-color: #ff85c0;
}
</style>