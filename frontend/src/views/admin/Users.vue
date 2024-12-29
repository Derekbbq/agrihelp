<template>
  <div class="admin-users">
    <div class="content" v-loading="loading">
      <div class="page-header">
        <h2 class="page-title">用户管理</h2>
        <el-button type="primary" @click="handleAdd">
          添加用户
        </el-button>
      </div>

      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="用户名">
            <el-input
              v-model="searchForm.username"
              placeholder="请输入用户名"
              clearable
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input
              v-model="searchForm.phone"
              placeholder="请输入手机号"
              clearable
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="用户状态">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择状态"
              clearable
            >
              <el-option
                v-for="item in userStatus"
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
        <el-table :data="users" border style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="username" label="用户名" width="120" />
          <el-table-column prop="nickname" label="昵称" width="120" />
          <el-table-column prop="phone" label="手机号" width="120" />
          <el-table-column prop="email" label="邮箱" width="180" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
                {{ row.status === 'active' ? '正常' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="注册时间" width="180" />
          <el-table-column prop="lastLoginTime" label="最后登录" width="180" />
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
                :type="row.status === 'active' ? 'danger' : 'success'"
                @click="handleToggleStatus(row)"
              >
                {{ row.status === 'active' ? '禁用' : '启用' }}
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

    <!-- 用户表单对话框 -->
    <el-dialog
      v-model="userDialog.visible"
      :title="userDialog.type === 'add' ? '添加用户' : '编辑用户'"
      width="500px"
    >
      <el-form
        ref="userFormRef"
        :model="userDialog.form"
        :rules="userDialog.rules"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="userDialog.form.username"
            :disabled="userDialog.type === 'edit'"
          />
        </el-form-item>
        <el-form-item
          label="密码"
          prop="password"
          v-if="userDialog.type === 'add'"
        >
          <el-input
            v-model="userDialog.form.password"
            type="password"
            show-password
          />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="userDialog.form.nickname" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userDialog.form.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userDialog.form.email" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="userDialog.form.status">
            <el-option
              v-for="item in userStatus"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="userDialog.visible = false">取消</el-button>
          <el-button
            type="primary"
            @click="submitUser"
            :loading="userDialog.loading"
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

const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 用户状态
const userStatus = [
  { label: '正常', value: 'active' },
  { label: '禁用', value: 'disabled' }
]

// 模拟用户数据
const users = ref([
  {
    id: 1,
    username: 'admin',
    nickname: '管理员',
    phone: '13800138000',
    email: 'admin@example.com',
    status: 'active',
    createTime: '2024-01-01 00:00:00',
    lastLoginTime: '2024-01-20 10:30:00'
  },
  {
    id: 2,
    username: 'user1',
    nickname: '张三',
    phone: '13900139000',
    email: 'user1@example.com',
    status: 'active',
    createTime: '2024-01-02 00:00:00',
    lastLoginTime: '2024-01-19 15:20:00'
  }
])

const searchForm = reactive({
  username: '',
  phone: '',
  status: ''
})

const userDialog = reactive({
  visible: false,
  type: 'add',
  loading: false,
  form: {
    username: '',
    password: '',
    nickname: '',
    phone: '',
    email: '',
    status: 'active'
  },
  rules: {
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
      { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
    ],
    nickname: [
      { required: true, message: '请输入昵称', trigger: 'blur' }
    ],
    phone: [
      { required: true, message: '请输入手机号', trigger: 'blur' },
      { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
    ],
    email: [
      { required: true, message: '请输入邮箱', trigger: 'blur' },
      { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
    ],
    status: [
      { required: true, message: '请选择状态', trigger: 'change' }
    ]
  }
})

const handleSearch = () => {
  currentPage.value = 1
  fetchUsers()
}

const resetSearch = () => {
  searchForm.username = ''
  searchForm.phone = ''
  searchForm.status = ''
  handleSearch()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  fetchUsers()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchUsers()
}

const fetchUsers = async () => {
  loading.value = true
  try {
    // TODO: 调用获取用户列表 API
    await new Promise(resolve => setTimeout(resolve, 1000))
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  userDialog.type = 'add'
  userDialog.form = {
    username: '',
    password: '',
    nickname: '',
    phone: '',
    email: '',
    status: 'active'
  }
  userDialog.visible = true
}

const handleEdit = (user) => {
  userDialog.type = 'edit'
  userDialog.form = { ...user }
  userDialog.visible = true
}

const submitUser = async () => {
  const formRef = document.querySelector('.el-dialog .el-form')
  if (!formRef) return

  try {
    await formRef.validate()
    userDialog.loading = true

    // TODO: 调用添加/编辑用户 API
    await new Promise(resolve => setTimeout(resolve, 1000))

    ElMessage.success(
      userDialog.type === 'add' ? '添加用户成功' : '编辑用户成功'
    )
    userDialog.visible = false
    fetchUsers()
  } catch (error) {
    if (error.message) {
      ElMessage.error(error.message)
    }
  } finally {
    userDialog.loading = false
  }
}

const handleToggleStatus = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要${user.status === 'active' ? '禁用' : '启用'}该用户吗？`,
      '提示',
      {
        type: 'warning'
      }
    )
    // TODO: 调用修改用户状态 API
    await new Promise(resolve => setTimeout(resolve, 1000))
    user.status = user.status === 'active' ? 'disabled' : 'active'
    ElMessage.success('操作成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const handleDelete = async (user) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？此操作不可恢复！', '警告', {
      type: 'error'
    })
    // TODO: 调用删除用户 API
    await new Promise(resolve => setTimeout(resolve, 1000))
    const index = users.value.findIndex(item => item.id === user.id)
    if (index > -1) {
      users.value.splice(index, 1)
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
.admin-users {
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
</style> 