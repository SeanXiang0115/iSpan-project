import { defineStore } from 'pinia';
import productsData from '@/data/productsData.json'

export const useProductsDepot = defineStore('product', {
    state: () => ({
        products: productsData
    }),

    actions: {
        //更新庫存統一邏輯
        updateStock(id, type, amount) {
            const product = this.products.find(p => p.id === id);
            if (product) {
                if (type === 'add') {
                    product.stock += amount;
                } else if (type === 'set') {
                    product.stock = Math.max(0, amount);
                }
            }
        },

        deleteProduct(id) {
            const index = this.products.findIndex(p => String(p.id) === String(id));
            if (index !== -1) {
                this.products.splice(index, 1);
            }
        }
    }

})