import request from './request'

export function createOrder(data) {
  return request({
    url: '/orders',
    method: 'post',
    data
  })
}

export function getOrders(params) {
  return request({
    url: '/orders',
    method: 'get',
    params
  })
}

export function getOrder(id) {
  return request({
    url: `/orders/${id}`,
    method: 'get'
  })
}

export function updateOrderStatus(id, status) {
  return request({
    url: `/orders/${id}/status`,
    method: 'put',
    params: {
      status
    }
  })
}
