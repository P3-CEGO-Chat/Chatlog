<style scoped>
@import url("~/assets/css/allChat.css");
</style>

<script lang="ts">

export default {
  data() {
    return {
      messages: [],
      currentPage: 1,
      originalPageCounter: 1,
      initialLoad: true,
      messageId: 65,
    };
  },

    async mounted() {
    const { data } = await useFetch(`http://localhost:8080/messages/${this.currentPage}`);
    this.messages = JSON.parse(data.value as string);
    console.log(this.messages);
    this.originalPageCounter = this.currentPage;
    
    
    this.$nextTick(() => {
      const scrollBar = this.$el.querySelector('.scrollBar');
      if (scrollBar) {
        scrollBar.scrollTop = scrollBar.scrollHeight;
      }
    });
  },

  methods: {
  async checkScroll(event: Event) {
    const target = event.target as Element;
    const scrollTopBeforeLoad = target.scrollTop;
    const scrollHeightBeforeLoad = target.scrollHeight;

    // Check if the user has scrolled to the top of the scrollbar
    if (target.scrollTop === 0) {
      console.log('Reached the top of the scrollbar');
      // Fetch more messages
      this.currentPage++;
      const {data} = await useFetch(`http://localhost:8080/messages/${this.currentPage}`); 
      const newMessages = JSON.parse(data.value as string);
      this.messages = newMessages.concat(this.messages);
      console.log(this.messages);
    }


    // Check if the user has scrolled to the bottom of the scrollbar
    this.$nextTick(async () => {
      const scrollHeightAfterLoad = target.scrollHeight;
      const scrollHeightChange = scrollHeightAfterLoad - scrollHeightBeforeLoad;
      target.scrollTop = scrollTopBeforeLoad + scrollHeightChange;

      // Check if the user has scrolled to the bottom of the scrollbar
      if (target.scrollTop + target.clientHeight === scrollHeightAfterLoad) {
        console.log('Reached the bottom of the scrollbar');
        if (!this.initialLoad && this.originalPageCounter != 1) {
          this.originalPageCounter--;
          const {data} = await useFetch(`http://localhost:8080/messages/${this.originalPageCounter}`);
          const prevMessages = JSON.parse(data.value as string);
          this.messages = this.messages.concat(prevMessages);
          console.log(this.messages);
        }
      }
    this.initialLoad = false;
    });
    },  
     async buttonClicked() {
      console.log('Button clicked');
      //find a specific message and update it
      this.messages = [];
      const {data} = await useFetch(`http://localhost:8080/messages/message-id/${this.messageId}`);
      const messageIdInterval = JSON.parse(data.value as string);
      this.messages = messageIdInterval;
      console.log(this.messages);
      const pageId = Math.ceil(this.messageId / 25);
      console.log(`Page ID: ${pageId}`);
      this.currentPage = pageId;
      } 
  },
};
</script> 

<template>
  <div class="container">
      <div class="messageWindow">
          <div class="header">Live chat</div>
              <div class="scrollBar" @scroll="checkScroll">
                  <div class="messageBox" v-for="message in messages" :key="message[0]">
                      <div class="messageHeader">
                          <div class="CustomerId">{{ message[1] }}:&nbsp</div>
                          <div class="messageContent">{{ message[3] }}</div>
                          <div class="Time">{{ new Date(message[2]).toLocaleString() }}</div>
                      </div>
                  </div>
              </div>
              <button class="myButton" @click="buttonClicked">Click me</button>
      </div>
  </div>
</template>
