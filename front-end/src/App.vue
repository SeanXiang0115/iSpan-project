<script setup>
// import { computed } from 'vue';
// import { useRoute } from 'vue-router';
// import DefaultLayout from '@/layouts/default.vue';
// import BlankLayout from '@/layouts/blank.vue';
import Navbar from '@/layouts/navbar.vue';
import Footer from '@/layouts/footer.vue';
import { onMounted } from 'vue';
import { useProductsDepot } from '@/stores/productsDepot';
import { useCartStore } from '@/stores/cart'
import { useAuthStore } from '@/stores/auth'


const depot = useProductsDepot();

onMounted(async () => {
    const authStore = useAuthStore()
    const cartStore = useCartStore()
    
    if (authStore.isLoggedIn) {
        await cartStore.fetchCart()
    }
})
// const route = useRoute();

// const layout = computed(() => {
//   if (route.meta.layout === 'blank') return BlankLayout;
//   return DefaultLayout;
// });
</script>

<template>
  <Navbar />
  <!-- <component :is="layout">
    <RouterView />
  </component> -->
  <RouterView />
  <Footer />
</template>

<style scoped></style>
