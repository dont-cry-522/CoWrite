```vue
<script setup>
import {getCurrentInstance} from "vue";
const notify = getCurrentInstance()?.appContext.config.globalProperties.$notify

function notifySuccess() {
  notify.success('操作成功！')
}
function notifyError() {
  notify.error('操作失败！请重试')
}
</script>

<template>
  <button @click="notifySuccess">成功</button>
  <button @click="notifyError">错误</button>
</template>
```