<script setup>
</script>

<template>
  <h1>這是首頁</h1>
  <div>假裝有地圖...</div>
  <RouterLink :to="{ name: 'StoreInfo' }" v-slot="{ navigate }">
    <button @click="navigate">某家店的地標</button>
  </RouterLink>


  <!-- 跳轉測試用 -->
  <ul>
    <li>
      <RouterLink to="/">首頁</RouterLink>
    </li>
    <li>
      <RouterLink to="/login">登入頁面</RouterLink>
    </li>
    <li>
      <RouterLink to="/storeInfo">店家資訊頁面(消費者)</RouterLink>
    </li>
    <li>
      <RouterLink to="/user/bookings">訂位查詢頁面(消費者)</RouterLink>
    </li>
    <div>======================================</div>
    <li>
      <RouterLink to="/owner/storeInfo">店家資訊編輯頁面(商家)</RouterLink>
    </li>
    <li>
      <RouterLink to="/owner/bookings">訂位後台頁面(商家)</RouterLink>
    </li>
    <li>
      <RouterLink to="/owner/bookings/seats">座位數量與時段維護(商家)</RouterLink>
    </li>
    <li>
      <RouterLink to="/owner/bookings/data">訂位資料管理(商家)</RouterLink>
    </li>
  </ul>

</template>

<style scoped></style>
