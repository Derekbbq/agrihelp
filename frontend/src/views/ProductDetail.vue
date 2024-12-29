<template>
  <div class="product-detail">
    <div class="content" v-loading="loading">
      <el-row :gutter="40">
        <el-col :span="12">
          <div class="image-container">
            <el-carousel trigger="click" height="400px">
              <el-carousel-item v-for="(image, index) in product.images" :key="index">
                <img :src="image" :alt="product.name" class="product-image">
              </el-carousel-item>
            </el-carousel>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-container">
            <h1 class="product-name">{{ product.name }}</h1>
            <div class="product-price">
              <span class="price-label">价格：</span>
              <span class="price-value">¥{{ product.price }}</span>
            </div>
            <div class="product-stock">
              <span class="stock-label">库存：</span>
              <span class="stock-value">{{ product.stock }}</span>
            </div>
            <div class="product-description">
              <h3>商品描述</h3>
              <p>{{ product.description }}</p>
            </div>
            <div class="product-actions">
              <el-input-number v-model="quantity" :min="1" :max="product.stock" />
              <el-button type="primary" size="large" @click="addToCart" :loading="addingToCart">
                加入购物车
              </el-button>
              <el-button type="danger" size="large" @click="buyNow" :loading="buying">
                立即购买
              </el-button>
            </div>
            <div class="seller-info">
              <h3>店家信息</h3>
              <p>{{ product.seller.name }}</p>
              <p>{{ product.seller.address }}</p>
              <el-rate v-model="product.seller.rating" disabled show-score />
            </div>
          </div>
        </el-col>
      </el-row>

      <div class="product-tabs">
        <el-tabs>
          <el-tab-pane label="商品详情">
            <div class="detail-content" v-html="product.detail"></div>
          </el-tab-pane>
          <el-tab-pane label="商品评价">
            <div class="reviews">
              <div v-for="review in product.reviews" :key="review.id" class="review-item">
                <div class="review-header">
                  <span class="reviewer">{{ review.user }}</span>
                  <el-rate v-model="review.rating" disabled />
                  <span class="review-time">{{ review.time }}</span>
                </div>
                <div class="review-content">{{ review.content }}</div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const addingToCart = ref(false)
const buying = ref(false)
const quantity = ref(1)

// 模拟商品数据
const product = ref({
  id: 1,
  name: '红富士苹果',
  price: 5.99,
  stock: 100,
  description: '新鲜采摘的红富士苹果，口感脆甜',
  images: [
    'https://img.yzcdn.cn/vant/apple-1.jpg',
    'https://img.yzcdn.cn/vant/apple-2.jpg'
  ],
  detail: `
    <h3>商品介绍</h3>
    <p>精选红富士苹果，产自陕西洛川苹果基地，果形周正，色泽鲜艳，口感脆甜。</p>
    <h3>规格参数</h3>
    <ul>
      <li>规格：5斤装</li>
      <li>产地：陕西洛川</li>
      <li>储存方法：常温/冷藏</li>
    </ul>
  `,
  seller: {
    name: '洛川苹果专营店',
    address: '陕西省延安市洛川县',
    rating: 4.8
  },
  reviews: [
    {
      id: 1,
      user: '张三',
      rating: 5,
      content: '苹果很新鲜，味道很好',
      time: '2024-01-20'
    },
    {
      id: 2,
      user: '李四',
      rating: 4,
      content: '包装很好，物流很快',
      time: '2024-01-19'
    }
  ]
})

const addToCart = async () => {
  addingToCart.value = true
  try {
    // TODO: 调用添加购物车 API
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('已添加到购物车')
  } catch (error) {
    ElMessage.error('添加失败，请重试')
  } finally {
    addingToCart.value = false
  }
}

const buyNow = async () => {
  buying.value = true
  try {
    // TODO: 调用立即购买 API
    await new Promise(resolve => setTimeout(resolve, 1000))
    router.push('/checkout')
  } catch (error) {
    ElMessage.error('购买失败，请重试')
  } finally {
    buying.value = false
  }
}

onMounted(async () => {
  const id = route.params.id
  loading.value = true
  try {
    // TODO: 调用获取商品详情 API
    await new Promise(resolve => setTimeout(resolve, 1000))
  } catch (error) {
    ElMessage.error('获取商品信息失败')
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.product-detail {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 180px);
}

.content {
  max-width: 1200px;
  margin: 0 auto;
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.image-container {
  border-radius: 8px;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-container {
  padding: 20px;
}

.product-name {
  font-size: 24px;
  color: #333;
  margin: 0 0 20px;
}

.product-price {
  margin-bottom: 20px;
}

.price-label {
  font-size: 16px;
  color: #666;
}

.price-value {
  font-size: 28px;
  color: #f56c6c;
  font-weight: bold;
}

.product-stock {
  margin-bottom: 20px;
}

.stock-label {
  font-size: 16px;
  color: #666;
}

.stock-value {
  font-size: 16px;
  color: #333;
}

.product-description {
  margin-bottom: 30px;
}

.product-description h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 10px;
}

.product-description p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.product-actions {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.seller-info {
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.seller-info h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 10px;
}

.seller-info p {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.product-tabs {
  margin-top: 40px;
}

.detail-content {
  padding: 20px 0;
}

.reviews {
  padding: 20px 0;
}

.review-item {
  padding: 20px 0;
  border-bottom: 1px solid #eee;
}

.review-item:last-child {
  border-bottom: none;
}

.review-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.reviewer {
  font-size: 16px;
  color: #333;
  margin-right: 20px;
}

.review-time {
  font-size: 14px;
  color: #999;
  margin-left: 20px;
}

.review-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}
</style>
