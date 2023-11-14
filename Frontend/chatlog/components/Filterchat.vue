<template>
    <div class="container">
        <div class="SearchField">
            <div class="SearchTex">
                Viser resultat for: "{{ keywordArray.map(keyword => keyword.word).join(', ')}}"
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
            this.ObjectArray.push(this.messageObject = {message: "Hej med dig din bums Hej med dig din bums Hej med dig din bums Hej med dig din bums Hej med dig din bums Hej med dig din bums Hej med dig din bums Hej med dig din bums Hej med dig din bums Hej med dig din bums", username: "user4", dateTime: "2023-11-07T13:28:21.531Z"})
            this.ObjectArray.push(this.messageObject = {message: "fuck dig", username: "user498", dateTime: "2023-11-07T13:28:21.531Z"})
            this.ObjectArray.push(this.messageObject = {message: "Jackpot", username: "user4", dateTime: "2023-11-07T13:28:21.531Z"})
            console.log(this.ObjectArray);
        },
    },
    props: {
    keywordArray: {
      type: Array,
      default: () => [],
        },
    },
    watch: {
    keywordArray(newVal) {
      console.log('Received new keywordArray:', newVal);
    },
  }, 
};
</script>
