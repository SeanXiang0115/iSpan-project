<template>
  <div class="store-registration-check-view">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2 class="text-admin-primary fw-bold mb-0">商家註冊審核</h2>
    </div>

    <!-- Status Tabs -->
    <div class="mb-4">
      <ul class="nav nav-tabs admin-tabs">
        <li class="nav-item">
          <a 
            class="nav-link" 
            :class="{ active: activeTab === 'all' }" 
            href="#" 
            @click.prevent="activeTab = 'all'"
          >
            全部
          </a>
        </li>
        <li class="nav-item">
          <a 
            class="nav-link" 
            :class="{ active: activeTab === 'pending' }" 
            href="#" 
            @click.prevent="activeTab = 'pending'"
          >
            未處理
          </a>
        </li>
        <li class="nav-item">
          <a 
            class="nav-link" 
            :class="{ active: activeTab === 'returned' }" 
            href="#" 
            @click.prevent="activeTab = 'returned'"
          >
            退回中
          </a>
        </li>
        <li class="nav-item">
          <a   
            class="nav-link" 
            :class="{ active: activeTab === 'approved' }" 
            href="#" 
            @click.prevent="activeTab = 'approved'"
          >
            已結案
          </a>
        </li>
      </ul>
      <div class="ms-3 d-flex align-items-center">
        <label class="me-2 fw-medium text-muted mb-0">申請類別:</label>
        <select v-model="filterType" class="form-select form-select-sm w-auto border-0 bg-light shadow-sm">
          <option value="">全部</option>
          <option value="false">初次註冊</option>
          <option value="true">資料修改</option>
        </select>
      </div>
    </div>

    <div class="admin-card overflow-hidden border-top-0 rounded-top-0">
      <div class="table-responsive">
        <table class="admin-table">
          <thead>
            <tr>
              <th>案件編號</th>
              <th>申請人帳號</th>
              <th>申請人姓名</th>
              <th>申請時間</th>
              <th>申請類別</th>
              <th>案件狀態</th>
              <th>審核</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in paginatedData" :key="item.id">
              <td>#{{ item.id }}</td>
              <td>{{ item.email }}</td>
              <td>
                <span class="fw-medium">{{ item.name }}</span>
              </td>
              <td>{{ formatDate(item.createdAt) }}</td>
              <td>
                <span class="badge" :class="item.isUpdate ? 'bg-primary' : 'bg-success'">
                  {{ item.isUpdate ? '資料修改' : '初次註冊' }}
                </span>
              </td>
              <td>
                <span :class="['status-badge', getStatusClass(item.status)]">
                  {{ getStatusDisplay(item.status) }}
                </span>
              </td>
              <td>
                <button 
                  class="btn-admin-outline-primary btn-sm" 
                  @click="openAuditModal(item)"
                  :disabled="item.status === 'APPROVED'"
                >
                  <i class="bi bi-file-earmark-check me-1"></i>
                  審核
                </button>
              </td>
            </tr>
            <tr v-if="paginatedData.length === 0">
              <td colspan="7" class="text-center py-5 text-muted">
                沒有找到符合狀態的申請資料
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination Section -->
      <div class="pagination-container border-top" v-if="totalItems > 0">
        <div class="pagination-info">
          顯示第 {{ startIndex + 1 }} 至 {{ Math.min(endIndex, totalItems) }} 筆，共 {{ totalItems }} 筆
        </div>
        <nav class="pagination-nav">
          <button 
            class="pagination-btn" 
            :disabled="currentPage === 1" 
            @click="changePage(currentPage - 1)"
          >
            <i class="bi bi-chevron-left"></i>
          </button>
          
          <div class="pagination-pages" v-if="totalPages <= 7">
            <button 
              v-for="page in totalPages" 
              :key="page" 
              :class="['pagination-btn', { active: currentPage === page }]"
              @click="changePage(page)"
            >
              {{ page }}
            </button>
          </div>
          <div class="pagination-pages" v-else>
            <button 
              v-for="page in visiblePages" 
              :key="page" 
              :class="['pagination-btn', { active: currentPage === page }]"
              @click="changePage(page)"
            >
              {{ page }}
            </button>
          </div>

          <button 
            class="pagination-btn" 
            :disabled="currentPage === totalPages" 
            @click="changePage(currentPage + 1)"
          >
            <i class="bi bi-chevron-right"></i>
          </button>
        </nav>
      </div>
    </div>

    <!-- Review Modal -->
    <div class="modal fade" :class="{ 'show d-block': showReviewModal }" tabindex="-1" aria-labelledby="reviewModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="reviewModalLabel">審核申請 #{{ selectedRegistration?.id }}</h5>
            <button type="button" class="btn-close" @click="closeReviewModal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div class="text-start">
              <div class="mb-3 p-3 bg-light rounded">
                <h6 class="fw-bold mb-2 border-bottom pb-2">申請人資料</h6>
                <p class="mb-1"><strong>姓名:</strong> {{ selectedRegistration?.name }}</p>
                <p class="mb-1"><strong>電話:</strong> {{ selectedRegistration?.phone }}</p>
                <p class="mb-0"><strong>地址:</strong> {{ selectedRegistration?.address }}</p>
                <p v-if="selectedRegistration?.storeName" class="mb-0 mt-1"><strong>商家名稱:</strong> {{ selectedRegistration?.storeName }}</p>
              </div>
              <div class="mb-3">
                <label for="audit-opinion" class="form-label fw-bold">審核意見</label>
                <textarea id="audit-opinion" class="form-control" rows="3" placeholder="請輸入審核意見 (退回時必填)" v-model="reviewOpinion"></textarea>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeReviewModal">取消</button>
            <button type="button" class="btn btn-danger" @click="reviewAction = 'reject'; submitReview()">退回申請</button>
            <button type="button" class="btn btn-success" @click="reviewAction = 'approve'; submitReview()">同意申請</button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="showReviewModal" class="modal-backdrop fade show"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import Swal from 'sweetalert2';
import { storeRegistrationAPI } from '@/api/storeRegistration';

// 狀態對應 (Backend Enum -> Frontend Tab Value)
const STATUS_MAP = {
    'pending': 'PENDING',
    'approved': 'APPROVED',
    'returned': 'RETURNED'
};

const activeTab = ref('pending'); // pending, approved, returned
const filterType = ref(''); // '', 'true', 'false'
const registrations = ref([]);
const loading = ref(false);
const currentPage = ref(0); // 0-indexed
const pageSize = ref(10);
const totalPages = ref(0);
const totalItems = ref(0); // Added for pagination info

// Modal related
const showReviewModal = ref(false);
const selectedRegistration = ref(null);
const reviewAction = ref('approve'); // approve, reject
const reviewOpinion = ref('');

const fetchRegistrations = async () => {
    loading.value = true;
    try {
        const status = STATUS_MAP[activeTab.value];
        const params = {
            page: currentPage.value, // API is 0-indexed
            size: pageSize.value
        };
        
        if (status) {
            params.status = status;
        }
        if (filterType.value !== '') {
            params.isUpdate = filterType.value === 'true';
        }
        
        const response = await storeRegistrationAPI.getAllApplications(params);
        
        // Ensure we handle Page<T> format
        registrations.value = response.data?.content || response.content || [];
        totalPages.value = response.data?.totalPages || response.totalPages || 0;
        totalItems.value = response.data?.totalElements || response.totalElements || 0;
        
    } catch (error) {
        console.error('Fetch error:', error);
        Swal.fire('錯誤', '無法載入申請資料', 'error');
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    fetchRegistrations();
});

watch(activeTab, () => {
    currentPage.value = 0; // Reset to first page on tab change
    fetchRegistrations();
});

watch(filterType, () => {
    currentPage.value = 0; // Reset to first page on filter change
    fetchRegistrations();
});

// Helper for status badge class
const getStatusClass = (status) => {
  switch (status) {
    case 'APPROVED':
        return 'status-active'; // Green
    case 'RETURNED':
        return 'status-banned'; // Red
    case 'PENDING':
        return 'status-pending'; // Amber
    default:
        return 'status-user'; // Gray
  }
};

// Map backend status to display text
const getStatusDisplay = (status) => {
    switch (status) {
        case 'APPROVED': return '已結案';
        case 'RETURNED': return '退回中';
        case 'PENDING': return '未處理';
        default: return status;
    }
};

// Formatting Date
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleString('zh-TW', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// Pagination computed properties
const startIndex = computed(() => currentPage.value * pageSize.value);
const endIndex = computed(() => startIndex.value + (registrations.value ? registrations.value.length : 0));
const paginatedData = computed(() => registrations.value || []);

const visiblePages = computed(() => {
  const pages = [];
  const maxVisible = 5;
  let start = Math.max(1, currentPage.value - 2);
  let end = Math.min(totalPages.value, start + maxVisible - 1);
  
  if (end - start + 1 < maxVisible) {
    start = Math.max(1, end - maxVisible + 1);
  }
  
  for (let i = start; i <= end; i++) {
    pages.push(i);
  }
  return pages;
});

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
    fetchRegistrations();
  }
};

// Audit Logic
const openAuditModal = async (item) => {
  const { value: result } = await Swal.fire({
    title: `審核申請 #${item.id}`,
    html: `
      <div class="text-start">
        <div class="mb-3 p-3 bg-light rounded">
          <h6 class="fw-bold mb-2 border-bottom pb-2">申請人資料</h6>
          <p class="mb-1"><strong>姓名:</strong> ${item.name}</p>
          <p class="mb-1"><strong>電話:</strong> ${item.phone}</p>
          <p class="mb-0"><strong>地址:</strong> ${item.address}</p>
          ${item.storeName ? `<p class="mb-0 mt-1"><strong>商家名稱:</strong> ${item.storeName}</p>` : ''}
        </div>
        <div class="mb-3">
          <label class="form-label fw-bold">審核意見</label>
          <textarea id="audit-opinion" class="form-control" rows="3" placeholder="請輸入審核意見 (退回時必填)"></textarea>
        </div>
      </div>
    `,
    showDenyButton: true,
    showCancelButton: true,
    confirmButtonText: '同意申請',
    denyButtonText: '退回申請',
    cancelButtonText: '取消',
    confirmButtonColor: '#10b981', // Green
    denyButtonColor: '#ef4444', // Red
    width: '600px',
    preConfirm: () => {
      const container = Swal.getHtmlContainer();
      return {
        action: 'approve',
        opinion: container.querySelector('#audit-opinion').value,
        lastUpdatedAt: item.updatedAt
      };
    },
    preDeny: () => {
      const container = Swal.getHtmlContainer();
      const opinion = container.querySelector('#audit-opinion').value;
      if (!opinion || !opinion.trim()) {
        Swal.showValidationMessage('退回申請時必須填寫審核意見');
        return false;
      }
      return {
        action: 'reject', // Changed to match Backend API expectation
        opinion: opinion.trim(),
        lastUpdatedAt: item.updatedAt
      };
    }
  });

  if (result) {
    if (result.action === 'approve') {
       handleApprove(item, result.opinion, result.lastUpdatedAt);
    } else if (result.action === 'reject') {
       handleReturn(item, result.opinion, result.lastUpdatedAt);
    }
  }
};

const handleApprove = async (item, opinion, lastUpdatedAt) => {
  const confirmResult = await Swal.fire({
    title: '確認要同意這筆申請?',
    html: `
      <div class="text-start bg-light p-3 rounded small">
        <p class="mb-1"><strong>姓名:</strong> ${item.name}</p>
        <p class="mb-1"><strong>電話:</strong> ${item.phone}</p>
        <p class="mb-0"><strong>地址:</strong> ${item.address}</p>
      </div>
      <p class="mt-3 text-muted small">此操作將把案件狀態改為「已結案」</p>
    `,
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '確認同意',
    cancelButtonText: '取消',
    confirmButtonColor: '#10b981'
  });

  if (confirmResult.isConfirmed) {
    try {
      await storeRegistrationAPI.reviewApplication(item.id, {
          action: 'approve',
          opinion: opinion,
          lastUpdatedAt: lastUpdatedAt
      });
      
      Swal.fire('已結案', '申請已成功同意', 'success');
      fetchRegistrations(); // Reload data

    } catch (error) {
      console.error(error);
      Swal.fire('錯誤', '更新失敗: ' + (error.response?.data?.error || error.message), 'error');
    }
  }
};

const handleReturn = async (item, opinion, lastUpdatedAt) => {
  const confirmResult = await Swal.fire({
    title: '確認要退回這筆申請?',
    html: `
      <div class="text-start">
        <label class="fw-bold">您的審核意見:</label>
        <div class="p-2 bg-light border rounded text-danger mt-1">
          ${opinion}
        </div>
      </div>
      <p class="mt-3 text-muted small">此操作將把案件狀態改為「退回中」</p>
    `,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '確認退回',
    cancelButtonText: '取消',
    confirmButtonColor: '#ef4444'
  });

  if (confirmResult.isConfirmed) {
    try {
      await storeRegistrationAPI.reviewApplication(item.id, {
          action: 'reject',
          opinion: opinion,
          lastUpdatedAt: lastUpdatedAt
      });

      Swal.fire('已退回', '申請已退回給使用者', 'success');
      fetchRegistrations(); // Reload data

    } catch (error) {
       console.error(error);
      Swal.fire('錯誤', '更新失敗: ' + (error.response?.data?.error || error.message), 'error');
    }
  }
};

</script>

<style lang="scss" scoped>
@import '@/assets/styles/custom.scss';

.store-registration-check-view {
  animation: fadeIn 0.5s ease-out;
}

.admin-tabs {
  border-bottom: 1px solid #e5e7eb;
  
  .nav-item {
    margin-bottom: -1px;
    
    .nav-link {
        color: #6b7280;
        border: none;
        border-bottom: 2px solid transparent;
        padding: 0.75rem 1.5rem;
        font-weight: 500;
        transition: all 0.2s ease;
        
        &:hover {
            color: #1e3c72;
            background-color: transparent;
        }
        
        &.active {
            color: #1e3c72;
            background-color: transparent;
            border-bottom-color: #1e3c72;
            font-weight: 600;
        }
    }
  }
}

.table-responsive {
  margin: 0;
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 500;
  display: inline-block;

  &.status-active {
    background-color: rgba(16, 185, 129, 0.1);
    color: #10b981;
  }

  &.status-banned {
    background-color: rgba(239, 68, 68, 0.1);
    color: #ef4444;
  }
  
  &.status-pending {
     background-color: rgba(245, 158, 11, 0.1); /* Amber/Orange */
     color: #f59e0b;
  }

  &.status-user {
    background-color: rgba(107, 114, 128, 0.1);
    color: #6b7280;
  }
}

.btn-admin-outline-primary {
    color: #1e3c72;
    border: 1px solid #1e3c72;
    background: transparent;
    transition: all 0.2s;
    
    &:hover:not(:disabled) {
        background-color: #1e3c72;
        color: white;
    }
    
    &:disabled {
        border-color: #e5e7eb;
        color: #9ca3af;
        cursor: not-allowed;
    }
}

// Pagination Styles (Copied from UserListView for consistency)
.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.5rem;
  background-color: #fff;

  .pagination-info {
    font-size: 0.875rem;
    color: #6b7280;
  }

  .pagination-nav {
    display: flex;
    align-items: center;
    gap: 0.5rem;
  }

  .pagination-pages {
    display: flex;
    gap: 0.25rem;
  }

  .pagination-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: 32px;
    height: 32px;
    padding: 0 6px;
    border: 1px solid #e5e7eb;
    background-color: #fff;
    color: #374151;
    border-radius: 6px;
    font-size: 0.875rem;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover:not(:disabled) {
      border-color: #1e3c72;
      color: #1e3c72;
      background-color: rgba(30, 60, 114, 0.05);
    }

    &.active {
      background-color: #1e3c72;
      border-color: #1e3c72;
      color: #fff;
    }

    &:disabled {
      cursor: not-allowed;
      opacity: 0.5;
      background-color: #f9fafb;
    }
  }
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
