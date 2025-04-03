<template>
  <div class="home">
    <div class="hero">
      <div class="hero-content" v-motion-slide-visible-once-bottom>
        <h1>VC平台</h1>
        <p>連結創業者與投資者的橋樑</p>
        <div class="hero-buttons">
          <router-link to="/projects">
            <button class="primary-btn">瀏覽項目</button>
          </router-link>
          <router-link to="/register">
            <button class="secondary-btn">立即註冊</button>
          </router-link>
        </div>
      </div>
    </div>
    
    <div class="features">
      <div class="container">
        <h2 v-motion-slide-visible-once-bottom>平台特色</h2>
        <div class="feature-grid">
          <div class="feature-card" 
               v-for="(feature, index) in features" 
               :key="index"
               v-motion-slide-visible-once-bottom
               :delay="index * 100">
            <div class="icon">{{ feature.icon }}</div>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
          </div>
        </div>
      </div>
    </div>
    
    <div class="how-it-works">
      <div class="container">
        <h2 v-motion-slide-visible-once-bottom>如何使用</h2>
        <div class="steps">
          <div class="step" 
               v-for="(step, index) in steps" 
               :key="index"
               v-motion-slide-visible-once-bottom
               :delay="index * 200">
            <div class="step-number">{{ index + 1 }}</div>
            <h3>{{ step.title }}</h3>
            <p>{{ step.description }}</p>
          </div>
        </div>
      </div>
    </div>
    
    <div class="cta">
      <div class="container" v-motion-slide-visible-once-bottom>
        <h2>準備好開始了嗎？</h2>
        <p>加入我們的平台，開啟您的創業或投資之旅</p>
        <div class="cta-buttons">
          <router-link to="/register">
            <button class="primary-btn animate-pulse">立即註冊</button>
          </router-link>
          <router-link to="/projects">
            <button class="secondary-btn">瀏覽項目</button>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const features = ref([
  {
    icon: '🚀',
    title: '創業加速',
    description: '幫助創業者快速獲得資金支持，加速項目發展'
  },
  {
    icon: '💰',
    title: '智能投資',
    description: '為投資者提供優質項目，實現資金的高效配置'
  },
  {
    icon: '🔍',
    title: '專業評估',
    description: '專業團隊對項目進行全面評估，降低投資風險'
  },
  {
    icon: '🌐',
    title: '全球視野',
    description: '連接全球創業者與投資者，打造國際化平台'
  }
])

const steps = ref([
  {
    title: '註冊帳號',
    description: '創建您的個人或企業帳號，完善個人資料'
  },
  {
    title: '瀏覽項目',
    description: '探索平台上的優質創業項目，了解詳情'
  },
  {
    title: '投資或發布',
    description: '投資感興趣的項目，或發布您的創業計劃'
  },
  {
    title: '管理與追蹤',
    description: '在個人儀表板中管理您的投資或項目進展'
  }
])
</script>

<style scoped>
.home {
  color: var(--color-text);
}

.hero {
  height: 80vh;
  min-height: 500px;
  background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)), 
              url('https://images.unsplash.com/photo-1553729459-efe14ef6055d?auto=format&fit=crop&w=2070&q=80');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: white;
  position: relative;
  overflow: hidden;
}

.hero::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, rgba(100,108,255,0.3), rgba(83,91,242,0.3));
  z-index: 1;
}

.hero-content {
  max-width: 800px;
  padding: 0 2rem;
  position: relative;
  z-index: 2;
  animation: fadeInUp 1s ease-out;
}

.hero h1 {
  font-size: 4.5rem;
  margin-bottom: 1rem;
  font-weight: 700;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
}

.hero p {
  font-size: 1.8rem;
  margin-bottom: 2rem;
  opacity: 0.9;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.3);
}

.hero-buttons {
  display: flex;
  gap: 1.5rem;
  justify-content: center;
}

.primary-btn, .secondary-btn {
  padding: 1rem 2.5rem;
  font-size: 1.1rem;
  border-radius: 30px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.primary-btn {
  background: #646cff;
  color: white;
  border: none;
  box-shadow: 0 4px 15px rgba(100,108,255,0.3);
}

.primary-btn:hover {
  background: #535bf2;
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(100,108,255,0.4);
}

.secondary-btn {
  background: transparent;
  color: white;
  border: 2px solid white;
  backdrop-filter: blur(5px);
}

.secondary-btn:hover {
  background: rgba(255,255,255,0.1);
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(255,255,255,0.2);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 6rem 2rem;
}

.features, .how-it-works {
  text-align: center;
  position: relative;
}

h2 {
  font-size: 2.8rem;
  margin-bottom: 4rem;
  position: relative;
  display: inline-block;
  color: var(--color-heading);
}

h2:after {
  content: '';
  position: absolute;
  bottom: -15px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 4px;
  background: #646cff;
  border-radius: 2px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 2.5rem;
}

.feature-card {
  background: var(--color-background-soft);
  padding: 2.5rem;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.05);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(255,255,255,0.1);
}

.feature-card:hover {
  transform: translateY(-10px) scale(1.02);
  box-shadow: 0 8px 30px rgba(0,0,0,0.1);
}

.icon {
  font-size: 3.5rem;
  margin-bottom: 1.5rem;
  transition: transform 0.3s ease;
}

.feature-card:hover .icon {
  transform: scale(1.1);
}

.feature-card h3 {
  font-size: 1.6rem;
  margin-bottom: 1rem;
  color: var(--color-heading);
}

.steps {
  display: flex;
  justify-content: space-between;
  gap: 3rem;
  position: relative;
}

.step {
  flex: 1;
  position: relative;
  padding: 2rem;
}

.step:not(:last-child):after {
  content: '';
  position: absolute;
  top: 30px;
  right: -1.5rem;
  width: calc(100% - 60px);
  height: 3px;
  background: linear-gradient(90deg, #646cff, #535bf2);
  opacity: 0.3;
}

.step-number {
  width: 70px;
  height: 70px;
  background: linear-gradient(135deg, #646cff, #535bf2);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.8rem;
  font-weight: 600;
  margin: 0 auto 1.5rem;
  box-shadow: 0 4px 15px rgba(100,108,255,0.3);
  transition: transform 0.3s ease;
}

.step:hover .step-number {
  transform: scale(1.1) rotate(360deg);
}

.step h3 {
  font-size: 1.4rem;
  margin-bottom: 1rem;
  color: var(--color-heading);
}

.cta {
  background: linear-gradient(135deg, #646cff, #535bf2);
  color: white;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.cta::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg width="20" height="20" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><circle cx="2" cy="2" r="2" fill="white" opacity="0.1"/></svg>') repeat;
  z-index: 1;
}

.cta .container {
  position: relative;
  z-index: 2;
}

.cta h2 {
  color: white;
  margin-bottom: 2rem;
}

.cta h2:after {
  background: white;
}

.cta p {
  font-size: 1.3rem;
  margin-bottom: 2.5rem;
  opacity: 0.9;
}

.cta-buttons {
  display: flex;
  gap: 1.5rem;
  justify-content: center;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(100,108,255,0.4);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(100,108,255,0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(100,108,255,0);
  }
}

.animate-pulse {
  animation: pulse 2s infinite;
}

@media (max-width: 768px) {
  .hero h1 {
    font-size: 3rem;
  }
  
  .hero p {
    font-size: 1.3rem;
  }
  
  .steps {
    flex-direction: column;
  }
  
  .step:not(:last-child):after {
    display: none;
  }
  
  .container {
    padding: 4rem 1.5rem;
  }
  
  .feature-grid {
    grid-template-columns: 1fr;
  }
  
  h2 {
    font-size: 2.2rem;
  }
}

@media (prefers-reduced-motion: reduce) {
  * {
    animation: none !important;
    transition: none !important;
  }
}
</style> 