<template>
    <div>
      <Datepicker v-model="date" 
      range
      :clearable="true"
      :month-change-on-scroll="false"
      />
    </div>
  </template>
  

  <script setup>
  import { ref, onMounted  } from 'vue';
  import Datepicker from '@vuepic/vue-datepicker';
  import '@vuepic/vue-datepicker/dist/main.css';

  
  const date = ref();
  const isCalendarOpen = ref(false);

  onMounted(() => {
  const startDate = new Date();
  const endDate = new Date(new Date().setDate(startDate.getDate() - 7));
  date.value = [endDate, startDate];
});

  </script>

<style scoped>
@import '~/assets/css/Calendar.css';
</style>
  