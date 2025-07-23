<template>
  <div class="outline-editor">
    <draggable
        v-model="localItems"
        item-key="id"
        group="docs"
        @start="onDragStart"
        @change="onDragChange"
        :move="onMove"
        ghost-class="drag-ghost"
        chosen-class="drag-chosen"
        animation="200"
    >
      <template #item="{ element }">
        <transition name="list" mode="out-in">
          <div
              :key="element.id"
              class="outline-item"
              :style="{ paddingLeft: `${element.level * 1.75}rem` }"
          >
            <div
                v-for="i in element.level"
                :key="i"
                class="level-indicator"
                :style="{ left: `${(i - 1) * 1.75}rem` }"
            />
            <div
                class="item-content"
                @click="() => emit('select', element)"
                @contextmenu.prevent="openContextMenu($event, element)"
            >
              <ChevronRight class="icon arrow" />
              <FileText class="icon doc-icon" />
              <span class="title">{{ element.title }}</span>
              <button class="add-btn" @click.stop="() => addChild(element)">
                <Plus class="icon plus-icon" />
              </button>
            </div>
          </div>
        </transition>
      </template>
    </draggable>

    <!-- 上下文菜单 -->
    <div
        v-if="contextMenu.visible"
        class="context-menu"
        :style="{ top: `${contextMenu.y}px`, left: `${contextMenu.x}px` }"
        @click.stop="contextMenu.visible = false"
    >
      <ul>
        <li class="menu-item" @click="renameItem(contextMenu.item)">
          <i class="iconfont icon-edit" /> 重命名
        </li>
        <li class="menu-item" @click="copyLink(contextMenu.item)">
          <i class="iconfont icon-link" /> 复制链接
        </li>
        <li class="menu-item danger" @click="deleteItem(contextMenu.item)">
          <i class="iconfont icon-delete" /> 删除
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue'
import draggable from 'vuedraggable'
import { FileText, ChevronRight, Plus } from 'lucide-vue-next'

interface DocumentItem {
  id: string
  title: string
  level: number
  parentId: string | null
}

const props = defineProps<{
  items: DocumentItem[]
  documents: DocumentItem[]
}>()

const emit = defineEmits<{
  (e: 'update:items', value: DocumentItem[]): void
  (e: 'select', doc: DocumentItem): void
}>()

const localItems = ref<DocumentItem[]>([...props.items])

watch(() => props.items, (newVal) => {
  localItems.value = [...newVal]
})

watch(localItems, (val) => emit('update:items', val))

// 拖拽逻辑
let dragStartX = 0
const onDragStart = (e: DragEvent) => {
  dragStartX = e.clientX
}

const onDragChange = (e: any) => {
  const { oldIndex, newIndex } = e.moved
  const clientX = e.originalEvent?.clientX ?? dragStartX
  const deltaX = clientX - dragStartX

  const moved = localItems.value.splice(oldIndex, 1)[0]
  localItems.value.splice(newIndex, 0, moved)

  const levelOffset = Math.round(deltaX / 28) // 调整缩进灵敏度
  const newLevel = Math.min(4, Math.max(0, moved.level + levelOffset))
  moved.level = newLevel
}

const onMove = () => true

// 添加子节点
const addChild = (parent: DocumentItem) => {
  const newItem: DocumentItem = {
    id: Date.now().toString(),
    title: '新建子文档',
    level: Math.min(parent.level + 1, 4),
    parentId: parent.id,
  }
  const index = localItems.value.findIndex((i) => i.id === parent.id)
  localItems.value.splice(index + 1, 0, newItem)
}

// 右键菜单
const contextMenu = ref({
  visible: false,
  x: 0,
  y: 0,
  item: null as DocumentItem | null,
})

const openContextMenu = (event: MouseEvent, item: DocumentItem) => {
  contextMenu.value = {
    visible: true,
    x: event.clientX,
    y: event.clientY,
    item,
  }
}

const renameItem = (item: DocumentItem | null) => {
  if (!item) return
  const newTitle = prompt('请输入新标题', item.title)
  if (newTitle) item.title = newTitle
  contextMenu.value.visible = false
}

const copyLink = (item: DocumentItem | null) => {
  if (!item) return
  const url = `${window.location.origin}/docs/${item.id}`
  navigator.clipboard.writeText(url).then(() => {
    console.log('链接已复制')
  })
  contextMenu.value.visible = false
}

const deleteItem = (item: DocumentItem | null) => {
  if (!item) return
  if (confirm(`确定要删除 "${item.title}" 吗？`)) {
    localItems.value = localItems.value.filter((i) => i.id !== item.id)
  }
  contextMenu.value.visible = false
}
</script>

<style scoped>
.outline-editor {
  position: relative;
  padding: 0.5rem;
  background-color: #faf9ff;
  border-radius: 12px;
  height: calc(100vh - 4rem); /* 确保高度适应布局 */
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #d9cdf9 transparent;
}

.outline-editor::-webkit-scrollbar {
  width: 6px;
}

.outline-editor::-webkit-scrollbar-thumb {
  background-color: #d9cdf9;
  border-radius: 3px;
}

.outline-item {
  position: relative;
  user-select: none;
  transition: all 0.2s ease;
  margin-bottom: 2px;
}

.level-indicator {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 1px;
  background-color: #e8e0f5;
  transition: background-color 0.2s ease;
}

.outline-item:hover .level-indicator {
  background-color: #d9cdf9;
}

.item-content {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  background-color: white;
  box-shadow: 0 1px 2px rgba(148, 108, 230, 0.05);
  border: 1px solid #f0e9ff;
}

.item-content:hover {
  background-color: #f8f5ff;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(148, 108, 230, 0.1);
  border-color: #e0d4f9;
}

.icon {
  width: 14px;
  height: 14px;
  flex-shrink: 0;
  transition: all 0.2s ease;
}

.arrow {
  color: #c0b1e0;
  transform: rotate(0deg);
}

.item-content:hover .arrow {
  color: #946ce6;
}

.doc-icon {
  color: #946ce6;
}

.title {
  flex: 1;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.add-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  background: none;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  opacity: 0;
  transition: all 0.2s ease;
}

.outline-item:hover .add-btn {
  opacity: 1;
}

.plus-icon {
  color: #9ca3af;
}

.add-btn:hover .plus-icon {
  color: #946ce6;
}

/* 拖拽样式 */
.drag-ghost {
  opacity: 0.8;
  background-color: #f3eeff;
  transform: scale(1.02);
  box-shadow: 0 4px 12px rgba(148, 108, 230, 0.15);
  border: 1px solid #d9cdf9 !important;
}

.drag-chosen {
  background-color: #f0e9ff;
  box-shadow: 0 4px 12px rgba(148, 108, 230, 0.1);
  border: 1px solid #d9cdf9;
}

/* 上下文菜单 */
.context-menu {
  position: fixed;
  z-index: 1000;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  padding: 8px 0;
  min-width: 160px;
  border: 1px solid #f0e9ff;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 8px 16px;
  font-size: 13px;
  color: #333;
  cursor: pointer;
  transition: all 0.2s ease;
}

.menu-item i {
  margin-right: 8px;
  font-size: 14px;
  color: #946ce6;
}

.menu-item:hover {
  background-color: #f8f5ff;
  color: #946ce6;
}

.menu-item.danger:hover {
  color: #ff4d4f;
  background-color: #fff1f0;
}

.menu-item.danger i {
  color: #ff4d4f;
}

/* 过渡动画 */
.list-enter-active,
.list-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.list-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}

.list-leave-to {
  opacity: 0;
  transform: translateY(10px);
}
</style>