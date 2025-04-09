<template>
  <div class="project-list-page">
    <div class="category-header">
      <h2>全部專案</h2>
      <p class="category-description">發現具有潛力的創業項目，為您的投資創造價值</p>
    </div>
    
    <div class="search-container">
      <div class="search-bar">
        <input 
          v-model="searchQuery" 
          type="text" 
          placeholder="搜索項目..."
          @input="handleSearch"
        >
        <div class="search-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="11" cy="11" r="8"></circle>
            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
          </svg>
        </div>
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
    
    <div v-else-if="projects.length === 0" class="no-projects">
      <p>暫無項目</p>
    </div>
    
    <div v-else class="projects-grid">
      <div v-for="project in projects" 
           :key="project.id" 
           class="project-card"
           @click="viewProject(project.id)">
        <div class="card-header">
          <div class="category-tag">{{ getCategoryName(project.category) }}</div>
          <div class="card-id">#{{ project.id }}</div>
        </div>
        <h3>{{ project.title }}</h3>
        <p class="description">{{ project.description }}</p>
        <div class="project-info">
          <div class="amount">
            <span class="label">目標金額</span>
            <span class="value">${{ formatNumber(project.targetAmount) }}</span>
          </div>
          <div class="progress">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: getProgressPercentage(project) + '%' }"></div>
            </div>
            <span class="progress-text">{{ getProgressPercentage(project) }}%</span>
          </div>
        </div>
        <button class="invest-btn">查看詳情</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { projectService } from '../api/services';

const router = useRouter();
const projects = ref([]);
const loading = ref(true);
const error = ref(null);
const searchQuery = ref('');

const getProgressPercentage = (project) => {
  if (!project.targetAmount || project.targetAmount === 0) return 0;
  const percentage = (project.currentAmount / project.targetAmount) * 100;
  return Math.min(Math.round(percentage), 100);
};

const formatNumber = (num) => {
  return num ? num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") : "0";
};

const getCategoryName = (category) => {
  const categories = {
    'TECH': '科技創新',
    'HEALTH': '健康醫療',
    'FINANCE': '金融科技',
    'EDUCATION': '教育科技'
  };
  return categories[category] || category;
};

const fetchProjects = async () => {
  loading.value = true;
  error.value = null;
  
  try {
    const response = await projectService.getApprovedProjects();
    projects.value = response.data;
  } catch (err) {
    console.error('Failed to fetch projects:', err);
    error.value = err.response?.data?.message || '取得專案失敗';
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
  // 實現搜索邏輯
  fetchProjects();
};

const viewProject = (id) => {
  router.push(`/projects/${id}`);
};

onMounted(fetchProjects);
</script>

<style scoped>
.project-list-page {
  padding: 2rem;
  background-color: #121212;
  min-height: calc(100vh - 60px);
  color: #f5f5f5;
  width: 100%;
}

.category-header {
  text-align: center;
  margin-bottom: 3rem;
  padding: 2rem 0;
}

.category-header h2 {
  font-size: 2.5rem;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 1rem;
  text-transform: uppercase;
  letter-spacing: 2px;
  font-weight: 700;
}

.category-description {
  color: #f5f5f5;
  font-size: 1.2rem;
  max-width: 800px;
  margin: 0 auto;
  line-height: 1.6;
}

.search-container {
  max-width: 600px;
  margin: 0 auto 3rem;
}

.search-bar {
  position: relative;
  width: 100%;
}

.search-bar input {
  width: 100%;
  padding: 1rem 1.5rem 1rem 3rem;
  background-color: #1e1e1e;
  border: 1px solid #333;
  border-radius: 30px;
  font-size: 1.1rem;
  color: #f5f5f5;
  transition: all 0.3s;
}

.search-bar input:focus {
  outline: none;
  border-color: #646cff;
  box-shadow: 0 0 0 3px rgba(100, 108, 255, 0.2);
}

.search-icon {
  position: absolute;
  left: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: #646cff;
}

.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
  max-width: 1800px;
  margin: 0 auto;
  padding: 0 2rem;
}

.project-card {
  background: #1e1e1e;
  border-radius: 12px;
  padding: 1.8rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid #333;
  position: relative;
  overflow: hidden;
}

.project-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.project-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 30px rgba(100, 108, 255, 0.2);
  border-color: #646cff;
}

.project-card:hover::before {
  opacity: 1;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.category-tag {
  background: rgba(100, 108, 255, 0.2);
  color: #646cff;
  padding: 0.4rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: bold;
  letter-spacing: 1px;
}

.card-id {
  color: #666;
  font-size: 0.9rem;
  font-family: monospace;
  background: rgba(102, 102, 102, 0.1);
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
}

.project-card h3 {
  color: #f5f5f5;
  margin-bottom: 1.2rem;
  font-size: 1.5rem;
  font-weight: 600;
  line-height: 1.4;
}

.description {
  color: #f5f5f5;
  margin-bottom: 1.8rem;
  line-height: 1.6;
  font-size: 1.1rem;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.project-info {
  margin-bottom: 1.8rem;
  background: rgba(100, 108, 255, 0.05);
  padding: 1.2rem;
  border-radius: 8px;
}

.amount {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.2rem;
}

.label {
  color: #f5f5f5;
  font-size: 1.1rem;
}

.value {
  font-weight: bold;
  color: #646cff;
  font-size: 1.3rem;
}

.progress {
  margin-top: 1rem;
}

.progress-bar {
  height: 8px;
  background: #333;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 0.8rem;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 1rem;
  color: #f5f5f5;
  font-weight: 500;
}

.invest-btn {
  width: 100%;
  padding: 1rem;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-size: 1.1rem;
}

.invest-btn:hover {
  background: linear-gradient(90deg, #7a80ff, #4a4ab0);
  box-shadow: 0 0 20px rgba(100, 108, 255, 0.4);
  transform: translateY(-2px);
}

.loading {
  text-align: center;
  padding: 4rem 2rem;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 5px solid #333;
  border-top-color: #646cff;
  border-radius: 50%;
  margin: 0 auto 1rem;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.error, .no-projects {
  text-align: center;
  padding: 4rem 2rem;
  color: #f5f5f5;
  font-size: 1.2rem;
}

.retry-btn {
  margin-top: 1rem;
  padding: 1rem 2rem;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: bold;
  font-size: 1.1rem;
}

.retry-btn:hover {
  background: linear-gradient(90deg, #7a80ff, #4a4ab0);
  box-shadow: 0 0 20px rgba(100, 108, 255, 0.4);
  transform: translateY(-2px);
}

/* 響應式設計 */
@media (max-width: 1600px) {
  .projects-grid {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }
}

@media (max-width: 768px) {
  .project-list-page {
    padding: 1.5rem;
  }
  
  .category-header {
    padding: 1.5rem 0;
  }
  
  .category-header h2 {
    font-size: 2rem;
  }
  
  .category-description {
    font-size: 1.1rem;
    padding: 0 1rem;
  }
  
  .search-container {
    margin-bottom: 2rem;
  }
  
  .search-bar input {
    font-size: 1rem;
    padding: 0.8rem 1.5rem 0.8rem 2.8rem;
  }
  
  .projects-grid {
    grid-template-columns: 1fr;
    padding: 0 1rem;
    gap: 1.5rem;
  }
  
  .project-card {
    padding: 1.5rem;
  }
  
  .project-card h3 {
    font-size: 1.3rem;
  }
  
  .description {
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .project-list-page {
    padding: 1rem;
  }
  
  .category-header {
    padding: 1rem 0;
  }
  
  .category-header h2 {
    font-size: 1.8rem;
  }
  
  .category-description {
    font-size: 1rem;
  }
  
  .search-container {
    margin-bottom: 1.5rem;
  }
  
  .search-bar input {
    font-size: 0.9rem;
    padding: 0.7rem 1.2rem 0.7rem 2.5rem;
  }
  
  .projects-grid {
    padding: 0;
  }
  
  .project-card {
    padding: 1.2rem;
  }
  
  .project-card h3 {
    font-size: 1.2rem;
  }
  
  .description {
    font-size: 0.95rem;
  }
  
  .label, .value {
    font-size: 1rem;
  }
  
  .invest-btn {
    padding: 0.8rem;
    font-size: 1rem;
  }
}
</style> 