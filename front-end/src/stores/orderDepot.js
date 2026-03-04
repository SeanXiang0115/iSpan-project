import { defineStore } from 'pinia'

//存放訂單相關資訊
export const useOrderDepot = defineStore('order', {
    state: () => ({
        orders: JSON.parse(localStorage.getItem('orders')) || []
    }),

    actions: {

        addOrder(orderData) {
            const newOrder = {
                id: `ORD${Date.now()}`, // 產生訂單編號
                ...orderData,
                status: orderData.status || '待處理', // 接收不到資料的時候初始狀態
                orderDate: new Date().toLocaleString(),
                lastUpdate: new Date().toLocaleString()
            }
            this.orders.unshift(newOrder) // 放最前面
            this.saveToLocalStorage()
        },

        saveToLocalStorage() {
            localStorage.setItem('orders', JSON.stringify(this.orders))
        },

        updateOrder(updatedOrder) {
            const index = this.orders.findIndex(o => o.id === updatedOrder.id)
            if (index !== -1) {
                this.orders[index] = {
                    ...updatedOrder,
                    lastUpdate: new Date().toLocaleString() // 更新最後修改時間
                }
                this.saveToLocalStorage()
            }
        },

        deleteOrder(orderId) {
            this.orders = this.orders.filter(o => o.id !== orderId)
            this.saveToLocalStorage()
        },

        saveToLocalStorage() {
            localStorage.setItem('orders', JSON.stringify(this.orders))
        }
    }
})