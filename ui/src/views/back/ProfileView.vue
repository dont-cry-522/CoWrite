<script setup lang="ts">
import { ref } from 'vue'
import Avatar from '../../components/Avatar.vue'
import Textarea from '../../components/Textarea.vue'

// 假设我们从后端获取到的用户数据
const user = ref({
  username: '张三',
  email: 'zhangsan@example.com',
  avatar_url: 'https://via.placeholder.com/150',
  bio: '这是我的个人简介',
  theme_dark: 0, // 主题：0 浅色，1 深色
  email_notifications: 1, // 邮件通知：0 关闭，1 开启
  language: 'ZH', // 语言：EN / ZH
})

// 编辑状态
const editing = ref(false)

// 切换编辑状态
function toggleEdit() {
  editing.value = !editing.value
}

// 更新用户信息（假设为保存的操作）
function saveChanges() {
  // 这里模拟保存操作，实际应用中可以调用 API 保存修改的数据
  console.log('保存修改', user.value)
  editing.value = false
}

// 头像上传处理
function handleAvatarUpload(file: File) {
  // 处理头像上传
  console.log('上传头像', file)
  // 更新头像URL
  user.value.avatar_url = URL.createObjectURL(file)
}
</script>

<template>
  <div class="profile-page">
    <!-- 用户个人信息展示 -->
    <div class="profile-header">
      <div class="profile-avatar">
        <Avatar
            size="vlg"
            :src="user.avatar_url"
            :alt="user.username"
            :upload="true"
            :tooltip="'点击上传头像'"
            @upload="handleAvatarUpload"
        />
      </div>
      <div class="profile-info">
        <h1 class="title">{{ editing ? '编辑个人资料' : '个人资料' }}</h1>
        <p class="subtitle">在这里查看和编辑您的个人信息</p>
      </div>
    </div>

    <!-- 个人信息表单 -->
    <div class="profile-form">
      <!-- 用户名 -->
      <div class="form-group card">
        <label for="username">用户名</label>
        <input
            v-if="editing"
            v-model="user.username"
            id="username"
            type="text"
            placeholder="请输入用户名"
        />
        <p v-else>{{ user.username }}</p>
      </div>

      <!-- 邮箱 -->
      <div class="form-group card">
        <label for="email">邮箱</label>
        <input v-if="editing" v-model="user.email" id="email" type="email" placeholder="请输入邮箱" />
        <p v-else>{{ user.email }}</p>
      </div>

      <!-- 个人简介 -->
      <div class="form-group card">
        <label for="bio">个人简介</label>
        <Textarea
            v-if="editing"
            v-model="user.bio"
            :maxlength="200"
            :showCount="true"
            placeholder="请输入个人简介"
        />
        <p v-else>{{ user.bio }}</p>
      </div>

      <!-- 主题 -->
      <div class="form-group card">
        <label>主题模式</label>
        <select v-if="editing" v-model="user.theme_dark">
          <option :value="0">浅色模式</option>
          <option :value="1">深色模式</option>
        </select>
        <p v-else>{{ user.theme_dark === 0 ? '浅色模式' : '深色模式' }}</p>
      </div>

      <!-- 邮件通知设置 -->
      <div class="form-group card">
        <label>邮件通知</label>
        <select v-if="editing" v-model="user.email_notifications">
          <option :value="1">开启</option>
          <option :value="0">关闭</option>
        </select>
        <p v-else>{{ user.email_notifications === 1 ? '开启' : '关闭' }}</p>
      </div>

      <!-- 语言 -->
      <div class="form-group card">
        <label>语言</label>
        <select v-if="editing" v-model="user.language">
          <option :value="'EN'">英语</option>
          <option :value="'ZH'">中文</option>
        </select>
        <p v-else>{{ user.language === 'EN' ? '英语' : '中文' }}</p>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <button v-if="editing" @click="saveChanges">保存</button>
      <button @click="toggleEdit">{{ editing ? '取消' : '编辑' }}</button>
    </div>
  </div>
</template>

<style scoped>
.profile-page {
  padding: 32px;
  background-color: #f9fafb;
  min-height: 100%;
}

/* Profile Header */
.profile-header {
  display: flex;
  gap: 20px;
  align-items: center;
  margin-bottom: 32px;
}

.profile-avatar {
  position: relative;
  width: 85px;
  height: 85px;
  border-radius: 50%;
}

.profile-info {
  flex-grow: 1;
}

.title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
  color: #1e293b;
}

.subtitle {
  font-size: 16px;
  color: #64748b;
  margin-bottom: 24px;
}

.profile-form {
  margin-bottom: 20px;
}

/* Card style for each form field */
.card {
  background: #ffffff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  font-weight: bold;
  display: block;
  margin-bottom: 8px;
}

input,
textarea,
select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

textarea {
  min-height: 100px;
}

.action-buttons {
  display: flex;
  gap: 20px;
}

button {
  padding: 10px 20px;
  border: none;
  background-color: #3b82f6;
  color: white;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #2563eb;
}
</style>
