<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { RouterView } from 'vue-router';
import api from './api/index.js';
import { onMounted} from "vue";
// 存储info信息状态
const info = ref<string>('')

// 存储点的颜色
const statusColor = computed(() => {
  return info.value === 'PONG' ? 'green' : 'red'
})

// 异步获取信息
onMounted(async () => {
  const response = await api.commonApi.getBackInfo()
  info.value = (response as unknown) as string // 假设响应为 'PONG' 或其他字符串
})
// 获取当前路由
const route = useRoute()

// 动态计算过渡动画
const transitionName = computed(() => {
  return (route.meta.transition as string) || 'fade'; // 默认使用 'fade' 过渡
})
</script>

<template>
  <div class="point" :style="{ position: 'absolute', bottom: '10px', left: '10px', width: '10px', height: '10px', borderRadius: '50%', backgroundColor: statusColor }"></div>
  <transition :name="transitionName" mode="out-in">
    <RouterView />
  </transition>
</template>

<style>
html, body {
  height: 100%;
}
#app {
  height: 100%;
  display: flex;
  flex-direction: column;
}
/* 示例的过渡动画 */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s ease;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}

.slide-enter-active, .slide-leave-active {
  transition: transform 0.5s ease;
}
.slide-enter, .slide-leave-to {
  transform: translateX(100%);
}

.point {
  z-index: 999;
}
</style>
