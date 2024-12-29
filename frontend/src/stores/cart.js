import { defineStore } from 'pinia'

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: JSON.parse(localStorage.getItem('cartItems')) || []
  }),

  getters: {
    totalCount: (state) => state.items.reduce((sum, item) => sum + item.quantity, 0),
    totalAmount: (state) => state.items.reduce((sum, item) => sum + item.price * item.quantity, 0),
    
    // 格式化后的总金额
    formattedTotalAmount: (state) => {
      return state.items.reduce((sum, item) => sum + item.price * item.quantity, 0).toFixed(2)
    }
  },

  actions: {
    addToCart(product) {
      const existingItem = this.items.find(item => item.id === product.id)
      
      if (existingItem) {
        // 如果商品已存在，更新数量
        existingItem.quantity = product.quantity
      } else {
        // 如果商品不存在，添加新商品
        this.items.push({
          id: product.id,
          name: product.name,
          price: product.price,
          quantity: product.quantity,
          image: product.image,
          category: product.category
        })
      }
      
      // 保存到本地存储
      this.saveToLocalStorage()
    },

    removeFromCart(productId) {
      const index = this.items.findIndex(item => item.id === productId)
      if (index > -1) {
        this.items.splice(index, 1)
        // 保存到本地存储
        this.saveToLocalStorage()
      }
    },

    updateQuantity(productId, quantity) {
      const item = this.items.find(item => item.id === productId)
      if (item) {
        item.quantity = quantity
        // 保存到本地存储
        this.saveToLocalStorage()
      }
    },

    clearCart() {
      this.items = []
      // 清除本地存储
      localStorage.removeItem('cartItems')
    },

    saveToLocalStorage() {
      localStorage.setItem('cartItems', JSON.stringify(this.items))
    }
  }
}) 