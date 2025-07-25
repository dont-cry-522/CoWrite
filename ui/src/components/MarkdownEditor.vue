<template>
  <div ref="editorRoot" />
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import Editor from '@toast-ui/editor'
import vegaEmbed from 'vega-embed'
import '@toast-ui/editor/dist/toastui-editor.css'

const editorRoot = ref(null)
let editorInstance = null
let observer = null

function renderVegaLiteCharts() {
  const preview = editorRoot.value?.querySelector('.toastui-editor-contents')
  if (!preview) return

  // 查找所有 vega-lite 代码块
  preview.querySelectorAll('pre code.language-vega-lite').forEach((block, i) => {
    const code = block.textContent.trim()

    // 创建渲染容器
    const container = document.createElement('div')
    container.id = `vega-lite-${i}-${Date.now()}`
    container.style.minHeight = '300px'

    // 替换 <pre><code> 为图表容器
    const pre = block.closest('pre')
    if (pre) pre.replaceWith(container)

    try {
      const spec = JSON.parse(code)
      vegaEmbed(`#${container.id}`, spec, { actions: false }).catch(err => {
        container.innerHTML = `<pre style="color:red;">❌ Vega 渲染失败: ${err.message}</pre>`
      })
    } catch (err) {
      container.innerHTML = `<pre style="color:red;">❌ JSON 格式错误: ${err.message}</pre>`
    }
  })
}

onMounted(() => {
  // 初始化编辑器
  editorInstance = new Editor({
    el: editorRoot.value,
    height: '600px',
    previewStyle: 'vertical',
    initialEditType: 'markdown',
    initialValue: `# 示例：支持 Vega-Lite 图表

\`\`\`vega-lite
{
  "$schema": "https://vega.github.io/schema/vega-lite/v5.json",
  "description": "测试柱状图",
  "data": {
    "values": [
      { "x": "A", "y": 10 },
      { "x": "B", "y": 50 },
      { "x": "C", "y": 30 }
    ]
  },
  "mark": "bar",
  "encoding": {
    "x": { "field": "x", "type": "ordinal" },
    "y": { "field": "y", "type": "quantitative" }
  }
}
\`\`\`
`
  })

  // 使用 MutationObserver 监听预览区变化
  const preview = editorRoot.value.querySelector('.toastui-editor-contents')
  if (preview) {
    observer = new MutationObserver(() => {
      renderVegaLiteCharts()
    })
    observer.observe(preview, { childList: true, subtree: true })
  }

  // 首次渲染
  setTimeout(() => {
    renderVegaLiteCharts()
  }, 200)
})

onBeforeUnmount(() => {
  if (editorInstance) {
    editorInstance.destroy()
    editorInstance = null
  }
  if (observer) {
    observer.disconnect()
    observer = null
  }
})
</script>
