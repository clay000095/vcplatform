import { defineStore } from 'pinia';
import { userService } from '../api/services';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: null
  }),
  
  getters: {
    // 檢查是否已登入
    isAuthenticated: (state) => !!state.token && !!state.user,
    
    // 取得用戶角色
    userRole: (state) => state.user?.role || null,
    
    // 取得用戶名稱
    userName: (state) => state.user?.name || ''
  },
  
  actions: {
    initialize() {
      try {
        this.token = localStorage.getItem('token');
        const userStr = localStorage.getItem('user');
        
        if (this.token && userStr) {
          this.user = JSON.parse(userStr);
          this.fetchUser();
        }
      } catch (error) {
        console.warn('Failed to access localStorage:', error);
        this.token = null;
        this.user = null;
      }
    },

    async login(credentials) {
      const response = await userService.login(credentials);
      this.token = response.data.token;
      this.user = response.data.user;

      try {
        localStorage.setItem('token', this.token);
        localStorage.setItem('user', JSON.stringify(this.user));
      } catch (error) {
        console.warn('Failed to save to localStorage:', error);
      }
    },
    
    async fetchUser() {
      try {
        const response = await userService.getUserProfile();
        this.user = response.data;
        localStorage.setItem('user', JSON.stringify(this.user));
      } catch (error) {
        console.error('Failed to fetch user profile:', error);
        if (error.response?.status === 401) {
          this.logout();
        }
      }
    },

    async logout() {
      try {
        if (this.token) {
          await userService.logout();
        }
      } catch (error) {
        console.warn('Logout API call failed:', error);
      }

      this.user = null;
      this.token = null;

      try {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
      } catch (error) {
        console.warn('Failed to remove from localStorage:', error);
      }
    }
  }
}); 