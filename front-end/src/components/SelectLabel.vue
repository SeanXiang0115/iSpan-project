<script setup>
import { ref } from 'vue';

// 模擬 SQL 寫死的標籤資料表
const LabelTable = ['寵物友善', '有插座', '日式料理', '禁帶外食', '有停車場', '禁止趙活與狗入內'];

const selected = ref(LabelTable[0]);
const emit = defineEmits(['add']);

const handleAdd = () => {
    emit('add', selected.value);
};
</script>

<template>
    <div class>
        <select v-model="selected">
            <option v-for="item in LabelTable" :key="item" :value="item">
                {{ item }}
            </option>
        </select>
        <button @click="handleAdd">新增標籤</button>
    </div>
</template>

<style scoped></style>