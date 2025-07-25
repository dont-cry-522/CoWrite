<script setup lang="ts">
import { ref } from 'vue'
import {
  Laptop,
  MapPin,
  ShieldCheck,
  ShieldX,
  Lock,
  Ban
} from 'lucide-vue-next'

// 模拟登录记录数据
const loginRecords = ref([
  {
    id: 1,
    ip: '183.128.12.1',
    location: '杭州',
    device: 'Windows 11 / Chrome 120',
    loginTime: '2025-07-24T08:30:12',
    isBlocked: false
  },
  {
    id: 2,
    ip: '171.214.3.9',
    location: '成都',
    device: 'macOS 14 / Safari',
    loginTime: '2025-07-23T22:01:03',
    isBlocked: false
  },
  {
    id: 3,
    ip: '103.41.12.43',
    location: '未知',
    device: 'Android / 微信内置浏览器',
    loginTime: '2025-07-22T19:11:40',
    isBlocked: true
  }
])

const formatDate = (iso: string) => {
  const date = new Date(iso)
  return date.toLocaleString()
}

const blockIp = (id: number) => {
  const record = loginRecords.value.find(r => r.id === id)
  if (record && !record.isBlocked) {
    record.isBlocked = true
  }
}
</script>

<template>
  <div class="device-view">
    <h1 class="title">账号登录设备</h1>
    <p class="subtitle">您可查看最近的登录行为，并对可疑 IP 进行封禁。</p>

    <div class="device-list">
      <div class="device-card" v-for="record in loginRecords" :key="record.id">
        <div class="left">
          <Laptop class="device-icon" />
          <div class="info">
            <div class="device-name">{{ record.device }}</div>
            <div class="meta">
              <MapPin class="meta-icon" />
              <span>{{ record.location }} · {{ record.ip }}</span>
            </div>
            <div class="time">{{ formatDate(record.loginTime) }}</div>
          </div>
        </div>
        <div class="actions">
          <div class="status" :class="{ blocked: record.isBlocked, safe: !record.isBlocked }">
            <template v-if="record.isBlocked">
              <ShieldX class="status-icon" /> 已封禁
            </template>
            <template v-else>
              <ShieldCheck class="status-icon" /> 正常
            </template>
          </div>
          <button
              class="block-btn"
              :disabled="record.isBlocked"
              @click="blockIp(record.id)"
          >
            <template v-if="record.isBlocked">
              <Ban class="btn-icon" /> 已封禁
            </template>
            <template v-else>
              <Lock class="btn-icon" /> 封禁
            </template>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.device-view {
  padding: 32px;
  background-color: #f9fafb;
  font-family: system-ui, sans-serif;
}

.title {
  font-size: 26px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 6px;
}

.subtitle {
  font-size: 15px;
  color: #64748b;
  margin-bottom: 24px;
}

.device-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.device-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 18px 22px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.03);
  transition: box-shadow 0.2s;
}

.device-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
}

.left {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.device-icon {
  width: 28px;
  height: 28px;
  color: #4f46e5;
  flex-shrink: 0;
  margin-top: 4px;
}

.info {
  display: flex;
  flex-direction: column;
}

.device-name {
  font-size: 16px;
  font-weight: 600;
  color: #0f172a;
  margin-bottom: 4px;
}

.meta {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #6b7280;
  gap: 4px;
}

.meta-icon {
  width: 14px;
  height: 14px;
  color: #9ca3af;
}

.time {
  font-size: 12px;
  color: #9ca3af;
  margin-top: 4px;
}

.actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
  min-width: 100px;
}

.status {
  display: flex;
  align-items: center;
  font-weight: 500;
  font-size: 14px;
}

.status-icon {
  width: 16px;
  height: 16px;
  margin-right: 4px;
}

.status.safe {
  color: #10b981;
}

.status.blocked {
  color: #ef4444;
}

.block-btn {
  background-color: #f3f4f6;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 14px;
  color: #374151;
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  transition: all 0.2s;
}

.block-btn:hover {
  background-color: #e5e7eb;
}

.block-btn:disabled {
  background-color: #f1f5f9;
  color: #9ca3af;
  cursor: not-allowed;
}

.btn-icon {
  width: 16px;
  height: 16px;
}
</style>
