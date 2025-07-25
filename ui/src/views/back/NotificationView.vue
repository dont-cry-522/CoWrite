<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { Bell, Trash2, CheckCircle2 } from 'lucide-vue-next';
import api from '../../api/index';
import Pagination from '../../components/Pagination.vue';
const notifications = ref<any[]>([]);
const unreadCount = ref(0);
const page = ref(1);
const size = ref(10);
const total = ref(0);
const loading = ref(false);
const typeFilter = ref('');

const updatePageSize = (newSize: number) => {
  size.value = newSize;
  page.value = 1; // 重置页码
  fetchNotifications();
};

const updatePage = (newPage: number) => {
  page.value = newPage;
  fetchNotifications();
};

// 分页逻辑
const totalPages = computed(() => Math.ceil(total.value / size.value));

const fetchNotifications = async () => {
  loading.value = true;
  try {
    const res = await api.notificationApi.getNotifications({
      page: page.value,
      size: size.value,
      type: typeFilter.value || undefined
    });
    notifications.value = res.data.records || [];
    total.value = res.data.total || 0;
  } catch (e) {
    console.error('获取通知失败', e);
  } finally {
    loading.value = false;
  }
};

const fetchUnreadCount = async () => {
  try {
    const res = await api.notificationApi.getUnreadCount();
    unreadCount.value = res.data;
  } catch (e) {
    console.error('获取未读数量失败', e);
  }
};

const handleMarkAsRead = async (id: number) => {
  await api.notificationApi.markAsRead(id);
  await fetchNotifications();
  await fetchUnreadCount();
};

const handleMarkAllAsRead = async () => {
  await api.notificationApi.markAllAsRead();
  await fetchNotifications();
  await fetchUnreadCount();
};

const handleDelete = async (id: number) => {
  await api.notificationApi.deleteNotification(id);
  await fetchNotifications();
  await fetchUnreadCount();
};

const handleClearAll = async () => {
  await api.notificationApi.clearAllNotifications();
  await fetchNotifications();
  await fetchUnreadCount();
};

onMounted(() => {
  fetchNotifications();
  fetchUnreadCount();
});
</script>

<template>
  <div class="notification-view">
    <div class="header">
      <div class="left">
        <Bell class="icon" />
        <h1>消息通知中心</h1>
        <span class="unread" v-if="unreadCount > 0">未读 {{ unreadCount }}</span>
      </div>
      <div class="right">
        <button @click="handleMarkAllAsRead">
          <CheckCircle2 class="icon" /> 全部已读
        </button>
        <button @click="handleClearAll">
          <Trash2 class="icon" /> 清空
        </button>
      </div>
    </div>

    <div class="list">
      <div v-if="loading" class="loading">加载中，请稍候...</div>
      <div v-else-if="notifications.length === 0" class="empty-state">
        <img src="http://47.108.177.82:9000/moxie/empty-box.svg" alt="暂无通知" class="empty-image" />
        <h2 class="empty-title">暂无通知</h2>
        <p class="empty-description">您目前没有任何系统或消息通知</p>
      </div>

      <div
          v-else
          class="notification-item"
          v-for="item in notifications"
          :key="item.id"
          :class="{ unread: !item.read }"
      >
        <div class="content">
          <h3>{{ item.title || '系统通知' }}</h3>
          <p>{{ item.content }}</p>
          <span class="time">{{ item.createdAt }}</span>
        </div>
        <div class="tools">
          <button @click="handleMarkAsRead(item.id)" v-if="!item.read">标为已读</button>
          <button @click="handleDelete(item.id)">删除</button>
        </div>
      </div>
    </div>

    <!-- 👇 在 list 下方添加 -->
    <div class="pagination-wrapper" v-if="total > 0">
      <Pagination
          :currentPage="page"
          :totalPages="totalPages"
          :total="total"
          :pageSize="size"
          :pageSizes="[10, 20, 50, 100]"
          :maxVisible="5"
          :labels="{
      first: '首页',
      last: '末页',
      prev: '上一页',
      next: '下一页',
      goto: '跳转到',
      page: '页',
      total: '共 {total} 条'
    }"
          @update:page="updatePage"
          @update:pageSize="updatePageSize"
      />
    </div>
  </div>
</template>

<style scoped>
.notification-view {
  padding: 32px;
  background: #f9fafb;
  font-family: system-ui, sans-serif;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.header .left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header .left h1 {
  font-size: 22px;
  font-weight: 600;
  color: #1e293b;
}

.header .left .unread {
  color: #ef4444;
  font-weight: 500;
}

.header .right button {
  background: #5e4dcd;
  color: white;
  border: none;
  padding: 8px 14px;
  border-radius: 6px;
  margin-left: 10px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  transition: background 0.2s;
}

.header .right button:hover {
  background: #4c3cad;
}

.icon {
  width: 18px;
  height: 18px;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.notification-item {
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 18px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  transition: box-shadow 0.2s ease;
}

.notification-item.unread {
  border-left: 4px solid #5e4dcd;
  background: #f3f0ff;
}

.notification-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.content h3 {
  font-size: 16px;
  margin-bottom: 6px;
  font-weight: 600;
  color: #0f172a;
}

.content p {
  font-size: 14px;
  color: #4b5563;
  margin-bottom: 6px;
}

.time {
  font-size: 12px;
  color: #9ca3af;
}

.tools button {
  background: none;
  border: none;
  color: #6366f1;
  cursor: pointer;
  font-size: 14px;
  margin-left: 12px;
  transition: color 0.2s;
}

.tools button:hover {
  color: #4338ca;
}

.loading,
.empty {
  text-align: center;
  padding: 40px 0;
  color: #94a3b8;
  font-size: 16px;
}

.pagination button {
  background: none;
  border: none;
  cursor: pointer;
  color: #4b5563;
  font-size: 14px;
}

.pagination button:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}

.pagination-wrapper {
  margin-top: 32px;
  display: flex;
  justify-content: center;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  text-align: center;
  color: #94a3b8;
  background-color: #fff;
  border-radius: 12px;
  border: 1px dashed #e2e8f0;
  transition: 0.3s ease;
}

.empty-image {
  width: 120px;
  margin-bottom: 16px;
  opacity: 0.8;
}

.empty-title {
  font-size: 20px;
  color: #1e293b;
  font-weight: 600;
  margin-bottom: 4px;
}

.empty-description {
  font-size: 14px;
  color: #64748b;
}
</style>
