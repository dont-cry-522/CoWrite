<template>
  <div class="plugin-management">
    <h1 class="title">我的插件</h1>

    <!-- 搜索与分类 -->
    <div class="filters">
      <Input
          class="filter-input"
          v-model="searchQuery"
          placeholder="搜索插件..."
      />
      <Select
          class="filter-input"
          v-model="selectedCategory"
          :options="formattedCategoriesOptions"
          placeholder="选择分类"
      />
      <button class="btn open-store-btn" @click="openStore">打开插件商店</button>
    </div>

    <!-- 已安装插件 -->
    <section>
      <h2 class="section-title">已安装插件</h2>
      <div class="plugin-list">
        <div
            v-for="plugin in filteredInstalledPlugins"
            :key="plugin.id"
            class="plugin-card"
            @click="openPluginDetails(plugin)"
        >
          <div class="plugin-card-header">
            <h3>{{ plugin.plugin_name }}</h3>
            <button class="btn uninstall-btn" @click.stop="uninstallPlugin(plugin)">卸载</button>
          </div>
          <p>{{ plugin.plugin_type }} - {{ plugin.version }}</p>
        </div>
      </div>
      <Pagination
          :currentPage="currentInstalledPage"
          :totalPages="totalInstalledPages"
          :total="filteredInstalledPlugins.length"
          :pageSize="installedPageSize"
          :pageSizes="[10, 20, 50, 100]"
          :maxVisible="7"
          @update:page="updateInstalledPage"
          @update:pageSize="updateInstalledPageSize"
      />
    </section>

    <!-- 插件商店弹窗 -->
    <div v-if="showStore" class="store-popup">
      <div class="popup-content">
        <h2>插件商店</h2>
        <div class="plugin-list">
          <div
              v-for="plugin in availablePlugins"
              :key="plugin.id"
              class="plugin-card"
              @click="openPluginDetails(plugin)"
          >
            <div class="plugin-card-header">
              <h3>{{ plugin.plugin_name }}</h3>
              <button class="btn install-btn" @click.stop="installPlugin(plugin)">安装</button>
            </div>
            <p>{{ plugin.plugin_type }} - {{ plugin.version }}</p>
          </div>
        </div>
        <Pagination
            :currentPage="currentStorePage"
            :totalPages="totalStorePages"
            :total="availablePlugins.length"
            :pageSize="storePageSize"
            :pageSizes="[10, 20, 50, 100]"
            :maxVisible="7"
            @update:page="updateStorePage"
            @update:pageSize="updateStorePageSize"
        />
        <button class="btn close-btn" @click="closeStore">关闭商店</button>
      </div>
    </div>

    <!-- 插件详情弹窗 -->
    <div v-if="showPluginDetails && currentPlugin" class="plugin-details-popup">
      <div class="popup-content">
        <h2>{{ currentPlugin.plugin_name }} 详情</h2>
        <p>{{ currentPlugin.plugin_type }}</p>
        <p>{{ currentPlugin.documentation_url }}</p>
        <p v-if="currentPlugin.webhook_url">
          Webhook URL: <a :href="currentPlugin.webhook_url" target="_blank">{{ currentPlugin.webhook_url }}</a>
        </p>
        <button class="btn close-btn" @click="closePluginDetails">关闭</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import Pagination from '../../components/Pagination.vue'
import Input from '../../components/Input.vue'
import Select from '../../components/Select.vue'

interface Plugin {
  id: number
  plugin_name: string
  plugin_type: string
  version: string
  webhook_url?: string
  api_url: string
  documentation_url: string
  enabled: boolean
}

const plugins = ref<Plugin[]>([
  { id: 1, plugin_name: '支付网关', plugin_type: '支付', version: '1.0.0', api_url: 'https://example.com/api/payment', documentation_url: 'https://example.com/docs/payment', enabled: true },
  { id: 2, plugin_name: '分析追踪器', plugin_type: '分析', version: '2.1.0', api_url: 'https://example.com/api/analytics', documentation_url: 'https://example.com/docs/analytics', enabled: true },
  { id: 3, plugin_name: 'SEO优化器', plugin_type: 'SEO', version: '3.3.0', api_url: 'https://example.com/api/seo', documentation_url: 'https://example.com/docs/seo', enabled: false },
  { id: 4, plugin_name: '安全插件', plugin_type: '安全', version: '1.2.0', api_url: 'https://example.com/api/security', documentation_url: 'https://example.com/docs/security', enabled: true },
  { id: 5, plugin_name: '图像处理插件', plugin_type: '图像处理', version: '1.1.0', api_url: 'https://example.com/api/image', documentation_url: 'https://example.com/docs/image', enabled: false },
])

const searchQuery = ref('')
const selectedCategory = ref<string | null>(null)
const showStore = ref(false)
const showPluginDetails = ref(false)
const currentPlugin = ref<Plugin | null>(null)

// 分类数据
const categories = ['支付', '分析', 'SEO', '安全', '图像处理']

// 格式化分类选项
const formattedCategoriesOptions = computed(() => [
  { value: null, label: '所有分类' },
  ...categories.map(category => ({ value: category, label: category }))
])

// 分页控制
const currentInstalledPage = ref(1)
const installedPageSize = ref(10)
const currentStorePage = ref(1)
const storePageSize = ref(10)

// 安装插件相关
const installedPluginIds = [1, 2, 4]
const installedPlugins = computed(() => plugins.value.filter(plugin => installedPluginIds.includes(plugin.id)))
const availablePlugins = computed(() => plugins.value.filter(plugin => !installedPluginIds.includes(plugin.id)))

// 搜索和分页过滤已安装插件
const filteredInstalledPlugins = computed(() => {
  return installedPlugins.value
      .filter(plugin => plugin.plugin_name.toLowerCase().includes(searchQuery.value.toLowerCase()))
      .slice((currentInstalledPage.value - 1) * installedPageSize.value, currentInstalledPage.value * installedPageSize.value)
})

const totalInstalledPages = computed(() => Math.ceil(filteredInstalledPlugins.value.length / installedPageSize.value))

const filteredStorePlugins = computed(() => {
  return availablePlugins.value
      .filter(plugin => plugin.plugin_name.toLowerCase().includes(searchQuery.value.toLowerCase()))
      .slice((currentStorePage.value - 1) * storePageSize.value, currentStorePage.value * storePageSize.value)
})

const totalStorePages = computed(() => Math.ceil(filteredStorePlugins.value.length / storePageSize.value))

const openStore = () => { showStore.value = true }
const closeStore = () => { showStore.value = false }
const installPlugin = (plugin: Plugin) => { alert(`插件 "${plugin.plugin_name}" 已安装！`) }
const uninstallPlugin = (plugin: Plugin) => { alert(`插件 "${plugin.plugin_name}" 已卸载！`) }
const openPluginDetails = (plugin: Plugin) => { currentPlugin.value = plugin; showPluginDetails.value = true }
const closePluginDetails = () => { showPluginDetails.value = false }

const updateInstalledPage = (page: number) => { currentInstalledPage.value = page }
const updateInstalledPageSize = (size: number) => { installedPageSize.value = size; currentInstalledPage.value = 1 }
const updateStorePage = (page: number) => { currentStorePage.value = page }
const updateStorePageSize = (size: number) => { storePageSize.value = size; currentStorePage.value = 1 }
</script>

<style scoped>
.plugin-management {
  padding: 20px 30px;
  border-radius: 10px;
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
}

.title {
  font-size: 36px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 40px;
}

.filters {
  display: flex;
  gap: 20px;
  justify-content: space-between;
  margin-bottom: 40px;
}

.filter-input {
  width: 48%;
  max-width: 350px;
}

.open-store-btn {
  background-color: #06b6d4;
  color: #fff;
  padding: 12px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.open-store-btn:hover {
  background-color: #00a0b2;
}

.section-title {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 20px;
}

.plugin-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.plugin-card {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
  padding: 20px;
  transition: transform 0.3s, box-shadow 0.3s;
}

.plugin-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
}

.plugin-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.plugin-card h3 {
  font-size: 20px;
  font-weight: bold;
}

.plugin-card p {
  font-size: 14px;
  color: #a1b1cc;
}

.plugin-card button {
  padding: 10px 15px;
  border-radius: 8px;
  background-color: #06b6d4;
  color: #fff;
  border: none;
  cursor: pointer;
}

.plugin-card button:hover {
  background-color: #00a0b2;
}

.store-popup, .plugin-details-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: #fff;
  color: #1b2a41;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
  max-width: 600px;
  width: 100%;
}

.store-popup h2, .plugin-details-popup h2 {
  font-size: 24px;
  font-weight: bold;
}

.close-btn {
  background-color: #ff5c5c;
  color: #fff;
  padding: 12px 20px;
  border-radius: 8px;
  cursor: pointer;
}

.close-btn:hover {
  background-color: #ff2b2b;
}
</style>
