import { defineStore } from 'pinia'

export const useCartStore = defineStore('cart', {
    state: () => ({
        items: []
    }),

    getters: {
        totalQuantity(state) {
            return state.items.reduce((sum, item) => sum + item.quantity, 0)
        },
        totalPrice(state) {
            return state.items.reduce(
                (sum, item) => sum + item.price * item.quantity,
                0
            )
        }
    },

    actions: {
        addToCart(product) {
            const existing = this.items.find(i => i.id === product.id)
            if (existing) {
                existing.quantity++
            } else {
                this.items.push({ ...product, quantity: 1 })
            }
        },

        increase(id) {
            const item = this.items.find(i => i.id === id)
            if (item) item.quantity++
        },

        decrease(id) {
            const item = this.items.find(i => i.id === id)
            if (item && item.quantity > 1) item.quantity--
        },

        remove(id) {
            this.items = this.items.filter(i => i.id !== id)
        }
    }
})
