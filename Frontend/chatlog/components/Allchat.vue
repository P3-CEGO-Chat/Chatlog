<style scoped>
@import url("~/assets/css/allChat.css");
</style>

<script lang="ts">

export default {
  data() {
    return {
      messages: [],
      pageCounter: 2,
    };
  },

    async mounted() {
    const { data } = await useFetch('http://localhost:8080/messages/1');
    this.messages = JSON.parse(data.value as string);
    console.log(this.messages);
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

    if (target.scrollTop === 0) {
      console.log('Reached the top of the scrollbar');
      // Fetch more messages
      const {data} = await useFetch(`http://localhost:8080/messages/${this.pageCounter}`); 
      const newMessages = JSON.parse(data.value as string);
      this.messages = newMessages.concat(this.messages);
      console.log(this.messages);
      this.pageCounter++;
    }
    this.$nextTick(() => {
      const scrollHeightAfterLoad = target.scrollHeight;
      const scrollHeightChange = scrollHeightAfterLoad - scrollHeightBeforeLoad;
      target.scrollTop = scrollTopBeforeLoad + scrollHeightChange;
      if (target.scrollTop + target.clientHeight === scrollHeightAfterLoad) {
        console.log('Reached the bottom of the scrollbar');
      }
      });
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
        </div>
    </div>
</template>

