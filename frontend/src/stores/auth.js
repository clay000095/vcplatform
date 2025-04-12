import { defineStore } from 'pinia';
import { userService } from '../api/services';

// 開發環境下的狀態監控
const logState = (action, state) => {
  if (process.env.NODE_ENV === 'development') {
    console.group(`Auth Store - ${action}`);
    console.log('User:', state.user);
    console.log('Token:', state.token ? '****' + state.token.slice(-4) : null);
    console.log('Token Expiry:', state.tokenExpiry);
    console.log('Is Authenticated:', !!state.token && !!state.user && !isTokenExpired(state.tokenExpiry));
    console.groupEnd();
  }
};

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: null,
    tokenExpiry: null
  }),
  
  getters: {
    // 檢查是否已登入
    isAuthenticated: (state) => !!state.token && !!state.user && !isTokenExpired(state.tokenExpiry),
    
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
        const expiryStr = localStorage.getItem('tokenExpiry');
        
        if (this.token && userStr) {
          this.user = JSON.parse(userStr);
          this.tokenExpiry = expiryStr ? new Date(expiryStr) : null;
          
          // 如果 token 已過期，清除所有狀態
          if (isTokenExpired(this.tokenExpiry)) {
            this.logout();
          }
        }
        logState('Initialize', this);
      } catch (error) {
        console.warn('Failed to access localStorage:', error);
        this.clearAuthState();
      }
    },

    async login(credentials) {
      try {
        // 1. 登入獲取用戶信息
        const loginResponse = await userService.login(credentials);
        console.log('Login response:', loginResponse);

        if (!loginResponse || !loginResponse.data) {
          throw new Error('登入失敗：無效的響應');
        }

        // 2. 保存用戶信息
        const userData = loginResponse.data;
        if (!userData || !userData.id) {
          throw new Error('登入失敗：無效的用戶信息');
        }
        
        // 3. 生成一個臨時 token（實際應用中應該由後端生成）
        // 這裡我們使用用戶 ID 和時間戳生成一個簡單的 token
        const tempToken = btoa(`${userData.id}:${Date.now()}`);
        
        // 4. 設置狀態
        this.user = userData;
        this.token = tempToken;
        this.tokenExpiry = new Date(Date.now() + 24 * 60 * 60 * 1000);
        
        // 5. 保存到 localStorage
        this.saveToLocalStorage();
        
        // 6. 記錄狀態
        logState('Login', this);
        
        return true;
      } catch (error) {
        console.error('Login failed:', error);
        this.clearAuthState();
        throw error;
      }
    },
    
    async logout() {
      // 先清除本地狀態
      this.clearAuthState();
      
      // 然後嘗試調用登出API
      try {
        if (this.token) {
          await userService.logout();
        }
      } catch (error) {
        console.warn('Logout API call failed:', error);
        // API調用失敗不影響本地登出
      }
      
      logState('Logout', this);
    },

    saveToLocalStorage() {
      try {
        if (!this.token || !this.user) {
          throw new Error('Cannot save invalid auth state');
        }
        
        localStorage.setItem('token', this.token);
        localStorage.setItem('user', JSON.stringify(this.user));
        localStorage.setItem('tokenExpiry', this.tokenExpiry.toISOString());
        if (process.env.NODE_ENV === 'development') {
          console.log('Auth state saved to localStorage');
        }
      } catch (error) {
        console.warn('Failed to save to localStorage:', error);
        this.clearAuthState();
      }
    },

    clearAuthState() {
      this.user = null;
      this.token = null;
      this.tokenExpiry = null;
      try {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        localStorage.removeItem('tokenExpiry');
        if (process.env.NODE_ENV === 'development') {
          console.log('Auth state cleared from localStorage');
        }
      } catch (error) {
        console.warn('Failed to remove from localStorage:', error);
      }
    }
  }
});

// 檢查 token 是否過期
function isTokenExpired(expiryDate) {
  if (!expiryDate) return true;
  return new Date() > new Date(expiryDate);
} 