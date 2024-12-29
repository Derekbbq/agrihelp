<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>登录</h2>
      <el-form :model="form" :rules="rules" ref="formRef" @submit.prevent="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" show-password>
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
          <el-link type="primary" class="forget-password" href="#">忘记密码？</el-link>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading" block>登录</el-button>
        </el-form-item>
        <div class="register-link">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const rememberMe = ref(false)

const form = ref({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于 6 个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true
    
    // TODO: 调用登录 API
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 模拟登录成功
    localStorage.setItem('token', 'dummy-token')
    if (rememberMe.value) {
      localStorage.setItem('username', form.value.username)
    }
    
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    if (error.message) {
      ElMessage.error(error.message)
    }
  } finally {
    loading.value = false
  }
}

// 如果之前记住了用户名，自动填充
if (localStorage.getItem('username')) {
  form.value.username = localStorage.getItem('username')
  rememberMe.value = true
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 180px);
  padding: 20px;
  background-color: #f5f7fa;
}

.login-card {
  width: 100%;
  max-width: 400px;
}

.login-card h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.forget-password {
  float: right;
}

.register-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.register-link a {
  color: #409EFF;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>
