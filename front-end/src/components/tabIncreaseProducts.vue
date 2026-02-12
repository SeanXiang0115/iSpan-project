<script setup>
import { ref } from 'vue';
import { useProductsDepot } from '@/stores/productsDepot';
import Swal from 'sweetalert2';

const depot = useProductsDepot();

// 初始化表單資料
const form = ref({
    productName: '',
    price: 0,
    stock: 0,
    description: '',
    image: 'https://placehold.co/600x400?text=New+Product' // 預設圖片
});

const handleAddProduct = () => {
    if (!form.value.productName || form.value.price <= 0) {
        Swal.fire('錯誤', '請填寫完整的商品名稱與正確價格', 'error');
        return;
    }

    // 生成新 ID (例如取最後一個 ID + 1)
    const newId = (depot.products.length + 1).toString().padStart(4, '0');
    
    const newProduct = {
        id: newId,
        ...form.value,
        StockingTime: new Date().toISOString().split('T')[0] // 取得今天日期
    };

    // 推送到 Store
    depot.products.push(newProduct);

    Swal.fire({
        icon: 'success',
        title: '商品新增成功',
        text: `ID: ${newId} - ${form.value.productName}`,
        timer: 1500
    });

    // 清空表單
    form.value = { productName: '', price: 0, stock: 0, description: '', image: 'https://placehold.co/600x400?text=New+Product' };
};
</script>

<template>
    <div class="content-card">
        <div class="card-header"><h2 class="card-title">新增商品項目</h2></div>
        <div class="form-container">
            <div class="form-group">
                <label>商品名稱</label>
                <input v-model="form.productName" type="text" placeholder="請輸入商品名稱" />
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>售價 (NT$)</label>
                    <input v-model.number="form.price" type="number" />
                </div>
                <div class="form-group">
                    <label>初始庫存</label>
                    <input v-model.number="form.stock" type="number" />
                </div>
            </div>
            <div class="form-group">
                <label>商品描述</label>
                <textarea v-model="form.description" rows="4" placeholder="請輸入詳細描述..."></textarea>
            </div>
            <button @click="handleAddProduct" class="btn-submit">確認新增商品</button>
        </div>
    </div>
</template>

<style scoped>
.form-container { 
    display: flex; 
    flex-direction: column; 
    gap: 1.5rem; 
    padding: 1rem; 
}

.form-row { 
    display: flex; 
    gap: 1rem; 
}

.form-group { 
    flex: 1; display: flex; 
    flex-direction: column; 
    gap: 0.5rem; 
}

input, textarea { 
    padding: 0.75rem; 
    border: 1px solid #ddd; 
    border-radius: 6px; 
}

.btn-submit { 
    background-color: #1e3c72; 
    color: white; 
    padding: 1rem; 
    border: none; 
    border-radius: 6px; 
    cursor: pointer; 
    font-weight: bold; 
}
.btn-submit:hover { 
    background-color: #2a5298; 
    }
    
</style>