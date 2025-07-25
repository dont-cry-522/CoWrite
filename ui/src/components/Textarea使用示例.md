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


### Input 使用示例

```vue
<template>
  <div>
    <Input
      v-model="inputValue"
      label="用户名"
      placeholder="请输入用户名"
      :errorMessage="error"
      :icon="'eye'"
      :maxlength="20"
    />
    <Input
      v-model="password"
      label="密码"
      type="password"
      placeholder="请输入密码"
      :errorMessage="passwordError"
      :icon="'eye'"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Input from './components/Input.vue';

const inputValue = ref('');
const password = ref('');
const error = ref('用户名不能为空');
const passwordError = ref('');
</script>
```