<template>
  <div class="profile">
    <div class="content" v-loading="loading">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="user-card">
            <div class="user-avatar">
              <el-avatar :size="100" :src="userInfo.avatar">
                {{ userInfo.username?.charAt(0).toUpperCase() }}
              </el-avatar>
            </div>
            <h2 class="user-name">{{ userInfo.username }}</h2>
            <p class="user-role">{{ getRoleText(userInfo.role) }}</p>
            <el-divider />
            <div class="user-stats">
              <div class="stat-item">
                <div class="stat-value">{{ userInfo.orderCount }}</div>
                <div class="stat-label">订单数</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ userInfo.points }}</div>
                <div class="stat-label">积分</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="18">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>个人信息</span>
                <el-button type="primary" @click="startEdit">编辑</el-button>
              </div>
            </template>

            <el-form 
              ref="formRef"
              :model="form"
              :rules="rules"
              :disabled="!isEditing"
              label-width="100px"
            >
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" />
              </el-form-item>
              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="form.nickname" />
              </el-form-item>
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="form.phone" />
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" />
              </el-form-item>
              <el-form-item label="地址" prop="address">
                <el-input type="textarea" v-model="form.address" :rows="3" />
              </el-form-item>

              <el-form-item v-if="isEditing">
                <el-button type="primary" @click="saveProfile" :loading="saving">
                  保存
                </el-button>
                <el-button @click="cancelEdit">取消</el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <el-card class="security-card">
            <template #header>
              <div class="card-header">
                <span>账号安全</span>
              </div>
            </template>

            <div class="security-items">
              <div class="security-item">
                <div class="security-info">
                  <h3>登录密码</h3>
                  <p>建议您定期更改密码，设置一个安全性高的密码</p>
                </div>
                <el-button link type="primary" @click="changePassword">
                  修改密码
                </el-button>
              </div>
              <el-divider />
              <div class="security-item">
                <div class="security-info">
                  <h3>手机绑定</h3>
                  <p>已绑定：{{ maskPhone(userInfo.phone) }}</p>
                </div>
                <el-button link type="primary" @click="changePhone">
                  更换手机
                </el-button>
              </div>
              <el-divider />
              <div class="security-item">
                <div class="security-info">
                  <h3>邮箱绑定</h3>
                  <p>已绑定：{{ maskEmail(userInfo.email) }}</p>
                </div>
                <el-button link type="primary" @click="changeEmail">
                  更换邮箱
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="passwordDialog.visible"
      title="修改密码"
      width="400px"
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordDialog.form"
        :rules="passwordDialog.rules"
        label-width="100px"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="passwordDialog.form.oldPassword"
            type="password"
            show-password
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordDialog.form.newPassword"
            type="password"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordDialog.form.confirmPassword"
            type="password"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="passwordDialog.visible = false">取消</el-button>
          <el-button
            type="primary"
            @click="submitPasswordChange"
            :loading="passwordDialog.loading"
          >
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const saving = ref(false)
const isEditing = ref(false)
const formRef = ref(null)

// 模拟用户数据
const userInfo = reactive({
  username: 'zhangsan',
  nickname: '张三',
  phone: '13800138000',
  email: 'zhangsan@example.com',
  address: '陕西省西安市雁塔区',
  avatar: '',
  role: 'user',
  orderCount: 10,
  points: 500
})

const form = ref({
  username: userInfo.username,
  nickname: userInfo.nickname,
  phone: userInfo.phone,
  email: userInfo.email,
  address: userInfo.address
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入地址', trigger: 'blur' }
  ]
}

const passwordDialog = reactive({
  visible: false,
  loading: false,
  form: {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  },
  rules: {
    oldPassword: [
      { required: true, message: '请输入原密码', trigger: 'blur' }
    ],
    newPassword: [
      { required: true, message: '请输入新密码', trigger: 'blur' },
      { min: 6, message: '密码长度不能小于 6 个字符', trigger: 'blur' }
    ],
    confirmPassword: [
      { required: true, message: '请再次输入新密码', trigger: 'blur' },
      {
        validator: (rule, value, callback) => {
          if (value !== passwordDialog.form.newPassword) {
            callback(new Error('两次输入密码不一致!'))
          } else {
            callback()
          }
        },
        trigger: 'blur'
      }
    ]
  }
})

const getRoleText = (role) => {
  const roles = {
    admin: '管理员',
    seller: '店家',
    user: '普通用户'
  }
  return roles[role] || role
}

const maskPhone = (phone) => {
  return phone?.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

const maskEmail = (email) => {
  if (!email) return ''
  const [name, domain] = email.split('@')
  return `${name.charAt(0)}***@${domain}`
}

const startEdit = () => {
  isEditing.value = true
}

const cancelEdit = () => {
  isEditing.value = false
  form.value = {
    username: userInfo.username,
    nickname: userInfo.nickname,
    phone: userInfo.phone,
    email: userInfo.email,
    address: userInfo.address
  }
}

const saveProfile = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    saving.value = true
    
    // TODO: 调用更新个人信息 API
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    Object.assign(userInfo, form.value)
    isEditing.value = false
    ElMessage.success('保存成功')
  } catch (error) {
    if (error.message) {
      ElMessage.error(error.message)
    }
  } finally {
    saving.value = false
  }
}

const changePassword = () => {
  passwordDialog.visible = true
}

const submitPasswordChange = async () => {
  const formRef = document.querySelector('.el-dialog .el-form')
  if (!formRef) return

  try {
    await formRef.validate()
    passwordDialog.loading = true
    
    // TODO: 调用修改密码 API
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('密码修改成功')
    passwordDialog.visible = false
    passwordDialog.form = {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    }
  } catch (error) {
    if (error.message) {
      ElMessage.error(error.message)
    }
  } finally {
    passwordDialog.loading = false
  }
}

const changePhone = () => {
  ElMessage.info('功能开发中')
}

const changeEmail = () => {
  ElMessage.info('功能开发中')
}
</script>

<style scoped>
.profile {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 180px);
}

.content {
  max-width: 1200px;
  margin: 0 auto;
}

.user-card {
  text-align: center;
  padding: 20px;
}

.user-avatar {
  margin-bottom: 20px;
}

.user-name {
  font-size: 20px;
  color: #333;
  margin: 0 0 10px;
}

.user-role {
  color: #666;
  margin: 0;
}

.user-stats {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  color: #409EFF;
  font-weight: bold;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-top: 5px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.security-card {
  margin-top: 20px;
}

.security-items {
  padding: 0 20px;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
}

.security-info h3 {
  font-size: 16px;
  color: #333;
  margin: 0 0 8px;
}

.security-info p {
  font-size: 14px;
  color: #666;
  margin: 0;
}

:deep(.el-divider--horizontal) {
  margin: 16px 0;
}
</style> 