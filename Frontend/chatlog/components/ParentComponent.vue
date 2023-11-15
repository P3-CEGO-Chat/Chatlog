<template>
    <div class="parent">
      <Searchbar @toggle-calendar="showCalendar = !showCalendar" />
      <Calendar v-if="showCalendar" />
    </div>
  </template>

  <script>
  import Searchbar from './Searchbar.vue';
  import Calendar from './Calendar.vue';

  export default {
    components: {
      Searchbar,
      Calendar,
    },
    data() {
      return {
        showCalendar: false,
      };
    },
  };
  </script>