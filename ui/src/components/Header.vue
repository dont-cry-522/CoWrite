<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeftIcon } from 'lucide-vue-next'

// 获取当前路径和路由
const route = useRoute()
const router = useRouter()

// 根据当前路径自动生成面包屑
const breadcrumbItems = computed(() => {
  const pathSegments = route.path.split('/').filter(segment => segment)
  const breadcrumbArray = pathSegments.map((segment, index) => {
    const path = '/' + pathSegments.slice(0, index + 1).join('/')
    return { label: segment, path }
  })
  return breadcrumbArray
})

// 返回到上一个路由
const goBack = () => {
  router.go(-1)
}
</script>

<template>
  <div class="header">
    <div class="header-left">
      <button @click="goBack" class="back-button">
        <ArrowLeftIcon class="icon" /> 返回
      </button>
    </div>
    <div class="header-center">
      <!-- 面包屑导航 -->
      <nav class="breadcrumb">
        <span v-for="(item, index) in breadcrumbItems" :key="index">
          <a :href="item.path" class="breadcrumb-item">{{ item.label }}</a>
          <span v-if="index < breadcrumbItems.length - 1" class="separator"> / </span>
        </span>
      </nav>
    </div>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  justify-content: flex-start; /* 改为居左对齐 */
  align-items: center;
  padding: 16px 24px;
  background-color: #f9fafb;
  border-bottom: 1px solid #e5e7eb;
}

.header-left {
  display: flex;
  align-items: center;
}

.back-button {
  background-color: transparent;
  border: none;
  color: #4b5563;
  font-size: 14px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: background-color 0.2s ease;
}

.back-button:hover {
  background-color: #f3f4f6;
}

.header-center {
  flex-grow: 1;
  text-align: left; /* 改为居左对齐 */
}

.breadcrumb {
  display: inline-flex;
  align-items: center;
  font-size: 14px;
  color: #6b7280;
}

.breadcrumb-item {
  color: #4b5563;
  text-decoration: none;
  transition: color 0.2s ease;
}

.breadcrumb-item:hover {
  color: #1e40af;
}

.separator {
  color: #9ca3af;
  margin: 0 8px;
}

.icon {
  width: 16px;
  height: 16px;
  color: #4b5563;
}
</style>
