<template>
  <div class="layout">
    <div>
      <Searchbar @updateKeywordArray="updateKeywordArray" @updateDateTimeArray="updateDateTimeArray" />
      <Filterchat :keywordArray="keywordArray" :dateTimeArray="dateTimeArray" @updateMessageId="updateMessageId"/>
    </div>
    <div>
      <Allchat :messageId="messageId">
        ikke dav
      </Allchat>
    </div>
  </div>
</template>

<script lang="ts">

import('~/assets/css/main.css')
import Searchbar from './components/Searchbar.vue';
import Filterchat from './components/Filterchat.vue';

export default {
  name: 'App',
  components: {
    Searchbar,
    Filterchat
  },
  data() {
    return {
      keywordArray: Array<{ word: string, isUser: boolean }>(),
      dateTimeArray: Array<{dateTimeFrom: string, dateTimeTo: string}>(), 
      messageId: Number,
    }
  },
  methods: {
    updateKeywordArray(newKeywordArray: Array<{ word: string, isUser: boolean }>) {
      this.keywordArray = newKeywordArray;
      console.log('Received updateKeywordArray:', this.keywordArray);
    },
    updateDateTimeArray(newDateTimeArray: Array<{dateTimeFrom: string, dateTimeTo: string}>) {
      this.dateTimeArray = newDateTimeArray;
      console.log('Received updateDateTimeArray:', this.dateTimeArray);
    },
    updateMessageId(newMessageId: NumberConstructor) {
      this.messageId = newMessageId;
      console.log('Received updateMessageId:', this.messageId);
    }
  },
};
</script>