<template>
  <aside class="left-sidebar" :class="{ collapsed }">
    <!-- 折叠按钮 -->
    <div class="collapse-trigger" @click="collapsed = !collapsed">
      <IconMdiChevronRight v-if="collapsed" />
      <IconMdiChevronLeft v-else />
    </div>

    <!-- 用户与组织信息 -->
    <div class="user-org" v-if="!collapsed">
      <img class="avatar" :src="user.avatar" alt="avatar" />
      <div class="user-meta">
        <div class="nickname">{{ user.name }}</div>
        <select class="org-selector" v-model="currentOrg">
          <option disabled value="">选择组织</option>
          <option value="">个人空间</option>
          <option
              v-for="org in orgOptions"
              :key="org"
              :value="org"
          >
            {{ org }}
          </option>
        </select>
      </div>
    </div>

    <!-- 功能模块 -->
    <nav class="menu" v-if="!collapsed">
      <div
          v-for="item in menuItems"
          :key="item.id"
          class="menu-item"
          @click="handleMenuClick(item)"
      >
        <i :class="['iconfont', item.icon]" />
        <span class="label">{{ item.label }}</span>
      </div>
    </nav>

    <!-- 知识库列表 -->
    <div class="repos" v-if="!collapsed">
      <h3 class="section-title">我的知识库</h3>
      <ul class="repo-list">
        <li
            v-for="repo in repositories"
            :key="repo.id"
            class="repo-item"
            @click="selectRepository(repo)"
        >
          <i class="iconfont icon-folder" />
          {{ repo.name }}
        </li>
      </ul>
    </div>
  </aside>
</template>

<script lang="ts" setup>
import { ref } from 'vue';

const collapsed = ref(false);
const currentOrg = ref<string>(''); // 空为个人空间
const orgOptions = ref(['我的组织', '团队 Alpha', '写作小组']);

const user = ref({
  name: '小明',
  avatar: 'https://i.pravatar.cc/80?img=11',
});

const menuItems = ref([
  { id: 'ai', label: 'AI 写作', icon: 'icon-robot' },
  { id: 'star', label: '收藏', icon: 'icon-star' },
  { id: 'draft', label: '草稿箱', icon: 'icon-file' },
]);

const repositories = ref([
  { id: 1, name: '语雀项目文档' },
  { id: 2, name: '前端知识库' },
  { id: 3, name: '个人随笔' },
]);

function handleMenuClick(item: any) {
  console.log('功能点击：', item);
}

function selectRepository(repo: any) {
  console.log('选择知识库：', repo);
}
</script>

<style scoped>
.left-sidebar {
  position: relative;
  width: 260px;
  background-color: #ffffff;
  border-right: 1px solid #e5e6eb;
  height: 100vh;
  padding: 1rem 1rem 0;
  transition: width 0.3s ease;
  overflow-x: hidden;
  overflow-y: auto;
}

.left-sidebar.collapsed {
  width: 16px;
  padding: 1rem 0.5rem 0;
}

.collapse-trigger {
  position: absolute;
  top: 12px;
  right: 0;
  width: 24px;
  height: 24px;
  background: #f2f3f5;
  border-radius: 50%;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  z-index: 10;
  transition: background 0.2s ease;
}

.collapse-trigger i {
  font-size: 14px;
  color: #666;
}

.user-org {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin-right: 0.5rem;
}

.user-meta {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.nickname {
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}

.org-selector {
  font-size: 13px;
  padding: 4px 6px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  color: #606266;
  background-color: #fff;
  appearance: none;
  cursor: pointer;
}
.org-selector:focus {
  outline: none;
  border-color: #409eff;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin-right: 0.5rem;
}

.nickname {
  font-size: 14px;
  color: #333;
}

/* 功能菜单 */
.menu {
  margin-bottom: 1rem;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  color: #4e5969;
  font-size: 14px;
  transition: background 0.2s ease;
}

.menu-item:hover {
  background-color: #f5f7fa;
  color: #1d2129;
}

.menu-item i {
  margin-right: 8px;
  font-size: 16px;
}

/* 知识库区域 */
.section-title {
  font-size: 13px;
  font-weight: 600;
  color: #999;
  margin: 1rem 0 0.5rem;
}

.repo-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.repo-item {
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  color: #4e5969;
  display: flex;
  align-items: center;
}

.repo-item:hover {
  background-color: #f5f7fa;
  color: #1d2129;
}

.repo-item i {
  margin-right: 8px;
}
</style>
