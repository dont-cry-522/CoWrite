<template>
  <div class="container">
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
              class="item"
              :style="{ paddingLeft: `${element.level * 1.5}rem` }"
          >
            <div
                v-for="i in element.level"
                :key="i"
                class="level-line"
                :style="{ left: `${(i - 1) * 1.5}rem` }"
            />
            <div
                class="item-content"
                @click="() => emit('select', element)"
                @contextmenu.prevent="openContextMenu($event, element)"
            >
              <ChevronRight class="icon" />
              <FileText class="icon icon-blue" />
              <span class="title">{{ element.title }}</span>
              <button class="add-btn" @click.stop="() => addChild(element)">
                <Plus class="icon small" />
              </button>
            </div>
          </div>
        </transition>
      </template>
    </draggable>

    <div
        v-if="contextMenu.visible"
        class="context-menu"
        :style="{ top: `${contextMenu.y}px`, left: `${contextMenu.x}px` }"
        @click="contextMenu.visible = false"
    >
      <ul>
        <li class="menu-item" @click="renameItem(contextMenu.item)">重命名</li>
        <li class="menu-item" @click="copyLink(contextMenu.item)">复制链接</li>
        <li class="menu-item" @click="deleteItem(contextMenu.item)">删除</li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue'
import draggable from 'vuedraggable'
import { FileText, ChevronRight, Plus } from 'lucide-vue-next'

// 类型定义
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

watch(
    () => props.items,
    (newVal) => {
      localItems.value = [...newVal]
    }
)

watch(localItems, (val) => emit('update:items', val))

// 拖拽记录
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

  // 每移动 24px 水平距离 = 一个缩进层级
  const levelOffset = Math.round(deltaX / 24)

  const newLevel = Math.min(4, Math.max(0, moved.level + levelOffset))
  moved.level = newLevel
}

// 控制是否允许移动（默认允许）
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

// 菜单功能
const renameItem = (item: DocumentItem | null) => {
  if (!item) return
  const newTitle = prompt('请输入新标题', item.title)
  if (newTitle) item.title = newTitle
  contextMenu.value.visible = false
}

const copyLink = (item: DocumentItem | null) => {
  if (!item) return
  const url = `${window.location.origin}/docs/${item.id}`
  navigator.clipboard.writeText(url).then(() => alert('已复制链接'))
  contextMenu.value.visible = false
}

const deleteItem = (item: DocumentItem | null) => {
  if (!item) return
  localItems.value = localItems.value.filter((i) => i.id !== item.id)
  contextMenu.value.visible = false
}
</script>

<style scoped>
.container {
  position: relative;
}
.item {
  position: relative;
  user-select: none;
  transition: all 0.2s;
}
.level-line {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 1px;
  background-color: #e5e7eb;
}
.item-content {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-bottom: 4px;
  padding: 6px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}
.item-content:hover {
  background-color: #f1f1f1;
}
.title {
  font-size: 14px;
  font-weight: 500;
}
.add-btn {
  margin-left: auto;
  background: none;
  border: none;
  color: #9ca3af;
  cursor: pointer;
}
.add-btn:hover {
  color: #3b82f6;
}
.icon {
  width: 12px;
  height: 12px;
  color: #d1d5db;
}
.icon-blue {
  color: #3b82f6;
}
.icon.small {
  width: 10px;
  height: 10px;
}
.context-menu {
  position: absolute;
  z-index: 50;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
  font-size: 14px;
  padding: 4px 0;
}
.menu-item {
  padding: 6px 16px;
  cursor: pointer;
}
.menu-item:hover {
  background-color: #f5f5f5;
}
.drag-ghost {
  opacity: 0.6;
  background-color: #f0f9ff;
  transform: scale(1.02);
  transition: all 0.2s ease-in-out;
}
.drag-chosen {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border: 1px solid #c3dafe;
  background-color: #ebf4ff;
}
.list-enter-active,
.list-leave-active {
  transition: all 0.3s ease;
}
.list-enter-from {
  opacity: 0;
  transform: translateY(-6px);
}
.list-leave-to {
  opacity: 0;
  transform: translateY(6px);
}
</style>

