<script setup>
import { ref } from 'vue';

// 接收父層傳來的資料(dataList)
const props = defineProps(['bookings']);
defineEmits(['delete']);

// 紀錄哪一行正在編輯
const editingId = ref(null);

const startEdit = (id) => {
    editingId.value = id;
};

const saveEdit = () => {
    // 這裡暫時只切換回顯示模式
    editingId.value = null;
};

const cancelEdit = () => {
    editingId.value = null;
};
</script>

<template>
    <div>
        <h2>訂位清單</h2>
        <table>
            <thead>
                <tr>
                    <th>訂位編號</th>
                    <th>姓名</th>
                    <th>電話</th>
                    <th>日期</th>
                    <th>時間</th>
                    <th>人數</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in bookings" :key="item.id">
                    <template v-if="editingId === item.id">
                        <td>{{ item.id }}</td>
                        <td>{{ item.name }}</td>
                        <td>{{ item.phone }}</td>
                        <td><input type="date" v-model="item.date" /></td>
                        <td><input type="time" v-model="item.time" /></td>
                        <td>{{ item.people }}</td>
                        <td>
                            <button @click="saveEdit">儲存</button>
                            <button @click="cancelEdit">取消</button>
                        </td>
                    </template>

                    <template v-else>
                        <td>{{ item.id }}</td>
                        <td>{{ item.name }}</td>
                        <td>{{ item.phone }}</td>
                        <td>{{ item.date }}</td>
                        <td>{{ item.time }}</td>
                        <td>{{ item.people }}</td>
                        <td>
                            <button @click="startEdit(item.id)">修改</button>
                            <button @click="$emit('delete', item.id)">刪除</button>
                        </td>
                    </template>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<style scoped></style>
