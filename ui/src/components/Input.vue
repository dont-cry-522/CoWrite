<template>
  <div class="input-container" :class="{ 'input-error': errorMessage }">
    <label v-if="label" :for="id" class="input-label">{{ label }}</label>
    <div class="input-wrapper">
      <input
          :value="modelValue"
          @input="onInput"
          :type="type"
          :placeholder="placeholder"
          :disabled="disabled"
          :readonly="readonly"
          :id="id"
          :aria-describedby="errorMessage ? errorId : undefined"
          :class="['input-field', { 'input-disabled': disabled, 'input-readonly': readonly }]"
          :maxlength="maxlength"
      />
      <div v-if="icon" class="input-icon" :class="iconClass" @click="iconClick">
        <slot name="icon">{{ icon }}</slot>
      </div>
    </div>
    <p v-if="errorMessage" class="input-error-message" :id="errorId">{{ errorMessage }}</p>
  </div>
</template>

<script setup lang="ts">
import { computed, defineProps, defineEmits } from 'vue';

const props = defineProps({
  modelValue: {
    type: [String, Number],
    default: '',
  },
  type: {
    type: String,
    default: 'text',
  },
  label: String,
  placeholder: String,
  disabled: {
    type: Boolean,
    default: false,
  },
  readonly: {
    type: Boolean,
    default: false,
  },
  errorMessage: String,
  icon: String,
  iconClass: String,
  maxlength: {
    type: Number,
    default: 255,
  },
  id: {
    type: String,
    default: 'input-id',
  },
});

const emit = defineEmits(['update:modelValue']);

const errorId = computed(() => `${props.id}-error`);

const onInput = (event: Event) => {
  emit('update:modelValue', (event.target as HTMLInputElement).value);
};

const iconClick = () => {
  // 处理图标点击事件
};
</script>

<style scoped>
.input-container {
  position: relative;
  margin-bottom: 1rem;
}

.input-label {
  font-size: 14px;
  color: #333;
  margin-bottom: 8px;
  display: block;
}

.input-wrapper {
  position: relative;
}

.input-field {
  width: 100%;
  padding: 12px 16px;
  font-size: 14px;
  border-radius: 8px;
  border: 1px solid #ddd;
  outline: none;
  transition: border 0.3s ease;
}

.input-field:focus {
  border-color: #06b6d4;
}

.input-field.input-disabled {
  background-color: #f4f4f4;
  cursor: not-allowed;
}

.input-field.input-readonly {
  background-color: #f4f4f4;
  cursor: not-allowed;
}

.input-error {
  border-color: #f44336;
}

.input-error-message {
  color: #f44336;
  font-size: 12px;
  margin-top: 4px;
}

.input-icon {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
}

.input-icon:hover {
  opacity: 0.7;
}

.input-icon:focus {
  outline: none;
}
</style>
