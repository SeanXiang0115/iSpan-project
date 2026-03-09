import { defineStore } from 'pinia';
import axios from 'axios';

export const useProductsDepot = defineStore('productDepot', {
    state: () => ({
        products: []
    }),

    actions: {
        //更新庫存統一邏輯
        async fetchProducts() {
            const getCategory = (name) => {
                if (!name) return '其他'
                if (['鮮蔬', '鮮果', '蔬菜', '水果', '柿', '果禮盒'].some(k => name.includes(k))) return '生鮮'
                if (['果糖', '辛香料', '香料'].some(k => name.includes(k))) return '食品'
                if (['餐具', '護理', '運動瓶', '髮蠟', '紙袋'].some(k => name.includes(k))) return '日常用品'
                return '其他'
            }

            try {
                const response = await axios.get(`http://localhost:8080/api/products/all`)  // ← 這行不見了

                this.products = response.data.map(p => ({
                    id: p.productId,
                    productName: p.productName,
                    price: p.price,
                    description: p.productDescription,
                    image: p.image,
                    stock: p.stock ? p.stock.availableQuantity : 0,
                    added: p.added,
                    productCode: p.productCode,
                    lastUpdate: p.stock ? p.stock.updateAt : null,
                    category: getCategory(p.productName)
                }))
            } catch (error) {
                console.error("獲取資料失敗", error)
            }
        },

        async updateStock(id, type, amount) {
            try {
                await axios.put(`http://localhost:8080/api/products/updateStock`, {
                    productId: id,
                    type: type,
                    amount: amount
                });
                await this.fetchProducts();
            } catch (error) {
                console.error("更新庫存失敗", error)
            }
        },


        async deleteProduct(id) {

            try {
                await axios.delete(`http://localhost:8080/api/products/${id}`);

                const index = this.products.findIndex(p => String(p.id) === String(id));
                if (index !== -1) {
                    this.products.splice(index, 1);
                    console.log(`商品ID ${id}已成功從資料庫與前端刪除`)
                }
            } catch (error) {
                console.error("刪除失敗", error)
                throw error;
            }


        }
    }

})