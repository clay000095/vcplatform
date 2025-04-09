<template>
  <div class="founder-dashboard">
    <div class="dashboard-header">
      <h1>創投管理</h1>
      <router-link to="/projects/new" class="new-project-btn">
        <i class="fas fa-plus"></i>
        新增專案
      </router-link>
    </div>

    <div class="stats-cards">
      <div class="stat-card">
        <h3>總募資金額</h3>
        <p class="amount">{{ formatCurrency(totalRaised) }}</p>
      </div>
      <div class="stat-card">
        <h3>進行中專案</h3>
        <p class="count">{{ activeProjects.length }}</p>
      </div>
      <div class="stat-card">
        <h3>完成專案</h3>
        <p class="count">{{ closedProjects.length }}</p>
      </div>
    </div>

    <div class="projects-section">
      <h2>我的專案</h2>
      <div class="projects-grid">
        <div v-for="project in projects" :key="project.id" class="project-card">
          <div class="project-header">
            <h3>{{ project.name }}</h3>
            <span :class="['status-badge', project.status.toLowerCase()]">
              {{ getStatusText(project.status) }}
            </span>
          </div>
          
          <div class="progress-section">
            <div class="progress-bar">
              <div class="progress" :style="{ width: getProgressPercentage(project) + '%' }"></div>
            </div>
            <div class="progress-stats">
              <span>{{ formatCurrency(project.raisedAmount) }}</span>
              <span>{{ getProgressPercentage(project) }}%</span>
            </div>
          </div>
          
          <div class="project-footer">
            <span class="date">建立於 {{ formatDate(project.createdAt) }}</span>
            <router-link :to="'/projects/' + project.id" class="view-btn">
              查看詳情
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

const projects = ref([]);
const loading = ref(true);

const activeProjects = computed(() => {
  return projects.value.filter(p => p.status === 'active');
});

const closedProjects = computed(() => {
  return projects.value.filter(p => p.status === 'closed');
});

const totalRaised = computed(() => {
  return projects.value.reduce((sum, project) => sum + project.raisedAmount, 0);
});

const fetchProjects = async () => {
  try {
    const response = await axios.get('/api/projects/my', {
      params: {
        founderId: localStorage.getItem('userId')
      }
    });
    projects.value = response.data;
  } catch (error) {
    console.error('Error fetching projects:', error);
  } finally {
    loading.value = false;
  }
};

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('zh-TW', {
    style: 'currency',
    currency: 'TWD',
    minimumFractionDigits: 0
  }).format(amount);
};

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-TW');
};

const getProgressPercentage = (project) => {
  return Math.min(Math.round((project.raisedAmount / project.targetAmount) * 100), 100);
};

const getStatusText = (status) => {
  const statusMap = {
    'active': '進行中',
    'closed': '已結束',
    'pending': '審核中',
    'rejected': '已拒絕'
  };
  return statusMap[status.toLowerCase()] || status;
};

onMounted(() => {
  fetchProjects();
});
</script>

<style scoped>
.founder-dashboard {
  padding: 2rem 4rem;
  background: #121212;
  min-height: calc(100vh - 70px);
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.dashboard-header h1 {
  color: #f5f5f5;
  font-size: 2rem;
  margin: 0;
}

.new-project-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  color: white;
  padding: 0.8rem 1.5rem;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s ease;
}

.new-project-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(100, 108, 255, 0.4);
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.stat-card {
  background: #1e1e1e;
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid #333;
}

.stat-card h3 {
  color: #aaa;
  margin: 0 0 0.5rem 0;
  font-size: 1rem;
}

.stat-card .amount {
  color: #646cff;
  font-size: 1.8rem;
  font-weight: 600;
  margin: 0;
}

.stat-card .count {
  color: #f5f5f5;
  font-size: 1.8rem;
  font-weight: 600;
  margin: 0;
}

.projects-section h2 {
  color: #f5f5f5;
  margin: 0 0 1.5rem 0;
}

.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.project-card {
  background: #1e1e1e;
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid #333;
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: start;
  margin-bottom: 1rem;
}

.project-header h3 {
  color: #f5f5f5;
  margin: 0;
  font-size: 1.2rem;
}

.status-badge {
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
}

.status-badge.active {
  background: rgba(100, 255, 100, 0.1);
  color: #4caf50;
}

.status-badge.closed {
  background: rgba(255, 100, 100, 0.1);
  color: #f44336;
}

.status-badge.pending {
  background: rgba(255, 200, 100, 0.1);
  color: #ffc107;
}

.status-badge.rejected {
  background: rgba(150, 150, 150, 0.1);
  color: #9e9e9e;
}

.progress-section {
  margin: 1.5rem 0;
}

.progress-bar {
  height: 8px;
  background: #333;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 0.5rem;
}

.progress {
  height: 100%;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-stats {
  display: flex;
  justify-content: space-between;
  color: #aaa;
  font-size: 0.9rem;
}

.project-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1rem;
}

.date {
  color: #666;
  font-size: 0.9rem;
}

.view-btn {
  color: #646cff;
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.view-btn:hover {
  color: #7a80ff;
  text-decoration: underline;
}

@media (max-width: 768px) {
  .founder-dashboard {
    padding: 1.5rem;
  }
  
  .dashboard-header {
    flex-direction: column;
    gap: 1rem;
    align-items: flex-start;
  }
  
  .stats-cards {
    grid-template-columns: 1fr;
  }
  
  .projects-grid {
    grid-template-columns: 1fr;
  }
}
</style> 