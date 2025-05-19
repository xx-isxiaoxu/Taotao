<template>
  <div class="user-center">
    <!-- 添加背景图层，与首页保持一致 -->
    <div class="background-image"></div>
    
    <el-card class="user-center-card">
      <template #header>
        <div class="card-header">
          <div class="header-actions">
            <el-button type="primary" @click="goHome">返回首页</el-button>
            <el-button type="danger" @click="logout">退出登录</el-button>
          </div>
          <div class="user-brief">
            <el-avatar :size="64" :src="userStore.userInfo?.avatar">
              {{ userStore.userInfo?.nickname?.charAt(0) || userStore.userInfo?.username?.charAt(0) }}
            </el-avatar>
            <div class="user-info">
              <h3>{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</h3>
              <p>注册时间：{{ formatDate(userStore.userInfo?.createTime) }}</p>
            </div>
          </div>
        </div>
      </template>

      <el-tabs v-model="activeTab" class="user-tabs">
        <el-tab-pane label="个人信息" name="profile">
          <user-profile />
        </el-tab-pane>
        <el-tab-pane label="收货地址" name="address">
          <user-address />
        </el-tab-pane>
        <el-tab-pane label="我的订单" name="orders">
          <user-orders />
        </el-tab-pane>
        <el-tab-pane label="我的收藏" name="favorites">
          <user-favorites />
        </el-tab-pane>
        <el-tab-pane label="数据统计" name="statistics">
          <user-statistics />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import UserProfile from './components/UserProfile.vue'
import UserAddress from './components/UserAddress.vue'
import UserOrders from './components/UserOrders.vue'
import UserFavorites from './components/UserFavorites.vue'
import UserStatistics from './UserStatistics.vue'

const userStore = useUserStore()
const router = useRouter()
const route = useRoute()
const activeTab = ref('profile')

const goHome = () => {
  router.push('/home')
}
const logout = () => {
  userStore.logout()
  router.push('/login')
}
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString()
}

onMounted(() => {
  if (route.query.tab === 'orders') {
    activeTab.value = 'orders'
  }
})
</script>

<style scoped>
.user-center {
  /* min-height: 100vh; */
  position: relative;
  padding: 24px 0;
  background: #f5f6f7;
  display: flex;
  flex-direction: column;
}

.background-image {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: url('/Images/background.png') center center/cover no-repeat;
  opacity: 0.15;
  z-index: 0;
  pointer-events: none;
}

.user-center-card {
  position: relative;
  z-index: 1;
  width: 98%;
  max-width: 1800px;
  margin: 0 auto;
  flex: 1;
  min-height: 800px;
  background: rgba(255,255,255,0.96);
  border-radius: 32px;
  box-shadow: 0 8px 48px rgba(0,0,0,0.10);
  flex-direction: column;  /* 垂直排列 */
  overflow-y: auto;  /* 内容过多时可滚动 */
}

.card-header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.user-brief {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  text-align: left;
}

.user-info h3 {
  margin: 0;
  font-size: 24px;
  color: #333;
  font-weight: bold;
}

.user-info p {
  margin: 8px 0 0;
  color: #666;
  font-size: 14px;
}

.user-tabs {
  padding: 24px;
}

.header-actions {
  display: flex;
  gap: 16px;
}

/* 主题色统一 */
.el-button--primary {
  background-color: #ff69b4;
  border-color: #ff69b4;
}

.el-button--primary:hover {
  background-color: #ff85c0;
  border-color: #ff85c0;
}

.el-tabs__item.is-active {
  color: #ff69b4 !important;
}

.el-tabs__active-bar {
  background-color: #ff69b4 !important;
}

@media (max-width: 1920px) {
  .user-center-card {
    width: 95%;
    border-radius: 18px;
  }
  
  .card-header {
    padding: 16px;
  }
  
  .user-tabs {
    padding: 16px;
  }
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    gap: 16px;
  }
  
  .header-actions {
    width: 100%;
    justify-content: center;
  }
}
</style>


// 在组件导入列表中添加
const components = {
  UserProfile,
  UserAddress,
  UserOrders,
  UserFavorites,
  UserStatistics
}