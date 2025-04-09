import { createRouter, createWebHistory } from 'vue-router';
import { authService, currentUser } from '../services/auth';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('../views/Home.vue')
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/auth/Login.vue'),
      meta: { guest: true }
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('../views/auth/Register.vue'),
      meta: { guest: true }
    },
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: () => import('../views/Dashboard.vue'),
      meta: { requiresAuth: true },
      beforeEnter: (to, from, next) => {
        if (!currentUser.value) return next('/login');
        
        switch (currentUser.value.role) {
          case 'ADMIN':
            return next('/admin/dashboard');
          case 'FOUNDER':
            return next('/founder/dashboard');
          case 'INVESTOR':
            return next('/investor/dashboard');
          default:
            return next('/login');
        }
      }
    },
    {
      path: '/admin/dashboard',
      name: 'AdminDashboard',
      component: () => import('../views/admin/AdminDashboard.vue'),
      meta: { requiresAuth: true, role: 'ADMIN' }
    },
    {
      path: '/founder/dashboard',
      name: 'FounderDashboard',
      component: () => import('../views/founder/FounderDashboard.vue'),
      meta: { requiresAuth: true, role: 'FOUNDER' }
    },
    {
      path: '/investor/dashboard',
      name: 'InvestorDashboard',
      component: () => import('../views/investor/InvestorDashboard.vue'),
      meta: { requiresAuth: true, role: 'INVESTOR' }
    }
  ]
});

// 全局路由守衛
router.beforeEach((to, from, next) => {
  const isAuthenticated = authService.checkAuth();
  
  // 需要驗證的路由
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      next('/login');
      return;
    }

    // 檢查角色權限
    if (to.meta.role && currentUser.value?.role !== to.meta.role) {
      next('/dashboard');
      return;
    }
  }

  // 訪客路由（登入/註冊）
  if (to.matched.some(record => record.meta.guest)) {
    if (isAuthenticated) {
      next('/dashboard');
      return;
    }
  }

  next();
});

export default router; 