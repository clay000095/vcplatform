<template>
  <div class="project-detail" v-if="project">
    <div class="project-header">
      <div class="container">
        <h1>{{ project.name }}</h1>
        <div class="meta">
          <span :class="['status', getStatusClass(project.status)]">
            {{ projectStatus[project.status] }}
          </span>
          <span class="category">{{ getCategoryName(project.category) }}</span>
          <span class="creator">提案人：{{ project.creator }}</span>
        </div>
      </div>
    </div>

    <div class="container">
      <div class="project-content">
        <div class="main-content">
          <div class="project-image">
            <img :src="project.image || '/default-project.jpg'" :alt="project.name">
          </div>
          
          <div class="project-info">
            <h2>項目介紹</h2>
            <p>{{ project.description }}</p>
            
            <h2>項目進度</h2>
            <div class="progress-section">
              <div class="progress-bar">
                <div class="progress" :style="{ width: getProgressPercentage(project) + '%' }"></div>
              </div>
              <div class="progress-stats">
                <div class="stat">
                  <span class="value">¥{{ formatNumber(project.raisedAmount || 0) }}</span>
                  <span class="label">已籌集</span>
                </div>
                <div class="stat">
                  <span class="value">{{ getProgressPercentage(project) }}%</span>
                  <span class="label">達成率</span>
                </div>
                <div class="stat">
                  <span class="value">{{ project.daysLeft || 0 }}</span>
                  <span class="label">剩餘天數</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="side-content">
          <div class="investment-card">
            <h3>投資此項目</h3>
            <div class="investment-form">
              <div class="form-group">
                <label>投資金額</label>
                <input 
                  type="number" 
                  v-model="investmentAmount" 
                  placeholder="請輸入投資金額"
                  min="1"
                >
              </div>
              <button 
                class="invest-btn" 
                @click="handleInvest"
                :disabled="!investmentAmount || loading"
              >
                {{ loading ? '處理中...' : '立即投資' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  
  <div v-else-if="loading" class="loading">
    <div class="spinner"></div>
    <p>載入中...</p>
  </div>
  
  <div v-else class="error">
    <p>{{ error || '項目不存在' }}</p>
    <router-link to="/projects">
      <button>返回項目列表</button>
    </router-link>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { projectService, investmentService } from '../api/services';

const route = useRoute();
const router = useRouter();
const project = ref(null);
const loading = ref(true);
const error = ref(null);
const investmentAmount = ref('');

const projectStatus = {
  PENDING: '審核中',
  APPROVED: '募資中',
  COMPLETED: '已完成',
  FAILED: '未達標'
};

const getStatusClass = (status) => {
  return {
    'status-pending': status === 'PENDING',
    'status-approved': status === 'APPROVED',
    'status-completed': status === 'COMPLETED',
    'status-failed': status === 'FAILED'
  };
};

const getCategoryName = (category) => {
  const categories = {
    'TECH': '科技',
    'HEALTH': '健康',
    'FINANCE': '金融',
    'EDUCATION': '教育',
    'OTHER': '其他'
  };
  return categories[category] || category;
};

const getProgressPercentage = (project) => {
  if (!project.targetAmount || project.targetAmount === 0) return 0;
  const percentage = (project.raisedAmount || 0) / project.targetAmount * 100;
  return Math.min(Math.round(percentage), 100);
};

const formatNumber = (num) => {
  return num ? num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") : "0";
};

const fetchProject = async () => {
  loading.value = true;
  error.value = null;
  
  try {
    const response = await projectService.getProjectById(route.params.id);
    project.value = response.data;
  } catch (err) {
    console.error('Failed to fetch project:', err);
    error.value = err.response?.data?.message || '獲取項目失敗';
  } finally {
    loading.value = false;
  }
};

const handleInvest = async () => {
  if (!investmentAmount.value) return;
  
  loading.value = true;
  try {
    await investmentService.createInvestment({
      projectId: project.value.id,
      amount: Number(investmentAmount.value)
    });
    // 重新獲取項目資訊以更新進度
    await fetchProject();
    investmentAmount.value = '';
  } catch (err) {
    error.value = err.response?.data?.message || '投資失敗';
  } finally {
    loading.value = false;
  }
};

onMounted(fetchProject);
</script>

<style scoped>
.project-detail {
  padding-bottom: 4rem;
}

.project-header {
  background: var(--color-background-soft);
  padding: 2rem 0;
  margin-bottom: 2rem;
}

.project-header h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  color: var(--color-heading);
}

.meta {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.status, .category {
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.9rem;
}

.creator {
  color: var(--color-text-light);
}

.project-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 2rem;
}

.project-image {
  margin-bottom: 2rem;
  border-radius: 12px;
  overflow: hidden;
}

.project-image img {
  width: 100%;
  height: auto;
  object-fit: cover;
}

.project-info h2 {
  font-size: 1.5rem;
  margin: 2rem 0 1rem;
  color: var(--color-heading);
}

.progress-section {
  margin-top: 1rem;
}

.progress-bar {
  height: 8px;
  background: rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 1rem;
}

.progress {
  height: 100%;
  background: var(--color-primary);
  transition: width 0.3s ease;
}

.progress-stats {
  display: flex;
  justify-content: space-between;
}

.stat {
  text-align: center;
}

.value {
  display: block;
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--color-heading);
}

.label {
  font-size: 0.9rem;
  color: var(--color-text-light);
}

.investment-card {
  background: var(--color-background-soft);
  padding: 1.5rem;
  border-radius: 12px;
  position: sticky;
  top: 2rem;
}

.investment-card h3 {
  margin-bottom: 1.5rem;
  color: var(--color-heading);
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: var(--color-text);
}

input {
  width: 100%;
  padding: 0.8rem;
  border: 2px solid var(--color-border);
  border-radius: 6px;
  font-size: 1rem;
  transition: all 0.3s;
}

input:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(100, 108, 255, 0.1);
}

.invest-btn {
  width: 100%;
  padding: 1rem;
  background: var(--color-primary);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.invest-btn:hover:not(:disabled) {
  background: var(--color-primary-hover);
}

.invest-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading {
  text-align: center;
  padding: 4rem 0;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 5px solid rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  border-top-color: var(--color-primary);
  margin: 0 auto 1rem;
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.error {
  text-align: center;
  padding: 4rem 0;
}

@media (max-width: 768px) {
  .project-content {
    grid-template-columns: 1fr;
  }
  
  .investment-card {
    position: static;
    margin-top: 2rem;
  }
}
</style> 