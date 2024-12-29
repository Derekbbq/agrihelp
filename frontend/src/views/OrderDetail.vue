<template>
  <div v-if="order" class="order-detail">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/orders' }">我的订单</el-breadcrumb-item>
      <el-breadcrumb-item>订单详情</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="status-card">
      <div class="status-header">
        <el-tag :type="getStatusType(order.status)" size="large">
          {{ getStatusText(order.status) }}
        </el-tag>
        <div class="status-actions">
          <el-button
            v-if="order.status === 'unpaid'"
            type="primary"
            @click="handlePay"
          >
            去支付
          </el-button>
          <el-button
            v-if="order.status === 'shipped'"
            type="success"
            @click="handleConfirmReceive"
          >
            确认收货
          </el-button>
        </div>
      </div>
      <el-steps :active="getStatusStep(order.status)" finish-status="success">
        <el-step title="提交订单" />
        <el-step title="付款成功" />
        <el-step title="商家发货" />
        <el-step title="交易完成" />
      </el-steps>
    </el-card>

    <el-card class="order-info">
      <template #header>
        <div class="card-header">
          <span>订单信息</span>
        </div>
      </template>
      <el-descriptions :column="2">
        <el-descriptions-item label="订单编号">{{ order.orderNumber }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDate(order.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="收货人">{{ order.receiver }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ order.phone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ order.address }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card class="order-items">
      <template #header>
        <div class="card-header">
          <span>商品信息</span>
        </div>
      </template>
      <el-table :data="order.items" style="width: 100%">
        <el-table-column label="商品" min-width="400">
          <template #default="{ row }">
            <div class="product-cell">
              <el-image :src="row.image" :alt="row.name" class="product-image" />
              <div class="product-info">
                <div class="product-name">{{ row.name }}</div>
                <div class="product-price">¥{{ row.price }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="100" />
        <el-table-column label="小计" width="150">
          <template #default="{ row }">
            <span class="subtotal">¥{{ (row.price * row.quantity).toFixed(2) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="order-total">
        <span>共 {{ getTotalQuantity() }} 件商品，总计：</span>
        <span class="total-price">¥{{ order.totalAmount.toFixed(2) }}</span>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrder, updateOrderStatus } from '@/api/order'

export default {
  name: 'OrderDetail',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const order = ref(null)

    const loadOrder = async () => {
      try {
        const res = await getOrder(route.params.id)
        order.value = res.data
      } catch (error) {
        ElMessage.error('加载订单详情失败')
      }
    }

    const getStatusType = (status) => {
      const types = {
        unpaid: 'warning',
        pending: 'info',
        shipped: 'primary',
        completed: 'success',
        cancelled: 'danger'
      }
      return types[status] || 'info'
    }

    const getStatusText = (status) => {
      const texts = {
        unpaid: '待付款',
        pending: '待发货',
        shipped: '待收货',
        completed: '已完成',
        cancelled: '已取消'
      }
      return texts[status] || status
    }

    const getStatusStep = (status) => {
      const steps = {
        unpaid: 1,
        pending: 2,
        shipped: 3,
        completed: 4,
        cancelled: 0
      }
      return steps[status] || 0
    }

    const handlePay = () => {
      ElMessage('支付功能待实现')
    }

    const handleConfirmReceive = async () => {
      try {
        await ElMessageBox.confirm('确认已收到商品？')
        await updateOrderStatus(order.value.id, 'completed')
        ElMessage.success('确认收货成功')
        loadOrder()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('确认收货失败')
        }
      }
    }

    const getTotalQuantity = () => {
      return order.value.items.reduce((total, item) => total + item.quantity, 0)
    }

    const formatDate = (date) => {
      return new Date(date).toLocaleString()
    }

    onMounted(() => {
      loadOrder()
    })

    return {
      order,
      getStatusType,
      getStatusText,
      getStatusStep,
      handlePay,
      handleConfirmReceive,
      getTotalQuantity,
      formatDate
    }
  }
}
</script>

<style scoped>
.order-detail {
  padding: 20px;
}

.status-card {
  margin: 20px 0;
}

.status-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.status-actions {
  display: flex;
  gap: 10px;
}

.order-info {
  margin-bottom: 20px;
}

.card-header {
  font-weight: bold;
}

.product-cell {
  display: flex;
  align-items: center;
  gap: 20px;
}

.product-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
}

.product-info {
  flex: 1;
}

.product-name {
  margin-bottom: 5px;
}

.product-price {
  color: #666;
}

.subtotal {
  color: #f56c6c;
  font-weight: bold;
}

.order-total {
  margin-top: 20px;
  text-align: right;
  font-size: 16px;
}

.total-price {
  color: #f56c6c;
  font-size: 20px;
  font-weight: bold;
  margin-left: 10px;
}
</style>
