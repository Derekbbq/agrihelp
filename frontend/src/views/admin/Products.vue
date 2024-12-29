<template>
  <div class="admin-products">
    <div class="content" v-loading="loading">
      <div class="page-header">
        <h2 class="page-title">商品管理</h2>
        <el-button type="primary" @click="handleAdd">
          添加商品
        </el-button>
      </div>

      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="商品名称">
            <el-input
              v-model="searchForm.name"
              placeholder="请输入商品名称"
              clearable
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="商品分类">
            <el-select
              v-model="searchForm.category"
              placeholder="请选择分类"
              clearable
            >
              <el-option
                v-for="item in categories"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="商品状态">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择状态"
              clearable
            >
              <el-option
                v-for="item in productStatus"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              搜索
            </el-button>
            <el-button @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="table-container">
        <el-table :data="products" border style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column label="商品图片" width="100">
            <template #default="{ row }">
              <el-image
                :src="row.image"
                :preview-src-list="[row.image]"
                fit="cover"
                style="width: 50px; height: 50px"
              />
            </template>
          </el-table-column>
          <el-table-column prop="name" label="商品名称" min-width="200" show-overflow-tooltip />
          <el-table-column prop="category" label="分类" width="120" />
          <el-table-column prop="price" label="价格" width="120">
            <template #default="{ row }">
              ¥{{ row.price.toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="stock" label="库存" width="100" />
          <el-table-column prop="sales" label="销量" width="100" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 'on' ? 'success' : 'info'">
                {{ row.status === 'on' ? '上架' : '下架' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button
                link
                type="primary"
                @click="handleEdit(row)"
              >
                编辑
              </el-button>
              <el-button
                link
                :type="row.status === 'on' ? 'warning' : 'success'"
                @click="handleToggleStatus(row)"
              >
                {{ row.status === 'on' ? '下架' : '上架' }}
              </el-button>
              <el-button
                link
                type="danger"
                @click="handleDelete(row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 商品表单对话框 -->
    <el-dialog
      v-model="productDialog.visible"
      :title="productDialog.type === 'add' ? '添加商品' : '编辑商品'"
      width="700px"
    >
      <el-form
        ref="productFormRef"
        :model="productDialog.form"
        :rules="productDialog.rules"
        label-width="100px"
      >
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="productDialog.form.name" />
        </el-form-item>
        <el-form-item label="商品分类" prop="category">
          <el-select v-model="productDialog.form.category">
            <el-option
              v-for="item in categories"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商品图片" prop="image">
          <el-upload
            class="product-image-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleImageSuccess"
            :before-upload="beforeImageUpload"
          >
            <img v-if="productDialog.form.image" :src="productDialog.form.image" class="preview-image">
            <el-icon v-else class="uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input-number
            v-model="productDialog.form.price"
            :precision="2"
            :step="0.1"
            :min="0"
          />
        </el-form-item>
        <el-form-item label="商品库存" prop="stock">
          <el-input-number
            v-model="productDialog.form.stock"
            :min="0"
            :precision="0"
          />
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input
            v-model="productDialog.form.description"
            type="textarea"
            rows="4"
          />
        </el-form-item>
        <el-form-item label="商品状态" prop="status">
          <el-radio-group v-model="productDialog.form.status">
            <el-radio label="on">上架</el-radio>
            <el-radio label="off">下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="productDialog.visible = false">取消</el-button>
          <el-button
            type="primary"
            @click="submitProduct"
            :loading="productDialog.loading"
          >
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 商品状态
const productStatus = [
  { label: '上架', value: 'on' },
  { label: '下架', value: 'off' }
]

// 商品分类
const categories = [
  { label: '蔬菜', value: 'vegetables' },
  { label: '水果', value: 'fruits' },
  { label: '肉类', value: 'meat' },
  { label: '海鲜', value: 'seafood' },
  { label: '粮油', value: 'grocery' }
]

// 模拟商品数据
const products = ref([
  {
    id: 1,
    name: '有机胡萝卜',
    category: '蔬菜',
    image: 'https://img.alicdn.com/imgextra/i4/2200724907121/O1CN01LxFPxS22TeNhpOOOC_!!2200724907121.jpg',
    price: 5.99,
    stock: 100,
    sales: 50,
    status: 'on',
    description: '新鲜有机胡萝卜，产地直供',
    createTime: '2024-01-01 00:00:00'
  },
  {
    id: 2,
    name: '红富士苹果',
    category: '水果',
    image: 'https://img.alicdn.com/imgextra/i1/2200724907121/O1CN01KtQjcR22TeNe5zIX7_!!2200724907121.jpg',
    price: 8.99,
    stock: 200,
    sales: 80,
    status: 'on',
    description: '山东烟台红富士苹果，甜脆可口',
    createTime: '2024-01-02 00:00:00'
  },
  {
    id: 3,
    name: '新鲜青菜',
    category: '蔬菜',
    image: 'https://img.alicdn.com/imgextra/i2/2200724907121/O1CN01kUtnwR22TeNdKzMVM_!!2200724907121.jpg',
    price: 3.99,
    stock: 150,
    sales: 60,
    status: 'on',
    description: '本地种植青菜，新鲜采摘',
    createTime: '2024-01-03 00:00:00'
  },
  {
    id: 4,
    name: '有机土豆',
    category: '蔬菜',
    image: 'https://img.alicdn.com/imgextra/i3/2200724907121/O1CN01AWmWt822TeNhpP5rA_!!2200724907121.jpg',
    price: 4.99,
    stock: 300,
    sales: 120,
    status: 'on',
    description: '农家自种土豆，无农药',
    createTime: '2024-01-04 00:00:00'
  }
])

const searchForm = reactive({
  name: '',
  category: '',
  status: ''
})

const productDialog = reactive({
  visible: false,
  type: 'add',
  loading: false,
  form: {
    name: '',
    category: '',
    image: '',
    price: 0,
    stock: 0,
    description: '',
    status: 'on'
  },
  rules: {
    name: [
      { required: true, message: '请输入商品名称', trigger: 'blur' },
      { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
    ],
    category: [
      { required: true, message: '请选择商品分类', trigger: 'change' }
    ],
    image: [
      { required: true, message: '请上传商品图片', trigger: 'change' }
    ],
    price: [
      { required: true, message: '请输入商品价格', trigger: 'blur' }
    ],
    stock: [
      { required: true, message: '请输入商品库存', trigger: 'blur' }
    ],
    description: [
      { required: true, message: '请输入商品描述', trigger: 'blur' },
      { min: 10, max: 500, message: '长度在 10 到 500 个字符', trigger: 'blur' }
    ],
    status: [
      { required: true, message: '请选择商品状态', trigger: 'change' }
    ]
  }
})

const handleSearch = () => {
  currentPage.value = 1
  fetchProducts()
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.category = ''
  searchForm.status = ''
  handleSearch()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  fetchProducts()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchProducts()
}

const fetchProducts = async () => {
  loading.value = true
  try {
    // TODO: 调用获取商品列表 API
    await new Promise(resolve => setTimeout(resolve, 1000))
  } catch (error) {
    ElMessage.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  productDialog.type = 'add'
  productDialog.form = {
    name: '',
    category: '',
    image: '',
    price: 0,
    stock: 0,
    description: '',
    status: 'on'
  }
  productDialog.visible = true
}

const handleEdit = (product) => {
  productDialog.type = 'edit'
  productDialog.form = { ...product }
  productDialog.visible = true
}

const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB！')
    return false
  }
  return true
}

const handleImageSuccess = (response) => {
  productDialog.form.image = response.url
}

const submitProduct = async () => {
  const formRef = document.querySelector('.el-dialog .el-form')
  if (!formRef) return

  try {
    await formRef.validate()
    productDialog.loading = true

    // TODO: 调用添加/编辑商品 API
    await new Promise(resolve => setTimeout(resolve, 1000))

    ElMessage.success(
      productDialog.type === 'add' ? '添加商品成功' : '编辑商品成功'
    )
    productDialog.visible = false
    fetchProducts()
  } catch (error) {
    if (error.message) {
      ElMessage.error(error.message)
    }
  } finally {
    productDialog.loading = false
  }
}

const handleToggleStatus = async (product) => {
  try {
    await ElMessageBox.confirm(
      `确定要${product.status === 'on' ? '下架' : '上架'}该商品吗？`,
      '提示',
      {
        type: 'warning'
      }
    )
    // TODO: 调用修改商品状态 API
    await new Promise(resolve => setTimeout(resolve, 1000))
    product.status = product.status === 'on' ? 'off' : 'on'
    ElMessage.success('操作成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const handleDelete = async (product) => {
  try {
    await ElMessageBox.confirm('确定要删除该商品吗？此操作不可恢复！', '警告', {
      type: 'error'
    })
    // TODO: 调用删除商品 API
    await new Promise(resolve => setTimeout(resolve, 1000))
    const index = products.value.findIndex(item => item.id === product.id)
    if (index > -1) {
      products.value.splice(index, 1)
    }
    ElMessage.success('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}
</script>

<style scoped>
.admin-products {
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

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.search-bar {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.table-container {
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.product-image-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 120px;
  height: 120px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.product-image-uploader:hover {
  border-color: #409eff;
}

.preview-image {
  width: 120px;
  height: 120px;
  object-fit: cover;
}

.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style> 