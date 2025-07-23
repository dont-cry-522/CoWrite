<template>
  <div class="textarea-wrapper">
    <textarea
        ref="textarea"
        :value="modelValue"
        @input="handleInput"
        :maxlength="maxLength"
        v-bind="$attrs"
        class="textarea"
    />
    <div v-if="showCount && maxLength" class="char-count">
      {{ modelValue?.length || 0 }}/{{ maxLength }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, nextTick } from 'vue'

const props = defineProps<{
  modelValue: string
  showCount?: boolean
  maxLength?: number
}>()
const emit = defineEmits(['update:modelValue'])

const textarea = ref<HTMLTextAreaElement | null>(null)

const resize = () => {
  if (textarea.value) {
    textarea.value.style.height = 'auto'
    textarea.value.style.height = `${textarea.value.scrollHeight}px`
  }
}

const handleInput = (e: Event) => {
  const target = e.target as HTMLTextAreaElement
  emit('update:modelValue', target.value)
  resize()
}

onMounted(() => {
  resize()
})

watch(() => props.modelValue, () => {
  nextTick(resize)
})
</script>

<style scoped>
.textarea-wrapper {
  width: 100%;
  position: relative;
}

.textarea {
  width: 100%;
  min-height: 3rem;
  padding: 0.75rem 1rem;
  border: 1px solid #ccc;
  border-radius: 1rem;
  font-size: 14px;
  resize: none; /* 禁用手动拉伸 */
  line-height: 1.5;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
}

.textarea:focus {
  outline: none;
  border-color: #0ea5e9;
  box-shadow: 0 0 0 2px rgba(14, 165, 233, 0.15);
}

.char-count {
  font-size: 12px;
  color: #999;
  text-align: right;
  margin-top: 0.25rem;
}
</style>
