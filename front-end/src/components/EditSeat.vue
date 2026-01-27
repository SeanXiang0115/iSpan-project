<script setup>
// 1. 必須宣告 props 才能在 template 和 script 中使用 modelValue
const props = defineProps({
    modelValue: {
        type: Array,
        required: true
    }
});

// 2. 必須宣告 emits 才能發送更新事件
const emit = defineEmits(['update:modelValue']);

const updateCount = (type, delta) => {
    const updated = props.modelValue.map(item => {
        if (item.type === type) {
            const newCount = Math.max(0, item.count + delta); 
            return { ...item, count: newCount };
        }
        return item;
    });
    // 發送事件通知父組件更新資料
    emit('update:modelValue', updated);
};
</script>

<template>
    <div>
        <h3>座位數量設定</h3>
        <div v-for="seat in modelValue" :key="seat.type">
            <span>{{ seat.type }} 人座</span>
            <div>
                <button @click="updateCount(seat.type, -1)">-</button>
                <input type="number" :value="seat.count" readonly />
                <button @click="updateCount(seat.type, 1)">+</button>
            </div>
        </div>
    </div>
</template>

<style scoped></style>