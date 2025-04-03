<template>
  <div class="projects">
    <h1>項目列表項目列表</h1>
    
    <div v-if="loading" class="loading">loading...</div>
    
    <div v-else-if="error" class="error">
      <p>{{ error }}</p>
      <button @click="fetchProjects">重試</button>
    </div>
    
    <div v-else-if="projects.length === 0" class="empty">
      <p>暫無項目</p>
    </div>
    
    <div v-else class="project-list">
      <div v-for="project in projects" :key="project.id" class="project-card">
        <h2>{{ project.name }}</h2>
        <p>{{ project.description }}</p>
        <div class="project-info">
          <span>目標: ¥{{ project.targetAmount }}</span>
          <span>已籌至: ¥{{ project.raisedAmount || 0 }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const projects = ref([])
const loading = ref(true)
const error = ref(null)

const fetchProjects = async () => {
  loading.value = true
  error.value = null
  
  try {
    const response = await axios.get('http://localhost:8080/api/projects')
    projects.value = response.data
  } catch (err) {
    console.error('Failed to fetch projects:', err)
    error.value = err.message || '取得專案失敗'
  } finally {
    loading.value = false
  }
}

onMounted(fetchProjects)
</script>

<style scoped>
.projects {
  text-align: center;
}

h1 {
  margin-bottom: 2rem;
}

.loading, .error, .empty {
  padding: 2rem;
}

.project-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
  text-align: left;
}

.project-card {
  background: #1a1a1a;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.2);
}

.project-card h2 {
  margin-bottom: 0.5rem;
}

.project-card p {
  margin-bottom: 1rem;
  opacity: 0.8;
}

.project-info {
  display: flex;
  justify-content: space-between;
  color: #646cff;
  font-weight: bold;
}
</style> 