import { getCart, addToCart as apiAddToCart, updateCartItem, removeFromCart } from '@/api/cart'
import { ElMessage } from 'element-plus'

export default {
  namespaced: true,
  state: {
    items: [],
    loading: false
  },
  mutations: {
    SET_CART_ITEMS(state, items) {
      state.items = items
    },
    SET_LOADING(state, loading) {
      state.loading = loading
    },
    ADD_ITEM(state, item) {
      const existingItem = state.items.find(i => i.productId === item.productId)
      if (existingItem) {
        existingItem.quantity += item.quantity
      } else {
        state.items.push(item)
      }
    },
    UPDATE_ITEM_QUANTITY(state, { productId, quantity }) {
      const item = state.items.find(i => i.productId === productId)
      if (item) {
        item.quantity = quantity
      }
    },
    REMOVE_ITEM(state, productId) {
      state.items = state.items.filter(item => item.productId !== productId)
    }
  },
  actions: {
    async fetchCart({ commit }) {
      commit('SET_LOADING', true)
      try {
        const { data } = await getCart()
        commit('SET_CART_ITEMS', data)
      } catch (error) {
        ElMessage.error('获取购物车失败')
      } finally {
        commit('SET_LOADING', false)
      }
    },
    async addToCart({ commit }, { productId, quantity = 1 }) {
      try {
        const { data } = await apiAddToCart(productId, quantity)
        commit('ADD_ITEM', data)
        ElMessage.success('已添加到购物车')
      } catch (error) {
        ElMessage.error('添加到购物车失败')
        throw error
      }
    },
    async updateQuantity({ commit }, { productId, quantity }) {
      try {
        await updateCartItem(productId, quantity)
        commit('UPDATE_ITEM_QUANTITY', { productId, quantity })
        ElMessage.success('已更新数量')
      } catch (error) {
        ElMessage.error('更新数量失败')
        throw error
      }
    },
    async removeFromCart({ commit }, productId) {
      try {
        await removeFromCart(productId)
        commit('REMOVE_ITEM', productId)
        ElMessage.success('已从购物车移除')
      } catch (error) {
        ElMessage.error('移除失败')
        throw error
      }
    },
    clearCart({ commit }) {
      commit('SET_CART_ITEMS', [])
    }
  },
  getters: {
    cartItemCount: state => state.items.reduce((total, item) => total + item.quantity, 0),
    cartTotal: state => state.items.reduce((total, item) => total + item.price * item.quantity, 0),
    isLoading: state => state.loading
  }
}
