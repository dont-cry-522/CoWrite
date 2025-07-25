<template>
  <aside :class="['sider', { collapsed }]" :style="{ width: collapsed ? `${collapsedWidth}px` : `${width}px` }">

    <!-- Header with Collapse Toggle -->
    <div class="sider-header">
      <transition name="fade">
        <img v-if="!collapsed" src="../assets/electron-vite.png" alt="LOGO" width="35px" height="35px" style="border-radius: 8px;" />
      </transition>
      <button class="toggle-btn" @click="collapsed = !collapsed">
        <component :is="collapsed ? ChevronRightIcon : ChevronLeftIcon" class="w-4 h-4" />
      </button>
    </div>

    <!-- User Info Section -->
    <div class="user-info">
      <Avatar :src="userAvatar" :alt="userName" size="md" ring tooltip="点击更换头像" @upload="onUpload" />
      <transition name="fade">
        <div v-if="!collapsed" class="user-details">
          <div class="user-name">{{ userName }}</div>
          <div class="user-bio">{{ userBio }}</div>
        </div>
      </transition>
    </div>

    <!-- Menu Items -->
    <nav class="menu">
      <template v-for="item in menuItems" :key="item.path">
        <button
            class="menu-item"
            :class="{ active: isActive(item.path) }"
            @click="navigate(item.path)"
        >
          <component :is="item.icon" class="icon" />
          <transition name="slide-fade">
            <span v-if="!collapsed" class="label">{{ item.label }}</span>
          </transition>
          <span v-if="collapsed" class="tooltip">{{ item.label }}</span>
        </button>
        <!-- Submenu Items -->
        <div v-if="item.children && !collapsed" class="submenu">
          <button
              v-for="child in item.children"
              :key="child.path"
              class="submenu-item"
              :class="{ active: isActive(child.path) }"
              @click="navigate(child.path)"
          >
            <component :is="child.icon" class="icon" />
            <span class="label">{{ child.label }}</span>
          </button>
        </div>
      </template>
    </nav>
  </aside>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  HomeIcon,
  UsersIcon,
  SettingsIcon,
  ChevronLeftIcon,
  ChevronRightIcon,
} from 'lucide-vue-next'
import Avatar from './Avatar.vue'

// Props for customization
const props = defineProps({
  width: {
    type: Number,
    default: 240,
  },
  collapsedWidth: {
    type: Number,
    default: 64,
  },
  projectName: {
    type: String,
    default: '我的应用',
  },
  userAvatar: {
    type: String,
    default: 'https://i.pravatar.cc/150?img=12',
  },
  userName: {
    type: String,
    default: '张三',
  },
  userBio: {
    type: String,
    default: '这是我的个人简介',
  },
  menuItems: {
    type: Array,
    default: () => [
      { label: '仪表盘', icon: HomeIcon, path: '/dashboard' },
      { label: '用户管理', icon: UsersIcon, path: '/users', children: [
        { label: '用户列表', icon: UsersIcon, path: '/users/list' },
        { label: '用户权限', icon: UsersIcon, path: '/users/permissions' },
      ] },
      { label: '系统设置', icon: SettingsIcon, path: '/settings' },
    ],
  },
})

const router = useRouter()
const route = useRoute()
const collapsed = ref(false)

function isActive(path: string) {
  return route.path.startsWith(path) || route.name === path
}

function navigate(path: string) {
  router.push(path)
}

function onUpload(file: File) {
  console.log('上传了文件：', file.name)
  // 可上传到服务器或预览
}
</script>

<style scoped>
.sider {
  height: 100vh;
  background: white;
  border-right: 1px solid #e5e7eb;
  transition: width 0.7s ease;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.user-info {
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  border-bottom: 1px solid #e5e7eb;
}

.user-details {
  flex: 1;
  overflow: hidden;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #1e293b;
}

.user-bio {
  font-size: 12px;
  color: #64748b;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sider-header {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  border-bottom: 1px solid #e5e7eb;
}

.logo {
  font-size: 1.125rem;
  font-weight: 600;
  color: #06b6d4;
  transform-origin: left center; /* 动画从左侧开始 */
  transition: transform 0.3s ease;
}

.logo.collapsed {
  transform: scaleX(0); /* 文字收缩效果 */
}


.toggle-btn {
  background: transparent;
  border: none;
  cursor: pointer;
  color: #6b7280;
  padding: 4px;
  display: flex;
  align-items: center;
}

.menu {
  flex: 1;
  padding: 12px 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.menu-item {
  position: relative;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  font-size: 14px;
  color: #4b5563;
  background: transparent;
  border: none;
  cursor: pointer;
  transition: background 0.2s;
}

.menu-item:hover {
  background: #f0f9ff;
}

.menu-item.active {
  background: #e0f7fa;
  color: #0ea5e9;
  font-weight: 500;
}

.submenu {
  padding-left: 32px;
}

.submenu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  font-size: 14px;
  color: #4b5563;
  background: transparent;
  border: none;
  cursor: pointer;
  transition: background 0.2s;
}

.submenu-item:hover {
  background: #f0f9ff;
}

.submenu-item.active {
  background: #e0f7fa;
  color: #0ea5e9;
  font-weight: 500;
}

.icon {
  width: 18px;
  height: 18px;
}

.label {
  white-space: nowrap;
}

.tooltip {
  position: absolute;
  left: 100%;
  top: 50%;
  transform: translateY(-50%);
  background: #333;
  color: #fff;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  white-space: nowrap;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s;
}

.menu-item:hover .tooltip {
  opacity: 1;
}

/* Transitions */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateX(-10px);
}

.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s ease;
}
.slide-fade-enter-from,
.slide-fade-leave-to {
  opacity: 0;
  transform: translateX(-10px);
}
</style>
