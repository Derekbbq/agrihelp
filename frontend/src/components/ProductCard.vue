<template>
  <el-card class="product-card" :body-style="{ padding: '0' }">
    <div class="product-image">
      <img 
        :src="props.product.image" 
        :alt="props.product.name"
        @error="handleImageError"
      >
    </div>
    <div class="product-info">
      <h3>{{ props.product.name }}</h3>
      <p class="description">{{ props.product.description }}</p>
      <div class="price-stock">
        <span class="price">¥{{ props.product.price }}</span>
        <span class="stock">库存: {{ props.product.stock }}</span>
      </div>
      <div class="actions">
        <el-button type="primary" @click="addToCart">加入购物车</el-button>
        <el-button @click="viewDetails">查看详情</el-button>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const props = defineProps({
  product: {
    type: Object,
    required: true
  }
})

const router = useRouter()

const handleImageError = (e) => {
  // 设置默认图片
  e.target.src = '/images/products/default.jpg'
}

const addToCart = () => {
  ElMessage.success(`已将 ${props.product.name} 加入购物车`)
}

const viewDetails = () => {
  router.push(`/product/${props.product.id}`)
}
</script>

<style scoped>
.product-card {
  border-radius: 12px;
  transition: transform 0.3s, box-shadow 0.3s;
  background-color: #fff;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.product-image {
  height: 250px;
  overflow: hidden;
  border-radius: 12px 12px 0 0;
  background-color: #f5f5f5;
  position: relative;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.product-image img:hover {
  transform: scale(1.05);
}

.product-info {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.product-info h3 {
  margin: 0 0 15px;
  font-size: 18px;
  color: #333;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.description {
  color: #666;
  font-size: 14px;
  margin-bottom: 20px;
  height: 44px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.6;
}

.price-stock {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 10px 0;
  border-top: 1px solid #eee;
  border-bottom: 1px solid #eee;
}

.price {
  color: #f56c6c;
  font-size: 24px;
  font-weight: bold;
}

.stock {
  color: #909399;
  font-size: 14px;
  background-color: #f5f5f5;
  padding: 4px 12px;
  border-radius: 4px;
}

.actions {
  display: flex;
  gap: 15px;
  margin-top: auto;
}

.actions :deep(.el-button) {
  flex: 1;
  padding: 12px 20px;
  font-size: 16px;
}

:deep(.el-button--primary) {
  background-color: #4CAF50;
  border-color: #4CAF50;
}

:deep(.el-button--primary:hover) {
  background-color: #45a049;
  border-color: #45a049;
}
</style> 