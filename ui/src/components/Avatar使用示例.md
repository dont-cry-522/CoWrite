
### 🧾 Avatar 参数说明

| 功能         | 描述              |
| ---------- | --------------- |
| `alt`      | 用户名或缩写来源        |
| `src`      | 头像图片地址          |
| `fallback` | 自定义替代文字         |
| `ring`     | 显示边框            |
| `tooltip`  | 鼠标悬浮提示          |
| `upload`   | 启用点击上传          |
| `@upload`  | 上传事件回调（返回 File） |

```vue
<template>
  <div style="padding: 2rem; display: flex; gap: 2rem; align-items: center;">
    <!-- 默认头像 -->
    <Avatar alt="张三" tooltip="我是张三" />

    <!-- 带图片 -->
    <Avatar
        src="https://i.pravatar.cc/150?img=12"
        alt="陈实骏"
        size="lg"
        shape="rounded"
        ring
        tooltip="点击更换头像"
        upload
        @upload="onUpload"
    />

    <!-- 显示上传结果 -->
    <div v-if="uploadedName">上传了文件：{{ uploadedName }}</div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Avatar from './components/Avatar.vue'

const uploadedName = ref('')

function onUpload(file: File) {
  uploadedName.value = file.name
  // 可上传到服务器或预览
}
</script>
```




### 🧾 AvatarGroup 参数说明

| 参数名         | 类型                               | 默认值            | 说明                                         |
| ----------- | -------------------------------- | -------------- | ------------------------------------------ |
| `users`     | `Array<{ id?, name?, avatar? }>` | `[]`           | 用户数组，每个对象表示一个用户，包含头像 URL 和名称（可用于 fallback） |
| `max`       | `number`                         | `3`            | 最多显示几个头像，多余部分显示为 `+N` 数字圆形                 |
| `size`      | `"sm" \| "md" \| "lg" \| "xl"`   | `"md"`         | 头像大小，可传递到子 Avatar 中                        |
| `direction` | `"horizontal" \| "vertical"`     | `"horizontal"` | 展示方向，水平堆叠 or 垂直排列                          |
| `className` | `string`                         | `""`           | 额外传入的类名，应用于整个 AvatarGroup 容器               |



```vue
<template>
  <AvatarGroup
      :users="members"
      :max="4"
      size="md"
      direction="horizontal"
  />
</template>

<script setup lang="ts">
import AvatarGroup from './components/AvatarGroup.vue'

const members = [
  { id: 1, name: 'Alice', avatar: 'https://i.pravatar.cc/150?img=1' },
  { id: 2, name: 'Bob', avatar: 'https://i.pravatar.cc/150?img=2' },
  { id: 3, name: 'Charlie', avatar: 'https://i.pravatar.cc/150?img=3' },
  { id: 4, name: 'Daisy', avatar: 'https://i.pravatar.cc/150?img=4' },
  { id: 5, name: 'Ethan', avatar: 'https://i.pravatar.cc/150?img=5' },
]
</script>

```