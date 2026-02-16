<template>
  <div class="user-info-store-reg">
    <h4 class="mb-3 border-bottom pb-2">商家申請進度</h4>
                
    <div v-if="myRegistrations.length === 0" class="text-center py-5 text-muted">
        <i class="bi bi-inbox fs-1 mb-2 d-block"></i>
        尚無申請紀錄
    </div>

    <div v-else class="list-group">
        <div v-for="item in myRegistrations" :key="item.id" class="list-group-item list-group-item-action p-3 mb-2 border rounded shadow-sm">
            <div class="d-flex w-100 justify-content-between align-items-center mb-2">
                <h5 class="mb-1 fw-bold">店家註冊申請</h5>
                <span :class="getStatusBadgeClass(item.status)">{{ getStatusText(item.status) }}</span>
            </div>
            <p class="mb-1 text-muted small">申請時間: {{ formatTime(item.createdAt) }}</p>
            
            <!-- Show rejection reason if returned -->
            <div v-if="item.status === 'RETURNED' && item.reply" class="alert alert-danger mt-2 mb-2 p-2 small">
                <strong><i class="bi bi-exclamation-circle me-1"></i>退回原因:</strong> {{ item.reply }}
            </div>

            <div class="d-flex justify-content-end mt-2">
                    <button 
                    v-if="item.status === 'RETURNED'" 
                    class="btn btn-sm btn-outline-danger"
                    @click="handleResubmit(item.id)"
                    >
                    <i class="bi bi-pencil-square me-1"></i>重新編輯並提交
                    </button>
            </div>
        </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const myRegistrations = ref([])

const fetchMyRegistrations = async () => {
    try {
        const response = await axios.get('/api/store-registrations/my');
        myRegistrations.value = response.data;
    } catch (error) {
        console.error('Failed to fetch registrations', error);
    }
}

const getStatusBadgeClass = (status) => {
    switch (status) {
        case 'APPROVED': return 'badge bg-success';
        case 'RETURNED': return 'badge bg-danger';
        case 'PENDING': return 'badge bg-warning text-dark';
        default: return 'badge bg-secondary';
    }
};

const getStatusText = (status) => {
    switch (status) {
        case 'APPROVED': return '已通過';
        case 'RETURNED': return '已退回';
        case 'PENDING': return '審核中';
        default: return status;
    }
};

const formatTime = (timeString) => {
    if (!timeString) return '';
    return new Date(timeString).toLocaleString();
};

const handleResubmit = (id) => {
    router.push({ path: '/storeRegistration', query: { id: id } });
};

onMounted(() => {
    fetchMyRegistrations();
});
</script>

<style scoped>
/* Scoped styles if needed, mostly bootstrap classes used */
</style>
