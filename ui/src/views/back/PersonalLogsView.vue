<script setup lang="ts">
import { onMounted, ref } from 'vue';
import api from '../../api/index';
import { ClipboardList, AlertTriangle } from 'lucide-vue-next';
import dayjs from 'dayjs';

const formatTime = (timestamp: string | Date) => {
  return dayjs(timestamp).format('YYYY-MM-DD HH:mm:ss');
};

const logs = ref<any[]>([]);
const loading = ref(false);

const fetchLogs = async () => {
  loading.value = true;
  try {
    const res = await api.operatorApi.getMyOperationLogs();
    logs.value = res.data || [];
  } catch (e) {
    console.error('获取操作日志失败', e);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchLogs();
});
</script>

<template>
  <div class="log-view">
    <div class="header">
      <ClipboardList class="icon" />
      <h1 class="title">我的操作日志</h1>
    </div>
    <p class="subtitle">查看最近系统记录的操作行为，最多显示 100 条。</p>

    <div v-if="loading" class="loading">正在加载日志，请稍候...</div>

    <div v-else-if="logs.length === 0" class="empty-state">
      <img
          src="http://47.108.177.82:9000/moxie/undraw_creation_4036.svg"
          alt="暂无日志"
          class="empty-image"
      />
      <h2 class="empty-title">暂无操作日志</h2>
      <p class="empty-description">系统尚未记录您的任何操作行为</p>
    </div>

    <div v-else class="log-list">
      <div class="log-item" v-for="log in logs" :key="log.id">
        <div class="info">
          <div class="type-row">
            <span class="badge">{{ log.type || '未知类型' }}</span>
            <span class="timestamp">{{ formatTime(log.timestamp) }}</span>
          </div>
          <p class="description">{{ log.description }}</p>
        </div>
        <div class="status" :class="log.success ? 'status-success' : 'status-fail'">
          <template v-if="log.success">
            <span class="status-badge">
              ✅ 成功
            </span>
          </template>
          <template v-else>
            <span class="status-badge">
              <AlertTriangle class="status-icon" /> 失败
            </span>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.log-view {
  padding: 32px;
  background-color: #f9fafb;
  font-family: system-ui, sans-serif;
}

.header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
}

.icon {
  width: 22px;
  height: 22px;
  color: #5e4dcd;
}

.title {
  font-size: 26px;
  font-weight: 700;
  color: #1e293b;
}

.subtitle {
  font-size: 15px;
  color: #64748b;
  margin-bottom: 24px;
}

.loading {
  text-align: center;
  color: #94a3b8;
  font-size: 15px;
  padding: 40px 0;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #94a3b8;
}

.empty-image {
  width: 160px;
  margin-bottom: 16px;
  opacity: 0.8;
}

.empty-title {
  font-size: 20px;
  font-weight: 600;
  color: #475569;
  margin-bottom: 6px;
}

.empty-description {
  font-size: 14px;
  color: #9ca3af;
}

.log-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.log-item {
  background-color: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 16px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info {
  flex: 1;
}

.type-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.badge {
  font-size: 13px;
  color: #6366f1;
  background: #eef2ff;
  padding: 4px 10px;
  border-radius: 9999px;
  font-weight: 500;
}

.timestamp {
  font-size: 12px;
  color: #9ca3af;
}

.description {
  font-size: 14px;
  color: #334155;
}
.status {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  border-radius: 9999px;
  font-weight: 500;
}

.status-success .status-badge {
  background-color: #dcfce7;
  color: #15803d;
}

.status-fail .status-badge {
  background-color: #fee2e2;
  color: #b91c1c;
}

.status-icon {
  width: 16px;
  height: 16px;
  margin-right: 6px;
}

</style>
