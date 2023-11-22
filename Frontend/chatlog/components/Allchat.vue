<style scoped>
@import url("~/assets/css/allChat.css");
</style>

<script lang="ts">

interface Message {
  id: number;
  customerId: string;
  dateTime: string;
  text: string;
  isFlagged: boolean;
  ogUsername: string;
}

export default {
  data() {
    return {
      messages: <Message[]>[],
      currentPage: 1,
      originalPageCounter: 1,
      initialLoad: true,
      HighestMessageId: 0,
      title: "Live Chat",
    };
  },

  async mounted() {
    const { data } = await useFetch(`http://localhost:8080/messages/${this.currentPage}`);
    this.messages = JSON.parse(data.value as string).map((item: any[]): Message => ({
      id: item[0],
      customerId: item[1],
      text: item[3],
      dateTime: item[2],
      isFlagged: item[4],
      ogUsername: item[5],
    }));

    this.HighestMessageId = this.messages[this.messages.length - 1].id;
    this.originalPageCounter = this.currentPage;

    this.$nextTick(() => {
      this.scrollTobottom();
    });
  },

  props: {
    messageId: {
      type: Number,
      default: 0,
    },
  },

  watch: {
    async messageId() {
      this.findMessage();
    },
  },

  methods: {
    async checkScroll(event: Event) {
      const target = event.target as Element;
      const scrollTopBeforeLoad = target.scrollTop;
      const scrollHeightBeforeLoad = target.scrollHeight;

      // Check if the user has scrolled to the top of the scrollbar
      if (target.scrollTop === 0 && target.scrollHeight - target.clientHeight > 0) {
        // Fetch more messages
        this.currentPage++;
        const { data } = await useFetch(`http://localhost:8080/messages/${this.currentPage}`);
        const newMessages = JSON.parse(data.value as string).map((item: any[]): Message => ({
          id: item[0],
          customerId: item[1],
          text: item[3],
          dateTime: item[2],
          isFlagged: item[4],
          ogUsername: item[5],
        }));
        this.messages = newMessages.concat(this.messages);
      }


      // Check if the user has scrolled to the bottom of the scrollbar
      this.$nextTick(async () => {
        const scrollHeightAfterLoad = target.scrollHeight;
        const scrollHeightChange = scrollHeightAfterLoad - scrollHeightBeforeLoad;
        target.scrollTop = scrollTopBeforeLoad + scrollHeightChange;

        // Check if the user has scrolled to the bottom of the scrollbar
        if (target.scrollTop + target.clientHeight >= scrollHeightAfterLoad - 1) {
          if (!this.initialLoad && this.originalPageCounter != 1) {
            this.originalPageCounter--;
            const { data } = await useFetch(`http://localhost:8080/messages/${this.originalPageCounter}`);
            const prevMessages = JSON.parse(data.value as string).map((item: any[]): Message => ({
              id: item[0],
              customerId: item[1],
              text: item[3],
              dateTime: item[2],
              isFlagged: item[4],
              ogUsername: item[5],
            }));
            this.messages = this.messages.concat(prevMessages);
          }
        }
        this.initialLoad = false;
      });
    },

    async buttonClear() {
      this.messages = [];
      this.currentPage = 1;
      this.originalPageCounter = this.currentPage;
      const { data } = await useFetch(`http://localhost:8080/messages/${this.currentPage}`);
      this.messages = JSON.parse(data.value as string).map((item: any[]): Message => ({
        id: item[0],
        customerId: item[1],
        text: item[3],
        dateTime: item[2],
        isFlagged: item[4],
        ogUsername: item[5],
      }));
      this.$nextTick(() => {
        this.scrollTobottom();
        this.title = `Live Chat`;
      });
    },

    async findMessage() {
      console.log('Button clicked');
      
      this.initialLoad = true;
      //find a specific message and update it
      this.messages = [];
      const { data } = await useFetch(`http://localhost:8080/messages/message-id/${this.messageId}`);
      const messageIdInterval = JSON.parse(data.value as string).map((item: any[]): Message => ({
        id: item[0],
        customerId: item[1],
        text: item[3],
        dateTime: item[2],
        isFlagged: item[4],
        ogUsername: item[5],
      }));
      this.messages = messageIdInterval;
      this.title = `Chat historie`;
      this.findTheCurrentPage();
      this.$nextTick(() => {
        this.scrollToMessage();
      });
    },

    findTheCurrentPage() {
      let lowIntervalId = -(this.messageId % 25) + this.messageId + 1;
      lowIntervalId = Math.floor(lowIntervalId / 25);
      let NumberOfPages = Math.ceil(this.HighestMessageId / 25);
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
  }
};
</script> 

<template>
  <div class="container">
    <div class="messageWindow">
      <div class="header">{{ title }}<button class="clearButton" @click="buttonClear">Live Chat</button></div>
      <div class="scrollBar" @scroll="checkScroll">
        <div class="messageBox" v-for="message in messages" :key="message.id" :ref="`message-${message.id}`"
          :class="{ 'highlightedMessage': message.id === messageId }">
          <div class="messageHeader">
            <div class="CustomerId">{{ message.ogUsername }}:&nbsp</div>
            <div class="messageContent">{{ message.text }}</div>
            <div class="Time">{{ new Date(message.dateTime).toLocaleString() }}</div>
            <div v-if="message.isFlagged" class="flagged"> 
                <div class="icon">
                <Icon name="material-symbols:warning-outline-rounded" class="icon"/>
                    <div class="flaggedText">
                        Flagged reason
                    </div>
                </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
