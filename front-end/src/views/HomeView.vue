<script setup>
</script>

<template>
  <h1>這是首頁</h1>
  <div>假裝有地圖...</div>
  <RouterLink :to="{ name: 'StoreInfo' }" v-slot="{ navigate }">
    <button @click="navigate">某家店的地標</button>
  </RouterLink>
</template>

<style scoped></style>
