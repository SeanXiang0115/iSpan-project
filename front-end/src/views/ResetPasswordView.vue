<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import BaseCard from '@/components/common/BaseCard.vue';
import BaseButton from '@/components/common/BaseButton.vue';

const router = useRouter();
const newPassword = ref('');
const confirmPassword = ref('');
const isSubmitting = ref(false);

const handleResetPassword = () => {
    if (newPassword.value !== confirmPassword.value) {
        alert("兩次輸入的密碼不一致");
        return;
    }

    isSubmitting.value = true;
    
    // Simulate API call
    setTimeout(() => {
        isSubmitting.value = false;
        alert("密碼重設成功！請使用新密碼登入。");
        router.push('/login');
    }, 1500);
};
</script>

<template>
    <div class="reset-password-page bg-gdg-light min-vh-100 d-flex align-items-center justify-content-center p-3">
        <BaseCard maxWidth="450px" :shadow="true" :hoverEffect="false">
            <template #header>
                <div class="text-center mb-4">
                    <h2 class="fw-bold">設定新密碼</h2>
                </div>
            </template>

            <form @submit.prevent="handleResetPassword">
                <div class="mb-3">
                    <label for="newPassword" class="form-label fw-bold small">新密碼</label>
                    <input 
                        type="password" 
                        id="newPassword" 
                        v-model="newPassword" 
                        class="form-control custom-input" 
                        placeholder="請輸入新密碼"
                        required
                    >
                </div>

                <div class="mb-4">
                    <label for="confirmPassword" class="form-label fw-bold small">確認新密碼</label>
                    <input 
                        type="password" 
                        id="confirmPassword" 
                        v-model="confirmPassword" 
                        class="form-control custom-input" 
                        placeholder="請再次輸入新密碼"
                        required
                    >
                </div>

                <div class="d-grid gap-2">
                    <BaseButton 
                        color="gdg" 
                        class="py-2 fw-bold" 
                        label="重設密碼" 
                        :disabled="isSubmitting"
                        @click="handleResetPassword"
                    />
                </div>
            </form>
        </BaseCard>
    </div>
</template>

<style scoped>
.custom-input {
    border-radius: 6px;
    border: 1px solid #ced4da;
    padding: 0.6rem 1rem;
}

.custom-input:focus {
    border-color: #9f9572;
    box-shadow: 0 0 0 0.2rem rgba(159, 149, 114, 0.25);
}
</style>
