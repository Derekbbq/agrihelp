import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import store from '@/store'

const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    ElMessage.error('请求发送失败')
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果返回的状态码不是200，说明出错了
    if (res.code !== 200) {
      ElMessage.error(res.message || '错误')
      
      // 401: Token 过期或未登录
      if (res.code === 401) {
        store.dispatch('user/logout')
        router.push(`/login?redirect=${router.currentRoute.value.fullPath}`)
      }
      return Promise.reject(new Error(res.message || '错误'))
    }
    
    return res
  },
  error => {
    console.error('请求错误', error)
    const message = error.response?.data?.message || '请求失败'
    
    if (error.response) {
      switch (error.response.status) {
        case 401:
          store.dispatch('user/logout')
          router.push(`/login?redirect=${router.currentRoute.value.fullPath}`)
          break
        case 403:
          ElMessage.error('没有权限访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器错误')
          break
        default:
          ElMessage.error(message)
      }
    } else if (error.request) {
      ElMessage.error('服务器无响应')
    } else {
      ElMessage.error('请求配置错误')
    }
    
    return Promise.reject(error)
  }
)

export default request
