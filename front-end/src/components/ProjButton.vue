<script setup>
import { computed } from 'vue';

class Display {
    constructor(text1, className1, icon1) {
        this.text = text1;
        this.className = className1;
        this.icon = icon1;
    }
}

// const d = new Display('t', 't2');

// const d1 = {};
// d1.text = 't';
// d1.className = 't2';


const props = defineProps({ type: String });
const emit = defineEmits(['projClick']);

const display = computed(() => {

    switch (props.type) {
        case 'search':
            // return { text: "搜尋", className: 'btn btn-primary' };
            return new Display("搜尋", "btn btn-primary btn-sm", "bi bi-search");
        case 'add':
            return new Display("新增", "btn btn-success btn-sm", "bi bi-plus");
        case 'cal':
            return new Display("計算", "btn btn-warning btn-sm", "bi bi-calculator-fill");
        case 'delete':
            return new Display("刪除", "btn btn-danger btn-sm", "bi bi-trash");
        default:
            return new Display("未設定", null);
    }


})

</script>

<template>
    <button v-bind:class="display.className" @click="emit('projClick')">
        <i :class="display.icon"></i>{{ display.text }}
    </button>
</template>

<style></style>