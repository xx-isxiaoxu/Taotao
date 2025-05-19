<template>
  <div class="review-form">
    <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
      <el-form-item label="评分" prop="rating">
        <el-rate v-model="form.rating" />
      </el-form-item>
      
      <el-form-item label="评价内容" prop="content">
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="4"
          placeholder="请分享您对商品的使用体验"
        />
      </el-form-item>
      
      <el-form-item label="上传图片">
        <el-upload
          action="#"
          list-type="picture-card"
          :auto-upload="false"
          :on-change="handleImageChange"
          :on-remove="handleImageRemove"
          :limit="5"
        >
          <el-icon><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="submitReview">提交评价</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useReviewStore } from '@/stores/review'
import { useUserStore } from '@/stores/user'

const props = defineProps({
  productId: {
    type: [Number, String],
    required: true
  }
})

const emit = defineEmits(['submit-success'])

const reviewStore = useReviewStore()
const userStore = useUserStore()
const formRef = ref(null)

const form = reactive({
  rating: 5,
  content: '',
  images: []
})

const rules = {
  rating: [
    { required: true, message: '请选择评分', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入评价内容', trigger: 'blur' },
    { min: 10, message: '评价内容至少10个字符', trigger: 'blur' }
  ]
}

const handleImageChange = (file) => {
  // 这里应该实现图片上传到服务器的逻辑
  // 现在先用本地URL模拟
  const reader = new FileReader()
  reader.readAsDataURL(file.raw)
  reader.onload = () => {
    form.images.push(reader.result)
  }
}

const handleImageRemove = (file) => {
  const index = form.images.indexOf(file.url)
  if (index > -1) {
    form.images.splice(index, 1)
  }
}

const submitReview = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      // 检查是否登录
      if (!userStore.userInfo?.username) {
        ElMessage.warning('请先登录后再评价')
        return
      }
      
      // 提交评价
      reviewStore.addReview({
        productId: props.productId,
        userName: userStore.userInfo.nickname || userStore.userInfo.username,
        userAvatar: userStore.userInfo.avatar,
        rating: form.rating,
        content: form.content,
        images: form.images
      })
      
      ElMessage.success('评价成功')
      resetForm()
      emit('submit-success')
    }
  })
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  form.images = []
}
</script>

<style scoped>
.review-form {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

:deep(.el-upload--picture-card) {
  width: 100px;
  height: 100px;
  line-height: 100px;
}
</style> 