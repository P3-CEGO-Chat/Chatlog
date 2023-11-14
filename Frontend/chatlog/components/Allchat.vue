<style scoped>
@import url("~/assets/css/allChat.css");

</style>

<script>
    export default{
     data(){
        return{
            messages:[
            { id: 1, username: 'User1', dataTime: "2023-11-07T13:28:21.531Z", message: 'Hej! Jeg har haft en travl dag, men det går godt. Hvad laver I?' },
            { id: 2, username: 'User2', dataTime: "2023-11-07T13:28:21.531Z", message: 'Jeg arbejder på et projekt for skolen. Hvad med jer?' },
            { id: 3, username: 'User3', dataTime: "2023-11-07T13:28:21.531Z", message: 'Jeg slapper af derhjemme og ser en film. Hvad for et projekt arbejder du på?' },
                // Add more messages as needed
            ]
        }
    },
    methods: {
    formatDate(dateString) {
      const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' };
      return new Date(dateString).toLocaleDateString(undefined, options);
    }
  }
}
</script>

<template>
    <div class="container">
        <div class="messageWindow">
            <div class="header">Live chat</div>
                <div class="scrollBar">
                    <div class="messageBox" v-for="message in messages" :key="message.id">
                        <div class="messageHeader">
                            <div class="Username">{{ message.username }}:&nbsp</div>
                            <div class="message">{{ message.message }}</div>
                            <div class="DataTime">{{ formatDate(message.dataTime) }}</div>
                        </div>
                    </div>
                </div>
        </div>
    </div>
</template>

