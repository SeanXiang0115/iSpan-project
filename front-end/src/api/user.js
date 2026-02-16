import api from './config';

export const userAPI = {
    /**
     * 取得當前登入者詳細資料 (後端驗證測試)
     */
    getProfile() {
        return api.get('/users/profile');
    }
};

export default userAPI;
