<template>
  <div v-if="visible" class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <h3>{{ title }}</h3>
      <p>{{ content }}</p>
      <button @click="closeModal">关闭</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const visible = ref(false);
const title = ref('');
const content = ref('');

const showModal = (modalTitle: string, modalContent: string) => {
  title.value = modalTitle;
  content.value = modalContent;
  visible.value = true;
};

const closeModal = () => {
  visible.value = false;
};

defineExpose({ showModal, closeModal });
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: #3A4A7E;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  width: 100%;
  color: #F4EEFF;
}

h3 {
  margin-bottom: 10px;
  font-size: 18px;
  color: #5B7DB1;
}

button {
  margin-top: 10px;
  padding: 8px 16px;
  background-color: #5B7DB1;
  color: #F4EEFF;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #4A6A9E;
}
</style> 