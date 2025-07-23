<template>
  <div class="yuque-documents">
    <!-- 顶部操作区 - 统一淡紫色风格 -->
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
            <span class="btn-title">🤖 AI 帮你写</span>
            <span class="btn-desc">AI 助手帮你一键生成文档</span>
          </div>
        </button>
      </div>
    </div>

    <!-- 文档分类区和筛选区 -->
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

    <!-- 特色功能区 - 合并为一行 -->
    <div class="feature-section combined">
      <button class="feature-btn" @click="openFeature">
        <i class="iconfont icon-magic"></i>
        <span>🎙️ 试试新建~ </span>
      </button>
      <div class="user-info-combined">
        <img class="user-avatar" src="https://i.pravatar.cc/40?img=5" />
        <span>Saul Goodman / 默认知识库</span>
        <span class="date-info">2023-10-25</span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'Documents',
  setup() {
    const activeCategory = ref('edited');
    const activeFilter = ref('type');
    const activeDropdown = ref<string | null>(null);

    const docCategories = [
      { id: 'edited', label: '编辑过' },
      { id: 'viewed', label: '浏览过' },
      { id: 'liked', label: '我点赞的' },
      { id: 'commented', label: '我评论过' }
    ];

    const docFilters = [
      { id: 'type', label: '类型' },
      { id: 'owner', label: '归属' },
      { id: 'creator', label: '创建者' }
    ];

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
    };

    const toggleDropdown = (filterId: string) => {
      activeDropdown.value = activeDropdown.value === filterId ? null : filterId;
    };

    const selectFilterItem = (filterId: string, value: string) => {
      activeFilter.value = filterId;
      console.log(`Selected ${filterId}: ${value}`);
      activeDropdown.value = null;
    };

    return {
      docCategories,
      docFilters,
      filterOptions,
      activeCategory,
      activeFilter,
      activeDropdown,
      toggleDropdown,
      selectFilterItem
    };
  },
  methods: {
    createDocument() {
      console.log('创建文档');
    },
    createRepo() {
      console.log('创建知识库');
    },
    openTemplates() {
      console.log('打开模板中心');
    },
    useAI() {
      console.log('使用AI生成文档');
    },
    filterByCategory(category: string) {
      this.activeCategory = category;
      console.log('按分类筛选:', category);
    },
    applyFilter(filter: string) {
      this.activeFilter = filter;
      console.log('应用筛选:', filter);
    },
    openFeature() {
      console.log('打开特色功能');
    }
  }
});
</script>

<style scoped>
.yuque-documents {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  color: #1a1a1a;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
}

/* 操作按钮组 - 统一淡紫色风格 */
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

/* 主操作按钮 - 统一风格 */
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

/* 按钮交互效果 */
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

/* 合并分类和筛选区为一行 */
.combined-section {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.doc-section {
  flex: 1;
}

/* 筛选按钮和下拉菜单 */
.filter-section {
  display: flex;
  gap: 12px;
  align-items: center;
  padding-top: 38px;
  position: relative;
}

.filter-dropdown {
  position: relative;
}

.filter-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 12px;
  border-radius: 6px;
  background: white;
  border: 1px solid #e0e0e0;
  color: #555;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
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

/* 文档分类标签 */
.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #1a1a1a;
}

.tag-group {
  display: flex;
  gap: 12px;
}

.doc-tag {
  padding: 8px 16px;
  border-radius: 16px;
  background: white;
  border: 1px solid #f0f0f0;
  color: #646464;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
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

/* 特色功能区 - 合并为一行 */
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

.user-info-combined .user-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.user-info-combined .date-info {
  color: #888;
  margin-left: 12px;
}
</style>