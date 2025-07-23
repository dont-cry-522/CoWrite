<template>
  <div>
    <div ref="chartContainer" :style="{ width: width, height: height }"></div>
    <CustomModal ref="customModal" />
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, defineProps, nextTick } from 'vue';
import * as echarts from 'echarts';
import CustomModal from './CustomModal.vue';

// 定义组件的 props
const props = defineProps({
  chartType: {
    type: String,
    default: 'line',  // 默认类型为折线图
  },
  chartData: {
    type: Object,
    required: true,  // 必须传入数据
  },
  chartOptions: {
    type: Object,
    default: () => ({
      title: {
        text: '默认标题',
        left: 'center',
        textStyle: {
          color: '#5B7DB1',  // 标题颜色
          fontSize: 18,
        },
      },
      tooltip: {
        trigger: 'axis',
        backgroundColor: '#3A4A7E',  // 提示框背景色
        borderColor: '#5B7DB1',  // 提示框边框颜色
        textStyle: {
          color: '#F4EEFF',  // 提示框文字颜色
        },
      },
    }),
  },
  width: {
    type: String,
    default: '100%',
  },
  height: {
    type: String,
    default: '400px',
  },
  theme: {
    type: String,
    default: 'light',  // 默认主题为 light
  },
  animation: {
    type: Boolean,
    default: true,  // 默认开启动画
  },
  animationDuration: {
    type: Number,
    default: 1000,  // 默认动画时长为 1000ms
  },
  animationEasing: {
    type: String,
    default: 'cubicOut',  // 默认动画缓动效果
  },
  backgroundColor: {
    type: String,
    default: 'transparent',  // 默认背景色为透明
  },
});

const chartContainer = ref(null);
const customModal = ref(null);

// 存储 ECharts 实例
let chartInstance = null;

// 在 mounted 后初始化 ECharts
onMounted(() => {
  // 使用 nextTick 确保 DOM 元素渲染完成后再初始化
  nextTick(() => {
    chartInstance = echarts.init(chartContainer.value, props.theme);
    renderChart();
  });
});

// 监控数据和配置项的变化，动态更新图表
watch([() => props.chartData, () => props.chartOptions, () => props.theme], () => {
  renderChart();
});

// 渲染图表的函数
function renderChart() {
  const { chartType, chartData, chartOptions, animation, animationDuration, animationEasing, backgroundColor } = props;

  // 生成图表配置项
  const options = {
    ...chartOptions,
    backgroundColor,  // 背景色
    animation,  // 控制动画
    animationDuration,  // 动画时长
    animationEasing,  // 动画缓动效果
    series: [
      {
        type: chartType,
        data: chartData.data,
        name: chartData.name || '数据',
        itemStyle: {
          color: '#5B7DB1',  // 默认颜色
        },
        lineStyle: {
          color: '#5B7DB1',  // 折线颜色
          width: 3,
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#5B7DB1' },
            { offset: 1, color: '#3A4A7E' },
          ]),  // 区域渐变颜色
        },
        ...chartData.seriesOptions,  // 如果有额外的设置项则加入
      },
    ],
  };

  // 渲染图表
  chartInstance.setOption(options);

  // 点击效果
  chartInstance.on('click', (params) => {
    customModal.value.showModal(params.name, `值: ${params.value}`);
  });

  // hover 效果
  chartInstance.on('mouseover', (params) => {
    console.log('hover 到图表元素:', params);
  });
}
</script>

<style scoped>
/* 样式适应容器大小 */
div {
  width: 100%;
  height: 100%;
}
</style>
