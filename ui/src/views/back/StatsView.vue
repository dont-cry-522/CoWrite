<template>
  <div class="stats-view">
    <div class="card-container">
      <div class="card">
        <div class="card-header">文档创建次数</div>
        <div class="card-content">
          <WChart
            chartType="line"
            :chartData="{
              data: [120, 200, 150, 80, 70, 110, 130],
              name: '文档创建次数',
              seriesOptions: {
                smooth: true,  // 平滑曲线
              },
            }"
            :chartOptions="{
              xAxis: {
                type: 'category',
                data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月'],
                axisLine: {
                  lineStyle: {
                    color: '#5B7DB1',  // X 轴颜色
                  },
                },
              },
              yAxis: {
                type: 'value',
                axisLine: {
                  lineStyle: {
                    color: '#5B7DB1',  // Y 轴颜色
                  },
                },
              },
            }"
            theme="dark"
            :animation="true"
            :animationDuration="1500"
            animationEasing="elasticOut"
          />
          <div class="card-description">
            <p>本月文档创建次数：<strong>130</strong></p>
            <p>较上月增长：<strong>20%</strong></p>
            <p>总文档数：<strong>850</strong></p>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">协作次数</div>
        <div class="card-content">
          <WChart
            chartType="bar"
            :chartData="{
              data: [50, 80, 70, 60, 90, 100, 120],
              name: '协作次数',
              seriesOptions: {
                itemStyle: {
                  color: '#5B7DB1',  // 柱状图颜色
                },
              },
            }"
            :chartOptions="{
              xAxis: {
                type: 'category',
                data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月'],
                axisLine: {
                  lineStyle: {
                    color: '#5B7DB1',  // X 轴颜色
                  },
                },
              },
              yAxis: {
                type: 'value',
                axisLine: {
                  lineStyle: {
                    color: '#5B7DB1',  // Y 轴颜色
                  },
                },
              },
            }"
            theme="light"
            :animation="true"
            :animationDuration="2000"
            animationEasing="bounceOut"
          />
          <div class="card-description">
            <p>本月协作次数：<strong>120</strong></p>
            <p>较上月增长：<strong>15%</strong></p>
            <p>总协作次数：<strong>720</strong></p>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">文档访问量</div>
        <div class="card-content">
          <WChart
            chartType="line"
            :chartData="{
              data: [300, 400, 350, 450, 500, 600, 700],
              name: '文档访问量',
              seriesOptions: {
                smooth: true,  // 平滑曲线
              },
            }"
            :chartOptions="{
              xAxis: {
                type: 'category',
                data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月'],
                axisLine: {
                  lineStyle: {
                    color: '#5B7DB1',  // X 轴颜色
                  },
                },
              },
              yAxis: {
                type: 'value',
                axisLine: {
                  lineStyle: {
                    color: '#5B7DB1',  // Y 轴颜色
                  },
                },
              },
            }"
            theme="dark"
            :animation="true"
            :animationDuration="1500"
            animationEasing="elasticOut"
          />
          <div class="card-description">
            <p>本月文档访问量：<strong>700</strong></p>
            <p>较上月增长：<strong>16.7%</strong></p>
            <p>总访问量：<strong>4200</strong></p>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">文档分类分布</div>
        <div class="card-content">
          <WChart
            chartType="pie"
            :chartData="{
              data: [
                { value: 1048, name: '技术文档' },
                { value: 735, name: '产品文档' },
                { value: 580, name: '设计文档' },
                { value: 484, name: '市场文档' },
                { value: 300, name: '其他' },
              ],
              name: '文档分类分布',
              seriesOptions: {
                radius: '50%',
                itemStyle: {
                  borderRadius: 10,
                  borderColor: '#fff',
                  borderWidth: 2,
                },
              },
            }"
            :chartOptions="{
              tooltip: {
                trigger: 'item',
              },
            }"
            theme="light"
            :animation="true"
            :animationDuration="2000"
            animationEasing="bounceOut"
          />
          <div class="card-description">
            <p>技术文档占比：<strong>40%</strong></p>
            <p>产品文档占比：<strong>28%</strong></p>
            <p>设计文档占比：<strong>22%</strong></p>
            <p>市场文档占比：<strong>10%</strong></p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import WChart from '../../components/WChart.vue';
</script>

<style scoped>
.stats-view {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.card-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.card {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.card-header {
  padding: 16px;
  font-size: 16px;
  font-weight: bold;
  color: #3A4A7E;
  border-bottom: 1px solid #eee;
}

.card-content {
  padding: 16px;
}

.card-description {
  font-size: 14px;
  color: #666;
}

.card-description p {
  margin: 8px 0;
}

.card-description strong {
  color: #3A4A7E;
}
</style>