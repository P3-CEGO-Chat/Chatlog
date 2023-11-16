<style scoped>
@import url("~/assets/css/allChat.css");
</style>


<script setup lang="ts">

    const { data } = await useFetch('http://localhost:8080/messages/1');

    const messages = JSON.parse(data.value as string);

    console.log(messages);

</script>

<script lang="ts">

interface Message {
  customerId: string;
  username: string;
  message: string;
  dateTime: string;
}

export default {
  data() {
    return {
      messages: [] as any[],
      // Rest of your data
    };
  },
    mounted() {
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
    if (target.scrollTop === 0) {
      console.log('Reached the top of the scrollbar');
      // Fetch more messages
      const {data} = await useFetch('http://localhost:8080/messages/2',{mode: 'no-cors'}); 
      
      const newMessages: Message[] = JSON.parse(data.value as string);
      console.log(newMessages);
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
                    <div class="messageBox" v-for="message in messages" :key="message[0]">
                        <div class="messageHeader">
                            <div class="CustomerId">{{ message[1] }}:&nbsp</div>
                            <div class="messageContent">{{ message[2] }}</div>
                            <div class="Time">{{ new Date(message[3]).toLocaleString() }}</div>
                        </div>
                    </div>
                </div>
        </div>
    </div>
</template>

