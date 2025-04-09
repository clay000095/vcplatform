import { defineStore } from 'pinia';
import { userService } from '../api/services';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: null
  }),
  
  actions: {
    initialize() {
      try {
        this.token = localStorage.getItem('token');
      } catch (error) {
        console.warn('Failed to access localStorage:', error);
        this.token = null;
      }
    },

    async login(credentials) {
      const response = await userService.login(credentials);
      this.token = response.data.token;
      try {
        localStorage.setItem('token', this.token);
      } catch (error) {
        console.warn('Failed to save token to localStorage:', error);
      }
      await this.fetchUser();
    },
    
    async fetchUser() {
      const response = await userService.getUserProfile();
      this.user = response.data;
    },

    logout() {
      this.user = null;
      this.token = null;
      try {
        localStorage.removeItem('token');
      } catch (error) {
        console.warn('Failed to remove token from localStorage:', error);
      }
    }
  }
}); 