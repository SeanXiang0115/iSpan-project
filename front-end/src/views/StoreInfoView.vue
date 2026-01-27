<script setup>

</script>

<template>

  <h1>店家頁面(使用者)</h1>
  <RouterLink :to="{ name: 'Reservation' }" v-slot="{ navigate }">
    <button @click="navigate">我要訂位</button>
  </RouterLink>

</template>

<style></style>
