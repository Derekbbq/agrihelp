<template>
  <div class="product-list">
    <div class="filters">
      <div class="search-box">
        <el-input
          v-model="searchQuery"
          placeholder="搜索商品"
          clearable
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <div class="category-filter">
        <el-select v-model="selectedCategory" placeholder="选择分类" clearable @change="handleFilter">
          <el-option
            v-for="category in categories"
            :key="category.id"
            :label="category.name"
            :value="category.name"
          />
        </el-select>
      </div>

      <div class="sort-filter">
        <el-select v-model="sortBy" placeholder="排序方式" @change="handleFilter">
          <el-option label="默认排序" value="default" />
          <el-option label="价格从低到高" value="price-asc" />
          <el-option label="价格从高到低" value="price-desc" />
        </el-select>
      </div>
    </div>

    <div v-if="loading" class="loading">
      <el-skeleton :rows="5" animated />
    </div>

    <div v-else-if="products.length === 0" class="empty-state">
      <el-empty description="暂无商品" />
    </div>

    <div v-else class="products-grid">
      <div
        v-for="product in products"
        :key="product.id"
        class="product-card"
        @click="goToDetail(product.id)"
      >
        <img :src="product.image || defaultImage" :alt="product.name" class="product-image">
        <div class="product-info">
          <h3 class="product-name">{{ product.name }}</h3>
          <p class="product-description">{{ product.description }}</p>
          <div class="product-footer">
            <span class="product-price">¥{{ product.price }}</span>
            <el-button type="primary" size="small" @click.stop="addToCart(product)">
              加入购物车
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[12, 24, 36, 48]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import defaultImage from '@/assets/images/product-default.svg'

const router = useRouter()
const route = useRoute()
const store = useStore()

const loading = ref(false)
const products = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(12)
const searchQuery = ref('')
const selectedCategory = ref('')
const sortBy = ref('default')

const categories = [
  { id: 1, name: '水果' },
  { id: 2, name: '蔬菜' },
  { id: 3, name: '粮油' },
  { id: 4, name: '特产' }
]

const loadProducts = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 这里应该调用实际的API
    // const response = await getProducts({
    //   page: currentPage.value,
    //   size: pageSize.value,
    //   search: searchQuery.value,
    //   category: selectedCategory.value,
    //   sort: sortBy.value
    // })
    
    // 模拟数据
    products.value = Array(pageSize.value).fill(null).map((_, index) => ({
      id: index + 1,
      name: `测试商品 ${index + 1}`,
      description: '这是一个测试商品描述',
      price: Math.floor(Math.random() * 100) + 10,
      image: defaultImage
    }))
    total.value = 100
  } catch (error) {
    ElMessage.error('加载商品列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadProducts()
}

const handleFilter = () => {
  currentPage.value = 1
  loadProducts()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadProducts()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadProducts()
}

const goToDetail = (id) => {
  router.push(`/products/${id}`)
}

const addToCart = async (product) => {
  try {
    await store.dispatch('cart/addToCart', {
      productId: product.id,
      quantity: 1
    })
    ElMessage.success('已添加到购物车')
  } catch (error) {
    ElMessage.error('添加到购物车失败')
  }
}

onMounted(() => {
  if (route.query.category) {
    selectedCategory.value = route.query.category
  }
  loadProducts()
})
</script>

<style scoped>
.product-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.filters {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.search-box {
  flex: 1;
}

.loading {
  padding: 40px;
}

.empty-state {
  padding: 60px 0;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.product-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 15px;
}

.product-name {
  margin: 0 0 10px;
  font-size: 16px;
  font-weight: 500;
  color: #2c3e50;
}

.product-description {
  margin: 0 0 15px;
  font-size: 14px;
  color: #666;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-price {
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
</style>
