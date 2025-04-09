<template>
  <div class="auth-container">
    <div class="auth-card">
      <h1>登入</h1>
      <form @submit.prevent="handleLogin" class="auth-form">
        <div class="form-group">
          <label for="email">電子郵件</label>
          <input
            type="email"
            id="email"
            v-model="form.email"
            required
            placeholder="請輸入電子郵件"
          />
        </div>
        
        <div class="form-group">
          <label for="password">密碼</label>
          <input
            type="password"
            id="password"
            v-model="form.password"
            required
            placeholder="請輸入密碼"
          />
        </div>

        <div class="error-message" v-if="error">
          {{ error }}
        </div>

        <button type="submit" class="submit-btn" :disabled="loading">
          {{ loading ? '登入中...' : '登入' }}
        </button>

        <div class="auth-links">
          <router-link to="/register">還沒有帳號？立即註冊</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '../../services/auth';
import type { LoginRequest } from '../../types/user';

const router = useRouter();
const loading = ref(false);
const error = ref('');

const form = ref<LoginRequest>({
  email: '',
  password: ''
});

const handleLogin = async () => {
  loading.value = true;
  error.value = '';
  
  try {
    await authService.login(form.value);
    router.push('/dashboard');
  } catch (err: any) {
    error.value = err.response?.data?.message || '登入失敗，請稍後再試';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.auth-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #121212;
  padding: 2rem;
}

.auth-card {
  background: #1e1e1e;
  border-radius: 12px;
  padding: 2rem;
  width: 100%;
  max-width: 400px;
  border: 1px solid #333;
}

.auth-card h1 {
  color: #f5f5f5;
  margin: 0 0 2rem 0;
  text-align: center;
  font-size: 1.8rem;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  color: #aaa;
  font-size: 0.9rem;
}

.form-group input {
  background: #252525;
  border: 1px solid #333;
  border-radius: 6px;
  padding: 0.8rem 1rem;
  color: #f5f5f5;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.form-group input:focus {
  outline: none;
  border-color: #646cff;
  box-shadow: 0 0 0 2px rgba(100, 108, 255, 0.2);
}

.error-message {
  color: #f44336;
  font-size: 0.9rem;
  margin-top: -0.5rem;
}

.submit-btn {
  background: linear-gradient(90deg, #646cff, #3b3b8f);
  color: white;
  border: none;
  padding: 1rem;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 5px 15px rgba(100, 108, 255, 0.4);
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.auth-links {
  text-align: center;
  margin-top: 1rem;
}

.auth-links a {
  color: #646cff;
  text-decoration: none;
  font-size: 0.9rem;
  transition: color 0.3s ease;
}

.auth-links a:hover {
  color: #7a80ff;
  text-decoration: underline;
}
</style> 