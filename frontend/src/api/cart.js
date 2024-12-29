import request from '@/utils/request'

export function getCart() {
  return request({
    url: '/api/cart',
    method: 'get'
  })
}

export function addToCart(data) {
  return request({
    url: '/api/cart',
    method: 'post',
    data
  })
}

export function updateCartItem(id, data) {
  return request({
    url: `/api/cart/${id}`,
    method: 'put',
    data
  })
}

export function removeFromCart(id) {
  return request({
    url: `/api/cart/${id}`,
    method: 'delete'
  })
}

export function clearCart() {
  return request({
    url: '/api/cart/clear',
    method: 'delete'
  })
}
