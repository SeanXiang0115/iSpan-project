<template>
  <div class="user-info-content">
    <h4 class="mb-3 border-bottom pb-2">{{ title }}</h4>
    
    <template v-if="title === '個人資料'">
      <!-- 個人資料基本資訊可以放這裡 -->
      <div class="mb-4">
        <p class="text-muted">這裡是您的個人資料設定區塊。</p>
      </div>

      <!-- 2FA 設定區塊 -->
      <div class="card mt-4 shadow-sm border-0">
        <div class="card-header bg-white border-bottom-0 pt-4 pb-2">
          <h5 class="mb-0 text-gdg fw-bold"><i class="bi bi-shield-lock-fill me-2"></i>雙因素驗證 (2FA)</h5>
        </div>
        <div class="card-body">
          <div v-if="loading2FA" class="text-center py-3">
            <div class="spinner-border text-gdg" role="status">
              <span class="visually-hidden">載入中...</span>
            </div>
          </div>
          
          <div v-else>
            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-3">
              <div class="mb-3 mb-md-0">
                <h6 class="mb-1 fw-bold">Google Authenticator (TOTP)</h6>
                <p class="text-muted mb-0 small" style="max-width: 500px;">
                  啟用雙因素驗證可為您的帳號增加額外的安全性。登入時除了密碼，還需要輸入由手機 App (例如 Google Authenticator 或 Authy) 產生的驗證碼。
                </p>
              </div>
              <div class="text-md-end">
                <div v-if="is2FAEnabled" class="badge bg-success bg-opacity-10 text-success px-3 py-2 rounded-pill border border-success border-opacity-25 fs-6">
                  <i class="bi bi-check-circle-fill me-1"></i> 已啟用
                </div>
                <div v-else class="badge bg-secondary bg-opacity-10 text-secondary px-3 py-2 rounded-pill border border-secondary border-opacity-25 fs-6">
                  <i class="bi bi-dash-circle-fill me-1"></i> 未啟用
                </div>
              </div>
            </div>

            <!-- 操作按鈕 -->
            <div class="mt-4 pt-3 border-top">
              <button v-if="is2FAEnabled" @click="handleDisable2FA" class="btn btn-outline-danger px-4" :disabled="isSubmitting">
                <i class="bi bi-shield-x me-2"></i>停用 2FA
              </button>
              <button v-else @click="handleEnable2FA" class="btn btn-gdg text-white px-4" :disabled="isSubmitting">
                <i class="bi bi-shield-check me-2"></i>設定 2FA
              </button>
            </div>

            <!-- QR Code 顯示區塊 (設定中) -->
            <div v-if="showSetup" class="mt-4 p-4 border rounded bg-light" style="border-style: dashed !important; border-color: #adb5bd !important;">
              <h6 class="fw-bold mb-3 text-center text-dark">設定雙因素驗證</h6>
              <ol class="small mb-4 text-muted mx-auto" style="max-width: 450px;">
                <li class="mb-2">在您的手機上下載並安裝 <strong>Google Authenticator</strong> 或其他 TOTP 支援的 App。</li>
                <li class="mb-2">在 App 中選擇「掃描 QR Code」並對準下方圖片：</li>
              </ol>
              
              <div class="text-center mb-4">
                <template v-if="qrCodeImage">
                  <img :src="qrCodeImage" alt="2FA QR Code" class="img-fluid border p-2 bg-white rounded shadow-sm" style="max-width: 200px;">
                </template>
                <div class="mt-3 text-muted small">
                  如果無法掃描，請手動輸入設定金鑰：<br>
                  <code class="fs-6 user-select-all bg-white px-2 py-1 rounded border d-inline-block mt-1 text-dark">{{ secretKey }}</code>
                </div>
              </div>

              <div class="row justify-content-center">
                <div class="col-md-10 col-lg-8">
                  <label class="form-label small fw-bold text-dark">3. 輸入 App 上的 6 位數驗證碼來完成設定：</label>
                  <div class="input-group mb-3 shadow-sm rounded">
                    <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-123"></i></span>
                    <input type="text" v-model="verifyCode" class="form-control border-start-0 ps-0 text-center fw-bold text-tracking-widest fs-5" placeholder="• • • • • •" maxlength="6" inputmode="numeric" pattern="[0-9]*" />
                    <button class="btn btn-gdg px-4 fw-bold" @click="submitVerify2FA" :disabled="isSubmitting || verifyCode.length !== 6">
                      <span v-if="isSubmitting" class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                      驗證並啟用
                    </button>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </template>

    <template v-else>
      <div class="user-info-placeholder">
        <p class="text-muted">此處將放置{{ title }}的詳細內容。</p>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import userAPI from '@/api/user';
import Swal from 'sweetalert2';

const props = defineProps({
  title: {
    type: String,
    default: '項目'
  }
});

const is2FAEnabled = ref(false);
const loading2FA = ref(false);
const isSubmitting = ref(false);

const showSetup = ref(false);
const qrCodeImage = ref('');
const secretKey = ref('');
const verifyCode = ref('');

const fetch2FAStatus = async () => {
  if (props.title !== '個人資料') return;
  
  loading2FA.value = true;
  try {
    const response = await userAPI.get2FAStatus();
    // The response is already unwrapped by axios interceptor
    is2FAEnabled.value = response.data || false;
  } catch (error) {
    console.error('Error fetching 2FA status:', error);
  } finally {
    loading2FA.value = false;
  }
};

onMounted(() => {
  fetch2FAStatus();
});

const handleEnable2FA = async () => {
  isSubmitting.value = true;
  showSetup.value = false;
  verifyCode.value = '';
  
  try {
    const response = await userAPI.enable2FA();
    // axios interceptor 已經回傳了 response.data (即 ApiResponse 本身)
    const data = response.data;
    if (data) {
      qrCodeImage.value = data.qrCodeImage;
      secretKey.value = data.secret;
      showSetup.value = true;
    }
  } catch (error) {
    Swal.fire('錯誤', error.response?.data?.message || '產生 2FA 金鑰失敗', 'error');
  } finally {
    isSubmitting.value = false;
  }
};

const submitVerify2FA = async () => {
  if (verifyCode.value.length !== 6) return;
  
  isSubmitting.value = true;
  try {
    await userAPI.verify2FA({ code: verifyCode.value });
    
    await Swal.fire({
      icon: 'success',
      title: '設定成功',
      text: '雙因素驗證已成功啟用！',
      confirmButtonColor: '#9f9572'
    });
    
    is2FAEnabled.value = true;
    showSetup.value = false;
    verifyCode.value = '';
  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: '驗證失敗',
      text: error.response?.data?.message || '驗證碼無效，請確認後重新輸入',
      confirmButtonColor: '#9f9572'
    });
  } finally {
    isSubmitting.value = false;
  }
};

const handleDisable2FA = async () => {
  const result = await Swal.fire({
    title: '確定要停用 2FA 嗎？',
    text: '停用後您的帳號安全性可能降低。未來登入將不再需要驗證碼。',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#dc3545',
    cancelButtonColor: '#6c757d',
    confirmButtonText: '確定停用',
    cancelButtonText: '取消',
    reverseButtons: true
  });

  if (result.isConfirmed) {
    isSubmitting.value = true;
    try {
      await userAPI.disable2FA();
      await Swal.fire({
        icon: 'success',
        title: '已停用',
        text: '您的雙因素驗證已停用。',
        confirmButtonColor: '#9f9572',
        timer: 2000,
        showConfirmButton: false
      });
      is2FAEnabled.value = false;
      showSetup.value = false;
    } catch (error) {
      Swal.fire('錯誤', error.response?.data?.message || '停用失敗', 'error');
    } finally {
      isSubmitting.value = false;
    }
  }
};
</script>

<style scoped>
.text-gdg {
  color: #9f9572 !important;
}

.btn-gdg {
  background-color: #9f9572 !important;
  border-color: #9f9572 !important;
}

.btn-gdg:hover:not(:disabled) {
  background-color: #8f8562 !important;
  border-color: #8f8562 !important;
}

.btn-gdg:disabled {
  opacity: 0.7;
}

.text-tracking-widest {
  letter-spacing: 0.5em;
}
</style>
