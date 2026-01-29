import { createRouter, createWebHistory } from 'vue-router';

// 定義router
const routes = [
  {
    path: '/login',
    component: () => import('@/views/LoginView.vue'),
    name: 'Login',
    meta: { layout: 'blank' }
  }
];

// 建立router
const router = createRouter({
  history: createWebHistory(),
  routes: routes
});

export default router;
