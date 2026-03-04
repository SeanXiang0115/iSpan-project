<script setup>
import { ref } from 'vue';
import { useProductsDepot } from '@/stores/productsDepot.js';
import InputNumber from 'primevue/inputnumber';
import Select from 'primevue/select';
import Button from 'primevue/button';
import Message from 'primevue/message';
import Swal from 'sweetalert2';

// 初始化 Store
const depot = useProductsDepot();

// 響應式狀態
const selectedProduct = ref(null);
const editMode = ref('add'); // 'add', 'reduce', 'set'
const editValue = ref(0);

// 修改模式選項
const modes = [
    { label: '新增庫存 (+)', value: 'add' },
    { label: '調整總數 (=)', value: 'set' }
];

// 處理更新邏輯
const handleConfirm = () => {
    if (!selectedProduct.value) return;

    // 呼叫 store 的 action 更新庫存
    depot.updateStock(selectedProduct.value.id, editMode.value, editValue.value);

    // 取得更新後的最新產品資料以顯示在 Swal
    const currentProduct = depot.products.find(p => p.id === selectedProduct.value.id);

    Swal.fire({
        icon: 'success',
        title: '庫存更新成功',
        text: `產品 ${currentProduct.productName} 的庫存已更新為 ${currentProduct.stock} 件`,
        timer: 1500,
        showConfirmButton: false
    });

    // 重設輸入框
    editValue.value = 0;
};
</script>

<template>
<div class="p-6">
    <h1 class="text-2xl font-bold mb-4">庫存管理系統</h1>

    <div class="card shadow-sm p-4 bg-white rounded-lg border">
        
        
        <div class="flex flex-col gap-4">
            <div>
                <label class="block mb-2 text-gray-600 font-medium">1. 選擇商品項目</label>
                <Select
                    v-model="selectedProduct"
                    :options="depot.products"
                    optionLabel="productName"
                    placeholder="請選擇產品..."
                    class="w-full md:w-80"
                    filter
                    >
                </Select>
            </div>

            <transition name="fade">
                <div v-if="selectedProduct" class="mt-4 border-t pt-4">
                    <Message severity="secondary" :closable="false" class="mb-4">
                        目前庫存狀態：<span class="font-bold text-lg text-primary">{{ selectedProduct.stock }}</span> 件
                    </Message>

                    <div class="flex flex-wrap gap-4 items-end">
                        <div class="flex flex-col gap-2">
                            <span class="text-sm text-gray-500">2. 修改方式</span>
                            <Select
                                v-model="editMode" 
                                :options="modes" 
                                optionLabel="label" 
                                optionValue="value" 
                                class="w-44"
                                >
                            </Select>
                        </div>

                        <br/>
                        <div class="flex flex-col gap-2">
                            <span class="text-sm text-gray-500">3. 輸入數量</span>
                            <InputNumber 
                                v-model="editValue"
                                :min="0"
                                showButtons 
                                class="w-40"
                                buttonLayout="horizontal"
                                >
                            </InputNumber>
                        </div>

                        <br/>

                        <Button
                            label="確認修改" 
                            icon="pi pi-check" 
                            @click="handleConfirm" 
                            severity="success"
                            class="mb-1" 
                            >

                        </Button>
                    </div>
                </div>
            </transition>
        </div>
    </div> 
</div>
</template>

<style scoped>
/* 簡單的淡入動畫 */
.fade-enter-active, .fade-leave-active {
    transition: opacity 0.3s ease, transform 0.3s ease;
}
.fade-enter-from, .fade-leave-to {
    opacity: 0;
    transform: translateY(-10px);
}
</style>