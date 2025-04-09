import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import Home from '../views/Home.vue'
import Projects from '../views/Projects.vue'
import ProjectList from '../views/ProjectList.vue'
import CategoryProjects from '../components/CategoryProjects.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
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
    path: '/projects',
    name: 'Projects',
    component: Projects,
    children: [
      {
        path: '',
        name: 'project-list',
        component: ProjectList
      },
      {
        path: 'category/:category',
        name: 'category-projects',
        component: CategoryProjects,
        props: true
      }
    ]
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

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守衛
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const isAuthenticated = authStore.isAuthenticated
  const userRole = authStore.user?.role

  // 需要驗證的路由
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      next('/login')
      return
    }

    // 檢查角色權限
    if (to.meta.role && userRole !== to.meta.role) {
      next('/')
      return
    }
  }

  // 訪客路由（登入/註冊）
  if (to.matched.some(record => record.meta.guest)) {
    if (isAuthenticated) {
      next('/')
      return
    }
  }

  next()
})

export default router 