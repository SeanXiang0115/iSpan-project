<script setup>
// 透過 defineModel 與父組件實作雙向綁定
const interval = defineModel('interval', { type: Number });
const duration = defineModel('duration', { type: Number });

// 選項寫死，當父組件執行「儲存」動作時，會將這些選中的數值寫入資料庫。
const intervalOptions = [30, 60, 90];
const durationOptions = [60, 90, 120, 150, 180];
</script>

<template>
    <div>
        <h3>訂位規則設定</h3>

        <div>
            <label>訂位開放間隔 (分鐘)</label>
            <select v-model.number="interval">
                <option v-for="opt in intervalOptions" :key="opt" :value="opt">
                    {{ opt }}
                </option>
            </select>
        </div>

        <div>
            <label>用餐時間限制 (分鐘)</label>
            <select v-model.number="duration">
                <option v-for="opt in durationOptions" :key="opt" :value="opt">
                    {{ opt }}
                </option>
            </select>
        </div>
    </div>
</template>

<style scoped></style>
