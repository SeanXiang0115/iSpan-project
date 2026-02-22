import { defineStore } from 'pinia';

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
        admin: safeJSONParse(localStorage.getItem('adminUser')),
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
