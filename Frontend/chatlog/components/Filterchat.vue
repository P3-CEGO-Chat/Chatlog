

<style scoped>
 @import url('~/assets/css/filterchat.css');
 </style>

<script lang="ts">
import type { AsyncData } from '#app';
import type { PropType } from 'vue';

interface Message {
    id: number;
    customerId: string;
    dateTime: string;
    text: string;
    isFlagged: boolean;
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
        if (this.keywordArray.length > 0) {

            const usernameIndex = newVal.findIndex(item => item.isUser);

            let jsonData: any; // TODO: define type

            if (usernameIndex !== -1 && this.keywordArray.length >= 2) {
                console.log('searching with username and keyword');
                const { data } = await useFetch('http://localhost:8080/search/fulltext/custom', {
                    query: {
                        keywords: this.keywordArray[0].word,
                        username: this.keywordArray[usernameIndex].word.slice(1)
                    }
                });

                jsonData = JSON.parse(data.value as string);
            } else if (usernameIndex == -1 && this.keywordArray.length >= 2) {
                console.log('searching with keywords');

            } else if (usernameIndex == -1 && this.keywordArray.length == 1) {
                console.log('searching with keyword')
                const { data } = await useFetch('http://localhost:8080/search/', {
                    query: {
                        search: this.keywordArray[0].word
                    }
                });
                console.log(data.value);
                jsonData = JSON.parse(data.value as string);
            }
            
            
            console.log(jsonData);
            this.messages = jsonData.map((item: any[]): Message => ({
                id: item[0],
                customerId: item[1],
                dateTime: item[2],
                text: item[3],
                isFlagged: item[4],
                username: item[5]
            }));
            console.log(this.messages);
        }
    },
    'keywordArray.length': function(newLength) {
        if (newLength === 0) {
            this.messages = [];
        }
    }
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