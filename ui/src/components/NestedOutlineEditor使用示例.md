<script setup lang="ts">
// import { RouterView } from 'vue-router';

import { ref } from 'vue'
import NestedOutlineEditor from './components/NestedOutlineEditor.vue'

// 🧾 文档类型定义
interface DocumentItem {
  id: string
  title: string
  level: number
  parentId: string | null
}

const documents = ref<DocumentItem[]>([
  { id: '1', title: '一级文档 A', level: 0, parentId: null },
  { id: '2', title: '二级文档 A-1', level: 1, parentId: '1' },
  { id: '3', title: '一级文档 B', level: 0, parentId: null },
  { id: '4', title: '二级文档 B-1', level: 1, parentId: '3' },
  { id: '5', title: '三级文档 B-1-1', level: 2, parentId: '4' },
])

const selectedDoc = ref<DocumentItem | null>(null)

const handleSelect = (doc: DocumentItem) => {
  selectedDoc.value = doc
}
</script>

<template>
  <div class="p-6">
    <h1 class="text-xl font-bold mb-4">文档结构编辑器</h1>

    <NestedOutlineEditor
        v-model:items="documents"
        :documents="documents"
        @select="handleSelect"
    />

    <div v-if="selectedDoc" class="mt-4 p-4 border rounded bg-gray-50">
      <h2 class="font-semibold text-lg">当前选中文档</h2>
      <p><strong>标题：</strong> {{ selectedDoc.title }}</p>
      <p><strong>ID：</strong> {{ selectedDoc.id }}</p>
      <p><strong>层级：</strong> {{ selectedDoc.level }}</p>
    </div>
  </div>
<!--  <RouterView />-->
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
</style>
