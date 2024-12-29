<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-section">
        <h3>关于我们</h3>
        <p>助农电商平台致力于帮助农民销售农产品，促进农村经济发展。</p>
      </div>
      <div class="footer-section">
        <h3>联系方式</h3>
        <p>电话：400-123-4567</p>
        <p>邮箱：support@nongzhu.com</p>
      </div>
      <div class="footer-section">
        <h3>快速链接</h3>
        <router-link to="/about">关于我们</router-link>
        <router-link to="/contact">联系我们</router-link>
        <router-link to="/help">帮助中心</router-link>
      </div>
    </div>
    <div class="footer-bottom">
      <p>© 2024 助农电商平台 版权所有</p>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background-color: #333;
  color: #fff;
  padding: 40px 0 20px;
  margin-top: auto;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 40px;
  padding: 0 20px;
}

.footer-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.footer-section h3 {
  color: #4CAF50;
  margin-bottom: 10px;
  font-size: 18px;
}

.footer-section p {
  margin: 0;
  line-height: 1.6;
}

.footer-section a {
  color: #fff;
  text-decoration: none;
  display: block;
  margin-bottom: 8px;
  transition: color 0.3s;
}

.footer-section a:hover {
  color: #4CAF50;
}

.footer-bottom {
  max-width: 1200px;
  margin: 20px auto 0;
  padding: 20px 20px 0;
  border-top: 1px solid #444;
  text-align: center;
}

.footer-bottom p {
  margin: 0;
  color: #999;
}
</style> 