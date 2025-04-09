<template>
  <div class="projects-page">
    <div class="category-nav">
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
</script>

<style scoped>
.projects-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.category-nav {
  background: white;
  padding: 1rem;
  display: flex;
  justify-content: center;
  gap: 1rem;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.category-link {
  padding: 0.5rem 1rem;
  border-radius: 20px;
  color: #666;
  text-decoration: none;
  transition: all 0.3s ease;
}

.category-link:hover {
  background: #f0f0f0;
  color: #42b983;
}

.category-link.active {
  background: #42b983;
  color: white;
}

@media (max-width: 768px) {
  .category-nav {
    overflow-x: auto;
    padding: 1rem;
    justify-content: flex-start;
  }
  
  .category-link {
    white-space: nowrap;
  }
}
</style> 