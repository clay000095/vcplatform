import api from './config';

// 項目相關 API
export const projectService = {
  // 獲取所有已批准的項目
  getApprovedProjects() {
    return api.get('/projects');
  },
  
  // 獲取項目詳情
  getProjectById(id) {
    return api.get(`/projects/${id}`);
  },
  
  // 創建新項目
  createProject(project) {
    return api.post('/projects', project);
  },
  
  // 按類別獲取項目
  getProjectsByCategory(category) {
    return api.get(`/projects/category/${category}`);
  },
  
  // 搜索項目
  searchProjects(keyword) {
    return api.get(`/projects/search?q=${keyword}`);
  }
};

// 投資相關 API
export const investmentService = {
  // 創建投資
  createInvestment(investment) {
    return api.post('/investments', investment);
  },
  
  // 獲取用戶投資
  getUserInvestments(userId) {
    return api.get(`/investments/user/${userId}`);
  }
};

// 用戶相關 API
export const userService = {
  // 登錄
  login(credentials) {
    return api.post('/auth/login', credentials);
  },
  
  // 註冊
  register(user) {
    return api.post('/auth/register', user);
  },

}; 