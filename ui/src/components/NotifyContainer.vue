<template>
  <div class="notify-container">
    <TransitionGroup name="fade-list" tag="div">
      <Notify
          v-for="item in visibleList"
          :key="item.id"
          v-bind="item"
          :on-close="remove"
      />
    </TransitionGroup>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, nextTick } from 'vue'
import Notify from './Notify.vue'

const notifications = ref<
    { id: number; title: string; description?: string; type: any; duration?: number }[]
>([])

let seed = 0
const maxVisible = 3

const visibleList = computed(() => notifications.value.slice(0, maxVisible))

const add = (
    title: string,
    type: any,
    duration = 3000,
    description?: string
) => {
  const id = ++seed
  notifications.value.push({ id, title, description, type, duration })
}

const remove = (id: number) => {
  notifications.value = notifications.value.filter(n => n.id !== id)
  nextTick(() => {
    // 触发更新以填补空缺（如果存在排队）
  })
}

defineExpose({ add })
</script>

<style scoped>
.notify-container {
  position: fixed;
  top: 24px;
  right: 24px;
  width: 300px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  z-index: 9999;
}

.fade-list-enter-active,
.fade-list-leave-active {
  transition: all 0.25s ease;
}
.fade-list-enter-from {
  opacity: 0;
  transform: translateY(-8px);
}
.fade-list-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>
