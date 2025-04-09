<template>
  <div class="category-projects">
    <div class="category-header">
      <h2>{{ categoryTitle }}</h2>
      <p class="category-description">{{ categoryDescription }}</p>
    </div>
    
    <div class="projects-grid" v-if="projects.length">
      <div v-for="project in projects" :key="project.id" class="project-card">
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
        <button class="invest-btn" @click="viewProject(project.id)">查看詳情</button>
      </div>
    </div>
    
    <div v-else class="no-projects">
      <p>暫無{{ categoryTitle }}類別的專案</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { projectService } from '../api/services';

const props = defineProps({
  category: {
    type: String,
    required: true
  }
});

const router = useRouter();
const projects = ref([]);
const loading = ref(false);
const error = ref(null);

const categoryTitle = computed(() => {
  const titles = {
    'TECH': '科技創新',
    'HEALTH': '健康醫療',
    'FINANCE': '金融科技',
    'EDUCATION': '教育科技'
  };
  return titles[props.category] || props.category;
});

const categoryDescription = computed(() => {
  const descriptions = {
    'TECH': '探索前沿科技',
    'HEALTH': '關注健康醫療',
    'FINANCE': '創新金融服務',
    'EDUCATION': '革新教育方式'
  };
  return descriptions[props.category] || '';
});

const formatNumber = (num) => {
  return new Intl.NumberFormat().format(num);
};

const getProgressPercentage = (project) => {
  if (!project.targetAmount) return 0;
  return Math.round((project.currentAmount / project.targetAmount) * 100);
};

const viewProject = (id) => {
  router.push(`/projects/${id}`);
};

const fetchProjects = async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await projectService.getProjectsByCategory(props.category);
    projects.value = response.data;
  } catch (err) {
    error.value = '獲取專案列表失敗';
    console.error('Error fetching projects:', err);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchProjects();
});
</script>

<style scoped>
.category-projects {
  padding: 2rem;
}

.category-header {
  text-align: center;
  margin-bottom: 3rem;
}

.category-header h2 {
  font-size: 2rem;
  color: #2c3e50;
  margin-bottom: 1rem;
}

.category-description {
  color: #666;
  font-size: 1.1rem;
}

.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
}

.project-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.project-card:hover {
  transform: translateY(-5px);
}

.project-card h3 {
  color: #2c3e50;
  margin-bottom: 1rem;
  font-size: 1.3rem;
}

.description {
  color: #666;
  margin-bottom: 1.5rem;
  line-height: 1.5;
}

.project-info {
  margin-bottom: 1.5rem;
}

.amount {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.label {
  color: #666;
}

.value {
  font-weight: bold;
  color: #2c3e50;
}

.progress {
  margin-top: 1rem;
}

.progress-bar {
  height: 8px;
  background: #eee;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 0.5rem;
}

.progress-fill {
  height: 100%;
  background: #42b983;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 0.9rem;
  color: #666;
}

.invest-btn {
  width: 100%;
  padding: 0.8rem;
  background: #42b983;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s;
}

.invest-btn:hover {
  background: #3aa876;
}

.no-projects {
  text-align: center;
  padding: 3rem;
  color: #666;
}

@media (max-width: 768px) {
  .projects-grid {
    grid-template-columns: 1fr;
  }
  
  .category-projects {
    padding: 1rem;
  }
}
</style> 