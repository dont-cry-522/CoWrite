<template>
  <div class="plugin-management">
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

    <!-- 插件商店弹窗 -->
    <div v-if="showStore" class="store-popup">
      <div class="popup-content">
        <h2>插件商店</h2>
        <div class="plugin-list">
          <div
              v-for="plugin in plugins"
              :key="plugin.id"
              class="plugin-card"
              @click="openPluginDetails(plugin)"
          >
            <div class="plugin-card-header">
              <h3>{{ plugin.pluginName }}</h3>
              <button class="btn install-btn" @click.stop="installPlugin(plugin)">安装</button>
            </div>
            <p>{{ plugin.pluginType }} - {{ plugin.version }}</p>
          </div>
        </div>

        <!-- 分页 -->
        <Pagination
            :currentPage="currentStorePage"
            :totalPages="totalStorePages"
            :total="plugins.length"
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
        <h2>{{ currentPlugin.pluginName }} 详情</h2>
        <p>{{ currentPlugin.pluginType }}</p>
        <p>{{ currentPlugin.documentationUrl }}</p>
        <p v-if="currentPlugin.webhookUrl">
          Webhook URL: <a :href="currentPlugin.webhookUrl" target="_blank">{{ currentPlugin.webhookUrl }}</a>
        </p>
        <button class="btn close-btn" @click="closePluginDetails">关闭</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import api from '../../api'; // 引入接口方法
import Pagination from '../../components/Pagination.vue';
import Input from '../../components/Input.vue';
import Select from '../../components/Select.vue';

const categories = ['支付', '分析', 'SEO', '安全', '图像处理'];

const formattedCategoriesOptions = computed(() => [
  { value: null, label: '所有分类' },
  ...categories.map(category => ({ value: category, label: category }))
]);

interface Plugin {
  id: number;
  pluginName: string;
  pluginType: string;
  version: string;
  webhookUrl?: string;
  apiUrl: string;
  documentationUrl: string;
  enabled: boolean;
}

const plugins = ref<Plugin[]>([]); // 插件列表
const searchQuery = ref(''); // 搜索查询
const selectedCategory = ref<string | null>(null); // 选择分类
const showStore = ref(false); // 控制商店弹窗
const showPluginDetails = ref(false); // 控制插件详情弹窗
const currentPlugin = ref<Plugin | null>(null); // 当前选中的插件

// 分页控制
const currentStorePage = ref(1); // 当前商店页码
const storePageSize = ref(10); // 商店每页显示插件数量

// 请求插件数据
const loadPlugins = async () => {
  try {
    const result = await api.pluginApi.fetchPlugins({
      page: currentStorePage.value,
      size: storePageSize.value,
      keyword: searchQuery.value, // 传递搜索关键词
      category: selectedCategory.value, // 传递选择的分类
    });

    // 确保 result.data 和 result.data.records 存在
    if (result.data && result.data.records) {
      plugins.value = result.data.records; // 设置插件数据
    } else {
      console.error("返回的数据格式不正确，缺少 'records' 属性");
      plugins.value = []; // 如果没有 records，设置为空数组
    }
  } catch (error) {
    console.error('Error loading plugins:', error);
  }
};

onMounted(() => {
  loadPlugins(); // 页面加载时加载插件列表
});

// 搜索和分页控制
const updateStorePage = (page: number) => {
  currentStorePage.value = page;
  loadPlugins(); // 更新页面时重新加载插件
};

const updateStorePageSize = (size: number) => {
  storePageSize.value = size;
  currentStorePage.value = 1; // 重置到第一页
  loadPlugins(); // 更新每页数量时重新加载插件
};

// 总页数计算
const totalStorePages = computed(() => {
  return Math.ceil(plugins.value.length / storePageSize.value);
});

// 打开/关闭插件商店
const openStore = () => { showStore.value = true; };
const closeStore = () => { showStore.value = false; };

// 打开插件详情
const openPluginDetails = (plugin: Plugin) => { currentPlugin.value = plugin; showPluginDetails.value = true; };
const closePluginDetails = () => { showPluginDetails.value = false; };

// 安装插件
const installPlugin = (plugin: Plugin) => {
  alert(`插件 "${plugin.pluginName}" 已安装！`);
};
</script>

<style scoped>
.plugin-management {
  padding: 20px 30px;
  border-radius: 10px;
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
  height: 95%;
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
/* 限制插件商店和插件详情弹窗的最大高度 */
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
  max-width: 800px;
  width: 100%;
  max-height: 80vh; /* 最大高度为 80% 的视口高度 */
  overflow-y: auto; /* 超出内容时启用垂直滚动条 */
}

/* 如果需要进一步限制内容区域的最大高度，也可以设置 */
.plugin-list {
  max-height: 60vh; /* 设置插件列表最大高度为 60% 的视口高度 */
  overflow-y: auto; /* 启用滚动条 */
}

/* 样式保持不变，其它部分 */
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
