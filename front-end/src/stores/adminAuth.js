import { defineStore } from 'pinia';
import api from '@/api/config';

const safeJSONParse = (val) => {
    try {
        if (!val || val === 'undefined') return null;
        return JSON.parse(val);
    } catch (e) {
        console.warn('Failed to parse localStorage item', e);
        return null;
    }
};

export const useAdminAuthStore = defineStore('adminAuth', {
    state: () => ({
        // initialize state from local storage to enable user to stay logged in
        admin: null,
    }),

    getters: {
        isLoggedIn: (state) => !!state.admin,
        adminName: (state) => state.admin ? state.admin.name : '',
        adminPosition: (state) => state.admin ? state.admin.position : '',
        hasRole: (state) => (role) => state.admin && state.admin.position === role,
        hasAnyRole: (state) => (roles) => state.admin && roles.includes(state.admin.position),
    },

    actions: {
        login(adminData) {
            this.admin = adminData;

            // store user details and jwt in local storage to keep user logged in between page refreshes
            localStorage.setItem('isAdminLoggedIn', 'true');
        },

        async logout() {
            try {
                await api.post('/admins/logout');
            } catch (error) {
                console.error('Logout failed:', error);
            }
            this.logoutLocally();
        },

        logoutLocally() {
            this.admin = null;
            localStorage.removeItem('isAdminLoggedIn');

            // cleanup old legacy keys if necessary
            localStorage.removeItem('adminUser');
            localStorage.removeItem('adminAccessToken');
            localStorage.removeItem('adminRefreshToken');
            localStorage.removeItem('adminAccount');
            localStorage.removeItem('adminName');
            localStorage.removeItem('adminPosition');
        },

        async handleLogoutAndNotify(type = 'timeout') {
            this.logoutLocally();
            let config;
            if (type === 'idle') {
                config = { title: '閒置逾久，已自動登出', text: '閒置過久，已自動登出', confirmButtonText: '重新登入' };
            } else if (type === 'timeout') {
                config = { title: '登入逾期', text: '管理員登入工作階段已到期，請重新登入', confirmButtonText: '重新登入' };
            } else {
                config = { title: '請先登入', text: '此頁面需要管理員權限才能訪問', confirmButtonText: '前往登入' };
            }

            const Swal = (await import('sweetalert2')).default;
            await Swal.fire({
                icon: 'warning',
                title: config.title,
                text: config.text,
                confirmButtonText: config.confirmButtonText
            });
        },

        async syncAdminProfile() {

            try {
                const response = await api.get('/admins/me');

                // axios interceptor 已經回傳 response.data，所以這裡只需要再取 .data (ApiResponse 物件裡的 data)
                const latestAdminData = response.data;

                this.admin = { ...this.admin, ...latestAdminData };

            } catch (error) {
                console.error('同步管理員資料失敗:', error);

                if (error.response && [401, 403].includes(error.response.status)) {
                    // 如果 token 不合法或已過期，單純把前端狀態清掉即可，不要彈出 SweetAlert
                    // 讓 Vue Router 或當前畫面的 API 來決定是否需要踢回登入頁，避免干擾前台畫面
                    this.logoutLocally();
                }
            }
        }
    }
});
