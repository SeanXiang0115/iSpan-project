<script setup>
import MapContainer from '@/components/MapContainer.vue';
import MapStoreCard from '@/components/MapStoreCard.vue';
import SearchBar from '@/components/SearchBar.vue';
</script>

<template>
    <h1>地圖搜尋頁面</h1>

    <div>
        <SearchBar></SearchBar>
        <br>
        <MapContainer></MapContainer>
        <MapStoreCard></MapStoreCard>
        
    </div>




</template>

<style scoped>

</style>