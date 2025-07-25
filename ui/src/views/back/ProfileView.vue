<script setup lang="ts">
import { reactive,ref } from 'vue'
import Avatar from '../../components/Avatar.vue'
import Textarea from '../../components/Textarea.vue'
import api from '../../api/index'
import { useAuth } from '../../composables/useAuth'
const { getUserInfo, setUserInfo } = useAuth()
const userInfo = getUserInfo

// 使用 reactive 来管理 user 数据
const user = reactive({
  username: userInfo.value?.username || '',
  email: userInfo.value?.email || '',
  avatar_url: userInfo.value?.avatarUrl || 'https://via.placeholder.com/150',
  bio: userInfo.value?.bio || '这是我的个人简介',
  theme_dark: userInfo.value?.themeDark ? 1 : 0, // 转换为 1（深色） 或 0（浅色）
  email_notifications: 1, // 默认开启邮件通知
  language: userInfo.value?.language || 'zh-CN',
})

// 编辑状态
const editing = ref(false)

// 切换编辑状态
function toggleEdit() {
  editing.value = !editing.value
}

// 更新用户信息（假设为保存的操作）
const saveChanges = async () => {
  try {
    // 调用 updateUserInfo 接口，传递 user 数据
    const response = await api.userApi.updateUserInfo(user);
    console.log('用户信息更新成功', response);

    const updatedUserInfo = { ...userInfo.value, username: user.username, bio: user.bio, language: user.language, themeDark: user.theme_dark }; // 更新 avatarUrl
    setUserInfo(updatedUserInfo);  // 更新 useAuth 中的 userInfo

    // 关闭编辑模式
    editing.value = false;
  } catch (error) {
    console.error('用户信息更新失败', error);
  }
};

// 头像上传处理
const handleAvatarUpload = async (file: File) => {
  console.log(file); // 打印文件信息，检查文件是否正确传递
  try {
    const response = await api.userApi.uploadAvatar(file);
    console.log('头像上传成功', response);
    user.avatar_url = response.data;  // 假设返回的新头像 URL
    // 同步更新 useAuth 中的 userInfo
    const updatedUserInfo = { ...userInfo.value, avatarUrl: user.avatar_url }; // 更新 avatarUrl
    setUserInfo(updatedUserInfo);  // 更新 useAuth 中的 userInfo

  } catch (error) {
    console.error('头像上传失败', error);
  }
};
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
        <input disabled v-if="editing" v-model="user.email" id="email" type="email" placeholder="请输入邮箱" />
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
