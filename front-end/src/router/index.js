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
  }
];

// 建立router
const router = createRouter({
  history: createWebHistory(),
  routes: routes
});



export default router;
