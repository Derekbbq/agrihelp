<template>
  <div class="app-container">
    <el-header class="app-header">
      <div class="header-content">
        <router-link to="/" class="logo-container">
          <img src="/images/logo/logo.svg" alt="助农电商平台" class="logo">
          <span class="logo-text">助农电商平台</span>
        </router-link>
        <el-menu mode="horizontal" :router="true" class="nav-menu">
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/products">商品列表</el-menu-item>
          <el-menu-item index="/cart">购物车</el-menu-item>
          <el-menu-item index="/orders">我的订单</el-menu-item>
        </el-menu>
        <div class="user-actions">
          <router-link to="/cart" class="cart-link">
            <el-badge :value="cartCount" class="cart-badge">
              <el-icon><ShoppingCart /></el-icon>
            </el-badge>
          </router-link>
          <template v-if="isLoggedIn">
            <el-dropdown>
              <span class="user-profile">
                <el-avatar :size="32" :src="userAvatar">{{ userInitials }}</el-avatar>
                <span class="username">{{ username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="goToProfile">个人中心</el-dropdown-item>
                  <el-dropdown-item @click="goToOrders">我的订单</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login" class="login-link">
              <el-button type="primary">登录</el-button>
            </router-link>
            <router-link to="/register" class="register-link">
              <el-button>注册</el-button>
            </router-link>
          </template>
        </div>
      </div>
    </el-header>

    <el-main class="app-main">
      <router-view></router-view>
    </el-main>

    <div class="footer-links">
      <div class="footer-section">
        <h3>关于我们</h3>
        <router-link to="/about">平台介绍</router-link>
        <router-link to="/join">商家入驻</router-link>
        <router-link to="/careers">招贤纳士</router-link>
      </div>
      <div class="footer-section">
        <h3>联系方式</h3>
        <router-link to="/contact">联系我们</router-link>
        <router-link to="/feedback">意见反馈</router-link>
        <router-link to="/cooperation">商务合作</router-link>
      </div>
      <div class="footer-section">
        <h3>快速链接</h3>
        <router-link to="/help">帮助中心</router-link>
        <router-link to="/privacy">隐私政策</router-link>
        <router-link to="/terms">服务条款</router-link>
      </div>
    </div>

    <el-footer class="app-footer">
      <div class="footer-content">
        <p>© 2024 助农电商平台 版权所有</p>
        <p>浙ICP备XXXXXXXX号-1 | 浙公网安备 XXXXXXXXXXXXX号</p>
      </div>
    </el-footer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ShoppingCart } from '@element-plus/icons-vue'

// 模拟用户状态
const isLoggedIn = ref(false)
const username = ref('用户名')
const userAvatar = ref('')
const cartCount = ref(0)

const userInitials = computed(() => {
  return username.value.slice(0, 2)
})

const router = useRouter()

const goToProfile = () => {
  router.push('/profile')
}

const goToOrders = () => {
  router.push('/orders')
}

const handleLogout = () => {
  isLoggedIn.value = false
  router.push('/login')
}
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: fixed;
  width: 100%;
  z-index: 1000;
  padding: 0;
  height: 70px;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
}

.logo-container {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
}

.logo {
  height: 45px;
  margin-right: 15px;
}

.logo-text {
  font-size: 22px;
  font-weight: bold;
  color: #4CAF50;
}

.nav-menu {
  flex: 1;
  justify-content: center;
  border-bottom: none;
  margin: 0 40px;
}

.nav-menu :deep(.el-menu-item) {
  font-size: 16px;
  padding: 0 25px;
  height: 70px;
  line-height: 70px;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.cart-link {
  text-decoration: none;
  color: #333;
  font-size: 26px;
  padding: 5px;
}

.cart-badge :deep(.el-badge__content) {
  background-color: #4CAF50;
  border: none;
}

.user-profile {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px;
}

.username {
  margin-left: 10px;
  color: #333;
  font-size: 16px;
}

.login-link, .register-link {
  text-decoration: none;
  margin-left: 10px;
}

.app-main {
  margin-top: 70px;
  flex: 1;
  padding: 40px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 70px - 300px);
}

.footer-links {
  background-color: #f8f9fa;
  padding: 60px 0;
  border-top: 1px solid #eee;
  width: 100%;
}

.footer-links {
  max-width: 1400px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 60px;
  padding: 0 40px;
}

.footer-section {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.footer-section h3 {
  color: #4CAF50;
  margin-bottom: 20px;
  font-size: 20px;
  font-weight: 600;
}

.footer-section a {
  color: #666;
  text-decoration: none;
  font-size: 16px;
  transition: color 0.3s;
  padding: 5px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.footer-section a:hover {
  color: #4CAF50;
}

.app-footer {
  background-color: #333;
  color: #fff;
  padding: 30px;
  text-align: center;
  width: 100%;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 40px;
}

.footer-content p {
  margin: 8px 0;
  color: #999;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

:deep(.el-button) {
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 6px;
}

:deep(.el-button--primary) {
  background-color: #4CAF50;
  border-color: #4CAF50;
}

:deep(.el-button--primary:hover) {
  background-color: #45a049;
  border-color: #45a049;
}
</style>

