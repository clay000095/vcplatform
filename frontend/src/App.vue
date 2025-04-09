<script setup>
</script>

<template>
  <div class="app">
    <header>
      <nav class="navbar">
        <router-link to="/" class="nav-brand">
          <img src="/logo.svg" alt="VC Platform" class="nav-logo" />
          <span>VC Platform</span>
        </router-link>
        <div class="nav-links">
          <router-link to="/projects">項目列表</router-link>
        </div>
        <div class="nav-auth">
          <router-link to="/login" class="auth-btn login">登入</router-link>
          <router-link to="/register" class="auth-btn register">註冊</router-link>
        </div>
      </nav>
    </header>
    
    <main>
      <router-view />
    </main>
  </div>
</template>

<style>
.app {
  min-height: 100vh;
  background: #121212;
  width: 100%;
  margin: 0;
  padding: 0;
}

header {
  background: #1e1e1e;
  border-bottom: 1px solid #333;
  position: sticky;
  top: 0;
  z-index: 1000;
  width: 100%;
}

.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 4rem;
  height: 60px;
  width: 100%;
  box-sizing: border-box;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 1rem;
  color: #f5f5f5;
  text-decoration: none;
  font-size: 1.2rem;
  font-weight: 600;
}

.nav-logo {
  height: 32px;
  width: 32px;
}

.nav-links {
  display: flex;
  justify-content: center;
  flex: 1;
  margin: 0 2rem;
}

.nav-links a {
  color: #aaa;
  text-decoration: none;
  font-size: 1.1rem;
  padding: 0.5rem 1.5rem;
  border-radius: 6px;
  transition: all 0.3s;
}

.nav-links a:hover {
  color: #f5f5f5;
  background: rgba(100, 108, 255, 0.1);
}

.nav-links a.router-link-active {
  color: #646cff;
  font-weight: 600;
}

.nav-auth {
  display: flex;
  gap: 1rem;
}

.auth-btn {
  padding: 0.5rem 1.2rem;
  border-radius: 20px;
  font-size: 1rem;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.3s;
}

.auth-btn.login {
  color: #646cff;
  border: 1px solid #646cff;
}

.auth-btn.login:hover {
  background: rgba(100, 108, 255, 0.1);
  box-shadow: 0 0 15px rgba(100, 108, 255, 0.2);
}

.auth-btn.register {
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  color: white;
  border: none;
}

.auth-btn.register:hover {
  background: linear-gradient(90deg, #7a80ff, #4a4ab0);
  box-shadow: 0 0 15px rgba(100, 108, 255, 0.4);
  transform: translateY(-1px);
}

main {
  width: 100%;
}

@media (max-width: 768px) {
  .navbar {
    padding: 0 2rem;
  }
  
  .nav-brand span {
    display: none;
  }
  
  .nav-links {
    margin: 0 1rem;
  }
  
  .nav-links a {
    padding: 0.4rem 1rem;
    font-size: 1rem;
  }
  
  .auth-btn {
    padding: 0.4rem 1rem;
    font-size: 0.9rem;
  }
}

@media (max-width: 480px) {
  .navbar {
    padding: 0 1rem;
  }
  
  .nav-links {
    margin: 0 0.5rem;
  }
  
  .nav-links a {
    padding: 0.3rem 0.8rem;
    font-size: 0.9rem;
  }
  
  .auth-btn {
    padding: 0.3rem 0.8rem;
    font-size: 0.85rem;
  }
}
</style>
