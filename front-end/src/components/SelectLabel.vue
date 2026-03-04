<script setup>
import { ref, onMounted } from 'vue';
import storeAPI from '@/api/store';
import BaseButton from '@/components/common/BaseButton.vue';

// 標籤資料案
const categories = ref([]);

const selected = ref(null);
const emit = defineEmits(['add']);

const fetchCategories = async () => {
    try {
        const response = await storeAPI.getAllCategories();
        if (response && response.success) {
            categories.value = response.data;
            if (categories.value.length > 0) {
                selected.value = categories.value[0];
            }
        }
    } catch (error) {
        console.error('獲取標籤失敗:', error);
    }
};

onMounted(() => {
    fetchCategories();
});

const handleAdd = () => {
    if (!selected.value) {
        alert('請選擇一個標籤！');
        return;
    } else {
        // 發送整個物件，以便前端顯示名稱，存檔時使用 ID
        emit('add', selected.value);
    }
};
</script>

<template>
    <div class="label-selector-container">
        <div class="input-group">
            <select v-model="selected" class="form-select border-gdg custom-select">
                <option v-for="item in categories" :key="item.categoryId" :value="item">
                    {{ item.categoryName }}
                </option>
            </select>

            <BaseButton color="gdg" @click="handleAdd">
                <i class="bi bi-plus-lg me-1"></i>新增標籤
            </BaseButton>
        </div>
        <small class="text-muted mt-2 d-block">選擇上方標籤以加入您的店舖特色</small>
    </div>
</template>

<style scoped>
.border-gdg {
    border-color: #9f9572;
}

.custom-select {
    border-radius: 0;
    /* 維持直角風格 */
    max-width: 250px;
    background-color: #fff;
}

.custom-select:focus {
    border-color: #776f54;
    box-shadow: none;
}

/* 讓 input-group 內的元件緊密結合 */
.input-group :deep(.base-button) {
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
}
</style>