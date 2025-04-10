import axios from 'axios';
import { ref } from 'vue';
import type { User, LoginRequest, RegisterRequest, AuthResponse } from '../types/user';

// 創建全局狀態來存儲當前用戶信息
export const currentUser = ref<User | null>(null);
export const isAuthenticated = ref(false);

export const authService = {
  // 登入
  async login(data: LoginRequest): Promise<void> {
    try {
      const response = await axios.post<AuthResponse>('/api/auth/login', data);
      // 直接使用登入響應中的用戶數據
      const userData = response.data as unknown as User;
      currentUser.value = userData;
      isAuthenticated.value = true;
      // 保存用戶數據到 localStorage
      localStorage.setItem('user', JSON.stringify(userData));
      // 不需要保存 token，因為後端沒有返回
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  },

  // 註冊
  async register(data: RegisterRequest): Promise<void> {
    try {
      const response = await axios.post<AuthResponse>('/api/auth/register', data);
      // 直接設置用戶數據，不使用 this
      const userData = response.data.user;
      currentUser.value = userData;
      isAuthenticated.value = true;
      localStorage.setItem('user', JSON.stringify(userData));
    } catch (error) {
      console.error('Register error:', error);
      throw error;
    }
  },

  // 登出
  logout(): void {
    localStorage.removeItem('user');
    currentUser.value = null;
    isAuthenticated.value = false;
  },

  // 檢查是否已登入
  checkAuth(): boolean {
    const savedUser = localStorage.getItem('user');
    
    if (savedUser) {
      currentUser.value = JSON.parse(savedUser);
      isAuthenticated.value = true;
      return true;
    }
    
    return false;
  }
}; 