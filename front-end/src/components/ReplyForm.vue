<script setup>
import { ref } from 'vue';

const emit = defineEmits(['submit']);
const replyContent = ref('');

const handleSubmit = () => {
    if (!replyContent.value.trim()) {
        alert('請輸入回覆內容');
        return;
    }
    emit('submit', replyContent.value);
    replyContent.value = '';
};
</script>

<template>
    <div class="admin-card p-4">
        <h5 class="text-admin-primary mb-4">
            <i class="bi bi-reply-fill me-2"></i>回覆意見
        </h5>
        <form @submit.prevent="handleSubmit">
            <div class="mb-3">
                <!-- <label for="replyContent" class="form-label text-admin-secondary fw-semibold">回覆內容</label> -->
                <textarea 
                    id="replyContent" 
                    v-model="replyContent" 
                    class="admin-form-control" 
                    rows="5" 
                    placeholder="請輸入欲回覆的內容..."
                    required
                ></textarea>
            </div>
            <div class="d-flex justify-content-end gap-2">
                <button type="button" class="btn btn-admin-outline" @click="replyContent = ''">
                    取消
                </button>
                <button type="submit" class="btn btn-admin-primary">
                    <i class="bi bi-send-fill me-2"></i>送出回覆
                </button>
            </div>
        </form>
    </div>
</template>

<style scoped>
.admin-card {
    max-width: 800px;
    margin: 0 auto;
}

textarea {
    resize: none;
}
</style>
