<template>
  <div
      class="avatar-wrapper"
      :class="[sizeClass, shapeClass, ring ? 'avatar-ring' : '']"
      :style="{ background: gradient }"
      @mouseenter="showTooltip = true"
      @mouseleave="showTooltip = false"
      @click="handleClick"
  >
    <img
        v-if="src && !hasError"
        :src="src"
        :alt="alt"
        class="avatar-img"
        @error="hasError = true"
    />
    <span v-else class="avatar-fallback">
      {{ fallbackText }}
    </span>

    <!-- Tooltip -->
    <div v-if="tooltip && showTooltip" class="avatar-tooltip">
      {{ tooltip }}
    </div>

    <!-- 文件上传（隐藏） -->
    <input
        v-if="upload"
        ref="fileInput"
        type="file"
        accept="image/*"
        class="hidden"
        @change="handleUpload"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const props = defineProps<{
  src?: string
  alt?: string
  size?: 'sm' | 'md' | 'lg' | 'xl' | 'vlg'
  shape?: 'circle' | 'rounded' | 'square'
  fallback?: string
  ring?: boolean
  tooltip?: string
  upload?: boolean
}>()

const emit = defineEmits<{
  (e: 'upload', file: File): void
}>()

const hasError = ref(false)
const showTooltip = ref(false)
const fileInput = ref<HTMLInputElement | null>(null)

const sizeClass = computed(() => {
  return {
    sm: 'avatar-sm',
    md: 'avatar-md',
    lg: 'avatar-lg',
    xl: 'avatar-xl',
    vlg: 'avatar-vlg',
  }[props.size || 'md']
})

const shapeClass = computed(() => {
  return {
    circle: 'avatar-circle',
    rounded: 'avatar-rounded',
    square: 'avatar-square',
  }[props.shape || 'circle']
})

const fallbackText = computed(() => {
  if (props.fallback) return props.fallback
  const name = props.alt || '?'
  return name.slice(-2).toUpperCase()
})

const gradient = computed(() => {
  const name = props.alt || '?'
  const hash = [...name].reduce((acc, c) => acc + c.charCodeAt(0), 0)
  const gradients = [
    'linear-gradient(to right, #06b6d4, #3b82f6)',
    'linear-gradient(to right, #f43f5e, #f97316)',
    'linear-gradient(to right, #10b981, #84cc16)',
    'linear-gradient(to right, #8b5cf6, #0ea5e9)',
    'linear-gradient(to right, #f59e0b, #fcd34d)',
  ]
  return gradients[hash % gradients.length]
})

const handleClick = () => {
  if (props.upload && fileInput.value) {
    fileInput.value.click()
  }
}

const handleUpload = (e: Event) => {
  const target = e.target as HTMLInputElement;
  const file = target.files?.[0];
  console.log(file); // 打印文件，确保正确传递
  if (file) {
    emit('upload', file);
  }
};
</script>

<style scoped>
.avatar-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  overflow: hidden;
  cursor: pointer;
  user-select: none;
  transition: transform 0.3s;
}
.avatar-wrapper:hover {
  transform: scale(1.05);
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-fallback {
  font-size: 0.9em;
  z-index: 1;
}

.avatar-sm {
  width: 32px;
  height: 32px;
  font-size: 12px;
}
.avatar-md {
  width: 40px;
  height: 40px;
  font-size: 14px;
}
.avatar-lg {
  width: 56px;
  height: 56px;
  font-size: 16px;
}

.avatar-vlg {
  width: 80px;
  height: 80px;
  font-size: 16px;
}

.avatar-xl {
  width: 80px;
  height: 80px;
  font-size: 18px;
}

.avatar-circle {
  border-radius: 999px;
}
.avatar-rounded {
  border-radius: 12px;
}
.avatar-square {
  border-radius: 0;
}

.avatar-ring {
  box-shadow: 0 0 0 2px rgba(255, 255, 255, 0.8), 0 0 0 4px rgba(0, 204, 255, 0.5);
}

.avatar-tooltip {
  position: absolute;
  bottom: -24px;
  background-color: rgba(0, 0, 0, 0.7);
  padding: 2px 6px;
  font-size: 12px;
  border-radius: 4px;
  color: white;
  white-space: nowrap;
}
.hidden {
  display: none;
}
</style>
