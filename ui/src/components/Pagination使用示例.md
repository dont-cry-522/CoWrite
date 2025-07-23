### 🔧 Props（组件属性）

| Prop 名称       | 类型         | 默认值                 | 说明            |
| ------------- | ---------- | ------------------- | ------------- |
| `currentPage` | `number`   | 必传                  | 当前页码          |
| `totalPages`  | `number`   | 必传                  | 总页数（非总条数）     |
| `total`       | `number`   | 必传                  | 数据总条数（如 200）  |
| `pageSize`    | `number`   | 必传                  | 当前每页条数        |
| `pageSizes`   | `number[]` | `[10, 20, 50, 100]` | 每页条数选项        |
| `maxVisible`  | `number`   | `5`                 | 最多显示多少个页码按钮   |
| `labels`      | `object`   | 可选                  | 用于多语言或自定义按钮文字 |

### 🔤 labels 可配置项

| 键名      | 默认值             | 含义              |
| ------- | --------------- | --------------- |
| `first` | `'首页'`          | 首页按钮文字          |
| `last`  | `'末页'`          | 末页按钮文字          |
| `prev`  | `'上一页'`         | 上一页按钮文字         |
| `next`  | `'下一页'`         | 下一页按钮文字         |
| `goto`  | `'跳转到'`         | 跳转区域文字          |
| `page`  | `'页'`           | 跳转后缀（如：页）       |
| `total` | `'共 {total} 条'` | 总条数显示文本（支持模板替换） |

### 🔁 Emits（事件）

| 事件名                | 类型               | 说明        |
| ------------------ | ---------------- | --------- |
| `@update:page`     | `(page: number)` | 当前页变化时触发  |
| `@update:pageSize` | `(size: number)` | 每页条数变化时触发 |


### 使用示例

```vue
<script setup lang="ts">
import Pagination from './components/Pagination.vue'
import { ref, computed } from 'vue'

const total = 231
const pageSize = ref(20)
const currentPage = ref(1)

const totalPages = computed(() => Math.ceil(total / pageSize.value))

const updatePage = (page: number) => {
  currentPage.value = page
}

const updatePageSize = (size: number) => {
  pageSize.value = size
  currentPage.value = 1 // 重置到第一页
}
</script>

<template>
  <Pagination
      :currentPage="currentPage"
      :totalPages="totalPages"
      :total="total"
      :pageSize="pageSize"
      :pageSizes="[10, 20, 50, 100]"
      :maxVisible="7"
      :labels="{
      first: 'First',
      last: 'Last',
      prev: 'Prev',
      next: 'Next',
      goto: 'Go to',
      page: 'page',
      total: 'Total: {total} items'
    }"
      @update:page="updatePage"
      @update:pageSize="updatePageSize"
  />
</template>
```