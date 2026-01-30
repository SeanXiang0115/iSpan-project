<script setup>
import { useCartStore } from '@/stores/cart'

const cartStore = useCartStore()
</script>

<template>
    <h1>購物車</h1>

    <table class="table" v-if="cartStore.items.length">
        <thead>
        <tr>
            <th>商品</th>
            <th>價格</th>
            <th>數量</th>
            <th>小計</th>
            <th></th>
        </tr>
        </thead>

        <tbody>
        <tr v-for="item in cartStore.items" :key="item.id">
            <td>{{ item.name }}</td>
            <td>NT$ {{ item.price }}</td>
            <td>
            <button @click="cartStore.decrease(item.id)">-</button>
            {{ item.quantity }}
            <button @click="cartStore.increase(item.id)">+</button>
            </td>
            <td>NT$ {{ item.price * item.quantity }}</td>
            <td>
            <button class="btn btn-danger btn-sm"
                @click="cartStore.remove(item.id)">
                刪除
            </button>
            </td>
        </tr>
        </tbody>
    </table>

    <h3>總金額：NT$ {{ cartStore.totalPrice }}</h3>

    <div v-if="!cartStore.items.length">
        購物車是空的 🛒
    </div>
</template>