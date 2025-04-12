import axios from 'axios';

// 創建 axios 實例
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  },
  withCredentials: true
});

// 請求攔截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// 響應攔截器
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response) {
      console.error('Response error:', error.response.data);
      
      // 如果是401錯誤，可能是token過期，清除本地狀態
      if (error.response.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        localStorage.removeItem('tokenExpiry');
      }
    } else if (error.request) {
      console.error('Request error:', error.request);
    } else {
      console.error('Error:', error.message);
    }
    return Promise.reject(error);
  }
);

export default api; 