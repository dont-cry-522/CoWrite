<template>
  <aside class="right-sidebar" :class="{ collapsed }">
    <!-- 折叠按钮 -->
    <div class="collapse-trigger" @click="emit('toggle')">
      <IconMdiChevronLeft v-if="collapsed" />
      <IconMdiChevronRight v-else />
    </div>

    <!-- 展开时内容区域 -->
    <div v-if="!collapsed" class="sidebar-content">
      <!-- 当前知识库 -->
      <div class="repo-section" @click="emit('switchRepo')">
        <div class="repo-label">当前知识库</div>
        <div class="repo-name">
          {{ currentRepo.name || '未选择' }}
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
  width: 260px;
  background-color: #fff;
  padding: 1rem;
  transition: width 0.3s ease;
  overflow-y: auto;
  border-left: 1px solid #e5e6eb;
}

.right-sidebar.collapsed {
  width: 24px;
  padding: 1rem 0.5rem;
}

/* 折叠按钮 */
.collapse-trigger {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 24px;
  height: 24px;
  background: #f2f3f5;
  border-radius: 50%;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

/* 当前知识库区域 */
.repo-section {
  margin-bottom: 1.5rem;
  cursor: pointer;
}

.repo-label {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.repo-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.repo-name:hover {
  color: #42b983;
}

/* 列表项样式 */
.sidebar-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  color: #4e5969;
  transition: background 0.2s ease;
}

.sidebar-item:hover {
  background-color: #f5f7fa;
  color: #1d2129;
}

.sidebar-icon {
  font-size: 16px;
  margin-right: 8px;
}

.right-sidebar.collapsed .sidebar-text {
  display: none;
}

.right-sidebar.collapsed .sidebar-icon {
  margin: 0 auto;
}

.sidebar-sublist {
  padding-left: 16px;
  margin-top: 4px;
}

.subitem {
  font-size: 13px;
  padding: 6px 12px;
}

</style>
