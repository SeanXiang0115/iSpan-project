import { defineStore } from 'pinia'
import api from '@/api/config'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        // 不再從 localStorage 讀取 user 的詳細資料與 token
        user: null,
    }),

    getters: {
        // 判斷是否登入：依據記憶體中是否有 user 物件
        isLoggedIn: (state) => !!state.user,
        userName: (state) => state.user ? state.user.name : '',
    },

    actions: {
        login(userData) {
            this.user = userData;
            // 設立一個安全的不帶敏感資訊的 flag 讓重新整理時知道要抓資料
            localStorage.setItem('isUserLoggedIn', 'true');
        },
        async logout() {
            try {
                await api.post('/auth/logout');
            } catch (error) {
                console.error('Logout failed:', error);
            }
            this.logoutLocally();
        },
        // 僅清除前端狀態
        logoutLocally() {
            this.user = null;
            localStorage.removeItem('isUserLoggedIn');
        },
        updateUser(userData) {
            this.user = { ...this.user, ...userData };
        },
        checkAuth() {
            return this.isLoggedIn;
        },
        /**
         * 統一處理登出與提示
         * @param {string} type - 'timeout' (逾時) 或 'unauthorized' (未登入)
         */
        async handleLogoutAndNotify(type = 'timeout') {
            this.logoutLocally();
            const config = type === 'timeout'
                ? { title: '登入逾期', text: '您的登入工作階段已到期，請重新登入' }
                : { title: '請先登入', text: '此頁面需要登入後才能訪問' };

            const Swal = (await import('sweetalert2')).default;
            await Swal.fire({
                icon: 'warning',
                title: config.title,
                text: config.text,
                confirmButtonText: type === 'timeout' ? '重新登入' : '前往登入'
            });

            // 由於 store 無法直接存取 router，這部分留給呼叫端處理跳轉，或從外部傳入 router
        },
        async syncUserProfile() {

            try {
                const response = await api.get('/auth/me');

                // axios interceptor 已經回傳 response.data，所以這裡只需要再取 .data (ApiResponse 物件裡的 data)
                const latestUserData = response.data;

                this.updateUser(latestUserData);

            } catch (error) {
                console.error('同步使用者資料失敗:', error);

                if (error.response && [401, 403].includes(error.response.status)) {
                    // 如果 token 不合法或已過期，單純把前端狀態清掉即可，不要彈出 SweetAlert
                    // 讓 Vue Router 或當前畫面的 API 來決定是否需要踢回登入頁，避免干擾 Admin 畫面
                    this.logoutLocally();
                }
            }
        }
    }
})
