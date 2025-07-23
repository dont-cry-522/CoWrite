<template>
  <aside class="right-sidebar" :class="{ collapsed }">
    <!-- 折叠按钮 -->
    <div class="collapse-trigger" @click="emit('toggle')">
      <IconMdiChevronLeft v-if="collapsed" />
      <IconMdiChevronRight v-else />
    </div>

    <!-- 展开时内容区域 -->
    <div v-if="!collapsed" class="sidebar-content">
      <!-- 当前知识库 - 卡片设计 -->
      <div class="repo-card" @click="emit('switchRepo')">
        <div class="repo-label">当前知识库</div>
        <div class="repo-name">
          {{ currentRepo.name || '未选择' }}
          <IconMdiChevronDown class="repo-icon" />
        </div>
      </div>

      <ul class="sidebar-list">
        <li
            v-for="item in catalog"
            :key="item.id"
            class="sidebar-item"
            :class="{ parent: item.children }"
        >
          <!-- 一级标题项 -->
          <div
              class="sidebar-item-label"
              @click="!item.children && emit('selectItem', item)"
          >
            <IconMdiFolderOutline class="sidebar-icon" v-if="item.children" />
            <IconMdiHome class="sidebar-icon" v-else-if="item.id === 'home'" />
            <IconMdiFileDocumentOutline class="sidebar-icon" v-else />
            <span class="sidebar-text">{{ item.title }}</span>
            <span class="hover-effect"></span>
          </div>

          <!-- 子级文档项 -->
          <ul v-if="item.children" class="sidebar-sublist">
            <li
                v-for="child in item.children"
                :key="child.id"
                class="sidebar-item subitem"
                @click="emit('selectItem', child)"
            >
              <IconMdiFileDocumentOutline class="sidebar-icon" />
              <span class="sidebar-text">{{ child.title }}</span>
              <span class="hover-effect"></span>
            </li>
          </ul>
        </li>
      </ul>

      <!-- 插槽 -->
      <slot />
    </div>
  </aside>
</template>

<script lang="ts" setup>
import { defineProps, defineEmits } from 'vue'

interface CatalogItem {
  id: string
  title: string
  children?: CatalogItem[] // 支持嵌套子项
}

const props = defineProps<{
  collapsed: boolean
  currentRepo: { id: number; name: string }
  catalog: CatalogItem[]
}>()

const emit = defineEmits<{
  (e: 'toggle'): void
  (e: 'switchRepo'): void
  (e: 'selectItem', item: CatalogItem | { id: string; title: string }): void
}>()
</script>

<style scoped>
.right-sidebar {
  position: relative;
  width: 280px;
  background-color: #f8f5ff; /* 淡紫色背景 */
  padding: 1.5rem;
  transition: all 0.3s ease;
  overflow-y: auto;
  border-left: 1px solid #e8e0f5;
  box-shadow: 0 2px 12px rgba(148, 108, 230, 0.08);
}

.right-sidebar.collapsed {
  width: 24px;
  padding: 1.5rem 0.5rem;
}

/* 折叠按钮 */
.collapse-trigger {
  position: absolute;
  top: 16px;
  left: 5px;
  width: 24px;
  height: 24px;
  background: #ffffff;
  border-radius: 50%;
  box-shadow: 0 2px 8px rgba(148, 108, 230, 0.2);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  z-index: 10;
  border: 1px solid #e8e0f5;
  transition: all 0.2s ease;
}

.collapse-trigger:hover {
  background: #f0e9ff;
  transform: scale(1.05);
}

/* 当前知识库区域 */
.repo-card {
  background: white;
  border-radius: 12px;
  padding: 1rem;
  margin-bottom: 1.5rem;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 2px 8px rgba(148, 108, 230, 0.05);
}

.repo-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(148, 108, 230, 0.1);
}

.repo-label {
  font-size: 12px;
  color: #999;
  margin-bottom: 6px;
}

.repo-name {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.repo-icon {
  color: #946ce6;
  opacity: 0.7;
}

/* 列表项样式 */
.sidebar-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-item {
  position: relative;
  display: flex;
  align-items: center;
  padding: 10px 14px;
  border-radius: 8px;
  cursor: pointer;
  color: #4e5969;
  transition: all 0.2s ease;
  overflow: hidden;
  margin-bottom: 4px;
}

.hover-effect {
  position: absolute;
  top: 0;
  left: 0;
  width: 0;
  height: 100%;
  background: linear-gradient(90deg, rgba(148, 108, 230, 0.1), transparent);
  transition: width 0.3s ease;
}

.sidebar-item:hover {
  background-color: #f3eeff;
  color: #946ce6;
}

.sidebar-item:hover .hover-effect {
  width: 100%;
}

.sidebar-icon {
  font-size: 16px;
  margin-right: 10px;
  color: #946ce6;
}

.sidebar-text {
  flex: 1;
}

.sidebar-sublist {
  padding-left: 24px;
  margin-top: 4px;
}

.subitem {
  font-size: 13px;
  padding: 8px 14px;
}

.subitem .sidebar-icon {
  font-size: 14px;
}

.parent > .sidebar-item-label {
  font-weight: 500;
}
</style>