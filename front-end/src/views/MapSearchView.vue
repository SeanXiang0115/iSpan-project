<script setup>
import MapContainer from '@/components/MapContainer.vue';
import MapCardContainer from '@/components/MapCardContainer.vue';
import SearchBar from '@/components/SearchBar.vue';
</script>

<template>
    

    <div>
        <SearchBar></SearchBar>
        <br>

        <div style="display: flex; gap: 5px;">
            <div style="flex: 4; border-radius: 2px; overflow: hidden;"><MapContainer></MapContainer></div>
            <div style="flex: 3; "><MapCardContainer></MapCardContainer></div>
        </div>
       
       
        
    </div>




</template>

<style scoped>

</style>