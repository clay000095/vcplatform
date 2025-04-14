<template>
  <div class="project-detail-page">
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>載入中...</p>
    </div>
    
    <div v-else-if="error" class="error">
      <p>{{ error }}</p>
      <button @click="fetchProject" class="retry-btn">重試</button>
    </div>
    
    <div v-else-if="!project" class="no-project">
      <p>找不到該項目</p>
      <router-link to="/projects" class="back-btn">返回項目列表</router-link>
    </div>
    
    <div v-else class="project-container">
      <div class="project-header">
        <div class="category-tag">{{ getCategoryName(project.category) }}</div>
        <h1>{{ project.title }}</h1>
        <p class="project-id">#{{ project.id }}</p>
      </div>
      
      <div class="project-content">
        <div class="project-description">
          <h2>項目簡介</h2>
          <p>{{ project.description }}</p>
        </div>
        
        <div class="project-info-card">
          <div class="info-item">
            <span class="label">目標金額</span>
            <span class="value">${{ formatNumber(project.targetAmount) }}</span>
          </div>
          
          <div class="info-item">
            <span class="label">當前金額</span>
            <span class="value">${{ formatNumber(project.currentAmount) }}</span>
          </div>
          
          <div class="progress-container">
            <span class="label">募資進度</span>
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: getProgressPercentage(project) + '%' }"></div>
            </div>
            <span class="progress-text">{{ getProgressPercentage(project) }}%</span>
          </div>
          
          <button class="invest-btn">投資此項目</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { projectService } from '../api/services';

const route = useRoute();
const router = useRouter();
const project = ref(null);
const loading = ref(true);
const error = ref(null);

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

const fetchProject = async () => {
  loading.value = true;
  error.value = null;
  
  try {
    const projectId = route.params.id;
    const response = await projectService.getProjectById(projectId);
    project.value = response.data;
  } catch (err) {
    console.error('Failed to fetch project:', err);
    error.value = err.response?.data?.message || '取得項目詳情失敗';
  } finally {
    loading.value = false;
  }
};

onMounted(fetchProject);
</script>

<style scoped>
.project-detail-page {
  padding: 2rem;
  background-color: #121212;
  min-height: calc(100vh - 60px);
  color: #f5f5f5;
  width: 100%;
}

.loading, .error, .no-project {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  text-align: center;
}

.spinner {
  border: 4px solid rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  border-top: 4px solid #646cff;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.retry-btn, .back-btn {
  margin-top: 1rem;
  padding: 0.6rem 1.5rem;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  border: none;
  border-radius: 20px;
  color: white;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.retry-btn:hover, .back-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(100, 108, 255, 0.3);
}

.project-container {
  max-width: 1200px;
  margin: 0 auto;
}

.project-header {
  margin-bottom: 2rem;
  text-align: center;
}

.category-tag {
  display: inline-block;
  padding: 0.4rem 1rem;
  background: rgba(100, 108, 255, 0.2);
  color: #646cff;
  border-radius: 20px;
  font-size: 0.9rem;
  margin-bottom: 1rem;
}

.project-header h1 {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.project-id {
  color: #aaa;
  font-size: 1rem;
}

.project-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 2rem;
}

.project-description {
  background: #1e1e1e;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.project-description h2 {
  margin-bottom: 1rem;
  color: #f5f5f5;
}

.project-description p {
  line-height: 1.6;
  color: #ddd;
}

.project-info-card {
  background: #1e1e1e;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  height: fit-content;
}

.info-item {
  margin-bottom: 1.5rem;
}

.info-item .label {
  display: block;
  color: #aaa;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.info-item .value {
  font-size: 1.5rem;
  font-weight: 600;
  color: #f5f5f5;
}

.progress-container {
  margin-bottom: 1.5rem;
}

.progress-container .label {
  display: block;
  color: #aaa;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.progress-bar {
  height: 10px;
  background: #333;
  border-radius: 5px;
  overflow: hidden;
  margin-bottom: 0.5rem;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  border-radius: 5px;
}

.progress-text {
  font-size: 1rem;
  color: #f5f5f5;
}

.invest-btn {
  width: 100%;
  padding: 1rem;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  border: none;
  border-radius: 5px;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 1rem;
}

.invest-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(100, 108, 255, 0.3);
}

@media (max-width: 768px) {
  .project-content {
    grid-template-columns: 1fr;
  }
}
</style> 