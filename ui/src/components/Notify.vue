<template>
  <div
      class="notify"
      :class="type"
      @mouseenter="clearTimer"
      @mouseleave="startTimer"
  >
    <div class="icon">{{ icon }}</div>
    <div class="content">
      <p class="title">{{ title }}</p>
      <p class="description" v-if="description">{{ description }}</p>
    </div>
    <button class="close" @click="close">×</button>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, computed } from 'vue'

const props = defineProps<{
  id: number
  title: string
  description?: string
  type: 'success' | 'error' | 'info' | 'warning'
  duration?: number
  onClose: (id: number) => void
}>()

let timer: any = null

const startTimer = () => {
  if (props.duration !== 0) {
    timer = setTimeout(() => props.onClose(props.id), props.duration || 3000)
  }
}
const clearTimer = () => clearTimeout(timer)
const close = () => {
  clearTimer()
  props.onClose(props.id)
}

const icon = computed(() => {
  switch (props.type) {
    case 'success': return '✅'
    case 'error': return '❌'
    case 'info': return 'ℹ️'
    case 'warning': return '⚠️'
    default: return ''
  }
})

onMounted(startTimer)
onBeforeUnmount(clearTimer)
</script>

<style scoped>
.notify {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 12px;
  background-color: #f9fafb;
  border: 1px solid #e5e7eb;
  box-shadow: 0 4px 16px rgba(0,0,0,0.05);
  animation: fade-in 0.25s ease-out;
  position: relative;
  overflow: hidden;
}

.success { background-color: #ecfdf5; border-color: #d1fae5; }
.error   { background-color: #fef2f2; border-color: #fecaca; }
.info    { background-color: #eff6ff; border-color: #bfdbfe; }
.warning { background-color: #fefce8; border-color: #fde68a; }

.icon {
  font-size: 20px;
  line-height: 1;
  margin-top: 2px;
}

.content {
  flex: 1;
  font-size: 14px;
  color: #374151;
}

.title {
  font-weight: 600;
  font-size: 14px;
  margin-bottom: 2px;
}

.description {
  font-size: 13px;
  color: #6b7280;
  line-height: 1.4;
}

.close {
  background: none;
  border: none;
  color: #9ca3af;
  font-size: 16px;
  cursor: pointer;
  margin-left: 8px;
}

.close:hover {
  color: #4b5563;
}

@keyframes fade-in {
  from { opacity: 0; transform: translateY(-8px); }
  to   { opacity: 1; transform: translateY(0); }
}
</style>
