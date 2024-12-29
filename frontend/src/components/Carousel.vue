<template>
  <div class="carousel-container">
    <el-carousel :interval="5000" type="card" height="400px">
      <el-carousel-item v-for="item in carouselItems" :key="item.id">
        <div class="carousel-content" :style="{ backgroundImage: `url(${item.image})` }">
          <div class="carousel-info">
            <h2>{{ item.title }}</h2>
            <p>{{ item.description }}</p>
            <el-button type="primary" size="large" @click="handleClick(item)">
              {{ item.buttonText }}
            </el-button>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const carouselItems = ref([
  {
    id: 1,
    title: '新鲜有机蔬菜',
    description: '来自高原的天然好味道，无农药，更健康',
    image: '/images/banners/vegetables-banner.jpg',
    buttonText: '立即选购',
    link: '/category/vegetables'
  },
  {
    id: 2,
    title: '当季水果精选',
    description: '应季鲜果，产地直供，品质保证',
    image: '/images/banners/fruits-banner.jpg',
    buttonText: '查看详情',
    link: '/category/fruits'
  },
  {
    id: 3,
    title: '助农特产系列',
    description: '支持农户，品味乡村美食',
    image: '/images/banners/specialty-banner.jpg',
    buttonText: '了解更多',
    link: '/category/specialty'
  }
])

const handleClick = (item) => {
  router.push(item.link)
}
</script>

<style scoped>
.carousel-container {
  width: 100%;
  margin: 20px 0;
}

.carousel-content {
  height: 100%;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  border-radius: 12px;
  overflow: hidden;
  position: relative;
  display: flex;
  align-items: center;
}

.carousel-content::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(90deg, rgba(0,0,0,0.6) 0%, rgba(0,0,0,0.3) 50%, rgba(0,0,0,0) 100%);
}

.carousel-info {
  position: relative;
  color: #fff;
  padding: 40px;
  max-width: 500px;
}

.carousel-info h2 {
  font-size: 36px;
  font-weight: 600;
  margin: 0 0 16px;
  line-height: 1.2;
}

.carousel-info p {
  font-size: 18px;
  margin: 0 0 24px;
  opacity: 0.9;
  line-height: 1.6;
}

:deep(.el-carousel__item) {
  border-radius: 12px;
}

:deep(.el-carousel__item:not(.is-active)) {
  filter: brightness(0.7);
  transform: scale(0.9);
}

:deep(.el-button) {
  padding: 12px 24px;
  font-size: 16px;
}

@media (max-width: 768px) {
  .carousel-content {
    align-items: flex-end;
  }

  .carousel-info {
    padding: 20px;
    background: linear-gradient(0deg, rgba(0,0,0,0.8) 0%, rgba(0,0,0,0.4) 100%);
    width: 100%;
    max-width: none;
  }

  .carousel-info h2 {
    font-size: 24px;
    margin-bottom: 8px;
  }

  .carousel-info p {
    font-size: 14px;
    margin-bottom: 16px;
  }
}
</style> 