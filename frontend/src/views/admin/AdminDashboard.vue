<template>
  <div class="admin-dashboard">
    <div class="dashboard-header">
      <h1>管理員控制台</h1>
      <div class="header-actions">
        <button class="action-btn" @click="activeTab = 'users'">
          <i class="fas fa-users"></i>
          用戶管理
        </button>
        <button class="action-btn" @click="activeTab = 'projects'">
          <i class="fas fa-project-diagram"></i>
          專案管理
        </button>
      </div>
    </div>

    <div class="stats-cards">
      <div class="stat-card">
        <h3>總用戶數</h3>
        <p class="count">{{ users.length }}</p>
        <div class="stat-details">
          <span>創投: {{ founderCount }}</span>
          <span>投資者: {{ investorCount }}</span>
        </div>
      </div>
      <div class="stat-card">
        <h3>總專案數</h3>
        <p class="count">{{ projects.length }}</p>
        <div class="stat-details">
          <span>進行中: {{ activeProjectCount }}</span>
          <span>已結束: {{ closedProjectCount }}</span>
        </div>
      </div>
      <div class="stat-card">
        <h3>總交易額</h3>
        <p class="amount">{{ formatCurrency(totalTransactions) }}</p>
      </div>
    </div>

    <!-- 用戶管理標籤 -->
    <div v-if="activeTab === 'users'" class="section">
      <div class="section-header">
        <h2>用戶列表</h2>
        <div class="filters">
          <select v-model="userFilter" class="filter-select">
            <option value="all">全部用戶</option>
            <option value="founder">創投</option>
            <option value="investor">投資者</option>
          </select>
        </div>
      </div>

      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>信箱</th>
              <th>角色</th>
              <th>註冊時間</th>
              <th>狀態</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in filteredUsers" :key="user.id">
              <td>{{ user.id }}</td>
              <td>{{ user.email }}</td>
              <td>
                <span :class="['role-badge', user.role.toLowerCase()]">
                  {{ getRoleText(user.role) }}
                </span>
              </td>
              <td>{{ formatDate(user.createdAt) }}</td>
              <td>
                <span :class="['status-badge', user.status ? 'active' : 'inactive']">
                  {{ user.status ? '正常' : '停用' }}
                </span>
              </td>
              <td>
                <button class="table-btn view" @click="viewUserDetails(user)">
                  <i class="fas fa-eye"></i>
                </button>
                <button class="table-btn" :class="user.status ? 'danger' : 'success'"
                        @click="toggleUserStatus(user)">
                  <i class="fas" :class="user.status ? 'fa-ban' : 'fa-check'"></i>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 專案管理標籤 -->
    <div v-if="activeTab === 'projects'" class="section">
      <div class="section-header">
        <h2>專案列表</h2>
        <div class="filters">
          <select v-model="projectFilter" class="filter-select">
            <option value="all">全部專案</option>
            <option value="pending">待審核</option>
            <option value="active">進行中</option>
            <option value="closed">已結束</option>
          </select>
        </div>
      </div>

      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>專案名稱</th>
              <th>創建者</th>
              <th>目標金額</th>
              <th>已募集</th>
              <th>狀態</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="project in filteredProjects" :key="project.id">
              <td>{{ project.id }}</td>
              <td>{{ project.name }}</td>
              <td>{{ project.founderEmail }}</td>
              <td>{{ formatCurrency(project.targetAmount) }}</td>
              <td>{{ formatCurrency(project.raisedAmount) }}</td>
              <td>
                <span :class="['status-badge', project.status.toLowerCase()]">
                  {{ getStatusText(project.status) }}
                </span>
              </td>
              <td>
                <button class="table-btn view" @click="viewProjectDetails(project)">
                  <i class="fas fa-eye"></i>
                </button>
                <button v-if="project.status === 'PENDING'"
                        class="table-btn success" @click="approveProject(project)">
                  <i class="fas fa-check"></i>
                </button>
                <button v-if="project.status === 'PENDING'"
                        class="table-btn danger" @click="rejectProject(project)">
                  <i class="fas fa-times"></i>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const activeTab = ref('users');
const users = ref([]);
const projects = ref([]);
const userFilter = ref('all');
const projectFilter = ref('all');
const loading = ref(true);

// 計算屬性
const founderCount = computed(() => {
  return users.value.filter(u => u.role === 'FOUNDER').length;
});

const investorCount = computed(() => {
  return users.value.filter(u => u.role === 'INVESTOR').length;
});

const activeProjectCount = computed(() => {
  return projects.value.filter(p => p.status === 'ACTIVE').length;
});

const closedProjectCount = computed(() => {
  return projects.value.filter(p => p.status === 'CLOSED').length;
});

const totalTransactions = computed(() => {
  return projects.value.reduce((sum, project) => sum + project.raisedAmount, 0);
});

const filteredUsers = computed(() => {
  if (userFilter.value === 'all') return users.value;
  return users.value.filter(user => user.role.toLowerCase() === userFilter.value);
});

const filteredProjects = computed(() => {
  if (projectFilter.value === 'all') return projects.value;
  return projects.value.filter(project => project.status.toLowerCase() === projectFilter.value);
});

// 方法
const fetchData = async () => {
  try {
    const [usersResponse, projectsResponse] = await Promise.all([
      axios.get('/api/admin/users'),
      axios.get('/api/admin/projects')
    ]);
    users.value = usersResponse.data;
    projects.value = projectsResponse.data;
  } catch (error) {
    console.error('Error fetching admin data:', error);
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

const getRoleText = (role) => {
  const roleMap = {
    'FOUNDER': '創投',
    'INVESTOR': '投資者',
    'ADMIN': '管理員'
  };
  return roleMap[role] || role;
};

const getStatusText = (status) => {
  const statusMap = {
    'ACTIVE': '進行中',
    'CLOSED': '已結束',
    'PENDING': '待審核',
    'REJECTED': '已拒絕'
  };
  return statusMap[status] || status;
};

const viewUserDetails = (user) => {
  // 實現查看用戶詳情的邏輯
};

const toggleUserStatus = async (user) => {
  try {
    await axios.post(`/api/admin/users/${user.id}/toggle-status`);
    user.status = !user.status;
  } catch (error) {
    console.error('Error toggling user status:', error);
  }
};

const viewProjectDetails = (project) => {
  // 實現查看專案詳情的邏輯
};

const approveProject = async (project) => {
  try {
    await axios.post(`/api/admin/projects/${project.id}/approve`);
    project.status = 'ACTIVE';
  } catch (error) {
    console.error('Error approving project:', error);
  }
};

const rejectProject = async (project) => {
  try {
    await axios.post(`/api/admin/projects/${project.id}/reject`);
    project.status = 'REJECTED';
  } catch (error) {
    console.error('Error rejecting project:', error);
  }
};

onMounted(() => {
  fetchData();
});
</script>

<style scoped>
.admin-dashboard {
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

.header-actions {
  display: flex;
  gap: 1rem;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: #1e1e1e;
  color: #f5f5f5;
  border: 1px solid #333;
  padding: 0.8rem 1.5rem;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-btn:hover {
  background: #2a2a2a;
  transform: translateY(-1px);
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

.stat-card .count {
  color: #f5f5f5;
  font-size: 1.8rem;
  font-weight: 600;
  margin: 0 0 0.5rem 0;
}

.stat-card .amount {
  color: #646cff;
  font-size: 1.8rem;
  font-weight: 600;
  margin: 0 0 0.5rem 0;
}

.stat-details {
  display: flex;
  justify-content: space-between;
  color: #666;
  font-size: 0.9rem;
}

.section {
  margin-top: 2rem;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.section-header h2 {
  color: #f5f5f5;
  margin: 0;
}

.filters {
  display: flex;
  gap: 1rem;
}

.filter-select {
  background: #1e1e1e;
  border: 1px solid #333;
  color: #f5f5f5;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  font-size: 0.9rem;
}

.table-container {
  background: #1e1e1e;
  border-radius: 12px;
  border: 1px solid #333;
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th {
  background: #252525;
  color: #aaa;
  font-weight: 500;
  text-align: left;
  padding: 1rem;
}

td {
  padding: 1rem;
  color: #f5f5f5;
  border-top: 1px solid #333;
}

.role-badge,
.status-badge {
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
}

.role-badge.founder {
  background: rgba(100, 108, 255, 0.1);
  color: #646cff;
}

.role-badge.investor {
  background: rgba(100, 255, 100, 0.1);
  color: #4caf50;
}

.role-badge.admin {
  background: rgba(255, 100, 100, 0.1);
  color: #f44336;
}

.status-badge.active {
  background: rgba(100, 255, 100, 0.1);
  color: #4caf50;
}

.status-badge.inactive {
  background: rgba(255, 100, 100, 0.1);
  color: #f44336;
}

.status-badge.pending {
  background: rgba(255, 200, 100, 0.1);
  color: #ffc107;
}

.table-btn {
  background: none;
  border: none;
  color: #aaa;
  padding: 0.4rem;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.table-btn:hover {
  transform: translateY(-1px);
}

.table-btn.view:hover {
  color: #646cff;
}

.table-btn.success:hover {
  color: #4caf50;
}

.table-btn.danger:hover {
  color: #f44336;
}

@media (max-width: 768px) {
  .admin-dashboard {
    padding: 1.5rem;
  }
  
  .dashboard-header {
    flex-direction: column;
    gap: 1rem;
  }
  
  .header-actions {
    width: 100%;
  }
  
  .action-btn {
    flex: 1;
    justify-content: center;
  }
  
  .stats-cards {
    grid-template-columns: 1fr;
  }
  
  .section-header {
    flex-direction: column;
    gap: 1rem;
  }
  
  .filters {
    width: 100%;
  }
  
  .filter-select {
    width: 100%;
  }
}
</style> 