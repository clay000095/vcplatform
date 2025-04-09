<template>
  <div class="how-to-use">
    <h1 class="title">如何使用</h1>
    
    <div class="steps">
      <div class="step">
        <div class="circle">1</div>
        <h3>註冊帳號</h3>
        <p>創建您的個人或企業帳號，完善個人資料</p>
      </div>
      
      <div class="step">
        <div class="circle">2</div>
        <h3>瀏覽項目</h3>
        <p>探索平台上的優質創業項目，了解詳情</p>
      </div>
      
      <div class="step">
        <div class="circle">3</div>
        <h3>投資或發布</h3>
        <p>投資您感興趣的項目，或發布您的創業計劃</p>
      </div>
      
      <div class="step">
        <div class="circle">4</div>
        <h3>管理與追蹤</h3>
        <p>在個人儀表板中管理您的投資或項目進展</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.how-to-use {
  padding: 6rem 2rem;
  background: #121212;
  color: #fff;
}

.title {
  text-align: center;
  font-size: 2.5rem;
  margin-bottom: 5rem;
  font-weight: 600;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  position: relative;
}

.title::after {
  content: '';
  position: absolute;
  bottom: -1rem;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 4px;
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  border-radius: 2px;
}

.steps {
  display: flex;
  justify-content: center;
  gap: 4rem;
  flex-wrap: wrap;
  max-width: 1400px;
  margin: 0 auto;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  width: 220px;
}

.circle {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #646cff, #3b3b8f);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2.5rem;
  font-weight: bold;
  color: white;
  margin-bottom: 1.5rem;
  box-shadow: 0 0 30px rgba(100, 108, 255, 0.3);
  transition: all 0.3s ease;
}

.step:hover .circle {
  transform: translateY(-5px);
  box-shadow: 0 0 40px rgba(100, 108, 255, 0.5);
}

.step h3 {
  font-size: 1.3rem;
  margin: 0 0 0.8rem 0;
  color: #f5f5f5;
}

.step p {
  font-size: 1rem;
  color: #aaa;
  line-height: 1.5;
  margin: 0;
}

@media (max-width: 1200px) {
  .steps {
    gap: 3rem;
  }
  
  .step {
    width: 200px;
  }
  
  .circle {
    width: 90px;
    height: 90px;
    font-size: 2.2rem;
  }
}

@media (max-width: 768px) {
  .how-to-use {
    padding: 4rem 1.5rem;
  }
  
  .title {
    font-size: 2rem;
    margin-bottom: 4rem;
  }
  
  .steps {
    gap: 2.5rem;
  }
  
  .step {
    width: 180px;
  }
  
  .circle {
    width: 80px;
    height: 80px;
    font-size: 2rem;
  }
  
  .step h3 {
    font-size: 1.2rem;
  }
  
  .step p {
    font-size: 0.95rem;
  }
}

@media (max-width: 480px) {
  .how-to-use {
    padding: 3rem 1rem;
  }
  
  .title {
    font-size: 1.8rem;
    margin-bottom: 3rem;
  }
  
  .steps {
    gap: 2rem;
  }
  
  .step {
    width: 160px;
  }
  
  .circle {
    width: 70px;
    height: 70px;
    font-size: 1.8rem;
  }
  
  .step h3 {
    font-size: 1.1rem;
  }
  
  .step p {
    font-size: 0.9rem;
  }
}
</style> 