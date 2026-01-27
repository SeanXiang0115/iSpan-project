<script setup>

import { useRouter } from 'vue-router';

const router = useRouter();

const isLogin = false;

const loginOrNot = () => {
  if (isLogin) {
    router.push({ name: 'UserBookings' });
  } else {
    router.push({ name: 'Login' });
  }
}

</script>

<template>

  <h1>訂位頁面(使用者)</h1>
  <div>按下訂位沒登入跳轉到登入頁面，有則跳到訂位頁面</div>

  <button @click="loginOrNot">訂位</button>
  


</template>

<style scoped></style>
