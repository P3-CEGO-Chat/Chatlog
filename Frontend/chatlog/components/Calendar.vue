<template>
    <div>
      <Datepicker v-model="date" 
      :auto-position="false"
      range
      :clearable="true"
      :month-change-on-scroll="false"
      ref="datepickerRef"
      @open="onOpen"
      @closed="onClose"
      > <template #trigger>
       <Icon name="heroicons-solid:calendar-days" color="grey" class="calendarIcon" size="1.5em" ></Icon>
        </template>
</datepicker>
<!--<button class="calendarButton">
      <Icon name="heroicons-solid:calendar-days" color="grey" class="calendarIcon" size="1.5em" />
    </button>-->
    </div>
   <!-- <button @click="CalendarHandler">Open/Close</button> -->
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
.datepicker-wrapper {
  display: flex;
  justify-content: flex-end;
}
</style>
  