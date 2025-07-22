<template>
  <div class="documents">
    <h1>Documents</h1>
    <div class="document-list">
      <div class="document-item" v-for="doc in documents" :key="doc.id">
        <h2>{{ doc.title }}</h2>
        <p>{{ doc.content }}</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
export default {
  name: 'Documents',
  data() {
    return {
      documents: [
        { id: 1, title: 'Document 1', content: 'This is the content of document 1.' },
        { id: 2, title: 'Document 2', content: 'This is the content of document 2.' },
        { id: 3, title: 'Document 3', content: 'This is the content of document 3.' },
      ],
    };
  },
};
</script>

<style scoped>
.documents {
  padding: 2rem;
}

.document-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.document-item {
  background-color: #f9f9f9;
  padding: 1rem;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.document-item h2 {
  margin: 0 0 0.5rem 0;
}

.document-item p {
  margin: 0;
}
</style> 