<template>
  <div class="layout">
    <div>
      <Searchbar @updateKeywordArray="updateKeywordArray" />
      <Filterchat :keywordArray="keywordArray" @updateMessageId="updateMessageId" />
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
      messageId: Number,
    }
  },
  methods: {
    updateKeywordArray(newKeywordArray: Array<{ word: string, isUser: boolean }>) {
      this.keywordArray = newKeywordArray;
      console.log('Received updateKeywordArray:', this.keywordArray);
    },
    updateMessageId(newMessageId: NumberConstructor) {
      this.messageId = newMessageId;
      console.log('Received updateMessageId:', this.messageId);
    }
  },
};
</script>