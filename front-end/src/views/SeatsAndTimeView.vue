<script setup>
import { ref } from 'vue';
import SeatManager from '@/components/EditSeat.vue';
import TimeSlotManager from '@/components/EditTime.vue';

// 假資料初始狀態
const seatData = ref([
  { type: 2, count: 5 },
  { type: 4, count: 10 },
  { type: 6, count: 2 },
  { type: 8, count: 1 }
]);

const bookingConfig = ref({
  interval: 30,
  duration: 90
});

const saveSettings = () => {
  console.log('儲存設定至資料庫:', {
    seats: seatData.value,
    config: bookingConfig.value
  });
  alert('設定已暫存，請查看 Console！');
};
</script>

<template>
  <div>
    <h1>商家編輯畫面</h1>
    
    <section>
      <SeatManager v-model="seatData" />
    </section>

    <hr />

    <section>
      <TimeSlotManager 
        v-model:interval="bookingConfig.interval"
        v-model:duration="bookingConfig.duration"
      />
    </section>

    <div>
      <button @click="saveSettings">儲存所有設定</button>
    </div>
  </div>
</template>

<style scoped></style>