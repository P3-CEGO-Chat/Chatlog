<template>
    <div>
      <Datepicker v-model="date" 
      range
      :clearable="true"
      :month-change-on-scroll="false"
      ref="datepickerRef"
      @open="onOpen"
      @closed="onClose"
      />
    </div>
    <button @click="CalendarHandler">Open/Close</button>
  </template>
  

  <script setup lang="ts">
    import { ref, onMounted  } from 'vue';
    import Datepicker from '@vuepic/vue-datepicker';
    import '@vuepic/vue-datepicker/dist/main.css';
    import type { DatePickerInstance } from '@vuepic/vue-datepicker';

    
    const date = ref();
    const datepickerRef = ref<DatePickerInstance>(null);
    let isOpen = false;

    onMounted(() => {
      const startDate = new Date();
      const endDate = new Date(new Date().setDate(startDate.getDate() - 7));
      date.value = [endDate, startDate];
    });

    const onOpen = () => {
      isOpen = true;
    }

    const onClose = () => {
      isOpen = false;
    }

    const CalendarHandler = () => {
      if (!isOpen && datepickerRef) {
        datepickerRef.value?.openMenu();
      } else if (isOpen && datepickerRef) {
        datepickerRef.value?.closeMenu();
      }
    }

    

  </script>

<style scoped>
@import '~/assets/css/Calendar.css';
</style>
  