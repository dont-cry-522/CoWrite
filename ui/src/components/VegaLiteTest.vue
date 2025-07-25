<template>
  <div ref="vegaContainer" style="width: 100%; height: 400px;" />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import vegaEmbed from 'vega-embed'

const vegaContainer = ref(null)

const spec = {
  $schema: 'https://vega.github.io/schema/vega-lite/v5.json',
  description: '柱状图示例',
  data: {
    values: [
      { category: 'A', value: 28 },
      { category: 'B', value: 55 },
      { category: 'C', value: 43 }
    ]
  },
  mark: 'bar',
  encoding: {
    x: { field: 'category', type: 'ordinal' },
    y: { field: 'value', type: 'quantitative' }
  }
}

onMounted(() => {
  vegaEmbed(vegaContainer.value, spec, { actions: false })
      .then(() => console.log('✅ Vega 图成功渲染'))
      .catch(err => {
        console.error('❌ Vega 渲染失败:', err)
      })
})
</script>
