<template>
  <div class="register">
    <div class="register-container">
      <div class="register-header">
        <h1>註冊帳號</h1>
        <p>加入我們的平台，開啟您的創業或投資之旅</p>
      </div>
      
      <form @submit.prevent="handleSubmit" class="register-form">
        <div class="form-group">
          <label for="email">電子郵件</label>
          <input 
            type="email" 
            id="email" 
            v-model="form.email" 
            :class="{ 'error': errors.email }"
            placeholder="請輸入您的電子郵件"
            required
          >
          <span class="error-message" v-if="errors.email">{{ errors.email }}</span>
        </div>

        <div class="form-group">
          <label for="password">密碼</label>
          <div class="password-input">
            <input 
              :type="showPassword ? 'text' : 'password'" 
              id="password" 
              v-model="form.password" 
              :class="{ 'error': errors.password }"
              placeholder="請輸入密碼"
              required
            >
            <button 
              type="button" 
              class="toggle-password"
              @click="showPassword = !showPassword"
            >
              <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
            </button>
          </div>
          <span class="error-message" v-if="errors.password">{{ errors.password }}</span>
        </div>

        <div class="form-group">
          <label for="role">註冊身份</label>
          <div class="role-selector">
            <div 
              class="role-option" 
              :class="{ 'selected': form.role === 'FOUNDER' }"
              @click="form.role = 'FOUNDER'"
            >
              <i class="fas fa-rocket"></i>
              <span>創業者</span>
            </div>
            <div 
              class="role-option" 
              :class="{ 'selected': form.role === 'INVESTOR' }"
              @click="form.role = 'INVESTOR'"
            >
              <i class="fas fa-chart-line"></i>
              <span>投資者</span>
            </div>
          </div>
        </div>
<!-- 
        <div class="form-group terms">
          <label class="checkbox-container">
            <input 
              type="checkbox" 
              v-model="form.agreeTerms"
              required
            >
            <span class="checkmark"></span>
            我已閱讀並同意 <a href="#" @click.prevent="showTerms">服務條款</a> 和 <a href="#" @click.prevent="showPrivacy">隱私政策</a>
          </label>
        </div> -->

        <button type="submit" class="submit-btn" :disabled="isLoading">
          <span v-if="!isLoading">註冊</span>
          <span v-else class="loading-spinner"></span>
        </button>

        <div class="login-link">
          已有帳號？ <router-link to="/login">立即登入</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import api from '../../api/config'

const router = useRouter()
const showPassword = ref(false)
const isLoading = ref(false)

const form = reactive({
  email: '',
  password: '',
  role: 'FOUNDER',
  agreeTerms: false
})

const errors = reactive({
  email: '',
  password: ''
})

const validateForm = () => {
  let isValid = true
  errors.email = ''
  errors.password = ''

  if (!form.email.trim()) {
    errors.email = '請輸入電子郵件'
    isValid = false
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
    errors.email = '請輸入有效的電子郵件地址'
    isValid = false
  }

  if (!form.password) {
    errors.password = '請輸入密碼'
    isValid = false

  }

  return isValid
}

const handleSubmit = async () => {
  if (!validateForm()) return

  try {
    isLoading.value = true
    const response = await api.post('/auth/register', {
      email: form.email,
      password: form.password,
      role: form.role
    })

    if (response.data) {
      router.push('/login')
    }
  } catch (error) {
    if (error.response?.data?.message) {
      alert(error.response.data.message)
    } else {
      alert('註冊失敗，請稍後再試')
    }
  } finally {
    isLoading.value = false
  }
}

const showTerms = () => {
  // TODO: 實現服務條款顯示邏輯
}

const showPrivacy = () => {
  // TODO: 實現隱私政策顯示邏輯
}
</script>

<style scoped>
.register {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #121212;
  padding: 2rem;
}

.register-container {
  background: #1e1e1e;
  border-radius: 1rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  width: 100%;
  max-width: 500px;
  padding: 2.5rem;
  border: 1px solid #333;
}

.register-header {
  text-align: center;
  margin-bottom: 2rem;
}

.register-header h1 {
  font-size: 2rem;
  color: #ffffff;
  margin-bottom: 0.5rem;
}

.register-header p {
  color: #999;
  font-size: 1rem;
}

.register-form {
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
  font-weight: 500;
  color: #ffffff;
}

.form-group input {
  padding: 0.75rem 1rem;
  border: 1px solid #333;
  border-radius: 0.5rem;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: #252525;
  color: #ffffff;
}

.form-group input:focus {
  border-color: #6366f1;
  box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.2);
  outline: none;
}

.form-group input.error {
  border-color: #ef4444;
}

.error-message {
  color: #ef4444;
  font-size: 0.875rem;
}

.password-input {
  position: relative;
}

.toggle-password {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
  padding: 0;
}

.role-selector {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.role-option {
  padding: 1rem;
  border: 1px solid #333;
  border-radius: 0.5rem;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #252525;
  color: #ffffff;
}

.role-option:hover {
  border-color: #6366f1;
  background: #2a2a2a;
}

.role-option.selected {
  background: #6366f1;
  border-color: #6366f1;
  color: white;
}

.role-option i {
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
  display: block;
}

.terms {
  font-size: 0.875rem;
  color: #666;
}

.checkbox-container {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.checkbox-container input {
  width: 1rem;
  height: 1rem;
}

.terms a {
  color: #6366f1;
  text-decoration: none;
}

.terms a:hover {
  text-decoration: underline;
}

.submit-btn {
  background: #6366f1;
  color: white;
  padding: 1rem;
  border: none;
  border-radius: 0.5rem;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.submit-btn:hover:not(:disabled) {
  background: #4f46e5;
  transform: translateY(-1px);
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  display: inline-block;
  width: 1.25rem;
  height: 1.25rem;
  border: 2px solid #ffffff;
  border-radius: 50%;
  border-top-color: transparent;
  animation: spin 1s linear infinite;
}

.login-link {
  text-align: center;
  font-size: 0.875rem;
  color: #999;
}

.login-link a {
  color: #6366f1;
  text-decoration: none;
  font-weight: 500;
}

.login-link a:hover {
  text-decoration: underline;
  color: #4f46e5;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 640px) {
  .register {
    padding: 1rem;
  }

  .register-container {
    padding: 1.5rem;
  }

  .register-header h1 {
    font-size: 1.75rem;
  }
}
</style> 