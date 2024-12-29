<template>
  <div class="products-container">
    <div class="search-bar">
      <el-input
        v-model="searchQuery"
        placeholder="搜索商品..."
        prefix-icon="Search"
        clearable
        @input="handleSearch"
      />
      <el-select v-model="sortBy" placeholder="排序方式" @change="handleSort">
        <el-option label="默认排序" value="default" />
        <el-option label="价格从低到高" value="priceAsc" />
        <el-option label="价格从高到低" value="priceDesc" />
        <el-option label="销量优先" value="sales" />
      </el-select>
    </div>

    <div class="products-grid">
      <ProductCard
        v-for="product in filteredProducts"
        :key="product.id"
        :product="product"
      />
    </div>

    <el-pagination
      v-model:current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next"
      @current-change="handlePageChange"
      class="pagination"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import ProductCard from '@/components/ProductCard.vue'

const searchQuery = ref('')
const sortBy = ref('default')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(100)

// 模拟商品数据
const products = ref([
  {
    id: 1,
    name: '有机胡萝卜',
    description: '新鲜采摘的有机胡萝卜，富含胡萝卜素，营养健康',
    price: 5.99,
    stock: 100,
    image: 'https://img.alicdn.com/imgextra/i3/2208876287052/O1CN01LxFPxS22TeNhpOOOC_!!2208876287052.jpg',
    category: '蔬菜'
  },
  {
    id: 2,
    name: '红富士苹果',
    description: '山东烟台红富士苹果，甜脆可口，果香浓郁',
    price: 8.99,
    stock: 200,
    image: 'https://img.alicdn.com/imgextra/i1/2208876287052/O1CN01KtQjcR22TeNe5zIX7_!!2208876287052.jpg',
    category: '水果'
  },
  {
    id: 3,
    name: '新鲜青菜',
    description: '当季新鲜青菜，绿色无公害，口感清甜',
    price: 3.99,
    stock: 150,
    image: 'https://img.alicdn.com/imgextra/i2/2208876287052/O1CN01kUtnwR22TeNdKzMVM_!!2208876287052.jpg',
    category: '蔬菜'
  },
  {
    id: 4,
    name: '有机大米',
    description: '东北黑土地有机大米，颗粒饱满，香糯可口',
    price: 39.99,
    stock: 50,
    image: 'https://img.alicdn.com/imgextra/i4/2208876287052/O1CN01AWmWt822TeNhpP5rA_!!2208876287052.jpg',
    category: '粮油'
  },
  {
    id: 5,
    name: '花生油',
    description: '纯正花生油，压榨工艺，香味浓郁',
    price: 59.99,
    stock: 30,
    image: 'https://img.alicdn.com/imgextra/i1/2208876287052/O1CN01HT6wHn22TeNi8cIKc_!!2208876287052.jpg',
    category: '粮油'
  },
  {
    id: 6,
    name: '农家腊肠',
    description: '传统工艺制作，农家风味，香辣可口',
    price: 45.99,
    stock: 40,
    image: 'https://img.alicdn.com/imgextra/i2/2208876287052/O1CN01QhHYVp22TeNfKzn5v_!!2208876287052.jpg',
    category: '腊味'
  }
])

const filteredProducts = computed(() => {
  let result = products.value

  // 搜索过滤
  if (searchQuery.value) {
    result = result.filter(product => 
      product.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      product.description.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  }

  // 排序
  switch (sortBy.value) {
    case 'priceAsc':
      result = [...result].sort((a, b) => a.price - b.price)
      break
    case 'priceDesc':
      result = [...result].sort((a, b) => b.price - a.price)
      break
    case 'sales':
      // 这里可以添加按销量排序的逻辑
      break
  }

  return result
})

const handleSearch = () => {
  currentPage.value = 1
}

const handleSort = () => {
  currentPage.value = 1
}

const handlePageChange = (page) => {
  currentPage.value = page
}
</script>

<style scoped>
.products-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px 40px;
}

.search-bar {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
}

.search-bar :deep(.el-input) {
  width: 300px;
}

.search-bar :deep(.el-select) {
  width: 200px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 30px;
  margin-bottom: 40px;
}

.pagination {
  margin-top: 40px;
  text-align: center;
}

.pagination :deep(.el-pagination) {
  justify-content: center;
  font-size: 16px;
}
</style> 