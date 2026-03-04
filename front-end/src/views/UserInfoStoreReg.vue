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
                <h5 class="mb-1 fw-bold">
                    <span v-if="item.isUpdate" class="text-primary"><i class="bi bi-pencil-square me-2"></i>商店資料修改申請</span>
                    <span v-else class="text-success"><i class="bi bi-shop me-2"></i>店家註冊申請</span>
                </h5>
                <span :class="getStatusBadgeClass(item.status)">{{ getStatusText(item.status) }}</span>
            </div>
            <p class="mb-1 text-muted small">申請時間: {{ formatTime(item.createdAt) }}</p>
            
            <!-- Details -->
            <div class="mt-3 p-3 bg-light rounded small">
                <div class="row">
                    <div class="col-md-6 mb-2"><strong>負責人姓名：</strong>{{ item.name }}</div>
                    <div class="col-md-6 mb-2"><strong>聯絡電話：</strong>{{ item.phone }}</div>
                    <div class="col-md-6 mb-2"><strong>商家名稱：</strong>{{ item.storeName || '未填寫' }}</div>
                    <div class="col-md-12 mb-0"><strong>商家地址：</strong>{{ item.address }}</div>
                </div>
            </div>

            <!-- Show rejection reason if returned -->
            <div v-if="item.status === 'RETURNED' && item.reply" class="alert alert-danger mt-3 mb-0 p-2 small">
                <strong><i class="bi bi-exclamation-circle me-1"></i>退回原因:</strong> {{ item.reply }}
            </div>

            <!-- Buttons -->
            <div class="d-flex justify-content-end mt-3 gap-2">
                <button 
                  v-if="item.status === 'RETURNED'" 
                  class="btn btn-sm btn-outline-danger"
                  @click="handleResubmit(item.id)"
                >
                  <i class="bi bi-arrow-repeat me-1"></i>重新編輯並提交
                </button>
                <button 
                  v-if="item.status === 'PENDING'" 
                  class="btn btn-sm btn-outline-primary"
                  @click="handleEditPending(item)"
                >
                  <i class="bi bi-pencil-square me-1"></i>修改申請內容
                </button>
            </div>
        </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { storeRegistrationAPI } from '@/api/storeRegistration';
import Swal from 'sweetalert2';

const router = useRouter()
const myRegistrations = ref([])

const fetchMyRegistrations = async () => {
    try {
        const data = await storeRegistrationAPI.getMyApplications();
        myRegistrations.value = data;
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

const handleEditPending = async (item) => {
    const { value: formValues } = await Swal.fire({
        title: '修改申請資料',
        html: `
            <div class="text-start">
                <div class="mb-3">
                    <label class="form-label fw-bold">負責人姓名 <span class="text-danger">*</span></label>
                    <input id="swal-owner-name" class="form-control" value="${item.name || ''}">
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold">商家名稱</label>
                    <input id="swal-store-name" class="form-control" value="${item.storeName || ''}" placeholder="若無可留空">
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold">商家電話 <span class="text-danger">*</span></label>
                    <input id="swal-store-phone" class="form-control" value="${item.phone || ''}">
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold">商家地址 <span class="text-danger">*</span></label>
                    <input id="swal-store-address" class="form-control" value="${item.address || ''}">
                </div>
            </div>
        `,
        focusConfirm: false,
        showCancelButton: true,
        confirmButtonText: '送出修改',
        cancelButtonText: '取消',
        confirmButtonColor: '#0d6efd',
        width: '600px',
        preConfirm: () => {
            const container = Swal.getHtmlContainer();
            const ownerName = container.querySelector('#swal-owner-name').value.trim();
            const storeName = container.querySelector('#swal-store-name').value.trim();
            const storePhone = container.querySelector('#swal-store-phone').value.trim();
            const storeAddress = container.querySelector('#swal-store-address').value.trim();

            if (!ownerName || !storePhone || !storeAddress) {
                Swal.showValidationMessage('請填寫所有標示星號 (*) 的必填欄位');
                return false;
            }

            return { ownerName, storeName, storePhone, storeAddress };
        }
    });

    if (formValues) {
        try {
            Swal.fire({
                title: '提交中...',
                allowOutsideClick: false,
                didOpen: () => { Swal.showLoading(); }
            });

            await storeRegistrationAPI.updatePendingApplication(item.id, formValues);
            
            Swal.fire('修改成功', '您的申請資料已更新！', 'success');
            fetchMyRegistrations();
        } catch (error) {
            console.error('Update error', error);
            const msg = error.response?.data?.error || '修改失敗，請稍後再試。';
            Swal.fire('錯誤', msg, 'error');
        }
    }
};

onMounted(() => {
    fetchMyRegistrations();
});
</script>

<style scoped>
/* Scoped styles if needed, mostly bootstrap classes used */
</style>
