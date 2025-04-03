import { defineStore } from 'pinia';
import { userService } from '../api/services';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token')
  }),
  
  actions: {
    async login(credentials) {
      const response = await userService.login(credentials);
      this.token = response.data.token;
      localStorage.setItem('token', this.token);
      await this.fetchUser();
    },
    
    async fetchUser() {
      const response = await userService.getUserProfile();
      this.user = response.data;
    }
  }
}); 