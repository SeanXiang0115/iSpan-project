import api from './config';

export const adminAPI = {
    /**
     * 管理員登入
     * @param {Object} data - { account, password }
     */
    login(data) {
        return api.post('/admins/login', data);
    },

    /**
     * 註冊管理員
     * @param {Object} data - { account, name, position }
     */
    register(data) {
        return api.post('/admins', data);
    },

    /**
     * 取得所有管理員
     */
    getAll() {
        return api.get('/admins');
    }
};

export default adminAPI;
