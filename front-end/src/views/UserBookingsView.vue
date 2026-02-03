<script setup>
import BaseButton from '@/components/common/BaseButton.vue';
import Swal from 'sweetalert2';

const updateBooking = (item) => {
    // 以下是彈窗架構
    Swal.fire({
        icon: 'success',
        title: '成功加入購物車',
        text: `已選購  ${item.productName}`,
        timer: 1500,
        showConfirmButton: false
    });
}

</script>

<template>
  <div class="container py-4">
    <h1 class="text-gdg mb-4">訂位查詢頁面(使用者)</h1>

    <div class="mb-4">

      <BaseButton color="gdg" @click="updateBooking">修改訂位</BaseButton>
    </div>
  </div>
</template>

<style scoped>
/* 保持乾淨 */
</style>