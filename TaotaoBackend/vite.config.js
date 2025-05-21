import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    host: '127.0.0.1', // 将 localhost 改为 IP 地址
    port: 5173,        // 保持原有端口
    open: true         // 自动打开浏览器
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  }
})