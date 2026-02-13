import axios from 'axios';
import Swal from 'sweetalert2';

// Create an axios instance with custom config
const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api', // Default to a common backend port
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
});

// Request interceptor
api.interceptors.request.use(
    (config) => {
        // You can add token to headers here
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response interceptor
api.interceptors.response.use(
    (response) => {
        return response.data;
    },
    (error) => {
        // Handle global errors here
        if (error.response && error.response.status === 401) {
            // Unauthorized or Token Expired
            console.error('Session expired or unauthorized');

            const authStore = useAuthStore();
            authStore.handleLogoutAndNotify('timeout').then(() => {
                if (typeof window !== 'undefined') {
                    window.location.href = '/login';
                }
            });
        }

        console.error('API Error:', error.response || error.message);
        return Promise.reject(error);
    }
);

export default api;
