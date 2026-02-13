import { defineStore } from 'pinia';

export const useAdminAuthStore = defineStore('adminAuth', {
    state: () => ({
        // initialize state from local storage to enable user to stay logged in
        admin: JSON.parse(localStorage.getItem('adminUser')) || null,
        accessToken: localStorage.getItem('adminAccessToken') || null,
        refreshToken: localStorage.getItem('adminRefreshToken') || null,
    }),

    getters: {
        isLoggedIn: (state) => !!state.accessToken,
        adminName: (state) => state.admin ? state.admin.name : '',
        adminPosition: (state) => state.admin ? state.admin.position : '',
    },

    actions: {
        login(adminData, accessToken, refreshToken) {
            this.admin = adminData;
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;

            // store user details and jwt in local storage to keep user logged in between page refreshes
            localStorage.setItem('adminUser', JSON.stringify(adminData));
            localStorage.setItem('adminAccessToken', accessToken);
            localStorage.setItem('adminRefreshToken', refreshToken);
        },

        logout() {
            this.admin = null;
            this.accessToken = null;
            this.refreshToken = null;

            localStorage.removeItem('adminUser');
            localStorage.removeItem('adminAccessToken');
            localStorage.removeItem('adminRefreshToken');

            // cleanup old legacy keys if necessary
            localStorage.removeItem('adminAccount');
            localStorage.removeItem('adminName');
            localStorage.removeItem('adminPosition');
        }
    }
});
