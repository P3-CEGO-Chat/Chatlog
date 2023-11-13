<template>
    <div class="container">
        <div class="SearchField">
            <div class="SearchTex">
                Viser resultat for: "{{ keyword }}"
            </div>
            
            <div class="searchedMessage" v-for="(object, index) in ObjectArray" :key="index"> 
                <div>
                    {{ object.username }}:&nbsp;
                </div>
                <div classe="messageContent">
                    {{ object.message }}
                </div> 
                <div class="dateTime">
                    {{ object.dateTime  }}
                </div>
            </div>
            <button @click="submitForm">submit</button>
        </div>
    </div>

</template>


<style scoped>
 @import url('~/assets/css/filterchat.css');
</style>

<script>

export default{
    data() {
        return {
            keyword: '',
            ObjectArray: [],
            messageObject: {message: "", username: "", dateTime: ""},
        };
    },
    methods: {
        submitForm() {
            this.ObjectArray.push(this.messageObject = {message: "Hello, how are you?", username: "user1222", dateTime: "2023-11-07T13:28:21.531Z"})
            this.ObjectArray.push(this.messageObject = {message: "I am doing well, thank you. How about you?", username: "user2", dateTime: "2023-11-07T13:28:21.531Z"})
            this.ObjectArray.push(this.messageObject = {message: "I am also doing well. Thanks for asking!", username: "user12222", dateTime: "2023-11-07T13:28:21.531Z"})
            console.log(this.ObjectArray);
        },
    }     
};
</script>
