<template>
  <div class="category-projects">
    <div class="category-header">
      <h2>{{ categoryTitle }}</h2>
      <p class="category-description">{{ categoryDescription }}</p>
    </div>
    
    <div class="projects-grid" v-if="projects.length">
      <div v-for="project in projects" 
           :key="project.id" 
           class="project-card"
           @click="viewProject(project.id)">
        <div class="card-header">
          <div class="category-tag">{{ categoryTitle }}</div>
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
    
    <div v-else class="no-projects">
      <p>暫無{{ categoryTitle }}類別的專案</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
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
  return num ? num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") : "0";
};

const getProgressPercentage = (project) => {
  if (!project.targetAmount || project.targetAmount === 0) return 0;
  const percentage = (project.currentAmount / project.targetAmount) * 100;
  return Math.min(Math.round(percentage), 100);
};

const viewProject = (id) => {
  router.push(`/projects/${id}`);
};

const fetchProjects = async () => {
  loading.value = true;
  error.value = null;
  try {
    console.log('Fetching projects for category:', props.category);
    const response = await projectService.getProjectsByCategory(props.category);
    console.log('API Response:', response.data);
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

// 監聽 category 參數變化
watch(() => props.category, (newCategory, oldCategory) => {
  if (newCategory !== oldCategory) {
    console.log('Category changed from', oldCategory, 'to', newCategory);
    fetchProjects();
  }
});
</script>

<style scoped>
.category-projects {
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

.no-projects {
  text-align: center;
  padding: 4rem 2rem;
  color: #f5f5f5;
  font-size: 1.2rem;
}

/* 響應式設計 */
@media (max-width: 1600px) {
  .projects-grid {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }
}

@media (max-width: 768px) {
  .category-projects {
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
  .category-projects {
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