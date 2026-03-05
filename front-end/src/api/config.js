import axios from 'axios';
import Swal from 'sweetalert2';
import { useAuthStore } from '@/stores/auth';
import { useAdminAuthStore } from '@/stores/adminAuth';

// Create an axios instance with custom config
const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api', // Default to a common backend port
    timeout: 10000,
    withCredentials: true, // 允許跨域請求攜帶 HttpOnly Cookie
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
});

// Request interceptor
api.interceptors.request.use(
    (config) => {
        // Token 現在由 HttpOnly Cookie 負責，後端會自動從 Cookie 中讀取
        // 因此不再需要手動從 localStorage 撈取 Token 並塞入 Authorization Header
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response interceptor
api.interceptors.response.use(
    (response) => {
        return response.data;
    },
    (error) => {
        // Handle global errors here
        if (error.response && error.response.status === 401) {
            // Ignore 401 errors from login, logout, and token check endpoints 
            // so components and stores can handle them without global re-directs disrupting UX
            const isIgnoredEndpoint = error.config && error.config.url && (
                error.config.url.includes('/login') ||
                error.config.url.includes('/auth/login') ||
                error.config.url.includes('/logout') ||
                error.config.url.includes('/me')
            );

            if (!isIgnoredEndpoint) {
                // Unauthorized or Token Expired
                console.error('Session expired or unauthorized');

                // 根據當前網頁路徑判斷是前台還是後台，而不是根據 API URL
                const isAdminContext = typeof window !== 'undefined' && window.location.pathname.startsWith('/admin');

                if (isAdminContext) {
                    const adminAuthStore = useAdminAuthStore();
                    adminAuthStore.handleLogoutAndNotify('timeout').then(() => {
                        if (typeof window !== 'undefined') {
                            window.location.href = '/admin/login';
                        }
                    });
                } else {
                    const authStore = useAuthStore();
                    authStore.handleLogoutAndNotify('timeout').then(() => {
                        if (typeof window !== 'undefined') {
                            window.location.href = '/login';
                        }
                    });
                }
            }
        }

        console.error('API Error:', error.response || error.message);
        return Promise.reject(error);
    }
);

export default api;
