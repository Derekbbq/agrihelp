import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const service = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
  timeout: 5000
})

service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code && res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      if (res.code === 401) {
        router.push('/login')
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  error => {
    console.error('Response error:', error)
    const message = error.response?.data?.message || '请求失败'
    ElMessage.error(message)
    if (error.response?.status === 401) {
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

export default service 