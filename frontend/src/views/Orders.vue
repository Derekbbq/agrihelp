<template>
  <div class="orders-container">
    <div class="filter-section">
      <el-tabs v-model="activeStatus" @tab-click="handleStatusChange">
        <el-tab-pane label="全部" name="全部"></el-tab-pane>
        <el-tab-pane label="待付款" name="待付款"></el-tab-pane>
        <el-tab-pane label="待发货" name="待发货"></el-tab-pane>
        <el-tab-pane label="待收货" name="待收货"></el-tab-pane>
        <el-tab-pane label="已完成" name="已完成"></el-tab-pane>
      </el-tabs>
    </div>

    <div class="orders-list">
      <div v-for="order in filteredOrders" :key="order.orderId" class="order-card">
        <div class="order-header">
          <span class="order-id">订单号：{{ order.orderId }}</span>
          <span class="order-time">下单时间：{{ order.orderTime }}</span>
          <span class="order-status" :class="order.status">{{ order.status }}</span>
        </div>

        <div class="order-items">
          <div v-for="item in order.items" :key="item.id" class="order-item">
            <img :src="item.image" :alt="item.name" class="item-image">
            <div class="item-info">
              <h3>{{ item.name }}</h3>
              <p class="item-category">{{ item.category }}</p>
              <div class="item-price">
                <span class="price">¥{{ item.price }}</span>
                <span class="quantity">x {{ item.quantity }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="order-footer">
          <div class="order-total">
            共 {{ order.totalItems }} 件商品，实付款：
            <span class="total-price">¥{{ order.totalAmount }}</span>
          </div>
          <div class="order-actions">
            <template v-if="order.status === '待付款'">
              <el-button type="primary" @click="handlePay(order)">立即付款</el-button>
              <el-button type="danger" plain @click="handleCancel(order)">取消订单</el-button>
            </template>
            <template v-else-if="order.status === '待发货'">
              <el-button type="info" plain @click="handleRemind(order)">提醒发货</el-button>
            </template>
            <template v-else-if="order.status === '待收货'">
              <el-button type="success" @click="handleConfirm(order)">确认收货</el-button>
              <el-button type="primary" plain @click="handleTrack(order)">查看物流</el-button>
            </template>
            <template v-else-if="order.status === '已完成'">
              <el-button type="primary" plain @click="handleReview(order)">评价</el-button>
              <el-button plain @click="handleBuyAgain(order)">再次购买</el-button>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeStatus = ref('全部')

// 模拟订单数据
const orders = ref([
  {
    orderId: 'ORDER202401200001',
    orderTime: '2024-01-20 10:30:00',
    status: '待付款',
    totalItems: 3,
    totalAmount: 25.97,
    items: [
      {
        id: 1,
        name: '红富士苹果',
        category: '水果',
        price: 5.99,
        quantity: 2,
        image: 'https://img14.360buyimg.com/n1/jfs/t1/117494/23/23901/167197/63da3cbeF3f9c277a/7e6a608be8c373dd.jpg'
      },
      {
        id: 2,
        name: '有机胡萝卜',
        category: '蔬菜',
        price: 3.99,
        quantity: 1,
        image: 'https://img14.360buyimg.com/n1/jfs/t1/36429/25/19599/193202/63da3cc3F34d79a27/d26b123219987c41.jpg'
      }
    ]
  },
  {
    orderId: 'ORDER202401190001',
    orderTime: '2024-01-19 15:20:00',
    status: '待收货',
    totalItems: 1,
    totalAmount: 39.90,
    items: [
      {
        id: 3,
        name: '有机大米',
        category: '粮油',
        price: 39.90,
        quantity: 1,
        image: 'https://img14.360buyimg.com/n1/jfs/t1/101100/40/38453/183115/63da3cb6F67000a9b/b273a107f3b7da01.jpg'
      }
    ]
  }
])

const filteredOrders = computed(() => {
  if (activeStatus.value === '全部') {
    return orders.value
  }
  return orders.value.filter(order => order.status === activeStatus.value)
})

const handleStatusChange = () => {
  // 可以在这里添加额外的状态切换逻辑
}

const handlePay = (order) => {
  ElMessage.info('支付功能开发中...')
}

const handleCancel = (order) => {
  ElMessageBox.confirm(
    '确定要取消该订单吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    ElMessage.success('订单已取消')
  }).catch(() => {})
}

const handleRemind = (order) => {
  ElMessage.success('已提醒商家发货')
}

const handleConfirm = (order) => {
  ElMessageBox.confirm(
    '确认已收到商品？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info',
    }
  ).then(() => {
    ElMessage.success('确认收货成功')
  }).catch(() => {})
}

const handleTrack = (order) => {
  ElMessage.info('物流查询功能开发中...')
}

const handleReview = (order) => {
  ElMessage.info('评价功能开发中...')
}

const handleBuyAgain = (order) => {
  ElMessage.success('已将商品加入购物车')
}
</script>

<style scoped>
.orders-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.filter-section {
  margin-bottom: 20px;
}

.order-card {
  background: #fff;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.order-header {
  padding: 15px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f8f9fa;
}

.order-id {
  color: #333;
  font-weight: bold;
}

.order-time {
  color: #666;
}

.order-status {
  font-weight: bold;
}

.order-status.待付款 {
  color: #f56c6c;
}

.order-status.待发货 {
  color: #e6a23c;
}

.order-status.待收货 {
  color: #409eff;
}

.order-status.已完成 {
  color: #67c23a;
}

.order-items {
  padding: 15px;
}

.order-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.order-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
  margin-right: 15px;
}

.item-info {
  flex: 1;
}

.item-info h3 {
  margin: 0 0 5px;
  font-size: 16px;
  color: #333;
}

.item-category {
  color: #666;
  font-size: 14px;
  margin: 0 0 5px;
}

.item-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.quantity {
  color: #666;
}

.order-footer {
  padding: 15px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f8f9fa;
}

.order-total {
  color: #666;
}

.total-price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
  margin-left: 5px;
}

.order-actions {
  display: flex;
  gap: 10px;
}
</style> 