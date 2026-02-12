<script setup>
import { ref, computed } from 'vue'
import { useOrderDepot } from '@/stores/orderDepot.js'
import Swal from 'sweetalert2'

const orderDepot = useOrderDepot()
const filterStatus = ref('全部') // 篩選狀態
const editingOrder = ref(null) // 當前正在編輯的訂單

// 1. 篩選邏輯
const filteredOrders = computed(() => {
    if (filterStatus.value === '全部') return orderDepot.orders
    return orderDepot.orders.filter(o => o.status === filterStatus.value)
})

// 2. 開啟編輯
const startEdit = (order) => {
    editingOrder.value = JSON.parse(JSON.stringify(order)) // 深拷貝，避免直接改到原始資料
}

// 3. 儲存編輯
const saveEdit = () => {
    orderDepot.updateOrder(editingOrder.value)
    editingOrder.value = null
    Swal.fire('更新成功', '訂單資訊已更新', 'success')
}

// 4. 刪除訂單
const deleteOrder = (id) => {
    Swal.fire({
        title: '確定刪除？',
        icon: 'warning',
        showCancelButton: true
    }).then(res => {
        if (res.isConfirmed) orderDepot.deleteOrder(id)
    })
}


</script>

<template>
    <div class="order-management">
        <div class="header-row">
            <h2>訂單管理系統</h2>
            <div class="filter-box">
                <label>訂單狀態篩選：</label>
                <select v-model="filterStatus" class="select-filter">
                    <option>全部</option>
                    <option>待付款</option>
                    <option>待出貨</option>
                    <option>處理中</option>
                    <option>已完成</option>
                </select>
            </div>
        </div>

        <table class="order-table">
            <thead>
                <tr>
                    <th>訂單編號</th>
                    <th>客戶姓名</th>
                    <th>總金額</th>
                    <th>付款方式</th>
                    <th>狀態</th>
                    <th>訂單日期</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="order in filteredOrders" :key="order.id">
                    <td>{{ order.id }}</td>
                    <td>{{ order.customer.name }}</td>
                    <td>NT$ {{ order.totalPrice }}</td>
                    <td >{{ order.customer.paymentMethod === 'credit_card' ? '信用卡' : '貨到付款' }}</td>
                    <td>
                        <span :class="['status-badge', order.status]">{{ order.status }}</span>
                    </td>
                    <td>{{ order.orderDate }}</td>
                    <td>
                        <button @click="startEdit(order)" class="btn-edit">編輯</button>
                        <button @click="deleteOrder(order.id)" class="btn-del">刪除</button>
                    </td>
                </tr>
            </tbody>
        </table>

        <div v-if="editingOrder" class="edit-modal-overlay">
            <div class="edit-modal">
                <h3>編輯訂單：{{ editingOrder.id }}</h3>
                <div class="form-grid">
                    <div class="form-item">
                        <label>姓名</label>
                        <input v-model="editingOrder.customer.name" type="text">
                    </div>
                    <div class="form-item">
                        <label>電話</label>
                        <input v-model="editingOrder.customer.phone" type="text">
                    </div>
                    <div class="form-item full-width">
                        <label>地址</label>
                        <input v-model="editingOrder.customer.address" type="text">
                    </div>
                    <div class="form-item">
                        <label>付款方式</label>
                        <select v-model="editingOrder.customer.paymentMethod">
                            <option value="credit_card">信用卡</option>
                            <option value="cod">貨到付款</option>
                        </select>
                    </div>
                    <div class="form-item">
                        <label>訂單狀態</label>
                        <select v-model="editingOrder.status">
                            <option>待付款</option>
                            <option>待出貨</option>
                            <option>處理中</option>
                            <option>已完成</option>
                        </select>
                    </div>
                </div>
                <div class="modal-info">
                    <p>訂單日期：{{ editingOrder.orderDate }}</p>
                    <p class="highlight">最新編輯時間：{{ editingOrder.lastUpdate }}</p>
                </div>
                <div class="modal-btns">
                    <button @click="saveEdit" class="btn-save">儲存變更</button>
                    <button @click="editingOrder = null" class="btn-cancel">取消</button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.order-management { 
    padding: 20px; 
}

.header-row { 
    display: flex; 
    justify-content: space-between; 
    align-items: center; 
    margin-bottom: 20px; 
}

.order-table { 
    width: 100%; 
    border-collapse: collapse; 
    background: white; 
    border-radius: 8px; 
    overflow: hidden; 
}

.order-table th, .order-table td { 
    padding: 15px; 
    border-bottom: 1px solid #eee; 
    text-align: left; 
}

.order-table th { 
    background: #f8f9fa; 
}

.status-badge { 
    padding: 4px 10px; 
    border-radius: 20px; 
    font-size: 0.8rem; 
}
.status-badge.待付款 { 
    background: #ffeaf8; 
    color: #f13434; 
}

.status-badge.待出貨 { 
    background: #fff3cd; 
    color: #856404; 
}

.status-badge.處理中 { 
    background: #cfe2ff; 
    color: #084298; 
}

.status-badge.已完成 { 
    background: #d1e7dd; 
    color: #0f5132; 
}

.btn-edit { 
    background: #198754; 
    color: white; 
    border: none; 
    padding: 5px 12px; 
    border-radius: 4px; 
    cursor: pointer; 
    margin-right: 5px; 
}

.btn-del { 
    background: #dc3545; 
    color: white; 
    border: none; 
    padding: 5px 12px; 
    border-radius: 4px; 
    cursor: pointer; 
}


/* 編輯視窗樣式 */
.edit-modal-overlay { 
    position: fixed; 
    top: 0; 
    left: 0; 
    width: 100%; 
    height: 100%; 
    background: rgba(0,0,0,0.5); 
    display: flex; 
    justify-content: center; 
    align-items: center; 
    z-index: 1000; 
}

.edit-modal { 
    background: white; 
    padding: 30px; 
    border-radius: 12px; 
    width: 600px; 
    box-shadow: 0 10px 25px rgba(0,0,0,0.2); 
}

.form-grid { 
    display: grid; 
    grid-template-columns: 1fr 1fr; 
    gap: 15px; 
}


.full-width { 
    grid-column: span 2; 
}

.form-item label { 
    display: block; 
    font-size: 0.85rem; 
    color: #666; 
    margin-bottom: 5px; 
}

.form-item input, .form-item select { 
    width: 100%; 
    padding: 8px; 
    border: 1px solid #ccc; 
    border-radius: 4px; 
}

.modal-info { 
    margin-top: 20px; 
    padding-top: 15px; 
    border-top: 1px solid #eee; 
    font-size: 0.85rem; 
}

.highlight { 
    color: #1e3c72; 
    font-weight: bold; 
}

.modal-btns { 
    margin-top: 20px; 
    display: flex; 
    gap: 10px; 
}

.btn-save { 
    flex: 2; 
    background: #198754; 
    color: white; 
    border: none; 
    padding: 10px; 
    border-radius: 6px; 
    cursor: pointer; 
}

.btn-cancel { 
    flex: 1; 
    background: #eee; 
    border: none; 
    padding: 10px; 
    border-radius: 6px; 
    cursor: pointer; 
}

</style>