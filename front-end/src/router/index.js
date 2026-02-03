import { createRouter, createWebHistory } from 'vue-router';



// 定義router
const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
  },

  {
    path: '/shopStore',
    component: () => import('@/views/shopStore.vue'),
    name: 'ShopStore'
  },
  {
    path: '/Cart',
    component: () => import('@/views/shopCart.vue'),
    name: 'ShopCart'
  },
  {
    path: '/storeInfo',
    name: 'StoreInfo',
    component: () => import('@/views/StoreInfoView.vue'),
  },
  {
    path: '/storeInfo/reservation',
    name: 'Reservation',
    component: () => import('@/views/ReservationView.vue'),
  },
  {
    path: '/user/bookings',
    name: 'UserBookings',
    component: () => import('@/views/UserBookingsView.vue')
  },
  {
    path: '/owner/storeInfo',
    name: 'OwnerStoreInfo',
    component: () => import('@/views/OwnerProfileView.vue')
  },
  {
    path: '/owner/bookings/seats',
    name: 'Seats',
    component: () => import('@/views/SeatsAndTimeView.vue')
  },
  {
    path: '/owner/bookings/data',
    name: 'Data',
    component: () => import('@/views/BookingDataView.vue')
  },
  // {
  //   path: '/home',
  //   component: () => import('@/views/HomeView.vue'),
  //   name: 'Home'
  // },
  {
    path: '/about',
    component: () => import('@/views/AboutView.vue'),
    name: 'About'
  },
  {
    path: '/mapSearch',
    component: () => import('@/views/MapSearchView.vue'),
    name: 'MapSearch'
  },
  {
    path: '/feedback',
    component: () => import('@/views/FeedbackView.vue'),
    name: 'Feedback'
  },
  {
    path: '/feedbackAP',
    component: () => import('@/views/FeedbackAPView.vue'),
    name: 'FeedbackAP'
  },
  {
    path: '/register',
    component: () => import('@/views/RegisterView.vue'),
    name: 'Register',
    meta: { layout: 'blank' }
  },
  {
    path: '/productsDetail/:id',
    component: () => import('@/views/productsDetail.vue'),
    name: 'productsDetail',
  },
  {
    path: '/checkOut',
    component: () => import('@/views/checkOut.vue'),
    name: 'checkOut',
  }

];

// 建立router
const router = createRouter({
  history: createWebHistory(),
  routes: routes
});



export default router;
