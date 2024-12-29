<template>
  <div class="app-layout">
    <header class="header">
      <div class="header-content">
        <router-link to="/" class="logo-container">
          <img :src="logo" alt="助农扶贫平台" class="logo">
        </router-link>
        <nav class="nav-menu">
          <router-link to="/" class="nav-item">首页</router-link>
          <router-link to="/products" class="nav-item">商品列表</router-link>
          <router-link v-if="!isLoggedIn" to="/login" class="nav-item">登录</router-link>
          <router-link v-if="!isLoggedIn" to="/register" class="nav-item">注册</router-link>
          <template v-else>
            <router-link to="/cart" class="nav-item">购物车</router-link>
            <router-link to="/orders" class="nav-item">我的订单</router-link>
            <el-dropdown>
              <span class="user-menu">
                {{ username }}
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </nav>
      </div>
    </header>

    <main class="main-content">
      <router-view></router-view>
    </main>

    <footer class="footer">
      <div class="footer-content">
        <p>&copy; 2024 助农扶贫平台. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'
import logo from '@/assets/images/logo.svg'

const store = useStore()
const router = useRouter()

const isLoggedIn = computed(() => store.state.user.isLoggedIn)
const username = computed(() => store.state.user.username)

const logout = async () => {
  try {
    await store.dispatch('user/logout')
    ElMessage.success('退出登录成功')
    router.push('/login')
  } catch (error) {
    ElMessage.error('退出登录失败')
  }
}
</script>

<style scoped>
.app-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-container {
  display: flex;
  align-items: center;
  text-decoration: none;
}

.logo {
  height: 40px;
  width: auto;
}

.nav-menu {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.nav-item {
  text-decoration: none;
  color: #2c3e50;
  font-size: 1rem;
  font-weight: 500;
  transition: color 0.3s ease;
}

.nav-item:hover {
  color: #409eff;
}

.nav-item.router-link-active {
  color: #409eff;
  font-weight: 600;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  color: #2c3e50;
}

.main-content {
  flex: 1;
  margin-top: 74px;
  padding: 20px;
  background-color: #f8f9fa;
}

.footer {
  background-color: #2c3e50;
  color: #fff;
  padding: 2rem 0;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}
</style>
