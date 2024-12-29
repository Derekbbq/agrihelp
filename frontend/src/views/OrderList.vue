<template>
  <div class="order-list">
    <h2>我的订单</h2>

    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="待付款" name="unpaid" />
      <el-tab-pane label="待发货" name="pending" />
      <el-tab-pane label="待收货" name="shipped" />
      <el-tab-pane label="已完成" name="completed" />
    </el-tabs>

    <div class="order-content">
      <el-card v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <div class="order-info">
            <span class="order-number">订单号：{{ order.orderNumber }}</span>
            <span class="order-date">下单时间：{{ formatDate(order.createTime) }}</span>
          </div>
          <el-tag :type="getStatusType(order.status)">
            {{ getStatusText(order.status) }}
          </el-tag>
        </div>

        <div class="order-items">
          <div v-for="item in order.items" :key="item.id" class="order-item">
            <el-image :src="item.image" :alt="item.name" class="item-image" />
            <div class="item-info">
              <div class="item-name">{{ item.name }}</div>
              <div class="item-price">¥{{ item.price }} × {{ item.quantity }}</div>
            </div>
          </div>
        </div>

        <div class="order-footer">
          <div class="order-total">
            共 {{ getTotalQuantity(order) }} 件商品，
            总计：<span class="total-price">¥{{ order.totalAmount.toFixed(2) }}</span>
          </div>
          <div class="order-actions">
            <el-button
              v-if="order.status === 'unpaid'"
              type="primary"
              @click="handlePay(order)"
            >
              去支付
            </el-button>
            <el-button
              v-if="order.status === 'shipped'"
              type="success"
              @click="handleConfirmReceive(order)"
            >
              确认收货
            </el-button>
            <el-button
              @click="$router.push(`/orders/${order.id}`)"
            >
              查看详情
            </el-button>
          </div>
        </div>
      </el-card>

      <el-empty
        v-if="!orders.length"
        description="暂无订单"
      >
        <el-button type="primary" @click="$router.push('/products')">
          去购物
        </el-button>
      </el-empty>

      <div class="pagination" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrders, updateOrderStatus } from '@/api/order'

export default {
  name: 'OrderList',
  setup() {
    const activeTab = ref('all')
    const orders = ref([])
    const total = ref(0)
    const currentPage = ref(1)
    const pageSize = ref(10)

    const loadOrders = async () => {
      try {
        const params = {
          page: currentPage.value,
          size: pageSize.value,
          status: activeTab.value === 'all' ? '' : activeTab.value
        }
        const res = await getOrders(params)
        orders.value = res.data.records
        total.value = res.data.total
      } catch (error) {
        ElMessage.error('加载订单列表失败')
      }
    }

    const handleTabClick = () => {
      currentPage.value = 1
      loadOrders()
    }

    const handleSizeChange = (val) => {
      pageSize.value = val
      loadOrders()
    }

    const handleCurrentChange = (val) => {
      currentPage.value = val
      loadOrders()
    }

    const handlePay = (order) => {
      ElMessage('支付功能待实现')
    }

    const handleConfirmReceive = async (order) => {
      try {
        await ElMessageBox.confirm('确认已收到商品？')
        await updateOrderStatus(order.id, 'completed')
        ElMessage.success('确认收货成功')
        loadOrders()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('确认收货失败')
        }
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

    const getTotalQuantity = (order) => {
      return order.items.reduce((total, item) => total + item.quantity, 0)
    }

    const formatDate = (date) => {
      return new Date(date).toLocaleString()
    }

    onMounted(() => {
      loadOrders()
    })

    return {
      activeTab,
      orders,
      total,
      currentPage,
      pageSize,
      handleTabClick,
      handleSizeChange,
      handleCurrentChange,
      handlePay,
      handleConfirmReceive,
      getStatusType,
      getStatusText,
      getTotalQuantity,
      formatDate
    }
  }
}
</script>

<style scoped>
.order-list {
  padding: 20px;
}

.order-content {
  margin-top: 20px;
}

.order-card {
  margin-bottom: 20px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.order-info {
  display: flex;
  gap: 20px;
}

.order-number {
  font-weight: bold;
}

.order-date {
  color: #666;
}

.order-items {
  border-top: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
  padding: 20px 0;
}

.order-item {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.order-item:last-child {
  margin-bottom: 0;
}

.item-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
}

.item-info {
  flex: 1;
}

.item-name {
  margin-bottom: 5px;
}

.item-price {
  color: #666;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.total-price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}

.order-actions {
  display: flex;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
