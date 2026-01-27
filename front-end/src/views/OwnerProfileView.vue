<script setup>
import { ref } from 'vue';
import SelectLabel from '@/components/SelectLabel.vue';

const isEditing = ref(false);

const storeDescription = ref('店家簡介，好多的字...');
const myLabels = ref(['寵物友善']);

const addLabel = (newLabel) => {
  if (!myLabels.value.includes(newLabel)) {
    myLabels.value.push(newLabel);
  }
};

const removeLabel = (index) => {
  myLabels.value.splice(index, 1);
};

const handleSave = () => {
  alert('儲存成功！');
  isEditing.value = false;
};
</script>

<template>
  <h1>店家資訊編輯頁面</h1>

  <div class="info-section">
    <div v-if="!isEditing">
      <p>{{ storeDescription }}</p>
    </div>
    <div v-else>
      <label>編輯簡介：</label>
      <input type="text" v-model="storeDescription" class="edit-input" placeholder="請輸入店家簡介" />
    </div>
  </div>

  <br>

  <div class="label-section">
    <h3>店舖標籤：</h3>
    <div class="tag-list">
      <span v-for="(tag, index) in myLabels" :key="tag" class="tag-item">
        {{ tag }}
        <button v-if="isEditing" @click="removeLabel(index)" class="del-btn">x</button>
      </span>
    </div>

    <br>
    <div v-if="isEditing" class="edit-box">
      <SelectLabel @add="addLabel" />
    </div>
  </div>

  <br>

  <button v-if="!isEditing" @click="isEditing = true">編輯資訊</button>
  <button v-else @click="handleSave">儲存編輯</button>

  <hr>

  <div>
    <h2>訂位編輯系統</h2>
    <RouterLink :to="{ name: 'Seats' }" v-slot="{ navigate }">
      <button @click="navigate">點擊進入系統</button>
    </RouterLink>
  </div>
</template>

<style scoped></style>