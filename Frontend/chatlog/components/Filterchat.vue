

<style scoped>
 @import url('~/assets/css/filterchat.css');
 </style>

<script lang="ts">
import type { PropType } from 'vue';

interface Message {
    id: number;
    customerId: string;
    text: string;
    dateTime: string;
    username: string;
}


export default{
    data() {
        return {
            keyword: "" as string | unknown, // explicitly define the type of keyword
            ObjectArray: [],
            messages : <Message[]>[],/* Array<{ id: string, customerId: string, text: string, dateTime: string, username: string, userId: string }>() */
        };
    },
    methods: {
        /* submitForm() {
            this.ObjectArray.push(this.messageObject = {message: "Hej alle sammen! Hvordan har I det i dag?", username: "user1222", dateTime: "2023-11-07T13:28:21.531Z"})
            this.ObjectArray.push(this.messageObject = {message: "Hej! Jeg har det godt, tak. Hvordan går det med dig?", username: "user2", dateTime: "2023-11-07T13:28:21.531Z"})
            this.ObjectArray.push(this.messageObject = {message: "Hej! Jeg har haft en travl dag, men det går godt. Hvad laver I?", username: "user12222", dateTime: "2023-11-07T13:28:21.531Z"})
            console.log(this.ObjectArray);
        }, */
    },
    props: {
        keywordArray: {
            type: Array as PropType<{ word: string, isUser: boolean }[]>,
            default: () => [] as { word: string, isUser: boolean }[]
        },
    },
    watch: {
    async keywordArray(newVal: Array<{ word: string, isUser: boolean }>) {

        const { data } = await useFetch('http://localhost:8080/search/', {
            query: {
                search: this.keywordArray[0].word
            }
        });
        console.log(data.value);
        this.messages = JSON.parse(data.value as string).map((item: any[]): Message => ({
            id: item[0],
            customerId: item[1],
            text: item[2],
            dateTime: item[3],
            username: item[4]
        }));
        console.log(this.messages);
        console.log('Received new keywordArray:', newVal);
    },
  }, 
};
</script>

<template>
    <div class="container">
        <div class="SearchField">
            <div class="SearchTex">

                Viser resultat for: "{{ keywordArray.map(keyword => keyword.word).join(', ')}}"
            </div>
            <div class="scrollBar">
                <div class="searchedMessage" v-for="(message) in messages" :key="message.id"> 
                    <div class="messagesender">
                        {{ message.username }}:
                    </div>
                    <div classe="messageContent">
                        {{ message.text }}
                    </div> 
                    <div class="dateTime">
                        {{ new Date(message.dateTime).toLocaleString()  }}
                    </div>
                </div>
            </div>
            <!-- <button @click="submitForm">submit</button> -->
        </div>
    </div>

</template>