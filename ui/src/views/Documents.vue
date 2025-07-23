<template>
  <div class="yuque-documents">
    <!-- 顶部操作区 -->
    <div class="action-section">
      <div class="action-group">
        <button class="action-btn" @click="createDocument">
          <i class="iconfont icon-file-add"></i>
          <div class="btn-content">
            <span class="btn-title">📄 新建文档</span>
            <span class="btn-desc">文档、表格、画板、数据表</span>
          </div>
        </button>
        <button class="action-btn" @click="createRepo">
          <i class="iconfont icon-repo-add"></i>
          <div class="btn-content">
            <span class="btn-title">📚 新建知识库</span>
            <span class="btn-desc">使用知识库整理知识</span>
          </div>
        </button>
      </div>
      <div class="action-group">
        <button class="action-btn" @click="openTemplates">
          <i class="iconfont icon-template"></i>
          <div class="btn-content">
            <span class="btn-title">🎨 模板中心</span>
            <span class="btn-desc">从模板中获取灵感</span>
          </div>
        </button>
        <button class="action-btn" @click="useAI">
          <i class="iconfont icon-ai"></i>
          <div class="btn-content">
            <span class="btn-title">🤖 AI帮你写</span>
            <span class="btn-desc">AI助手帮你一键生成文档</span>
          </div>
        </button>
      </div>
    </div>

    <!-- 文档分类区和筛选区（核心修复区域） -->
    <div class="combined-section">
      <div class="doc-section">
        <h2 class="section-title">文档</h2>
        <div class="tag-group">
          <button
              v-for="category in docCategories"
              :key="category.id"
              class="doc-tag"
              :class="{ active: activeCategory === category.id }"
              @click="filterByCategory(category.id)"
          >
            {{ category.label }}
          </button>
        </div>
      </div>
      <div class="filter-section">
        <div class="filter-dropdown" v-for="filter in docFilters" :key="filter.id">
          <button
              class="filter-btn"
              :class="{ active: activeFilter === filter.id }"
              @click="toggleDropdown(filter.id)"
          >
            {{ filter.label }}
            <i class="iconfont icon-arrow-down"></i>
          </button>
          <div class="dropdown-menu" v-if="activeDropdown === filter.id">
            <div
                v-for="item in filterOptions[filter.id]"
                :key="item.value"
                class="dropdown-item"
                @click="selectFilterItem(filter.id, item.value)"
            >
              {{ item.label }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 特色功能区 -->
    <div class="feature-section combined">
      <button class="feature-btn" @click="openFeature">
        <i class="iconfont icon-magic"></i>
        <span>🎙️ 试试新建~ </span>
      </button>
      <div class="user-info-combined">
        <img class="user-avatar" :src="userInfo?.avatarUrl" />
        <span>{{ userInfo?.username }} / 默认知识库</span>
        <span class="date-info">{{ new Date().toISOString().split('T')[0] }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useAuth } from '../composables/useAuth'

// -----------------------
// 认证状态（用户信息）
// -----------------------
const { getUserInfo } = useAuth()
const userInfo = getUserInfo

// -----------------------
// 文档分类和筛选
// -----------------------
const activeCategory = ref('edited')
const activeFilter = ref('type')
const activeDropdown = ref<string | null>(null)

const docCategories = [
  { id: 'edited', label: '编辑过' },
  { id: 'viewed', label: '浏览过' },
  { id: 'liked', label: '我点赞的' },
  { id: 'commented', label: '我评论过' }
]

const docFilters = [
  { id: 'type', label: '类型' },
  { id: 'owner', label: '归属' },
  { id: 'creator', label: '创建者' }
]

const filterOptions = {
  type: [
    { value: 'all', label: '✓ 所有' },
    { value: 'doc', label: '文档' },
    { value: 'sheet', label: '表格' },
    { value: 'board', label: '画板' },
    { value: 'table', label: '数据表' }
  ],
  owner: [
    { value: 'me', label: '我的' },
    { value: 'team', label: '团队的' },
    { value: 'shared', label: '共享的' }
  ],
  creator: [
    { value: 'me', label: '我创建的' },
    { value: 'others', label: '他人创建的' }
  ]
}

// -----------------------
// 交互方法
// -----------------------
const toggleDropdown = (filterId: string) => {
  activeDropdown.value = activeDropdown.value === filterId ? null : filterId
}

const selectFilterItem = (filterId: string, value: string) => {
  activeFilter.value = filterId
  console.log(`Selected ${filterId}: ${value}`)
  activeDropdown.value = null
}

const createDocument = () => {
  console.log('创建文档')
}

const createRepo = () => {
  console.log('创建知识库')
}

const openTemplates = () => {
  console.log('打开模板中心')
}

const useAI = () => {
  console.log('使用AI生成文档')
}

const filterByCategory = (category: string) => {
  activeCategory.value = category
  console.log('按分类筛选:', category)
}

const openFeature = () => {
  console.log('打开特色功能')
}
</script>


<style scoped>
.yuque-documents {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  color: #1a1a1a;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
}

/* 操作区样式（保持不变） */
.action-section {
  display: flex;
  gap: 20px;
  margin-bottom: 32px;
}
.action-group {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.action-btn {
  display: flex;
  align-items: center;
  padding: 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: none;
  text-align: left;
  width: 100%;
  background-color: #f3eeff;
  color: #6a3dc8;
  border: 1px solid #e8e0f5;
}
.action-btn i {
  font-size: 24px;
  margin-right: 16px;
  flex-shrink: 0;
  color: #6a3dc8;
}
.btn-content {
  display: flex;
  flex-direction: column;
}
.btn-title {
  font-weight: 600;
  font-size: 16px;
  margin-bottom: 4px;
}
.btn-desc {
  font-size: 13px;
  opacity: 0.8;
}
.action-btn:hover {
  background-color: #e8e0f5;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(106, 61, 200, 0.1);
}
.action-btn:active {
  background-color: #d4c6f2;
  transform: translateY(0);
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 核心区域：分类标签 + 筛选按钮 */
.combined-section {
  display: flex;
  align-items: center; /* 垂直居中，避免高度差 */
  flex-wrap: nowrap;   /* 禁止换行！确保永远同行 */
  gap: 20px;           /* 按钮组间距 */
  margin-bottom: 24px;
}

/* 如果筛选按钮有下沉问题，补充： */
.filter-section {
  align-self: center; /* 强制筛选区垂直居中 */
}

/* 文档分类区 */
.doc-section {
  flex: 1; /* 占据主要宽度 */
  min-width: 0; /* 允许内容收缩 */
}
.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #1a1a1a;
}
.tag-group {
  display: flex;
  gap: 16px; /* 增大分类按钮间距（原12px） */
  flex-wrap: wrap; /* 防止极端宽度下拥挤 */
}
.doc-tag {
  padding: 9px 18px; /* 增大按钮内边距（原8px 16px） */
  border-radius: 16px;
  background: white;
  border: 1px solid #f0f0f0;
  color: #646464;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
  white-space: nowrap; /* 防止文字换行 */
}
.doc-tag:hover {
  background: #f3eeff;
  color: #6a3dc8;
  border-color: #e8e0f5;
}
.doc-tag.active {
  background: #6a3dc8;
  color: white;
  border-color: #6a3dc8;
}

/* 筛选区（核心修复） */
.filter-section {
  display: flex;
  gap: 16px; /* 增大筛选按钮间距（原12px） */
  align-items: center;
  /* 移除原padding-top: 38px，让筛选区与分类区顶部对齐 */
  min-width: 0;
}
.filter-dropdown {
  position: relative;
}
.filter-btn {
  display: flex;
  align-items: center;
  gap: 6px; /* 增大按钮内部图标与文字间距（原4px） */
  padding: 9px 14px; /* 增大按钮内边距（原8px 12px） */
  border-radius: 6px;
  background: white;
  border: 1px solid #e0e0e0;
  color: #555;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
  white-space: nowrap;
}
.filter-btn:hover {
  border-color: #c0b1e0;
  color: #6a3dc8;
}
.filter-btn.active {
  background: #f3eeff;
  color: #6a3dc8;
  border-color: #d4c6f2;
}
.filter-btn i {
  font-size: 12px;
  transition: transform 0.2s;
}
.filter-btn.active i {
  transform: rotate(180deg);
}
.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  width: 160px;
  background: white;
  border: 1px solid #e8e0f5;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(106, 61, 200, 0.1);
  z-index: 10;
  margin-top: 4px;
  overflow: hidden;
}
.dropdown-item {
  padding: 8px 12px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
}
.dropdown-item:hover {
  background-color: #f3eeff;
  color: #6a3dc8;
}

/* 特色功能区（保持不变） */
.feature-section.combined {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 24px 0;
  padding: 12px 0;
}
.feature-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  color: #6a3dc8;
  font-size: 15px;
  cursor: pointer;
  background: none;
  border: none;
  transition: all 0.2s;
  border-radius: 6px;
}
.feature-btn:hover {
  background-color: #f3eeff;
}
.feature-btn:active {
  background-color: #e8e0f5;
}
.feature-btn i {
  font-size: 18px;
}
.user-info-combined {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  color: #555;
}
.user-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}
.date-info {
  color: #888;
  margin-left: 12px;
}
</style>