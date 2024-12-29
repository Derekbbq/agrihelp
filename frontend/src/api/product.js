import request from '@/utils/request'

// 获取商品列表
export function getProducts(params) {
  return request({
    url: '/api/products',
    method: 'get',
    params
  })
}

// 获取商品详情
export function getProduct(id) {
  return request({
    url: `/api/products/${id}`,
    method: 'get'
  })
}

// 获取商品分类
export function getCategories() {
  return request({
    url: '/categories',
    method: 'get'
  })
}

// 搜索商品
export function searchProducts(keyword) {
  return request({
    url: '/api/products/search',
    method: 'get',
    params: { keyword }
  })
}

// 获取推荐商品
export function getFeaturedProducts() {
  return request({
    url: '/products/featured',
    method: 'get'
  })
}

// 获取新品上市
export function getNewProducts() {
  return request({
    url: '/products/new',
    method: 'get'
  })
}

// 获取热销商品
export function getHotProducts() {
  return request({
    url: '/products/hot',
    method: 'get'
  })
}

export function getProductsByCategory(category) {
  return request({
    url: '/api/products/category',
    method: 'get',
    params: { category }
  })
}
