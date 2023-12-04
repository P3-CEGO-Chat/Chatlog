<template>
  <div>
    <Datepicker v-model="date" locale="dk" cancelText="Ryd" selectText="Vælg" :auto-position="false" range
      no-disabled-range :clearable="true" :month-change-on-scroll="false" ref="datepickerRef" @open="onOpen"
      @closed="onClose">
      <template #trigger>
        <Icon name="heroicons-solid:calendar-days" color="grey" class="calendarIcon" size="2.5em">
        </Icon>
      </template>
    </Datepicker>
  </div>
</template>
  

<script lang="ts">
import { ref/*, onMounted*/ } from 'vue';
import '@vuepic/vue-datepicker/dist/main.css';
import type { DatePickerInstance } from '@vuepic/vue-datepicker';


export default {
  data() {
    return {
      date: ref(),
      datepickerRef: ref<DatePickerInstance>(null),
      isOpen: false,
    };
  },
  methods: {
    // This function will be called when `keywordArray` changes
    sendDateTime(dateTimeFrom: string, dateTimeTo: string) {
      this.$emit("updateDateTimeObject", {dateTimeFrom, dateTimeTo});
    },

    onOpen() {
      this.isOpen = true;
    },
    onClose() {
      this.isOpen = false;
    },

  },

  watch: {
    date(newDate) {
      if (newDate[1] === undefined || newDate[1] === null) {
        newDate[1] = new Date(newDate[0].getFullYear(), newDate[0].getMonth(), newDate[0].getDate(), 23, 59, 59, 999);
        newDate[0] = new Date(newDate[0].getFullYear(), newDate[0].getMonth(), newDate[0].getDate(), 0, 0, 0, 0);
      }

      const startDateISO = newDate[0].toISOString();
      const endDateISO = newDate[1].toISOString();
      this.sendDateTime(startDateISO, endDateISO);
    },
  },

}

</script>

<script setup lang="ts">

import Datepicker from '@vuepic/vue-datepicker';
</script>

<style scoped>
@import '~/assets/css/Calendar.css';
</style>
    