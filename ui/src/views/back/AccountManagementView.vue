<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

function goTo(path: string) {
  router.push(path);
}
</script>

<template>
  <div class="home-view">
    <h1 class="title">欢迎来到管理后台</h1>
    <p class="subtitle">这里是您的数据控制中心，您可以在这里快速访问常用功能。</p>

    <div class="quick-links">
      <div class="card" @click="goTo('/back/users')">
        <h3>用户管理</h3>
        <p>查看和管理平台用户</p>
      </div>
      <div class="card" @click="goTo('/back/settings')">
        <h3>系统设置</h3>
        <p>配置系统参数与权限</p>
      </div>
      <div class="card" @click="goTo('/back/stats')">
        <h3>数据统计</h3>
        <p>了解平台实时数据</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-view {
  padding: 32px;
  background-color: #f9fafb;
  min-height: 100%;
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

.quick-links {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.card {
  background-color: white;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 20px;
  width: 240px;
  cursor: pointer;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card h3 {
  font-size: 18px;
  color: #0f172a;
  margin-bottom: 6px;
}

.card p {
  font-size: 14px;
  color: #6b7280;
}
</style>
