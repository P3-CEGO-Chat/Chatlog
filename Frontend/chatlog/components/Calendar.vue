<template>
  <div>
    <Datepicker v-model="date" locale="dk" cancelText="Ryd" selectText="Vælg"
    :auto-position="false"
    range
    no-disabled-range
    :clearable="true"
    :month-change-on-scroll="false"
    ref="datepickerRef"
    @open="onOpen"
    @closed="onClose"
    >
      <template #trigger>
        <Icon name="heroicons-solid:calendar-days" 
              color="grey" 
              class="calendarIcon" 
              size="2.5em">
        </Icon>
      </template>
    </Datepicker>
  </div>
</template>
  

  <script setup lang="ts">
    import { ref, onMounted  } from 'vue';
    import Datepicker from '@vuepic/vue-datepicker';
    import '@vuepic/vue-datepicker/dist/main.css';
    import type { DatePickerInstance } from '@vuepic/vue-datepicker';
    import { format } from 'date-fns'


    
    const date = ref();
    const datet = ref(new Date());
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
    watch(date, (newDate) => {
    if (newDate[0].getTime() === newDate[1].getTime()) {
      // If the start and end dates are the same, set the end date to be the start date plus some time interval
      newDate[1] = new Date(newDate[0].getTime() + 1000 * 60 * 60 * 24); // Plus 24 hours
    }
    console.log(newDate);
  });
 
  const formatDate = (datet: number | Date) => {
  return format(datet, 'dd.MM.yyyy, HH:mm');
};

  </script>

<style scoped>
@import '~/assets/css/Calendar.css';
</style>
    