import api from './config';

export const storeRegistrationAPI = {
    /**
     * 提交新申請
     * @param {Object} data 
     */
    submitApplication(data) {
        return api.post('/store-registrations', data);
    },

    /**
     * 更新並重新提交申請
     * @param {Number} id 
     * @param {Object} data 
     */
    updateApplication(id, data) {
        return api.put(`/store-registrations/${id}`, data);
    },

    /**
     * 取得使用者的申請紀錄
     */
    getMyApplications() {
        return api.get('/store-registrations/my');
    },

    /**
     * 管理員取得所有申請 (支援分頁與篩選)
     * @param {Object} params { status, page, size }
     */
    getAllApplications(params) {
        return api.get('/store-registrations', { params });
    },

    /**
     * 管理員審核申請
     * @param {Number} id 
     * @param {Object} data { action: 'approve' | 'reject', opinion: '...' }
     */
    reviewApplication(id, data) {
        return api.put(`/store-registrations/${id}/review`, data);
    }
};

export default storeRegistrationAPI;
