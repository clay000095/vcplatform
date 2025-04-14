<template>
  <div class="projects-page">
    <div class="category-nav" v-if="!isProjectDetail">
      <router-link 
        to="/projects"
        class="category-link"
        :class="{ active: !currentCategory }"
      >
        全部專案
      </router-link>
      <router-link 
        v-for="cat in categories" 
        :key="cat.value"
        :to="{ name: 'category-projects', params: { category: cat.value }}"
        class="category-link"
        :class="{ active: currentCategory === cat.value }"
      >
        {{ cat.label }}
      </router-link>
    </div>
    
    <router-view></router-view>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const categories = [
  { value: 'TECH', label: '科技創新' },
  { value: 'HEALTH', label: '健康醫療' },
  { value: 'FINANCE', label: '金融科技' },
  { value: 'EDUCATION', label: '教育科技' }
];

const currentCategory = computed(() => {
  return route.params.category || '';
});

const isProjectDetail = computed(() => {
  return route.name === 'project-detail';
});
</script>

<style scoped>
.projects-page {
  min-height: calc(100vh - 60px);
  background: #121212;
  width: 100%;
}

.category-nav {
  background: #1e1e1e;
  padding: 1rem 2rem;
  display: flex;
  justify-content: center;
  gap: 1.5rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  position: sticky;
  top: 60px;
  z-index: 100;
  border-bottom: 1px solid #333;
  max-width: 1800px;
  margin: 0 auto;
}

.category-link {
  padding: 0.6rem 1.5rem;
  border-radius: 20px;
  color: #aaa;
  text-decoration: none;
  transition: all 0.3s ease;
  font-weight: 500;
  position: relative;
  overflow: hidden;
  font-size: 1.1rem;
}

.category-link::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: -1;
  border-radius: 20px;
}

.category-link:hover {
  color: #f5f5f5;
  box-shadow: 0 0 10px rgba(100, 108, 255, 0.2);
}

.category-link:hover::before {
  opacity: 0.1;
}

.category-link.active {
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  color: white;
  box-shadow: 0 0 15px rgba(100, 108, 255, 0.4);
}

.category-link.active::before {
  opacity: 1;
}

@media (max-width: 768px) {
  .category-nav {
    padding: 1rem;
    overflow-x: auto;
    justify-content: flex-start;
    -webkit-overflow-scrolling: touch;
    scrollbar-width: none; /* Firefox */
    -ms-overflow-style: none; /* IE and Edge */
  }
  
  .category-nav::-webkit-scrollbar {
    display: none; /* Chrome, Safari, Opera */
  }
  
  .category-link {
    white-space: nowrap;
    padding: 0.5rem 1.2rem;
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .category-nav {
    padding: 0.8rem;
    gap: 0.8rem;
  }
  
  .category-link {
    padding: 0.4rem 1rem;
    font-size: 0.9rem;
  }
}
</style> 