<template>
  <div class="user-info-placeholder">
     <h4 class="mb-3 border-bottom pb-2">{{ title }}</h4>
     <p class="text-muted">此處將放置{{ title }}的詳細內容。</p>
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: '項目'
  }
})
</script>
