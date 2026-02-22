<template>
  <div class="test-view container py-5">
    <h2 class="text-admin-primary fw-bold mb-4">小功能測試區</h2>
    
    <!-- 僅限商家顯示的按鈕 -->
    <div class="card shadow-sm border-0 mb-4" v-if="authStore.user?.isStore">
      <div class="card-body">
        <h5 class="card-title fw-bold text-success">商家專屬功能</h5>
        <p class="card-text text-muted">您的身分是立案商家。您可以在此修改您的商店基本資料。這些修改將經歷與新商家註冊相同的審核流程。</p>
        <button class="btn btn-success" @click="openEditModal" :disabled="loading">
          <i class="bi bi-pencil-square me-2"></i> 修改商店基本資料
        </button>
      </div>
    </div>

    <!-- 非商家顯示區塊 (測試用) -->
    <div class="card shadow-sm border-0 bg-light" v-else>
      <div class="card-body">
        <h5 class="card-title fw-bold text-secondary">一般使用者區</h5>
        <p class="card-text text-muted">您的身分是一般使用者，無商家專屬功能。</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { storeRegistrationAPI } from '@/api/storeRegistration';
import Swal from 'sweetalert2';

const authStore = useAuthStore();
const loading = ref(false);

const openEditModal = async () => {
    loading.value = true;
    try {
        // 先嘗試抓取過去的申請紀錄，以帶入預設資料
        const res = await storeRegistrationAPI.getMyApplications();
        const apps = res.data || res || [];
        
        console.log("My applications:", apps);

        // 如果有正在審核中或退回的案件，需要提示他先去我的訊息處理
        const pendingOrReturned = apps.find(a => a.status === 'PENDING' || a.status === 'RETURNED');
        if (pendingOrReturned) {
            Swal.fire('無法申請', '您目前已經有一筆審核中或被退回的申請案，請至「我的訊息」查看或修改。', 'warning');
            loading.value = false;
            return;
        }

        // 找尋最後一次 APPROVED 的紀錄來作為預設值
        const approvedApps = apps.filter(a => a.status === 'APPROVED').sort((a,b) => b.id - a.id);
        const latestApproved = approvedApps.length > 0 ? approvedApps[0] : null;

        const defaultName = latestApproved?.name || authStore.user?.name || '';
        const defaultStoreName = latestApproved?.storeName || '';
        const defaultPhone = latestApproved?.phone || '';
        const defaultAddress = latestApproved?.address || '';

        loading.value = false;

        // 顯示 SweetAlert 編輯視窗
        const { value: formValues } = await Swal.fire({
            title: '修改商店基本資料',
            html: `
                <div class="text-start">
                    <div class="mb-3">
                        <label class="form-label fw-bold">負責人姓名 <span class="text-danger">*</span></label>
                        <input id="swal-owner-name" class="form-control" value="${defaultName}">
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">商家名稱</label>
                        <input id="swal-store-name" class="form-control" value="${defaultStoreName}" placeholder="若無可留空">
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">商家電話 <span class="text-danger">*</span></label>
                        <input id="swal-store-phone" class="form-control" value="${defaultPhone}">
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">商家地址 <span class="text-danger">*</span></label>
                        <input id="swal-store-address" class="form-control" value="${defaultAddress}">
                    </div>
                    <div class="alert alert-info mt-3 small mb-0">
                        <i class="bi bi-info-circle me-1"></i> 送出後需經管理員審核才會正式更新資料。<br>
                        審核進度可至「會員中心 > 我的訊息」追蹤。
                    </div>
                </div>
            `,
            focusConfirm: false,
            showCancelButton: true,
            confirmButtonText: '送出修改申請',
            cancelButtonText: '取消',
            confirmButtonColor: '#10b981',
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
            submitUpdateApplication(formValues);
        }

    } catch (error) {
        console.error('Failed to prepare edit modal', error);
        loading.value = false;
        Swal.fire('錯誤', '無法載入現有資料，請稍後再試。', 'error');
    }
};

const submitUpdateApplication = async (data) => {
    try {
        Swal.fire({
            title: '提交中...',
            allowOutsideClick: false,
            didOpen: () => { Swal.showLoading(); }
        });

        // 呼叫既有的 createApplication (後端已放寬 isStore)
        await storeRegistrationAPI.submitApplication(data);

        Swal.fire('提交成功', '您的修改申請已送出！管理員審核後即會更新。您可至「我的訊息」查看進度。', 'success');

    } catch (error) {
        console.error('Submit error:', error);
        let errorMsg = '提交失敗，請檢查資料或稍後再試。';
        if (error.response && error.response.data && error.response.data.error) {
             errorMsg = error.response.data.error;
        }
        Swal.fire('錯誤', errorMsg, 'error');
    }
};
</script>

<style scoped>
.test-view {
  animation: fadeIn 0.5s ease-out;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
