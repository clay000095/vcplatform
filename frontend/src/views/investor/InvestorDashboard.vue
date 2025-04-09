<template>
  <div class="investor-dashboard">
    <div class="dashboard-header">
      <h1>投資管理</h1>
      <router-link to="/projects" class="browse-btn">
        <i class="fas fa-search"></i>
        瀏覽專案
      </router-link>
    </div>

    <div class="stats-cards">
      <div class="stat-card">
        <h3>總投資金額</h3>
        <p class="amount">{{ formatCurrency(totalInvested) }}</p>
      </div>
      <div class="stat-card">
        <h3>投資專案數</h3>
        <p class="count">{{ investments.length }}</p>
      </div>
      <div class="stat-card">
        <h3>活躍投資</h3>
        <p class="count">{{ activeInvestments.length }}</p>
      </div>
    </div>

    <div class="investments-section">
      <h2>我的投資</h2>
      <div class="investments-grid">
        <div v-for="investment in investments" :key="investment.id" class="investment-card">
          <div class="project-info">
            <h3>{{ investment.project.name }}</h3>
            <span :class="['status-badge', investment.project.status.toLowerCase()]">
              {{ getStatusText(investment.project.status) }}
            </span>
          </div>
          
          <div class="investment-details">
            <div class="detail-item">
              <span class="label">投資金額</span>
              <span class="value">{{ formatCurrency(investment.amount) }}</span>
            </div>
            <div class="detail-item">
              <span class="label">投資佔比</span>
              <span class="value">{{ getInvestmentPercentage(investment) }}%</span>
            </div>
          </div>
          
          <div class="progress-section">
            <div class="progress-bar">
              <div class="progress" :style="{ width: getProjectProgress(investment.project) + '%' }"></div>
            </div>
            <div class="progress-stats">
              <span>專案進度</span>
              <span>{{ getProjectProgress(investment.project) }}%</span>
            </div>
          </div>
          
          <div class="investment-footer">
            <span class="date">投資於 {{ formatDate(investment.createdAt) }}</span>
            <router-link :to="'/projects/' + investment.project.id" class="view-btn">
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

const investments = ref([]);
const loading = ref(true);

const activeInvestments = computed(() => {
  return investments.value.filter(inv => inv.project.status === 'active');
});

const totalInvested = computed(() => {
  return investments.value.reduce((sum, inv) => sum + inv.amount, 0);
});

const fetchInvestments = async () => {
  try {
    const response = await axios.get('/api/investments/my', {
      params: {
        investorId: localStorage.getItem('userId')
      }
    });
    investments.value = response.data;
  } catch (error) {
    console.error('Error fetching investments:', error);
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

const getInvestmentPercentage = (investment) => {
  return Math.round((investment.amount / investment.project.targetAmount) * 100);
};

const getProjectProgress = (project) => {
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
  fetchInvestments();
});
</script>

<style scoped>
.investor-dashboard {
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

.browse-btn {
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

.browse-btn:hover {
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

.investments-section h2 {
  color: #f5f5f5;
  margin: 0 0 1.5rem 0;
}

.investments-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.investment-card {
  background: #1e1e1e;
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid #333;
}

.project-info {
  display: flex;
  justify-content: space-between;
  align-items: start;
  margin-bottom: 1rem;
}

.project-info h3 {
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

.investment-details {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin: 1rem 0;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.detail-item .label {
  color: #aaa;
  font-size: 0.9rem;
}

.detail-item .value {
  color: #f5f5f5;
  font-size: 1.1rem;
  font-weight: 500;
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

.investment-footer {
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
  .investor-dashboard {
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
  
  .investments-grid {
    grid-template-columns: 1fr;
  }
  
  .investment-details {
    grid-template-columns: 1fr;
  }
}
</style> 