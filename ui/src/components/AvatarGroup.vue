<template>
  <div :class="['avatar-group', directionClass]">
    <div
        v-for="(user, index) in visibleUsers"
        :key="user.id || index"
        class="avatar-item"
    >
      <Avatar
          :src="user.avatar"
          :alt="user.name || '?'"
          :size="size"
          class="avatar-border"
      />
    </div>

    <div
        v-if="remaining > 0"
        :class="['avatar-extra', sizeClass]"
    >
      +{{ remaining }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import Avatar from './Avatar.vue'

interface User {
  id?: string | number
  name?: string
  avatar?: string
}

const props = defineProps<{
  users: User[]
  max?: number
  size?: 'sm' | 'md' | 'lg' | 'xl'
  direction?: 'horizontal' | 'vertical'
}>()

const size = props.size || 'md'
const max = props.max ?? 3

const visibleUsers = computed(() => props.users.slice(0, max))
const remaining = computed(() => props.users.length - max)

const sizeClass = computed(() => {
  return {
    sm: 'extra-sm',
    md: 'extra-md',
    lg: 'extra-lg',
    xl: 'extra-xl',
  }[size]
})

const directionClass = computed(() =>
    props.direction === 'vertical' ? 'vertical' : 'horizontal'
)
</script>

<style scoped>
.avatar-group {
  display: flex;
  align-items: center;
}
.avatar-group.horizontal {
  flex-direction: row;
}
.avatar-group.vertical {
  flex-direction: column;
}
.avatar-item {
  margin-left: -8px;
  transition: transform 0.2s;
}
.avatar-item:first-child {
  margin-left: 0;
}
.avatar-item:hover {
  transform: translateY(-4px);
}

.avatar-border {
  border: 2px solid #fff;
  box-sizing: content-box;
}

.avatar-extra {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #ddd;
  color: #333;
  border: 2px solid #fff;
  border-radius: 50%;
  font-weight: 500;
}
.extra-sm {
  width: 24px;
  height: 24px;
  font-size: 10px;
}
.extra-md {
  width: 32px;
  height: 32px;
  font-size: 12px;
}
.extra-lg {
  width: 40px;
  height: 40px;
  font-size: 14px;
}
.extra-xl {
  width: 56px;
  height: 56px;
  font-size: 16px;
}
</style>
