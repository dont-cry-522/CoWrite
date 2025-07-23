
### Select 参数说明

| 参数名           | 类型                                             | 说明           |
| ------------- | ---------------------------------------------- | ------------ |
| `v-model`     | `string \| number \| null`                     | 选中的值         |
| `options`     | `{ value: string \| number; label: string }[]` | 下拉选项         |
| `placeholder` | `string`                                       | 占位文本，默认“请选择” |

### 使用示例

```vue
<template>
  <div class="app">
    <h2>请选择一个国家</h2>
    <Select
        v-model="selectedCountry"
        :options="countries"
        placeholder="请选择国家"
    />

    <div class="result">
      当前选择：{{ selectedCountry || '未选择' }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Select from './components/Select.vue' // 替换为你的实际路径

const selectedCountry = ref<string | null>(null)

const countries = [
  { value: 'cn', label: '中国' },
  { value: 'us', label: '美国' },
  { value: 'jp', label: '日本' },
  { value: 'uk', label: '英国' },
  { value: 'fr', label: '法国' },
]
</script>

<style scoped>
.app {
  max-width: 400px;
  margin: 4rem auto;
  font-family: 'Segoe UI', sans-serif;
  font-size: 16px;
  color: #333;
}
h2 {
  margin-bottom: 1rem;
  text-align: center;
}
.result {
  margin-top: 1.5rem;
  text-align: center;
  font-weight: bold;
}
</style>
```