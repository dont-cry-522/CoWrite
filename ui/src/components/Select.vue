<template>
  <div class="select-wrapper" ref="wrapper">
    <button
        class="select-trigger"
        :class="{ open }"
        @click="toggle"
        :aria-expanded="open"
    >
      <span>{{ selectedLabel || placeholder }}</span>
      <ChevronDownIcon class="chevron" />
    </button>

    <transition name="fade-slide">
      <ul v-if="open" class="select-dropdown">
        <li
            v-for="option in options"
            :key="option.value"
            :class="{ selected: option.value === modelValue }"
            @click="select(option.value)"
        >
          <CheckIcon v-if="option.value === modelValue" class="check" />
          {{ option.label }}
        </li>
      </ul>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { onClickOutside } from '@vueuse/core'
import { ChevronDown as ChevronDownIcon, Check as CheckIcon } from 'lucide-vue-next'

const props = defineProps<{
  modelValue: string | number | null
  options: { value: string | number; label: string }[]
  placeholder?: string
}>()
const emit = defineEmits(['update:modelValue'])

const open = ref(false)
const wrapper = ref<HTMLElement | null>(null)

onClickOutside(wrapper, () => (open.value = false))

const selectedLabel = computed(() => {
  const selected = props.options.find(o => o.value === props.modelValue)
  return selected?.label
})

function toggle() {
  open.value = !open.value
}
function select(value: string | number) {
  emit('update:modelValue', value)
  open.value = false
}
</script>

<style scoped>
.select-wrapper {
  position: relative;
  width: 100%;
}

.select-trigger {
  width: 100%;
  padding: 0.5rem 0.75rem;
  border: 1px solid #ccc;
  background: white;
  border-radius: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.875rem;
  transition: all 0.2s ease;
  cursor: pointer;
}
.select-trigger.open {
  border-color: #0ea5e9;
  box-shadow: 0 0 0 2px rgba(14, 165, 233, 0.2);
}

.chevron {
  width: 1rem;
  height: 1rem;
  color: #999;
  transition: transform 0.3s ease;
}
.select-trigger.open .chevron {
  transform: rotate(180deg);
}

.select-dropdown {
  position: absolute;
  z-index: 10;
  top: calc(100% + 4px);
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #ccc;
  border-radius: 0.75rem;
  padding: 0.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.select-dropdown li {
  padding: 0.5rem;
  border-radius: 0.5rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: background-color 0.2s ease;
}
.select-dropdown li:hover {
  background-color: #f0f9ff;
}
.select-dropdown li.selected {
  background-color: #e0f7fa;
  font-weight: 500;
}

.check {
  width: 1rem;
  height: 1rem;
  color: #0ea5e9;
}

.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.2s ease;
}
.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(-8px);
}
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>
