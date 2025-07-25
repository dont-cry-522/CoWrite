<template>
  <div class="layout">
    <div class="container">
      <!-- 左侧 -->
      <LeftSidebar
          title="知识库"
          :items="repositories"
          :collapsed="isLeftCollapsed"
          @toggle="isLeftCollapsed = !isLeftCollapsed"
          @select="selectRepository"
          @menuClick="onMenuClick"
          @repoClick="onRepoSelect"
      />

      <!-- 主内容区域 -->
      <main class="content">
        <RouterView />
      </main>

      <!-- 右侧 -->
      <RightSidebar
          v-if="!isHideRightSidebar"
          :collapsed="isRightCollapsed"
          :currentRepo="selectedRepo"
          :catalog="docCatalog"
          @toggle="isRightCollapsed = !isRightCollapsed"
          @selectItem="handleSidebarItemClick"
          @switchRepo="openRepoSelector"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();
import RightSidebar from '../components/RightSidebar.vue';
import LeftSidebar from "../components/LeftSidebar.vue";
const isRightCollapsed = ref(false)
const isHideRightSidebar = ref(false);
const selectedRepo = ref({ id: 1, name: '知识库 1' })
const docCatalog = ref([
  {
    id: 'home',
    title: '首页',
    icon: 'home',
  },
  {
    id: 'dir1',
    title: '目录',
    children: [
      { id: 'doc1', title: '接口文档.md' },
      { id: 'doc2', title: '产品需求文档.md' },
    ],
  },
])

function onMenuClick(item: any) {
  isHideRightSidebar.value = true;
  router.push(`/${item.id}`)
}

function onRepoSelect(repo: any) {
  isHideRightSidebar.value = false;
  selectedRepo.value = repo
  // docCatalog.value = getDocsByRepoId(repo.id)
  docCatalog.value = [
    {
      id: 'home',
      title: '更换后首页',
      icon: 'home',
    },
    {
      id: 'dir1',
      title: '更换后',
      children: [
        { id: 'doc1', title: '接口文档.md' },
        { id: 'doc2', title: '产品需求文档.md' },
      ],
    },
  ]
}

interface Repository {
  id: number;
  name: string;
  icon?: string;
}

const isLeftCollapsed = ref(false);

const repositories = ref<Repository[]>([
  { id: 1, name: '知识库 1', icon: 'icon-database' },
  { id: 2, name: '知识库 2', icon: 'icon-database' },
  { id: 3, name: '知识库 3', icon: 'icon-database' },
]);

function selectRepository(repo: Repository) {
  console.log('选择知识库:', repo);
  // 可以在这里加入路由跳转或状态更新逻辑
}

function handleSidebarItemClick(item: any) {
  console.log('右侧栏点击了目录项或首页：', item);
  // 你可以根据 item.id 或 item.label 来判断点击了什么，并执行跳转或显示内容
}

function openRepoSelector() {
  console.log('点击了当前知识库名称，准备弹出选择框');
  // 在这里触发弹窗、下拉菜单或跳转组织选择页面
}
</script>

<style scoped>
.layout {
  display: flex;
  flex-direction: column;
  margin: 0;
}

.container {
  display: flex;
  flex: 1;
}

nav ul {
  list-style: none;
  padding: 0;
}

nav ul li {
  margin: 1rem 0;
  cursor: pointer;
}

nav ul li:hover {
  color: #42b983;
}

.content {
  flex: 1;
  padding: 1rem;
  overflow-y: auto;
  min-height: 0;
}
</style> 