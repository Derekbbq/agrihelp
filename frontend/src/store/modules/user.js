import { login, register, getUserInfo } from '@/api/auth'
import { ElMessage } from 'element-plus'

export default {
  namespaced: true,
  state: {
    token: localStorage.getItem('token') || '',
    userInfo: null,
    isLoggedIn: false,
    username: ''
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
      state.isLoggedIn = true
      state.username = userInfo.username
    },
    CLEAR_AUTH(state) {
      state.token = ''
      state.userInfo = null
      state.isLoggedIn = false
      state.username = ''
      localStorage.removeItem('token')
    }
  },
  actions: {
    async login({ commit, dispatch }, credentials) {
      try {
        const { data } = await login(credentials)
        commit('SET_TOKEN', data.token)
        await dispatch('getUserInfo')
        ElMessage.success('登录成功')
        return data
      } catch (error) {
        ElMessage.error(error.message || '登录失败')
        throw error
      }
    },
    async register({ commit, dispatch }, userInfo) {
      try {
        const { data } = await register(userInfo)
        commit('SET_TOKEN', data.token)
        await dispatch('getUserInfo')
        ElMessage.success('注册成功')
        return data
      } catch (error) {
        ElMessage.error(error.message || '注册失败')
        throw error
      }
    },
    async getUserInfo({ commit }) {
      try {
        const { data } = await getUserInfo()
        commit('SET_USER_INFO', data)
        return data
      } catch (error) {
        ElMessage.error('获取用户信息失败')
        throw error
      }
    },
    async restoreSession({ commit, dispatch }) {
      try {
        await dispatch('getUserInfo')
      } catch (error) {
        commit('CLEAR_AUTH')
      }
    },
    logout({ commit }) {
      commit('CLEAR_AUTH')
      ElMessage.success('已退出登录')
    }
  },
  getters: {
    isAuthenticated: state => !!state.token,
    userInfo: state => state.userInfo,
    username: state => state.username
  }
}
