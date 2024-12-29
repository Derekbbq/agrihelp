<template>
  <div class="image-upload">
    <el-upload
      class="upload-component"
      action="/api/files/upload"
      :show-file-list="false"
      :on-success="handleSuccess"
      :on-error="handleError"
      :before-upload="beforeUpload"
    >
      <img v-if="modelValue" :src="modelValue" class="preview-image">
      <el-icon v-else class="upload-icon"><Plus /></el-icon>
    </el-upload>
  </div>
</template>

<script>
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'ImageUpload',
  components: {
    Plus
  },
  props: {
    modelValue: {
      type: String,
      default: ''
    }
  },
  emits: ['update:modelValue'],
  setup(props, { emit }) {
    const handleSuccess = (response) => {
      if (response.code === 200) {
        emit('update:modelValue', response.data)
        ElMessage.success('上传成功')
      } else {
        ElMessage.error(response.message || '上传失败')
      }
    }

    const handleError = () => {
      ElMessage.error('上传失败')
    }

    const beforeUpload = (file) => {
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

    return {
      handleSuccess,
      handleError,
      beforeUpload
    }
  }
}
</script>

<style scoped>
.image-upload {
  display: inline-block;
}

.upload-component {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.upload-component:hover {
  border-color: #409EFF;
}

.upload-icon {
  font-size: 28px;
  color: #8c939d;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
