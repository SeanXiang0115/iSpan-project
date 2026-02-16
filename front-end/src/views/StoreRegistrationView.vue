<script setup>
import { reactive, ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import BaseCard from '@/components/common/BaseCard.vue';
import BaseButton from '@/components/common/BaseButton.vue';
import Swal from 'sweetalert2';

const route = useRoute();
const router = useRouter();

const form = reactive({
  ownerName: '',
  storePhone: '',
  storeAddress: '',
  storeName: ''
});

const isSubmitting = ref(false);
const isEditMode = ref(false);
const registrationId = ref(null);

onMounted(async () => {
    // Check if there is an id in query for edit mode
    if (route.query.id) {
        isEditMode.value = true;
        registrationId.value = route.query.id;
        await fetchRegistrationData(route.query.id);
    }
});

const fetchRegistrationData = async (id) => {
    try {
        // Assume we have an API to get specific registration or use the 'my' endpoint and find it
        // Simpler: use the get-my-applications and find by ID, or if backend supports get-by-id for owner
        // For now, let's try fetching from /api/store-registrations/my and filtering
        const response = await axios.get('/api/store-registrations/my');
        const registration = response.data.find(r => r.id == id);
        
        if (registration) {
            form.ownerName = registration.name;
            form.storePhone = registration.phone;
            form.storeAddress = registration.address;
            form.storeName = registration.storeName || '';
        } else {
            Swal.fire('錯誤', '找不到該申請資料', 'error');
            router.push('/user-info'); // Redirect back
        }
    } catch (error) {
        console.error('Failed to fetch data', error);
        Swal.fire('錯誤', '載入資料失敗', 'error');
    }
};

const handleSubmit = async () => {
  if (isSubmitting.value) return;

  isSubmitting.value = true;
  
  const submissionData = { ...form };

  try {
    let response;
    if (isEditMode.value) {
        response = await axios.put(`/api/store-registrations/${registrationId.value}`, submissionData);
        Swal.fire({
            title: '重送成功',
            text: '您的申請已更新並重新送出審核',
            icon: 'success',
            confirmButtonText: '確定',
            confirmButtonColor: '#9f9572'
        }).then(() => {
            router.push('/user-info');
        });
    } else {
        response = await axios.post('/api/store-registrations', submissionData);
        Swal.fire({
            title: '申請已送出',
            text: '我們已收到您的商家註冊申請，將盡快進行審核',
            icon: 'success',
            confirmButtonText: '確定',
            confirmButtonColor: '#9f9572'
        }).then(() => {
            router.push('/user-info');
        });
    }
    
  } catch (error) {
    console.error('Registration failed:', error);
    const msg = error.response?.data?.error || '註冊失敗，請稍後再試。';
    
    // Check specific error messages from backend
    if (msg.includes("already has a pending")) {
        Swal.fire('重複申請', '您已有審核中或待處理的申請，請勿重複提交。', 'warning');
    } else if (msg.includes("already a store")) {
         Swal.fire('已是商家', '您已經是商家身分，無需再次申請。', 'info');
    } else {
        Swal.fire('錯誤', msg, 'error');
    }
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<template>
  <div class="store-registration-page bg-gdg-light min-vh-100 py-5">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
          <BaseCard :shadow="true" :hoverEffect="false">
            <template #header>
              <div class="text-center py-2">
                <h2 class="text-gdg fw-bold mb-0">{{ isEditMode ? '修改商家申請' : '申請成為商家' }}</h2>
                <p class="text-muted small mt-2">請填寫以下資訊完成註冊申請</p>
              </div>
            </template>

            <form @submit.prevent="handleSubmit" class="p-2">
              <div class="mb-4">
                <label for="ownerName" class="form-label fw-bold text-dark">負責人姓名</label>
                <input 
                  type="text" 
                  id="ownerName" 
                  v-model="form.ownerName" 
                  class="form-control custom-input" 
                  placeholder="請輸入負責人姓名" 
                  required
                >
              </div>

              <div class="mb-4">
                <label for="storeName" class="form-label fw-bold text-dark">商家名稱</label>
                <input 
                  type="text" 
                  id="storeName" 
                  v-model="form.storeName" 
                  class="form-control custom-input" 
                  placeholder="請輸入商家名稱 (選填)" 
                >
              </div>

              <div class="mb-4">
                <label for="storePhone" class="form-label fw-bold text-dark">商家電話</label>
                <input 
                  type="tel" 
                  id="storePhone" 
                  v-model="form.storePhone" 
                  class="form-control custom-input" 
                  placeholder="例如: 02-12345678" 
                  required
                >
              </div>

              <div class="mb-4">
                <label for="storeAddress" class="form-label fw-bold text-dark">商家地址</label>
                <textarea 
                  id="storeAddress" 
                  v-model="form.storeAddress" 
                  class="form-control custom-input" 
                  rows="3" 
                  placeholder="請輸入完整的商家營業地址" 
                  required
                ></textarea>
              </div>

              <div class="d-grid gap-2 mt-5">
                <BaseButton 
                  type="submit" 
                  color="gdg" 
                  size="lg" 
                  class="fw-bold" 
                  :disabled="isSubmitting"
                >
                  {{ isSubmitting ? '處理中...' : (isEditMode ? '更新並重新送出' : '提交註冊申請') }}
                </BaseButton>
              </div>
            </form>
          </BaseCard>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.text-gdg {
  color: #9f9572;
}

.custom-input {
  border: 1px solid #ced4da;
  border-radius: 4px; /* Using slightly rounded but clean design */
  padding: 0.75rem 1rem;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.custom-input:focus {
  border-color: #9f9572;
  box-shadow: 0 0 0 0.2rem rgba(159, 149, 114, 0.1);
  outline: none;
}

/* Match the sharp design mentioned in custom.scss comments if preferred */
/* .custom-input { border-radius: 0; } */
</style>
