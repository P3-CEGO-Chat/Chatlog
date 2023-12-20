<script lang="ts">
  
  import('~/assets/css/main.css')
  import Searchbar from '~/components/Searchbar.vue';
  import Filterchat from '~/components/Filterchat.vue';
  import Allchat from '~/components/Allchat.vue';
  
  export default {
    name: 'index',
    components: {
      Searchbar,
      Filterchat,
      Allchat
    },
    data() {
      return {
        keywordArray: Array<{ word: string, isUser: boolean, isCustomerId: boolean }>(),
        dateTimeObject: {dateTimeFrom: "", dateTimeTo: ""}, 
        messageId: 0 as number,
      }
    },
    methods: {
      updateKeywordArray(newKeywordArray: Array<{ word: string, isUser: boolean, isCustomerId: boolean }>) {
        this.keywordArray = newKeywordArray;
      },
      updateDateTimeObject(newDateTimeObject: {dateTimeFrom: string, dateTimeTo: string}) {
        this.dateTimeObject = newDateTimeObject;
      },
      updateMessageId(newMessageId: number) {
        this.messageId = newMessageId as number;
      },
      resetMessageId() {
        this.messageId = 0 as number;
      }
    },
  };
  </script>

<template>
  <div class="layout">
    <div>
      <Searchbar @updateKeywordArray="updateKeywordArray" @updateDateTimeObject="updateDateTimeObject" />
      <Filterchat :keywordArray="keywordArray" :dateTimeObject="dateTimeObject" @updateMessageId="updateMessageId"/>
    </div>
    <div>
      <Allchat :messageId="messageId" @resetMessageId="resetMessageId">
        ikke dav
      </Allchat>
    </div>
    <NuxtPage />
  </div>
</template>