<script setup>
import { useAuthStore } from './stores/auth';
import { storeToRefs } from 'pinia';

const authStore = useAuthStore();
const { isAuthenticated, userName, userRole } = storeToRefs(authStore);

const handleLogout = async () => {
  await authStore.logout();
};

// 將角色英文轉換為中文顯示
const getRoleDisplayName = (role) => {
  const roleMap = {
    'ADMIN': '管理員',
    'INVESTOR': '投資者',
    'FOUNDER': '創始人'
  };
  return roleMap[role] || role;
};
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
          <router-link to="/" class="nav-link">
            <i class="fas fa-home"></i>
            首頁
          </router-link>
          <router-link to="/projects" class="nav-link">
            <i class="fas fa-rocket"></i>
            項目列表
          </router-link>
          <!-- <router-link to="/about" class="nav-link">
            <i class="fas fa-info-circle"></i>
            關於我們
          </router-link> -->
        </div>
        
        <div class="nav-auth">
          <template v-if="isAuthenticated">
            <div class="user-info">
              <span class="user-role" :class="'role-' + userRole.toLowerCase()">
                {{ getRoleDisplayName(userRole) }}
              </span>
              <span class="user-name">{{ userName }}</span>
            </div>
            <button @click="handleLogout" class="auth-btn logout">
              <i class="fas fa-sign-out-alt"></i>
              登出
            </button>
          </template>
          <template v-else>
            <router-link to="/login" class="auth-btn login">
              <i class="fas fa-sign-in-alt"></i>
              登入
            </router-link>
            <router-link to="/register" class="auth-btn register">
              <i class="fas fa-user-plus"></i>
              註冊
            </router-link>
          </template>
        </div>
      </nav>
    </header>
    
    <main>
      <router-view />
    </main>
  </div>
</template>

<style>
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css');

.app {
  min-height: 100vh;
  background: #121212;
  width: 100%;
  margin: 0;
  padding: 0;
}

header {
  background: rgba(30, 30, 30, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 4rem;
  height: 70px;
  width: 100%;
  box-sizing: border-box;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 1rem;
  color: #f5f5f5;
  text-decoration: none;
  font-size: 1.3rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.nav-brand:hover {
  transform: translateY(-1px);
}

.nav-logo {
  height: 35px;
  width: 35px;
}

.nav-links {
  display: flex;
  justify-content: center;
  gap: 2.5rem;
  margin: 0 2rem;
}

.nav-link {
  color: #aaa;
  text-decoration: none;
  font-size: 1.1rem;
  font-weight: 500;
  padding: 0.6rem 1.2rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.nav-link i {
  font-size: 1.1rem;
}

.nav-link:hover {
  color: #f5f5f5;
  background: rgba(100, 108, 255, 0.1);
  transform: translateY(-1px);
}

.nav-link.router-link-active {
  color: #646cff;
  background: rgba(100, 108, 255, 0.1);
  font-weight: 600;
}

.nav-auth {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.8rem;
}

.user-role {
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
  color: white;
}

.role-admin {
  background: linear-gradient(90deg, #ff5722, #ff9800);
}

.role-investor {
  background: linear-gradient(90deg, #2196f3, #03a9f4);
}

.role-founder {
  background: linear-gradient(90deg, #8bc34a, #4caf50);
}

.user-name {
  color: #f5f5f5;
  font-size: 1rem;
  font-weight: 500;
}

.auth-btn {
  padding: 0.6rem 1.4rem;
  border-radius: 20px;
  font-size: 1rem;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.auth-btn i {
  font-size: 1rem;
}

.auth-btn.login {
  color: #646cff;
  border: 1px solid #646cff;
}

.auth-btn.login:hover {
  background: rgba(100, 108, 255, 0.1);
  box-shadow: 0 0 20px rgba(100, 108, 255, 0.2);
  transform: translateY(-1px);
}

.auth-btn.register {
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  color: white;
  border: none;
}

.auth-btn.register:hover {
  background: linear-gradient(90deg, #7a80ff, #4a4ab0);
  box-shadow: 0 0 20px rgba(100, 108, 255, 0.4);
  transform: translateY(-1px);
}

.auth-btn.logout {
  background: #ff4d4d;
  color: white;
  border: none;
  cursor: pointer;
}

.auth-btn.logout:hover {
  background: #ff6666;
  box-shadow: 0 0 20px rgba(255, 77, 77, 0.4);
  transform: translateY(-1px);
}

main {
  padding-top: 70px;
  width: 100%;
}

@media (max-width: 1024px) {
  .navbar {
    padding: 0 2rem;
  }
  
  .nav-links {
    gap: 1.5rem;
  }
  
  .nav-link {
    padding: 0.5rem 1rem;
  }
}

@media (max-width: 768px) {
  .navbar {
    padding: 0 1.5rem;
  }
  
  .nav-brand span {
    display: none;
  }
  
  .nav-links {
    gap: 1rem;
  }
  
  .nav-link {
    font-size: 1rem;
    padding: 0.4rem 0.8rem;
  }
  
  .nav-link i {
    font-size: 1rem;
  }
  
  .user-role {
    padding: 0.2rem 0.6rem;
    font-size: 0.75rem;
  }
  
  .user-name {
    font-size: 0.9rem;
  }
  
  .auth-btn {
    padding: 0.5rem 1rem;
    font-size: 0.9rem;
  }
}

@media (max-width: 576px) {
  .navbar {
    padding: 0 1rem;
  }
  
  .nav-links {
    display: none;
  }
  
  .auth-btn i {
    margin-right: 0;
  }
  
  .auth-btn span {
    display: none;
  }
}
</style>
