<style scoped>
@import '~/assets/css/Calendar.css';
</style>

<script setup lang="ts">
import Datepicker from '@vuepic/vue-datepicker';

const datepickerRef = ref();

// selectDate and clearDate 
const selectDate = () => {
  datepickerRef.value.selectDate();
}
const clearDate = () => {
  datepickerRef.value.clearValue();
}

</script>

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

    onCleared() {
      this.date = [new Date(), 0];
    },
  },


  watch: {
    date(newDate) {
      if (newDate[1] === 0) {
        this.sendDateTime("", "");
        return;
      }
      // if newDate[1] is undefined or null, set it to the end of the day and start of the day
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

<template>
  <div>
    <Datepicker @cleared="onCleared" v-model="date" locale="dk" cancelText="Ryd" selectText="Vælg" :auto-position="false" range
      no-disabled-range clearable :month-change-on-scroll="false" ref="datepickerRef" @open="onOpen"
      @closed="onClose">
      <template #trigger>
        <Icon name="heroicons-solid:calendar-days" color="grey" class="calendarIcon" size="2.5em">
        </Icon>
      </template>
      <template #action-buttons>
        <p class="custom-clear" @click="clearDate">Ryd</p>
        <p class="custom-select" @click="selectDate">Vælg</p>
      </template>
    </Datepicker>
  </div>
</template>