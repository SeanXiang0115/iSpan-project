import { createRouter, createWebHistory } from 'vue-router';

// 定義router
const routes = [
  {
    path: '/login',
    component: () => import('@/views/LoginView.vue'),
    name: 'Login'
  },
  {
    path: '/home',
    component: () => import('@/views/HomeView.vue'),
    name: 'Home'
  }, {
    path: '/about',
    component: () => import('@/views/AboutView.vue'),
    name: 'About'
  }, {
    path: '/mapSearch',
    component: () => import('@/views/MapSearchView.vue'),
    name: 'MapSearch'
  }, {
    path: '/feedbackAP',
    component: () => import('@/views/FeedbackAPView.vue'),
    name: 'FeedbackAP'
  }
];

// 建立router
const router = createRouter({
  history: createWebHistory(),
  routes: routes
});

export default router;
