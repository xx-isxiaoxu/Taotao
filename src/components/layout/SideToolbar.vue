<template>
  <div class="side-toolbar">
    <div class="toolbar-item" @click="openChat">
      <i class="el-icon-message"></i>
      <span>咨询</span>
    </div>
    <div class="toolbar-item" @click="onHotline">
      <i class="el-icon-phone"></i>
      <span>热线</span>
    </div>
    <div class="toolbar-item" @mouseenter="showQR = true" @mouseleave="showQR = false">
      <i class="el-icon-qrcode"></i>
      <span>二维码</span>
      <div v-if="showQR" class="qr-popup">
        <img src="/Images/qrcode.png" alt="二维码" />
        <div>扫码关注</div>
      </div>
    </div>
    <div class="toolbar-item" @click="scrollToTop" v-show="showBackToTop">
      <i class="el-icon-top"></i>
      <span>返回顶部</span>
    </div>
    <el-dialog v-model="showChat" title="AI客服" width="350px" :close-on-click-modal="false">
      <div class="chat-window">
        <div v-for="(msg, idx) in chatList" :key="idx" :class="['chat-msg', msg.from]">
          <span class="msg-content">{{ msg.content }}</span>
        </div>
      </div>
      <div class="chat-input-row">
        <el-input v-model="chatInput" placeholder="请输入内容..." @keyup.enter="sendMsg" class="chat-input" />
        <el-button type="primary" @click="sendMsg" class="chat-send">发送</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElDialog, ElInput, ElButton } from 'element-plus'

const showBackToTop = ref(false)
const showQR = ref(false)

const showChat = ref(false)
const chatList = ref([
  { from: 'ai', content: '您好，这里是AI客服，有什么可以帮您？' }
])
const chatInput = ref('')

const openChat = () => {
  showChat.value = true
}
const sendMsg = () => {
  const text = chatInput.value.trim()
  if (!text) return
  chatList.value.push({ from: 'user', content: text })
  chatInput.value = ''
  setTimeout(() => {
    chatList.value.push({ from: 'ai', content: '请问有什么可以帮您' })
  }, 500)
}

const handleScroll = () => {
  showBackToTop.value = window.scrollY > 300
}

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

const onConsult = openChat
const onHotline = () => {
  window.open('tel:400-123-4567')
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.side-toolbar {
  position: fixed;
  right: 24px;
  top: 30vh;
  z-index: 99;
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.toolbar-item {
  width: 60px;
  height: 60px;
  background-color: #edc6e7;
  border-radius: 18px;
  box-shadow: 0 2px 12px rgba(237,198,231,0.18);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #fff;
  font-weight: bold;
  font-size: 14px;
  transition: all 0.3s;
  position: relative;
}
.toolbar-item:hover {
  background-color: #e3b6d8;
  transform: scale(1.08) translateY(-2px);
}
.toolbar-item i {
  font-size: 26px;
  margin-bottom: 2px;
}
.toolbar-item span {
  font-size: 13px;
}
.qr-popup {
  position: absolute;
  right: 70px;
  left: auto;
  top: 0;
  background: #fff;
  color: #333;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(237,198,231,0.18);
  padding: 12px 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 120px;
  z-index: 100;
  text-align: right;
}
.qr-popup img {
  width: 80px;
  height: 80px;
  margin-bottom: 8px;
}
.el-dialog {
  z-index: 2000;
}
.chat-window {
  max-height: 260px;
  overflow-y: auto;
  background: #f8f8fa;
  border-radius: 8px;
  padding: 10px;
  margin-bottom: 10px;
}
.chat-msg {
  margin-bottom: 8px;
  display: flex;
}
.chat-msg.user {
  justify-content: flex-end;
}
.chat-msg.ai {
  justify-content: flex-start;
}
.msg-content {
  display: inline-block;
  padding: 6px 14px;
  border-radius: 16px;
  background: #edc6e7;
  color: #fff;
  font-size: 14px;
  max-width: 200px;
  word-break: break-all;
}
.chat-msg.user .msg-content {
  background: #9E8CB9;
}
.chat-input-row {
  display: flex;
  gap: 8px;
  align-items: center;
}
.chat-input {
  flex: 1;
}
.chat-send {
  min-width: 60px;
}
</style> 