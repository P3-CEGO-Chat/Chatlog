

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
    ogUsername: string;
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
        'keywordArray.length': async function(newLength) {
            if (this.keywordArray.length > 0) {

                const usernameIndex = this.keywordArray.findIndex(item => item.isUser);

                if (usernameIndex !== -1) { // if username is in the array


                    const arrayWithoutUsername = this.keywordArray.filter((item, index) => index !== usernameIndex); // remove username from array

                    let jsonData: any; // declare variable to store json data

                    const { data } = await useFetch('http://localhost:8080/search/fulltext/custom', {
                        query: {
                            keywords: arrayWithoutUsername.length == 0 ? "" : arrayWithoutUsername.map(item => item.word).join(','), // if array is empty, send empty string
                            username: this.keywordArray[usernameIndex].word.slice(1) // remove @ from username
                        }
                    });
                    console.log("noice");

                    console.log(data.value)
                    
                    jsonData = data.value as Message[];

                    console.log("jsonData:", jsonData);

                    console.log(arrayWithoutUsername);
                    console.log(arrayWithoutUsername.map(item => item.word).join(','));

                    console.log(jsonData);
                    this.messages = jsonData.map((item: any[]): Message => ({
                        id: item[0],
                        customerId: item[1],
                        text: item[2],
                        dateTime: item[3],
                        isFlagged: item[4],
                        ogUsername: item[5],
                        username: item[6],
                    }));

                }
                console.log("messages:", this.messages);
            } else {
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
        </div>
    </div>

</template>