import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Projects from '../views/Projects.vue'
import ProjectList from '../views/ProjectList.vue'
import CategoryProjects from '../components/CategoryProjects.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/projects',
    name: 'projects',
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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 