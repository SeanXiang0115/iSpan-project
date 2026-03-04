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
     * @param {Object|FormData} data - 店家資訊物件或 FormData (包含圖片)
     */
    updateMyStoreInfo(data) {
        const config = {};
        if (data instanceof FormData) {
            // 當資料是 FormData 時，覆蓋全域的 application/json 設定
            config.headers = { 'Content-Type': 'multipart/form-data' };
        }
        return api.put('/owner/store/me', data, config);
    },

    /**
     * 獲取所有可用的店家標籤
     */
    getAllCategories() {
        return api.get('/categories');
    }
};

export default storeAPI;
