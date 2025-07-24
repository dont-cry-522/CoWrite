<script setup lang="ts">
import { ref } from 'vue';
import SiderMenu from '../components/SiderMenu.vue';
import Header from '../components/Header.vue'; // 头部组件
import {
  HomeIcon,
  SettingsIcon,
  UsersIcon,
  BarChartIcon,
  BotIcon,
  SlidersHorizontalIcon,
  UserCircleIcon,
  BellIcon,
  FileTextIcon,
  ShieldIcon,
} from 'lucide-vue-next';
import { useAuth } from '../composables/useAuth'

const { getUserInfo } = useAuth()
const userInfo = getUserInfo
const collapsed = ref(false);

const menuItems = [
  { label: '首页', icon: HomeIcon, path: '/back/' },
  { label: 'AI 插件', icon: BotIcon, path: '/back/ai-plugins' },
  { label: '个人中心', icon: UserCircleIcon, path: '/back/profile' },
  { label: '我的消息', icon: BellIcon, path: '/back/notifications' },
  { label: '个人日志', icon: FileTextIcon, path: '/back/logs' },
  { label: '账号管理', icon: ShieldIcon, path: '/back/account' },
  { label: '设置', icon: SettingsIcon, path: '/back/settings' },
  { label: '用户', icon: UsersIcon, path: '/back/users' },
  { label: '数据统计', icon: BarChartIcon, path: '/back/stats' },
];

</script>

<template>
  <div id="back-layout">
    <!-- 侧边栏和内容区域 -->
    <div class="layout-container">
      <!-- 侧边栏 -->
      <SiderMenu
          logoText="我的应用"
          :menuItems="menuItems"
          v-model:collapsed="collapsed"
          :userName="userInfo?.username"
          :userAvatar="userInfo?.avatarUrl"
          :userBio="userInfo?.bio"
      />

      <!-- 内容区域 -->
      <div class="main-content">
        <!-- 头部 -->
        <Header />

        <!-- 页面具体内容 -->
        <div class="content-area">
          <router-view /> <!-- 动态显示页面内容 -->
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
#back-layout {
  display: flex;
  height: 100%;
}

.layout-container {
  display: flex;
  width: 100%;
  height: 100%;
}

.main-content {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  transition: padding-left 0.3s ease;
}

.main-content .content-area {
  flex: 1;
  padding: 20px;
  background-color: #f8fafc;
  overflow-y: auto;
}

/* 当侧边栏折叠时调整内容区的样式 */
.main-content .content-area {
  padding-left: 24px; /* 调整内容区域的左侧间距 */
}

#back-layout .layout-container {
  display: flex;
  height: 100%;
}
</style>
