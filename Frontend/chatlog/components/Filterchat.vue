<template>
    <div class="container">
        <div class="SearchField">
            <div class="SearchTex">
                Viser resultat for: "{{ keywordArray.map(keyword => keyword.word).join(', ')}}"
            </div>
            <div class="scrollBar">
                <div class="searchedMessage" v-for="(object, index) in ObjectArray" :key="index"> 
                    <div class="messagesender">
                        {{ object.username }}:
                    </div>
                    <div classe="messageContent">
                        {{ object.message }}
                    </div> 
                    <div class="dateTime">
                        {{ formatDate(object.dateTime)  }}
                    </div>
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
            this.ObjectArray.push(this.messageObject = {message: "Hej alle sammen! Hvordan har I det i dag?", username: "user1222", dateTime: "2023-11-07T13:28:21.531Z"})
            this.ObjectArray.push(this.messageObject = {message: "Hej! Jeg har det godt, tak. Hvordan går det med dig?", username: "user2", dateTime: "2023-11-07T13:28:21.531Z"})
            this.ObjectArray.push(this.messageObject = {message: "Hej! Jeg har haft en travl dag, men det går godt. Hvad laver I?", username: "user12222", dateTime: "2023-11-07T13:28:21.531Z"})
            console.log(this.ObjectArray);
        },
        formatDate(dateString) {
            const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' };
            return new Date(dateString).toLocaleDateString(undefined, options);
        }
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
    }
    }
};
</script>
