<template>
  <div class="projects">
    <h1>探索優質項目</h1>
    
    <div class="filters">
      <div class="search-bar">
        <input 
          v-model="searchQuery" 
          type="text" 
          placeholder="搜索項目..."
          @input="handleSearch"
        >
      </div>
      
      <div class="category-filters">
        <button 
          v-for="cat in categories" 
          :key="cat.value"
          :class="['category-btn', { active: selectedCategory === cat.value }]"
          @click="filterByCategory(cat.value)"
        >
          {{ cat.label }}
        </button>
      </div>
    </div>

    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>載入中...</p>
    </div>
    
    <div v-else-if="error" class="error">
      <p>{{ error }}</p>
      <button @click="fetchProjects" class="retry-btn">重試</button>
    </div>
    
    <div v-else-if="projects.length === 0" class="empty">
      <p>暫無項目</p>
    </div>
    
    <div v-else class="project-list">
      <div v-for="project in projects" 
           :key="project.id" 
           class="project-card"
           @click="viewProject(project.id)">
        <div class="project-image">
          <img :src="project.image || '/default-project.jpg'" :alt="project.name">
          <span class="status-tag" :class="getStatusClass(project.status)">
            {{ projectStatus[project.status] }}
          </span>
        </div>
        
        <div class="project-content">
          <h2>{{ project.name }}</h2>
          <p class="description">{{ project.description }}</p>
          
          <div class="progress-section">
            <div class="progress-bar">
              <div class="progress" :style="{ width: getProgressPercentage(project) + '%' }"></div>
            </div>
            <div class="progress-info">
              <div class="target">
                <span class="amount">¥{{ formatNumber(project.targetAmount) }}</span>
                <span class="label">目標金額</span>
              </div>
              <div class="raised">
                <span class="amount">¥{{ formatNumber(project.raisedAmount || 0) }}</span>
                <span class="label">已籌集</span>
              </div>
              <div class="percentage">
                <span class="amount">{{ getProgressPercentage(project) }}%</span>
                <span class="label">達成率</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { projectService } from '../api/services'

const router = useRouter()
const projects = ref([])
const loading = ref(true)
const error = ref(null)
const searchQuery = ref('')
const selectedCategory = ref('ALL')

const categories = [
  { value: 'ALL', label: '全部' },
  { value: 'TECH', label: '科技' },
  { value: 'HEALTH', label: '健康' },
  { value: 'FINANCE', label: '金融' },
  { value: 'EDUCATION', label: '教育' },
  { value: 'OTHER', label: '其他' }
]

const projectStatus = {
  PENDING: '審核中',
  APPROVED: '募資中',
  COMPLETED: '已完成',
  FAILED: '未達標'
}

const getStatusClass = (status) => {
  return `status-${status.toLowerCase()}`
}

const getProgressPercentage = (project) => {
  if (!project.targetAmount || project.targetAmount === 0) return 0
  const percentage = (project.raisedAmount || 0) / project.targetAmount * 100
  return Math.min(Math.round(percentage), 100)
}

const formatNumber = (num) => {
  return num ? num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") : "0"
}

const fetchProjects = async () => {
  loading.value = true
  error.value = null
  
  try {
    const response = await projectService.getApprovedProjects()
    projects.value = response.data
  } catch (err) {
    console.error('Failed to fetch projects:', err)
    error.value = err.response?.data?.message || '取得專案失敗'
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  // 實現搜索邏輯
  fetchProjects()
}

const filterByCategory = (category) => {
  selectedCategory.value = category
  fetchProjects()
}

const viewProject = (id) => {
  router.push(`/projects/${id}`)
}

onMounted(fetchProjects)
</script>

<style scoped>
.projects {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

h1 {
  font-size: 2.5rem;
  text-align: center;
  margin-bottom: 2rem;
  color: var(--color-heading);
}

.filters {
  margin-bottom: 2rem;
}

.search-bar {
  max-width: 600px;
  margin: 0 auto 1.5rem;
}

.search-bar input {
  width: 100%;
  padding: 1rem 1.5rem;
  border: 2px solid #eee;
  border-radius: 30px;
  font-size: 1rem;
  transition: all 0.3s;
}

.search-bar input:focus {
  outline: none;
  border-color: #646cff;
  box-shadow: 0 0 0 3px rgba(100, 108, 255, 0.1);
}

.category-filters {
  display: flex;
  justify-content: center;
  gap: 1rem;
  flex-wrap: wrap;
}

.category-btn {
  padding: 0.5rem 1.5rem;
  border: 2px solid #eee;
  border-radius: 20px;
  background: white;
  color: #666;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.category-btn:hover {
  border-color: #646cff;
  color: #646cff;
}

.category-btn.active {
  background: #646cff;
  border-color: #646cff;
  color: white;
}

.project-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
}

.project-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
  cursor: pointer;
}

.project-card:hover {
  transform: translateY(-5px);
}

.project-image {
  position: relative;
  height: 200px;
}

.project-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.status-tag {
  position: absolute;
  top: 1rem;
  right: 1rem;
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
  color: white;
}

.status-pending { background: #ffd700; }
.status-approved { background: #4CAF50; }
.status-completed { background: #2196F3; }
.status-failed { background: #f44336; }

.project-content {
  padding: 1.5rem;
}

.project-content h2 {
  font-size: 1.25rem;
  margin-bottom: 0.5rem;
  color: #333;
}

.description {
  color: #666;
  margin-bottom: 1.5rem;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.progress-bar {
  height: 8px;
  background: #eee;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 1rem;
}

.progress {
  height: 100%;
  background: #646cff;
  transition: width 0.3s ease;
}

.progress-info {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
  text-align: center;
}

.amount {
  display: block;
  font-weight: 600;
  color: #333;
  font-size: 1.1rem;
}

.label {
  display: block;
  color: #666;
  font-size: 0.9rem;
  margin-top: 0.2rem;
}

.loading {
  text-align: center;
  padding: 4rem 0;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 5px solid #eee;
  border-top-color: #646cff;
  border-radius: 50%;
  margin: 0 auto 1rem;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.error, .empty {
  text-align: center;
  padding: 4rem 0;
  color: #666;
}

.retry-btn {
  margin-top: 1rem;
  padding: 0.8rem 2rem;
  background: #646cff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s;
}

.retry-btn:hover {
  background: #535bf2;
}

@media (max-width: 768px) {
  .project-list {
    grid-template-columns: 1fr;
  }
  
  .category-filters {
    flex-wrap: nowrap;
    overflow-x: auto;
    padding-bottom: 1rem;
    -webkit-overflow-scrolling: touch;
  }
  
  .category-btn {
    flex-shrink: 0;
  }
}
</style> 