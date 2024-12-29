<template>
  <div class="home">
    <Carousel />
    <div class="content">
      <h2 class="section-title">热门商品</h2>
      <el-row :gutter="20">
        <el-col v-for="product in products" :key="product.id" :xs="24" :sm="12" :md="8" :lg="6">
          <ProductCard :product="product" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Carousel from '@/components/Carousel.vue'
import ProductCard from '@/components/ProductCard.vue'
import { getProducts } from '@/api/product'

const products = ref([
  {
    id: 1,
    name: '有机胡萝卜',
    category: '蔬菜',
    image: '/images/products/carrot.jpg',
    price: 5.99,
    originalPrice: 7.99,
    stock: 100,
    tags: ['有机', '新鲜'],
    description: '新鲜采摘的有机胡萝卜，无农药，产地直供'
  },
  {
    id: 2,
    name: '陕西红富士苹果',
    category: '水果',
    image: '/images/products/apple.jpg',
    price: 8.99,
    originalPrice: 12.99,
    stock: 200,
    tags: ['特级', '新鲜'],
    description: '陕西洛川红富士苹果，果形周正，口感脆甜'
  },
  {
    id: 3,
    name: '云南小青菜',
    category: '蔬菜',
    image: '/images/products/vegetables.jpg',
    price: 3.99,
    originalPrice: 5.99,
    stock: 150,
    tags: ['当季', '新鲜'],
    description: '云南高原小青菜，清脆可口，营养丰富'
  },
  {
    id: 4,
    name: '新疆土豆',
    category: '蔬菜',
    image: '/images/products/potato.jpg',
    price: 4.99,
    originalPrice: 6.99,
    stock: 300,
    tags: ['特级', '新鲜'],
    description: '新疆天山土豆，颗粒饱满，口感绵密'
  },
  {
    id: 5,
    name: '东北大米',
    category: '粮油',
    image: '/images/products/rice.jpg',
    price: 39.90,
    originalPrice: 49.90,
    stock: 500,
    tags: ['五常', '新米'],
    description: '东北五常大米，颗粒饱满，香糯可口'
  },
  {
    id: 6,
    name: '四川腊肠',
    category: '腊味',
    image: '/images/products/sausage.jpg',
    price: 45.90,
    originalPrice: 59.90,
    stock: 100,
    tags: ['特产', '腊味'],
    description: '四川农家自制腊肠，传统工艺，味道醇厚'
  }
])

// onMounted(async () => {
//   try {
//     const response = await getProducts()
//     products.value = response.data
//   } catch (error) {
//     console.error('Failed to fetch products:', error)
//   }
// })
</script>

<style scoped>
.home {
  padding: 20px;
}

.content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.section-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.el-row {
  margin-bottom: 20px;
}
</style>
