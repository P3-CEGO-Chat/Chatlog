<style scoped>
@import url("~/assets/css/allChat.css");

</style>

<script>
    export default{
     data(){
        return{
            messages:[
            { id: 1, sender: 'User1', time: '0000-00-00 00:00:00', content: 'Hello, thvil få et stort mødelokale booket til jer hele dagen. Men i kommer nok til at dele det med den anden gruppe studerende - Der er dog masser af plads.is is User1' },
            { id: 2, sender: 'User2', time: '2022-03-01 10:05:00', content: 'Hello, this is User2' },
            { id: 3, sender: 'User3', time: '2022-03-01 10:10:00', content: 'Hello, this is User3' },
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
            <div class="messageBox" v-for="message in messages" :key="message.id">
                    <div class="messageHeader">
                        <div class="CustomerId">{{ message.sender }}:&nbsp</div>
                        <div class="messageContent">{{ message.content }}</div>
                        <div class="Time">{{ message.time }}</div>
                    </div>
            </div>
        </div>
    </div>
</template>

