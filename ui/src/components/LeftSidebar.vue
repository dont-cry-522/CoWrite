<template>
  <aside class="left-sidebar" :class="{ collapsed }">
    <!-- Collapse trigger -->
    <div class="collapse-trigger" @click="toggleSidebar">
      <IconMdiChevronRight v-if="collapsed" />
      <IconMdiChevronLeft v-else />
    </div>

    <!-- User card -->
    <div
        class="user-panel-wrapper"
        @mouseenter="showUserPanel = true"
        @mouseleave="showUserPanel = false"
    >
      <div class="user-org-card">
        <div class="user-org">
          <img class="avatar" :src="userInfo?.avatarUrl" alt="avatar" />
          <div class="user-meta" v-if="!collapsed">
            <div class="nickname">👤 {{ userInfo?.username }}</div>
          </div>
        </div>
      </div>

      <transition name="fade">
        <div class="user-panel" v-show="showUserPanel">
          <div class="panel-header">
            <h3>{{ userInfo?.username }}</h3>
            <p class="user-type">普通用户，购买可享更多权益</p>
            <button class="upgrade-btn">购买</button>
          </div>
          <div class="panel-divider"></div>
          <ul class="panel-menu">
            <li class="panel-item" @click="goToCreationCenter">创作中心</li>
            <li class="panel-item" @click="goToSettings">设置</li>
            <li class="panel-item" @click="logout">退出登录</li>
          </ul>
        </div>
      </transition>
    </div>

    <!-- Search box -->
    <div class="search-container" v-if="!collapsed">
      <div class="search-wrapper">
        <i class="iconfont icon-search"></i>
        <input
            type="text"
            placeholder="搜索 Ctrl+J"
            class="search-input"
        />
      </div>
      <button class="add-btn">
        <div class="plus-icon">+</div>
      </button>
    </div>

    <!-- 功能菜单项 -->
    <nav class="menu" v-if="!collapsed">
      <div
          class="menu-item"
          @click="handleMenuClick(startCreateItem)"
          :class="{ active: selectedMenuItem === startCreateItem.id }"
      >
        <i class="iconfont icon-create" />
        <span class="label">✏️ {{ startCreateItem.label }}</span>
        <span class="hover-effect"></span>
      </div>

      <div
          v-for="item in menuItems"
          :key="item.id"
          class="menu-item"
          @click="handleMenuClick(item)"
          :class="{ active: selectedMenuItem === item.id }"
      >
        <i :class="['iconfont', item.icon]" />
        <span class="label">{{ item.emoji }} {{ item.label }}</span>
        <span class="hover-effect"></span>
      </div>
    </nav>

    <!-- 知识库区域 -->
    <div class="repos" v-if="!collapsed">
      <h3 class="section-title">📚 我的知识库</h3>
      <ul class="repo-list">
        <li
            v-for="repo in repositories"
            :key="repo.id"
            class="repo-item"
            @click="selectRepository(repo)"
        >
          <i class="iconfont icon-folder" />
          <span class="repo-name">📁 {{ repo.name }}</span>
          <span class="hover-effect"></span>
        </li>
      </ul>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../composables/useAuth'

// Emits
const emit = defineEmits(['menuClick', 'repoClick'])

const collapsed = ref(false)
const showUserPanel = ref(false)
const selectedMenuItem = ref<string>('startCreate')

const { getUserInfo, clearAuth } = useAuth()
const userInfo = getUserInfo
const router = useRouter()

// 菜单功能项
const menuItems = ref([
  { id: 'ai', label: 'AI 写作', icon: 'icon-robot', emoji: '🤖' },
  { id: 'star', label: '收藏', icon: 'icon-star', emoji: '⭐' },
  { id: 'draft', label: '草稿箱', icon: 'icon-file', emoji: '📝' }
])

const startCreateItem = {
  id: 'startCreate',
  label: '开始创作',
  icon: 'icon-create'
}

// 仓库数据
const repositories = ref([
  { id: 1, name: '语雀项目文档' },
  { id: 2, name: '前端知识库' },
  { id: 3, name: '个人随笔' }
])

function handleMenuClick(item: any) {
  selectedMenuItem.value = item.id
  emit('menuClick', item)
}

function selectRepository(repo: any) {
  emit('repoClick', repo)
}

function toggleSidebar() {
  collapsed.value = !collapsed.value
  if (collapsed.value) showUserPanel.value = false
}

function goToCreationCenter() {
  router.push('/back/stats')
}

function goToSettings() {
  router.push('/back/settings')
}

async function logout() {
  try {
    clearAuth()
    router.push('/login')
  } catch (error) {
    console.error('退出登录失败:', error)
    alert('退出登录失败，请稍后再试')
  }
}
</script>

<style scoped>
.left-sidebar {
  position: relative;
  width: 280px;
  background-color: #f8f5ff;
  border-right: 1px solid #e8e0f5;
  height: 100vh;
  padding: 1.5rem 1rem 0;
  transition: all 0.3s ease;
  overflow: visible;
  box-shadow: 0 2px 12px rgba(148, 108, 230, 0.08);
}

.left-sidebar.collapsed {
  width: 45px;
  padding: 1.5rem 0.5rem 0;
}

/* 折叠按钮 - 核心修改 */
.collapse-trigger {
  position: absolute;
  top: 2px;
  right: 5px;
  width: 30px;
  height: 30px;
  background: #ffffff;
  border-radius: 50%;
  box-shadow: 0 2px 8px rgba(148, 108, 230, 0.2);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  z-index: 1001; /* 修改为更高的值 */
  transition: all 0.2s ease;
  border: 1px solid #e8e0f5;
}

.collapse-trigger:hover {
  background: #f0e9ff;
  transform: scale(1.05);
}

/* User card styles */
.user-org-wrapper {
  position: relative;
  margin-bottom: 1.5rem;
  z-index: 1000; /* 保持低于折叠按钮 */
}

/* 用户卡牌 - 辅助调整（避免遮挡按钮） */
.user-org-card {
  border-radius: 12px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 10; /* 低于折叠按钮的 z-index */
  position: relative; /* 确保 z-index 生效 */
}

.user-org-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(148, 108, 230, 0.15);
}

.user-org {
  display: flex;
  align-items: center;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 0.75rem;
  object-fit: cover;
  border: 2px solid #f0e9ff;
  box-shadow: 0 2px 4px rgba(148, 108, 230, 0.1);
}

.user-meta {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.nickname {
  font-size: 15px;
  font-weight: 600;
  color: #5e4dcd;
  margin-bottom: 6px;
}

.org-selector {
  font-size: 13px;
  padding: 6px 8px;
  border: 1px solid #e8e0f5;
  border-radius: 8px;
  color: #606266;
  background-color: #fff;
  background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e");
  background-repeat: no-repeat;
  background-position: right 8px center;
  background-size: 14px;
  appearance: none;
  cursor: pointer;
  transition: all 0.2s ease;
  width: 100%;
  padding-right: 24px;
}

.org-selector:focus {
  outline: none;
  border-color: #8a6de8;
  box-shadow: 0 0 0 2px rgba(138, 109, 232, 0.2);
}

/* User panel popup */
.user-panel {
  position: absolute;
  top: 0;
  left: calc(100% + 10px);
  width: 220px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  overflow: hidden;
  border: 1px solid #e8e0f5;
}

.panel-header {
  padding: 16px;
  background: linear-gradient(135deg, #f9f5ff 0%, #f3eeff 100%);
}

.panel-header h3 {
  margin: 0 0 4px;
  font-size: 16px;
  color: #2d3748;
}

.user-type {
  margin: 0 0 12px;
  font-size: 12px;
  color: #718096;
}

.upgrade-btn {
  width: 100%;
  padding: 8px;
  background: #5e4dcd;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.upgrade-btn:hover {
  background: #4c3cad;
}

.panel-divider {
  height: 1px;
  background: #e8e0f5;
  margin: 8px 0;
}

.panel-menu {
  list-style: none;
  padding: 8px 0;
  margin: 0;
}

.panel-item {
  padding: 10px 16px;
  font-size: 14px;
  color: #4a5568;
  cursor: pointer;
  transition: all 0.2s;
}

.panel-item:hover {
  background: #f3eeff;
  color: #5e4dcd;
}

/* Search box styles */
.search-container {
  display: flex;
  align-items: center;
  margin-bottom: 1.5rem;
  gap: 8px;
}

.search-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  background: white;
  border-radius: 6px;
  padding: 0 12px;
  border: 1px solid #e0e0e0;
  height: 36px;
  transition: all 0.2s ease;
}

.search-wrapper:focus-within {
  border-color: #8a6de8;
  box-shadow: 0 0 0 2px rgba(138, 109, 232, 0.2);
}

.icon-search {
  color: #a0a4ab;
  font-size: 16px;
  margin-right: 8px;
}

.search-input {
  flex: 1;
  padding: 8px 0;
  border: none;
  outline: none;
  font-size: 14px;
  color: #2d3748;
  height: 100%;
  background: transparent;
}

.search-input::placeholder {
  color: #a0a4ab;
  opacity: 1;
}

.add-btn {
  width: 36px;
  height: 36px;
  background: #5e4dcd;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
}

.add-btn:hover {
  background: #4c3cad;
}

.plus-icon {
  color: white;
  font-size: 20px;
  font-weight: 500;
  line-height: 1;
}

/* Menu styles */
.menu {
  margin-bottom: 1.5rem;
}

/* 功能菜单选中状态（与hover效果统一风格） */
.menu-item.active {
  background-color: #f3eeff;
  color: #5e4dcd;
}

/* 选中时激活hover动画效果（复用原有过渡） */
.menu-item.active .hover-effect {
  width: 100%;
}

.menu-item {
  position: relative;
  display: flex;
  align-items: center;
  padding: 10px 14px;
  border-radius: 8px;
  cursor: pointer;
  color: #4e5969;
  font-size: 14px;
  transition: all 0.2s ease;
  overflow: hidden;
  margin-bottom: 4px;
}

.menu-item i {
  margin-right: 10px;
  font-size: 16px;
  color: #8a6de8;
}

.menu-item .label {
  display: flex;
  align-items: center;
  gap: 6px;
}

.hover-effect {
  position: absolute;
  top: 0;
  left: 0;
  width: 0;
  height: 100%;
  background: linear-gradient(90deg, rgba(138, 109, 232, 0.1), transparent);
  transition: width 0.3s ease;
}

.menu-item:hover {
  background-color: #f3eeff;
  color: #5e4dcd;
}

.menu-item:hover .hover-effect {
  width: 100%;
}

/* Repositories styles */
.section-title {
  font-size: 13px;
  font-weight: 600;
  color: #8a6de8;
  margin: 1rem 0 0.75rem;
  padding-left: 8px;
  letter-spacing: 0.5px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.repo-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.repo-item {
  position: relative;
  padding: 10px 14px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  color: #4e5969;
  display: flex;
  align-items: center;
  overflow: hidden;
  margin-bottom: 4px;
}

.repo-item i {
  margin-right: 10px;
  color: #8a6de8;
}

.repo-item:hover {
  background-color: #f3eeff;
  color: #5e4dcd;
}

.repo-item:hover .hover-effect {
  width: 100%;
}

.repo-name {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: flex;
  align-items: center;
  gap: 6px;
}

.user-panel-wrapper {
  position: relative;
  display: inline-block;
  margin-top: 16px;
}
</style>