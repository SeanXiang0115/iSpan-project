<script setup>
import {useCartStore} from '@/stores/cart'
import { useProductsDepot } from '@/stores/productsDepot';
import Swal from 'sweetalert2';

import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import BaseCard from '@/components/common/BaseCard.vue';
import BaseButton from '@/components/common/BaseButton.vue';

const router = useRouter();
const cartStore = useCartStore()
const depot = useProductsDepot();


const goToDetail = (id) =>{
    //點擊進入商品詳情
    router.push({name:'productsDetail', params:{id}});
};

const addToCart = (item) => {
    cartStore.addToCart({
        id: item.id,
        name: item.productName,
        price: item.price,
        image: item.image,
        description: item.description,
        stock:item.stock ?? 99,
        quantity: 1
    })
    
    Swal.fire({
        icon: 'success',
        title: '成功加入購物車',
        text: `已選購  ${item.productName}`,
        timer: 1500,
        showConfirmButton: false
    });


}


// const productsList = computed(()=>{
//     const newList =products.value.map(x => {
//         let newData = {
//             id: x.id,
//             productName: x.productName,
//             price: x.price,
//             description: x.description,
//             image: x.image
//         }
//         return newData
//     })
//     return newList
// })


const productsList = computed(() => {
    return depot.products.map(x =>({
        id: x.id,
        productName: x.productName,
        price:x.price,
        description: x.description,
        image: x.image
    }));
});


</script>


<template>
<div class="shop-container">
    <div class="shop-grid">
    <!-- 卡片  -->
    <BaseCard 
        v-for="item in productsList" 
        :key="item.id" 
        :hover-effect="true"
        max-width="450px"
        class="product-card"
        @click="goToDetail(item.id)"
    >
        <template #header>
            <img :src="item.image" width="450" height="450" class="card-img-top" :alt="item.productName" />
        </template>
        
        <div class="text-center">
            <h5 class="card-title">{{item.productName}}</h5>
            <!-- <p class="card-text text-muted" style="font-size:0.9rem">{{item.description}}</p> -->
            <div class="price text-success">NT$ {{item.price}}</div>
        </div>
        
        <template #footer>
            <div class="text-center">
                <BaseButton 
                    color="gdg" 
                    label="加入購物車"
                    @click.stop="addToCart(item)"
                    width="90%"
                >
                </BaseButton>
            </div>
        </template>
    </BaseCard>
    </div>
</div>
</template>

<style scoped>
.shop-container {
    padding: 40px 20px;
}

.shop-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr); /* 水平均分為3欄 */
    gap: 30px;
    justify-items: center;
    align-items: start;
}

.product-card {
    cursor: pointer;
    width: 100%;
}

.product-card img {
    width: 100%;
    height: 450px;
    object-fit: cover;
}

.card-title {
    margin: 0 0 12px;
    font-size: 1.5rem;
    font-weight: 700;
}

.price {
    font-size: 1.25rem;
    font-weight: 600;
    margin-top: 10px;
}
</style>