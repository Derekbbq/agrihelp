<template>
  <div class="cart-container">
    <div class="cart-header">
      <h2>我的购物车</h2>
      <el-button type="danger" plain @click="handleClearCart" v-if="cartStore.items.length">
        清空购物车
      </el-button>
    </div>

    <div v-if="cartStore.items.length" class="cart-content">
      <el-table :data="cartStore.items" style="width: 100%">
        <el-table-column label="商品信息" width="400">
          <template #default="{ row }">
            <div class="product-info">
              <img :src="row.image" :alt="row.name" class="product-image">
              <div class="product-details">
                <h3>{{ row.name }}</h3>
                <p class="category">{{ row.category }}</p>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="单价" width="120">
          <template #default="{ row }">
            <span class="price">¥{{ row.price }}</span>
          </template>
        </el-table-column>

        <el-table-column label="数量" width="200">
          <template #default="{ row }">
            <el-input-number 
              v-model="row.quantity" 
              :min="1" 
              :max="99"
              size="small"
              @change="(value) => handleQuantityChange(row.id, value)"
            />
          </template>
        </el-table-column>

        <el-table-column label="小计" width="120">
          <template #default="{ row }">
            <span class="subtotal">¥{{ (row.price * row.quantity).toFixed(2) }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button 
              type="danger" 
              link
              @click="handleRemoveItem(row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="cart-footer">
        <div class="cart-total">
          <span>总计：</span>
          <span class="total-amount">¥{{ cartStore.formattedTotalAmount }}</span>
        </div>
        <el-button type="primary" size="large" @click="handleCheckout">
          结算 ({{ cartStore.totalCount }}件)
        </el-button>
      </div>
    </div>

    <div v-else class="empty-cart">
      <el-empty description="购物车是空的">
        <template #extra>
          <router-link to="/products">
            <el-button type="primary">去购物</el-button>
          </router-link>
        </template>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import { useCartStore } from '@/stores/cart'
import { ElMessageBox, ElMessage } from 'element-plus'

const cartStore = useCartStore()

const handleQuantityChange = (productId, quantity) => {
  cartStore.updateQuantity(productId, quantity)
}

const handleRemoveItem = (productId) => {
  ElMessageBox.confirm(
    '确定要从购物车中删除该商品吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    cartStore.removeFromCart(productId)
    ElMessage.success('商品已删除')
  }).catch(() => {})
}

const handleClearCart = () => {
  ElMessageBox.confirm(
    '确定要清空购物车吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    cartStore.clearCart()
    ElMessage.success('购物车已清空')
  }).catch(() => {})
}

const handleCheckout = () => {
  // TODO: 实现结算功能
  ElMessage.info('结算功能开发中...')
}
</script>

<style scoped>
.cart-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.cart-header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.product-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.product-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.product-details h3 {
  margin: 0 0 5px;
  font-size: 16px;
  color: #333;
}

.category {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.price, .subtotal {
  color: #f56c6c;
  font-weight: bold;
}

.cart-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 20px;
  padding: 20px 0;
  border-top: 1px solid #eee;
}

.cart-total {
  margin-right: 20px;
  font-size: 16px;
}

.total-amount {
  color: #f56c6c;
  font-size: 24px;
  font-weight: bold;
  margin-left: 10px;
}

.empty-cart {
  padding: 40px 0;
}
</style>
