<script setup>
import MapContainer from '@/components/MapContainer.vue';
import MapCardContainer from '@/components/MapCardContainer.vue';
import SearchBar from '@/components/SearchBar.vue';
</script>

<template>
    

    <div>
        <SearchBar></SearchBar>
        <br>

        <div style="display: flex;">
            <div style="flex: 4; background-color: lightblue;"><MapContainer></MapContainer></div>
            <div style="flex: 3; background-color: lightgreen;"><MapCardContainer></MapCardContainer></div>
        </div>
       
       
        
    </div>




</template>

<style scoped>

</style>