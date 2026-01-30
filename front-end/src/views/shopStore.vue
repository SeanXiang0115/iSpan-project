

<script setup>

import storeCard from '../component/storeCard.vue'
import { useRouter} from 'vue-router'
import { useCartStore } from '@/stores/cart';

const router =useRouter();
const goToCart = () => {
  // 括號內填寫你在 router/index.js 設定的 path
    router.push('/cart') 
}

const cartStore = useCartStore()

const products = [
    {
        id: 1,
        name: 'Fancy Product',
        price: 40,
        image: 'https://dummyimage.com/450x300/dee2e6/6c757d.jpg'
    },
    {
        id: 2,
        name: 'Cool Product',
        price: 60,
        image: 'https://dummyimage.com/450x300/adb5bd/212529.jpg'
    }
]

</script>

<template>
<h1>商店首頁</h1>


<storeCard />

<div class="row">
    <div class="col-md-4" v-for="p in products" :key="p.id">
    </div>
</div>


<!-- 那個車 -->
<div class="position-fixed top-50 end-0 p-1 pe-3">
    <button type="button" class="btn btn-primary position-relative" @click="router.push('/cart')" >
    <i class="bi bi-cart4" ></i>

        <span class="badge bg-danger position-absolute top-0 start-100 translate-middle">
        {{ cartStore.totalQuantity }}
        </span>
    </button>
</div>



    <!-- <button type="button"  class="btn btn-primary btn-lg" data-bs-toggle="modal" data-bs-target="#exampleModal"  @click="goToCart">
        結帳
    </button> -->
</template>

<style scoped>

</style>