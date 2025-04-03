<template>
  <div class="home">
    <div class="hero">
      <div class="hero-content">
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
        <h2>平台特色</h2>
        <div class="feature-grid">
          <div class="feature-card">
            <div class="icon">🚀</div>
            <h3>創業加速</h3>
            <p>幫助創業者快速獲得資金支持，加速項目發展</p>
          </div>
          <div class="feature-card">
            <div class="icon">💰</div>
            <h3>智能投資</h3>
            <p>為投資者提供優質項目，實現資金的高效配置</p>
          </div>
          <div class="feature-card">
            <div class="icon">🔍</div>
            <h3>專業評估</h3>
            <p>專業團隊對項目進行全面評估，降低投資風險</p>
          </div>
          <div class="feature-card">
            <div class="icon">🌐</div>
            <h3>全球視野</h3>
            <p>連接全球創業者與投資者，打造國際化平台</p>
          </div>
        </div>
      </div>
    </div>
    
    <div class="how-it-works">
      <div class="container">
        <h2>如何使用</h2>
        <div class="steps">
          <div class="step">
            <div class="step-number">1</div>
            <h3>註冊帳號</h3>
            <p>創建您的個人或企業帳號，完善個人資料</p>
          </div>
          <div class="step">
            <div class="step-number">2</div>
            <h3>瀏覽項目</h3>
            <p>探索平台上的優質創業項目，了解詳情</p>
          </div>
          <div class="step">
            <div class="step-number">3</div>
            <h3>投資或發布</h3>
            <p>投資感興趣的項目，或發布您的創業計劃</p>
          </div>
          <div class="step">
            <div class="step-number">4</div>
            <h3>管理與追蹤</h3>
            <p>在個人儀表板中管理您的投資或項目進展</p>
          </div>
        </div>
      </div>
    </div>
    
    <div class="cta">
      <div class="container">
        <h2>準備好開始了嗎？</h2>
        <p>加入我們的平台，開啟您的創業或投資之旅</p>
        <div class="cta-buttons">
          <router-link to="/register">
            <button class="primary-btn">立即註冊</button>
          </router-link>
          <router-link to="/projects">
            <button class="secondary-btn">瀏覽項目</button>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home {
  color: var(--color-text);
}

.hero {
  height: 80vh;
  min-height: 500px;
  background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)), url('/hero-bg.jpg');
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: white;
}

.hero-content {
  max-width: 800px;
  padding: 0 2rem;
}

.hero h1 {
  font-size: 4rem;
  margin-bottom: 1rem;
  font-weight: 700;
}

.hero p {
  font-size: 1.5rem;
  margin-bottom: 2rem;
  opacity: 0.9;
}

.hero-buttons {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.primary-btn, .secondary-btn {
  padding: 0.8rem 2rem;
  font-size: 1rem;
  border-radius: 30px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.primary-btn {
  background: #646cff;
  color: white;
  border: none;
}

.primary-btn:hover {
  background: #535bf2;
  transform: translateY(-3px);
}

.secondary-btn {
  background: transparent;
  color: white;
  border: 2px solid white;
}

.secondary-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  transform: translateY(-3px);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 5rem 2rem;
}

.features, .how-it-works {
  text-align: center;
}

h2 {
  font-size: 2.5rem;
  margin-bottom: 3rem;
  position: relative;
  display: inline-block;
}

h2:after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 50px;
  height: 3px;
  background: #646cff;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.feature-card {
  background: var(--color-background-soft);
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-10px);
}

.icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

.steps {
  display: flex;
  justify-content: space-between;
  gap: 2rem;
}

.step {
  flex: 1;
  position: relative;
}

.step:not(:last-child):after {
  content: '';
  position: absolute;
  top: 30px;
  right: -1rem;
  width: calc(100% - 60px);
  height: 2px;
  background: #e0e0e0;
}

.step-number {
  width: 60px;
  height: 60px;
  background: #646cff;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: 600;
  margin: 0 auto 1.5rem;
}

.step h3 {
  font-size: 1.3rem;
  margin-bottom: 1rem;
}

.cta {
  background: linear-gradient(135deg, #646cff, #535bf2);
  color: white;
  text-align: center;
}

.cta h2 {
  color: white;
}

.cta h2:after {
  background: white;
}

.cta p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  opacity: 0.9;
}

.cta-buttons {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

@media (max-width: 768px) {
  .hero h1 {
    font-size: 3rem;
  }
  
  .hero p {
    font-size: 1.2rem;
  }
  
  .steps {
    flex-direction: column;
  }
  
  .step:not(:last-child):after {
    display: none;
  }
}
</style> 