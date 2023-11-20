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
      HighestMessageId: 0,
    };
  },

    async mounted() {
    const { data } = await useFetch(`http://localhost:8080/messages/${this.currentPage}`);
    this.messages = JSON.parse(data.value as string);
    this.HighestMessageId = this.messages[this.messages.length - 1][0];
    console.log(`Highest message ID: ${this.HighestMessageId}`);
    console.log(this.messages);
    this.originalPageCounter = this.currentPage;

    this.$nextTick(() => {
      this.scrollTobottom();
    });
  },

  methods: {
  async checkScroll(event: Event) {
    const target = event.target as Element;
    const scrollTopBeforeLoad = target.scrollTop;
    const scrollHeightBeforeLoad = target.scrollHeight;

    // Check if the user has scrolled to the top of the scrollbar
    if (target.scrollTop === 0 && target.scrollHeight - target.clientHeight > 0) {
      console.log('Reached the top of the scrollbar');
      // Fetch more messages
      this.currentPage++;
      const {data} = await useFetch(`http://localhost:8080/messages/${this.currentPage}`); 
      const newMessages = JSON.parse(data.value as string);
      this.messages = newMessages.concat(this.messages);
      console.log(this.messages);
      console.log(`Current page: ${this.currentPage}`);
    }


    // Check if the user has scrolled to the bottom of the scrollbar
    this.$nextTick(async () => {
      const scrollHeightAfterLoad = target.scrollHeight;
      const scrollHeightChange = scrollHeightAfterLoad - scrollHeightBeforeLoad;
      target.scrollTop = scrollTopBeforeLoad + scrollHeightChange;

      // Check if the user has scrolled to the bottom of the scrollbar
      if (target.scrollTop + target.clientHeight >= scrollHeightAfterLoad - 1) {
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
      
      this.initialLoad = true;
      //find a specific message and update it
      this.messages = [];
      const {data} = await useFetch(`http://localhost:8080/messages/message-id/${this.messageId}`);
      const messageIdInterval = JSON.parse(data.value as string);
      this.messages = messageIdInterval;
      console.log(this.messages);
      this.findTheCurrentPage();
        this.$nextTick(() => {
          this.scrollToMessage();
    });

      },
    
    findTheCurrentPage(){
      let lowIntervalId = -(this.messageId % 25) + this.messageId + 1;
      lowIntervalId = Math.floor(lowIntervalId / 25);
      let NumberOfPages = Math.ceil(this.HighestMessageId/25);
      this.currentPage = NumberOfPages - lowIntervalId;
      this.originalPageCounter = this.currentPage;
      console.log(`Current page: ${this.currentPage}`);
    },

    scrollTobottom() {
      const scrollBar = this.$el.querySelector('.scrollBar');
    if (scrollBar) {
      scrollBar.scrollTop = scrollBar.scrollHeight;
    }
  },

    scrollToMiddle() {
      const scrollBar = this.$el.querySelector('.scrollBar');
      if (scrollBar) {
        scrollBar.scrollTop = (scrollBar.scrollHeight - scrollBar.clientHeight) / 2;
    }
  },

  scrollToMessage() {
  const messageRef = `message-${this.messageId}`;
  const messageElement = this.$refs[messageRef] as HTMLElement[];

  if (messageElement && messageElement[0]) {
    const scrollBar = this.$el.querySelector('.scrollBar');
    if (scrollBar) {
      scrollBar.scrollTop = messageElement[0].offsetTop - scrollBar.offsetTop;
      }
    } 
  }
  },
};
</script> 

<template>
  <div class="container">
      <div class="messageWindow">
          <div class="header">Live chat</div>
              <div class="scrollBar" @scroll="checkScroll">
                <div class="messageBox" v-for="message in messages" :key="message[0]" :ref="`message-${message[0]}`" :class="{'highlightedMessage': message[0] === messageId}">
                      <div class="messageHeader">
                          <div class="CustomerId">{{ message[5] }}:&nbsp</div>
                          <div class="messageContent">{{ message[3] }}</div>
                          <div class="Time">{{ new Date(message[2]).toLocaleString() }}</div>
                      </div>
                  </div>
              </div>
      </div>
      <button class="myButton" @click="buttonClicked">Click me</button>
  </div>
</template>
