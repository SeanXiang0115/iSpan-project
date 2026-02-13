import api from './config';

export const storeAPI = {
    /**
     * 獲取當前登入店家的資訊
     */
    getMyStoreInfo() {
        return api.get('/owner/store/me');
    },

    /**
     * 更新當前店家的資訊
     * @param {Object} data - { storeName }
     */
    updateMyStoreInfo(data) {
        return api.put('/owner/store/me', data);
    }
};

export default storeAPI;
