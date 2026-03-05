<script setup>
// import { computed } from 'vue';
// import { useRoute } from 'vue-router';
// import DefaultLayout from '@/layouts/default.vue';
// import BlankLayout from '@/layouts/blank.vue';
import Navbar from '@/layouts/navbar.vue';
import Footer from '@/layouts/footer.vue';
import { onMounted } from 'vue';
import { useProductsDepot } from '@/stores/productsDepot';
import { useCartStore } from '@/stores/cart';
import { useAuthStore } from '@/stores/auth';
import { useAdminAuthStore } from '@/stores/adminAuth';


const depot = useProductsDepot();

onMounted(async () => {
    const authStore = useAuthStore()
    const adminAuthStore = useAdminAuthStore();
    const cartStore = useCartStore()
    
    const isFrontend = !window.location.pathname.startsWith('/admin');
    const isAdminArea = window.location.pathname.startsWith('/admin');

    if (localStorage.getItem('isUserLoggedIn') === 'true') {
        await authStore.syncUserProfile();
        if (authStore.isLoggedIn) {
            await cartStore.fetchCart()
        } else if (isFrontend && !window.location.hash) {
            // 如果在前台刷新發現失效，給予逾期提示 (排除可能剛好在 router guard 打過的情況)
            authStore.handleLogoutAndNotify('timeout').then(() => {
                window.location.href = '/login';
            });
        }
    }
    
    if (localStorage.getItem('isAdminLoggedIn') === 'true') {
        await adminAuthStore.syncAdminProfile();
        if (!adminAuthStore.isLoggedIn && isAdminArea && !window.location.hash) {
            // 如果在後台刷新發現失效，給予逾期提示
            adminAuthStore.handleLogoutAndNotify('timeout').then(() => {
                window.location.href = '/admin/login';
            });
        }
    }
})
// const route = useRoute();
</script>

<template>
  <RouterView />
</template>

<style scoped></style>
