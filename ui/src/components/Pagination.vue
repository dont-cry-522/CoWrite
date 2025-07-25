<template>
  <div class="pagination-wrapper">
    <div class="pagination">
      <!-- 首页 -->
      <button
          class="pagination-button"
          :disabled="currentPage === 1"
          @click="changePage(1)"
      >
        {{ labels.first }}
      </button>

      <!-- 上一页 -->
      <button
          class="pagination-button"
          :disabled="currentPage === 1"
          @click="changePage(currentPage - 1)"
      >
        {{ labels.prev }}
      </button>

      <!-- 省略号前 -->
      <span v-if="pages[0] > 1" class="pagination-ellipsis">...</span>

      <!-- 页码按钮 -->
      <button
          v-for="page in pages"
          :key="page"
          class="pagination-button"
          :class="{ active: page === currentPage }"
          @click="changePage(page)"
      >
        {{ page }}
      </button>

      <!-- 省略号后 -->
      <span v-if="pages[pages.length - 1] < totalPages" class="pagination-ellipsis">...</span>

      <!-- 下一页 -->
      <button
          class="pagination-button"
          :disabled="currentPage === totalPages"
          @click="changePage(currentPage + 1)"
      >
        {{ labels.next }}
      </button>

      <!-- 末页 -->
      <button
          class="pagination-button"
          :disabled="currentPage === totalPages"
          @click="changePage(totalPages)"
      >
        {{ labels.last }}
      </button>
    </div>

    <div class="pagination-controls">
      <!-- 总条数 -->
      <span class="pagination-total">
        {{ labels.total.replace('{total}', total.toString()) }}
      </span>

      <!-- 每页选择 -->
      <select v-model.number="pageSizeLocal" @change="onPageSizeChange">
        <option v-for="size in pageSizes" :key="size" :value="size">
          {{ size }} / {{ labels.page }}
        </option>
      </select>

      <!-- 跳转到 -->
      <span>
        {{ labels.goto }}
        <input
            type="number"
            v-model.number="goToPage"
            @keyup.enter="changePage(goToPage)"
            min="1"
            :max="totalPages"
        />
        {{ labels.page }}
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch, defineProps, defineEmits } from 'vue'

const props = defineProps<{
  currentPage: number
  totalPages: number
  total: number
  pageSize: number
  pageSizes?: number[]
  maxVisible?: number
  labels?: {
    first?: string
    last?: string
    prev?: string
    next?: string
    goto?: string
    page?: string
    total?: string
  }
}>()

const emit = defineEmits<{
  (e: 'update:page', page: number): void
  (e: 'update:pageSize', size: number): void
}>()

const maxVisible = props.maxVisible ?? 5
const pageSizes = props.pageSizes ?? [10, 20, 50, 100]
const labels = {
  first: '首页',
  last: '末页',
  prev: '上一页',
  next: '下一页',
  goto: '跳转到',
  page: '页',
  total: '共 {total} 条',
  ...props.labels,
}

const pageSizeLocal = ref(props.pageSize)
const goToPage = ref(props.currentPage)

watch(() => props.pageSize, (val) => (pageSizeLocal.value = val))
watch(() => props.currentPage, (val) => (goToPage.value = val))

const changePage = (page: number) => {
  if (page < 1 || page > props.totalPages || page === props.currentPage) return
  emit('update:page', page)
}

const onPageSizeChange = () => {
  emit('update:pageSize', pageSizeLocal.value)
}

const pages = computed(() => {
  const result: number[] = []

  if (props.totalPages <= maxVisible) {
    for (let i = 1; i <= props.totalPages; i++) result.push(i)
    return result
  }

  const half = Math.floor(maxVisible / 2)
  let start = Math.max(1, props.currentPage - half)
  let end = start + maxVisible - 1

  if (end > props.totalPages) {
    end = props.totalPages
    start = end - maxVisible + 1
  }

  for (let i = start; i <= end; i++) result.push(i)
  return result
})
</script>

<style scoped>
.pagination-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  font-family: 'Arial', sans-serif;
  font-size: 14px;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  flex-wrap: wrap;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
  font-size: 14px;
}

.pagination-total {
  color: #666;
  font-size: 14px;
}

.pagination-button {
  padding: 8px 16px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
  cursor: pointer;
  background-color: #fff;
  color: #333;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
}

.pagination-button:hover:not(:disabled) {
  background-color: #e2e8f0;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.pagination-button:disabled {
  color: #bbb;
  cursor: not-allowed;
}

.pagination-button.active {
  background-color: #06b6d4;
  color: white;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.2);
}

.pagination-ellipsis {
  padding: 8px;
  font-size: 14px;
  color: #999;
}

select,
input[type='number'] {
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 14px;
  width: 70px;
}

input[type='number'] {
  max-width: 70px;
}

@media (prefers-color-scheme: dark) {
  .pagination-button {
    color: #ccc;
    background-color: #333;
    border-color: #444;
  }

  .pagination-button:hover:not(:disabled) {
    background-color: #444;
  }

  .pagination-button.active {
    background-color: #06b6d4;
    color: #fff;
  }

  .pagination-ellipsis {
    color: #666;
  }

  select,
  input[type='number'] {
    background-color: #2c2c2c;
    color: #ddd;
    border-color: #555;
  }
}
</style>
