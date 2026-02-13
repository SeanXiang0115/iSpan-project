<script setup>
import { ref, onMounted } from 'vue';
import SelectLabel from '@/components/SelectLabel.vue';
import BaseButton from '@/components/common/BaseButton.vue';
import Swal from 'sweetalert2';
import storeAPI from '@/api/store';

const isEditing = ref(false);

const storeName = ref('載入中...');
const storeDescription = ref('請輸入店家資訊與簡介...');
const myLabels = ref([]);

const fetchStoreInfo = async () => {
  try {
    const data = await storeAPI.getMyStoreInfo();
    if (data) {
      storeName.value = data.storeName || '未命名店家';
      storeDescription.value = data.description || '請輸入店家資訊與簡介...';
    }
  } catch (error) {
    console.error('獲取店家資訊失敗:', error);
    if (error.response && error.response.status === 403) {
      Swal.fire('權限不足', '您目前不具備店家身分', 'error');
    }
  }
};

onMounted(() => {
  fetchStoreInfo();
});

const addLabel = (newLabel) => {
  if (!myLabels.value.includes(newLabel)) {
    myLabels.value.push(newLabel);
  }
};

const removeLabel = (index) => {
  myLabels.value.splice(index, 1);
};

const handleSave = async () => {
  try {
    await storeAPI.updateMyStoreInfo({
      storeName: storeName.value
    });

    Swal.fire({
      icon: 'success',
      title: '成功儲存資訊！',
      confirmButtonText: '確定',
    });
    isEditing.value = false;
  } catch (error) {
    console.error('儲存店家資訊失敗:', error);
    Swal.fire('儲存失敗', error.response?.data || '發生未知錯誤', 'error');
  }
};
</script>

<template>
  <div class="container py-4">
    <h1 class="text-gdg mb-4">店家資訊編輯頁面</h1>

    <div class="store-name-section mb-4">
      <div v-if="!isEditing">
        <h2 class="h3 fw-bold text-gdg">{{ storeName }}</h2>
      </div>
      <div v-else>
        <label class="form-label text-gdg fw-bold">店家名稱：</label>
        <input type="text" v-model="storeName" class="form-control border-gdg" placeholder="請輸入店家名稱" />
      </div>
    </div>

    <div class="info-section mb-4">
      <div v-if="!isEditing">
        <label class="form-label text-gdg fw-bold">店家簡介：</label>
        <p class="p-3 border bg-light">{{ storeDescription }}</p>
      </div>
      <div v-else>
        <label class="form-label text-gdg fw-bold">編輯簡介：</label>
        <input type="text" v-model="storeDescription" class="form-control border-gdg" placeholder="請輸入店家簡介" />
      </div>
    </div>

    <div class="label-section mb-4">
      <h3 class="text-gdg h5 mb-3">店舖標籤：</h3>
      <div class="tag-list d-flex flex-wrap gap-2">
        <span v-for="(tag, index) in myLabels" :key="tag"
          class="badge rounded-0 border border-gdg text-gdg p-2 d-flex align-items-center">
          {{ tag }}
          <button v-if="isEditing" @click="removeLabel(index)" class="btn-close ms-2"
            style="font-size: 0.5rem;"></button>
        </span>
      </div>

      <div v-if="isEditing" class="edit-box mt-3 p-3 border bg-gdg-light">
        <SelectLabel @add="addLabel" />
      </div>
    </div>

    <div class="mb-5">
      <BaseButton v-if="!isEditing" color="gdg" @click="isEditing = true">編輯資訊</BaseButton>
      <BaseButton v-else color="gdg" @click="handleSave">儲存編輯</BaseButton>
    </div>

    <hr class="my-5">

    <div class="mt-4">
      <h2 class="text-gdg h4 mb-3">訂位編輯系統</h2>
      <RouterLink :to="{ name: 'Seats' }" v-slot="{ navigate }">
        <BaseButton color="gdg" @click="navigate">點擊進入系統</BaseButton>
      </RouterLink>
    </div>
  </div>
</template>

<style scoped>
.border-gdg {
  border-color: #9f9572 !important;
}
</style>