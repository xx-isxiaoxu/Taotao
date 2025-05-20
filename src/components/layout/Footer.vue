<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-section">
        <h3>关于我们</h3>
        <ul>
          <li><a href="/about">公司简介</a></li>
          <li><a href="/contact">联系我们</a></li>
          <li><a href="/careers">招贤纳士</a></li>
        </ul>
      </div>
      
      <div class="footer-section">
        <h3>客户服务</h3>
        <ul>
          <li><a href="/help">帮助中心</a></li>
          <li><a href="/shipping">配送说明</a></li>
          <li><a href="/returns">退换货政策</a></li>
        </ul>
      </div>
      
      <div class="footer-section">
        <h3>关注我们</h3>
        <div class="social-links">
          <a href="#" class="social-icon"><i class="el-icon-weixin"></i></a>
          <a href="#" class="social-icon"><i class="el-icon-weibo"></i></a>
          <a href="#" class="social-icon"><i class="el-icon-qq"></i></a>
        </div>
      </div>
      
      <div class="footer-section">
        <h3>联系方式</h3>
        <p>客服热线：400-123-4567</p>
        <p>工作时间：周一至周日 9:00-21:00</p>
        <p>邮箱：service@example.com</p>
      </div>
    </div>
    
    <div class="footer-bottom">
      <p>&copy; 2024 淘淘商城 版权所有 | ICP备案号：粤ICP备12345678号</p>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background-color: #2c3e50;
  color: #fff;
  padding: 40px 0 20px;
  margin-top: 40px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 40px;
  padding: 0 20px;
}

.footer-section h3 {
  font-size: 18px;
  margin-bottom: 20px;
  color: #fff;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section ul li {
  margin-bottom: 10px;
}

.footer-section a {
  color: #bdc3c7;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-section a:hover {
  color: #fff;
}

.social-links {
  display: flex;
  gap: 15px;
}

.social-icon {
  font-size: 24px;
  color: #bdc3c7;
  transition: color 0.3s;
}

.social-icon:hover {
  color: #fff;
}

.footer-bottom {
  text-align: center;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.footer-bottom p {
  color: #bdc3c7;
  font-size: 14px;
}
</style> 