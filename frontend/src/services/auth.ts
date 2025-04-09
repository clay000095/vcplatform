import axios from 'axios';
import { ref } from 'vue';
import type { User, LoginRequest, RegisterRequest, AuthResponse } from '../types/user';

// 創建全局狀態來存儲當前用戶信息
export const currentUser = ref<User | null>(null);
export const isAuthenticated = ref(false);

// 設置 axios 請求攔截器，自動添加 token
axios.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authService = {
  // 登入
  async login(data: LoginRequest): Promise<void> {
    try {
      const response = await axios.post<AuthResponse>('/api/auth/login', data);
      this.setAuthData(response.data);
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  },

  // 註冊
  async register(data: RegisterRequest): Promise<void> {
    try {
      const response = await axios.post<AuthResponse>('/api/auth/register', data);
      this.setAuthData(response.data);
    } catch (error) {
      console.error('Register error:', error);
      throw error;
    }
  },

  // 登出
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    currentUser.value = null;
    isAuthenticated.value = false;
  },

  // 檢查是否已登入
  checkAuth(): boolean {
    const token = localStorage.getItem('token');
    const savedUser = localStorage.getItem('user');
    
    if (token && savedUser) {
      currentUser.value = JSON.parse(savedUser);
      isAuthenticated.value = true;
      return true;
    }
    
    return false;
  },

  // 設置認證數據
  setAuthData(data: AuthResponse): void {
    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify(data.user));
    currentUser.value = data.user;
    isAuthenticated.value = true;
  }
}; 