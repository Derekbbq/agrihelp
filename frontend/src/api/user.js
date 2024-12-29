import request from './request'

// 用户登录
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 用户注册
export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/auth/user',
    method: 'get'
  })
}

// 更新用户信息
export function updateUserInfo(data) {
  return request({
    url: '/auth/user',
    method: 'put',
    data
  })
}

// 修改密码
export function changePassword(data) {
  return request({
    url: '/auth/password',
    method: 'put',
    data
  })
}

// 发送验证码
export function sendVerificationCode(email) {
  return request({
    url: '/auth/verification-code',
    method: 'post',
    data: { email }
  })
}

// 重置密码
export function resetPassword(data) {
  return request({
    url: '/auth/reset-password',
    method: 'post',
    data
  })
}
