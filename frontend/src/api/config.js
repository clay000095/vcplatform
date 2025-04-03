import axios from 'axios';

// 創建 axios 實例
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
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
  error => Promise.reject(error)
);

// 響應攔截器
api.interceptors.response.use(
  response => response,
  error => {
    console.error('API 錯誤:', error);
    return Promise.reject(error);
  }
);

export default api; 