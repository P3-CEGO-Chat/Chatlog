<style scoped>
@import url("~/assets/css/allChat.css");

</style>

<script setup lang="ts">
    

    interface Message {
        id: number;
        sender: string;
        time: string;
        content: string;
    }

    const messages = ref<Message[]>([]);

    useFetch('http://localhost:8080/messages/1').then((data) => {
        messages.value = JSON.parse(data) as Message[];
    });
    
</script>

<script lang="ts">
    export default{
     data(){
        return{
            messages:[
            { id: 1, sender: 'User1', time: '0000-00-00 00:00:00', content: 'Hej! Jeg har haft en travl dag, men det går godt. Hvad laver I?' },
            { id: 2, sender: 'User2', time: '2022-03-01 10:05:00', content: 'Jeg arbejder på et projekt for skolen. Hvad med jer?' },
            { id: 3, sender: 'User3', time: '2022-03-01 10:10:00', content: 'Jeg slapper af derhjemme og ser en film. Hvad for et projekt arbejder du på?' },
                // Add more messages as needed
            ]
        }
    }
}
</script>

<template>
    <div class="container">
        <div class="messageWindow">
            <div class="header">Live chat</div>
            {{ messages }}
                <div class="scrollBar">
                    <div class="messageBox" v-for="message in messages" :key="message.id">
                        <div class="messageHeader">
                            <div class="CustomerId">{{ message.sender }}:&nbsp</div>
                            <div class="messageContent">{{ message.content }}</div>
                            <div class="Time">{{ message.time }}</div>
                        </div>
                    </div>
                </div>
        </div>
    </div>
</template>

