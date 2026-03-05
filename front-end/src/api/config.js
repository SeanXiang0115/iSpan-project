import axios from 'axios';
import { useAuthStore } from '@/stores/auth';
import { useAdminAuthStore } from '@/stores/adminAuth';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
    timeout: 10000,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
});

// ===== Refresh Token 防重複觸發機制 =====
let isRefreshing = false;
let failedQueue = [];

const processQueue = (error) => {
    failedQueue.forEach(({ resolve, reject }) => {
        if (error) reject(error);
        else resolve();
    });
    failedQueue = [];
};

// ===== Request Interceptor =====
api.interceptors.request.use(
    (config) => {
        console.log(`[API REQ] ${config.method?.toUpperCase()} ${config.baseURL}${config.url}`);
        return config;
    },
    (error) => Promise.reject(error)
);

// ===== Response Interceptor =====
api.interceptors.response.use(
    (response) => {
        console.log(`[API OK ] ${response.status} ${response.config.url}`);
        return response.data;
    },

    async (error) => {
        const originalRequest = error.config;
        const status = error.response?.status;
        const url = originalRequest?.url;

        console.warn(`[API ERR] ${status} ${url}`, error.response?.data?.message ?? error.message);

        // 非 401 或 已重試過 → 直接拋出
        if (!error.response || status !== 401 || originalRequest._retry) {
            console.warn('[REFRESH] 跳過：非401 或 已重試');
            return Promise.reject(error);
        }

        // 忽略 login / logout / refresh 端點
        const ignoredEndpoints = ['/login', '/logout', '/refresh'];
        const isIgnored = ignoredEndpoints.some(ep => url?.includes(ep));
        if (isIgnored) {
            console.warn(`[REFRESH] 跳過：${url} 在忽略清單`);
            return Promise.reject(error);
        }

        const isAdminContext = typeof window !== 'undefined' && window.location.pathname.startsWith('/admin');
        const refreshEndpoint = isAdminContext ? '/admins/refresh' : '/auth/refresh';

        console.log(`[REFRESH] 偵測到 401，準備呼叫 ${refreshEndpoint}`);
        console.log(`[REFRESH] isRefreshing 目前狀態: ${isRefreshing}`);

        // 若已有請求在 refresh 中，加入等待佇列
        if (isRefreshing) {
            console.log('[REFRESH] 加入等待佇列');
            return new Promise((resolve, reject) => {
                failedQueue.push({ resolve, reject });
            }).then(() => {
                console.log('[REFRESH] 佇列恢復，重送:', url);
                return api(originalRequest);
            }).catch(err => Promise.reject(err));
        }

        originalRequest._retry = true;
        isRefreshing = true;

        try {
            console.log(`[REFRESH] 呼叫 ${refreshEndpoint} ...`);
            const refreshResult = await api.post(refreshEndpoint);
            console.log('[REFRESH] Refresh 成功！回應:', refreshResult);
            processQueue(null);
            console.log(`[REFRESH] 重送原始請求: ${url}`);
            return api(originalRequest);
        } catch (refreshError) {
            console.error('[REFRESH] Refresh 失敗！', refreshError.response?.status, refreshError.response?.data);
            processQueue(refreshError);

            if (isAdminContext) {
                const adminAuthStore = useAdminAuthStore();
                adminAuthStore.handleLogoutAndNotify('timeout').then(() => {
                    window.location.href = '/admin/login';
                });
            } else {
                const authStore = useAuthStore();
                authStore.handleLogoutAndNotify('timeout').then(() => {
                    window.location.href = '/login';
                });
            }
            return Promise.reject(refreshError);
        } finally {
            isRefreshing = false;
            console.log('[REFRESH] isRefreshing 重置為 false');
        }
    }
);

export default api;
