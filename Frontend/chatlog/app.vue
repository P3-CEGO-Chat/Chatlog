<template>
  <div class="layout">
    <div>
      <Searchbar @updateKeywordArray="updateKeywordArray" @updateDateTimeArray="updateDateTimeArray" />
      <Filterchat :keywordArray="keywordArray" :dateTimeArray="dateTimeArray" @updateMessageId="updateMessageId"/> -->
    </div>
    <div>
      
      <Allchat :messageId="messageId" @resetMessageId="resetMessageId">
        ikke dav
      </Allchat>
    </div>
  </div>
</template>

<script lang="ts">

import('~/assets/css/main.css')
import Searchbar from './components/Searchbar.vue';
import Filterchat from './components/Filterchat.vue';
import Allchat from './components/Allchat.vue';

export default {
  name: 'App',
  components: {
    Searchbar,
    Filterchat,
    Allchat    
  },
  data() {
    return {
      keywordArray: Array<{ word: string, isUser: boolean }>(),

      dateTimeArray: Array<{dateTimeFrom: string, dateTimeTo: string}>(),
      messageId: 0 as number,

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
    updateMessageId(newMessageId : number) {
      this.messageId = newMessageId as number;
      console.log('Received updateMessageId:', this.messageId);
    },
    resetMessageId() {
      this.messageId = 0 as number;
      console.log('Received resetMessageId:', this.messageId);
    }
  },
};
</script>