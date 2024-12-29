<template>
  <div class="admin-orders">
    <div class="content" v-loading="loading">
      <div class="page-header">
        <h2 class="page-title">订单管理</h2>
      </div>

      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="订单号">
            <el-input
              v-model="searchForm.orderNumber"
              placeholder="请输入订单号"
              clearable
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="订单状态">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择状态"
              clearable
            >
              <el-option
                v-for="item in orderStatus"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="下单时间">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
            />
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
        <el-table :data="orders" border style="width: 100%">
          <el-table-column prop="orderNumber" label="订单号" width="180" />
          <el-table-column label="商品信息" min-width="300">
            <template #default="{ row }">
              <div v-for="item in row.items" :key="item.id" class="order-item">
                <el-image
                  :src="item.image"
                  :preview-src-list="[item.image]"
                  fit="cover"
                  class="product-image"
                />
                <div class="item-info">
                  <p class="item-name">{{ item.name }}</p>
                  <p class="item-price">¥{{ item.price }} × {{ item.quantity }}</p>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="totalAmount" label="订单金额" width="120">
            <template #default="{ row }">
              ¥{{ row.totalAmount }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="订单状态" width="120">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="下单时间" width="180" />
          <el-table-column label="收货信息" min-width="200">
            <template #default="{ row }">
              <p>{{ row.receiver }}</p>
              <p>{{ row.phone }}</p>
              <p>{{ row.address }}</p>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button
                v-if="row.status === 'unpaid'"
                link
                type="primary"
                @click="handleShip(row)"
              >
                发货
              </el-button>
              <el-button
                link
                type="primary"
                @click="viewOrderDetail(row)"
              >
                查看详情
              </el-button>
              <el-button
                v-if="row.status === 'unpaid'"
                link
                type="danger"
                @click="handleCancel(row)"
              >
                取消订单
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

    <!-- 发货对话框 -->
    <el-dialog
      v-model="shipDialog.visible"
      title="订单发货"
      width="500px"
    >
      <el-form
        ref="shipFormRef"
        :model="shipDialog.form"
        :rules="shipDialog.rules"
        label-width="100px"
      >
        <el-form-item label="物流公司" prop="company">
          <el-select
            v-model="shipDialog.form.company"
            placeholder="请选择物流公司"
          >
            <el-option
              v-for="item in logistics"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="物流单号" prop="trackingNumber">
          <el-input v-model="shipDialog.form.trackingNumber" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="shipDialog.form.remark"
            type="textarea"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="shipDialog.visible = false">取消</el-button>
          <el-button
            type="primary"
            @click="submitShip"
            :loading="shipDialog.loading"
          >
            确认发货
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 订单状态
const orderStatus = [
  { label: '待付款', value: 'unpaid' },
  { label: '待发货', value: 'unshipped' },
  { label: '已发货', value: 'shipped' },
  { label: '已完成', value: 'completed' },
  { label: '已取消', value: 'cancelled' }
]

// 物流公司
const logistics = [
  { label: '顺丰速运', value: 'SF' },
  { label: '中通快递', value: 'ZTO' },
  { label: '圆通速递', value: 'YTO' },
  { label: '韵达快递', value: 'YD' },
  { label: '申通快递', value: 'STO' }
]

// 模拟订单数据
const orders = ref([
  {
    id: 1,
    orderNumber: 'ORDER202401200001',
    totalAmount: 25.97,
    status: 'unshipped',
    createTime: '2024-01-20 10:30:00',
    receiver: '张三',
    phone: '13800138000',
    address: '陕西省西安市雁塔区',
    items: [
      {
        id: 1,
        name: '红富士苹果',
        price: 5.99,
        quantity: 2,
        image: 'https://img.yzcdn.cn/vant/apple-1.jpg'
      },
      {
        id: 2,
        name: '有机胡萝卜',
        price: 3.99,
        quantity: 1,
        image: 'https://img.yzcdn.cn/vant/cat.jpeg'
      }
    ]
  },
  {
    id: 2,
    orderNumber: 'ORDER202401190001',
    totalAmount: 39.90,
    status: 'completed',
    createTime: '2024-01-19 15:20:00',
    receiver: '李四',
    phone: '13900139000',
    address: '陕西省西安市长安区',
    items: [
      {
        id: 3,
        name: '五常大米',
        price: 39.90,
        quantity: 1,
        image: 'https://img.yzcdn.cn/vant/sand.jpg'
      }
    ]
  }
])

const searchForm = reactive({
  orderNumber: '',
  status: '',
  dateRange: []
})

const shipDialog = reactive({
  visible: false,
  loading: false,
  currentOrder: null,
  form: {
    company: '',
    trackingNumber: '',
    remark: ''
  },
  rules: {
    company: [
      { required: true, message: '请选择物流公司', trigger: 'change' }
    ],
    trackingNumber: [
      { required: true, message: '请输入物流单号', trigger: 'blur' }
    ]
  }
})

const getStatusType = (status) => {
  const types = {
    unpaid: 'warning',
    unshipped: 'info',
    shipped: 'primary',
    completed: 'success',
    cancelled: 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    unpaid: '待付款',
    unshipped: '待发货',
    shipped: '已发货',
    completed: '已完成',
    cancelled: '已取消'
  }
  return texts[status] || status
}

const handleSearch = () => {
  currentPage.value = 1
  fetchOrders()
}

const resetSearch = () => {
  searchForm.orderNumber = ''
  searchForm.status = ''
  searchForm.dateRange = []
  handleSearch()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  fetchOrders()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchOrders()
}

const fetchOrders = async () => {
  loading.value = true
  try {
    // TODO: 调用获取订单列表 API
    await new Promise(resolve => setTimeout(resolve, 1000))
  } catch (error) {
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

const handleShip = (order) => {
  shipDialog.currentOrder = order
  shipDialog.visible = true
}

const submitShip = async () => {
  const formRef = document.querySelector('.el-dialog .el-form')
  if (!formRef) return

  try {
    await formRef.validate()
    shipDialog.loading = true

    // TODO: 调用订单发货 API
    await new Promise(resolve => setTimeout(resolve, 1000))

    if (shipDialog.currentOrder) {
      shipDialog.currentOrder.status = 'shipped'
    }
    ElMessage.success('发货成功')
    shipDialog.visible = false
    shipDialog.form = {
      company: '',
      trackingNumber: '',
      remark: ''
    }
  } catch (error) {
    if (error.message) {
      ElMessage.error(error.message)
    }
  } finally {
    shipDialog.loading = false
  }
}

const handleCancel = async (order) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
      type: 'warning'
    })
    // TODO: 调用取消订单 API
    await new Promise(resolve => setTimeout(resolve, 1000))
    order.status = 'cancelled'
    ElMessage.success('订单已取消')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消订单失败')
    }
  }
}

const viewOrderDetail = (order) => {
  router.push(`/admin/orders/${order.id}`)
}
</script>

<style scoped>
.admin-orders {
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

.order-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 5px 0;
}

.order-item:not(:last-child) {
  border-bottom: 1px solid #eee;
}

.product-image {
  width: 50px;
  height: 50px;
  border-radius: 4px;
}

.item-info {
  flex: 1;
}

.item-name {
  font-size: 14px;
  color: #333;
  margin: 0 0 5px;
}

.item-price {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style> 