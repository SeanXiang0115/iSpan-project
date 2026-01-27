<script setup>
import { ref } from 'vue';
import BookingTable from '@/components/EditBookingData.vue';

// 模擬假資料：訂位編號、姓名、電話、日期、時間、人數
const bookingList = ref([
  { id: '1', name: '趙活', phone: '0912-345-678', date: '2024-06-01', time: '18:00', people: 2 },
  { id: '2', name: 'Arthur Morgan', phone: '0988-888-888', date: '2024-06-02', time: '12:30', people: 4 }
]);

const handleDelete = (id) => {
  alert('點擊了刪除，ID 為: ' + id);
  // 這裡之後可以寫過濾掉該 id 的 logic
};
</script>

<template>
  <div>
    <h1>訂位管理系統</h1>

    <BookingTable :bookings="bookingList" @delete="handleDelete"></BookingTable>
    <!-- 傳給子層時變成bookings與delete -->
  </div>
</template>

<style scoped></style>
