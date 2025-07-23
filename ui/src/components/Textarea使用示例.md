### 多行输入框示例

```vue
<template>
  <div class="demo">
    <Textarea
        v-model="content"
        :max-length="200"
        :show-count="true"
        placeholder="请输入内容..."
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Textarea from './components/Textarea.vue' // 替换成你自己的路径

const content = ref('')
</script>
```