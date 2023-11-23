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
            dateTimeFrom: "",
            dateTimeTo: "",
        };
    },
    methods: {
        sendMessageId(messageId: Number) {
            this.$emit("updateMessageId", messageId);
        },
    },
    props: {
        keywordArray: {
            type: Array as PropType<{ word: string, isUser: boolean }[]>,
            default: () => [] as { word: string, isUser: boolean }[]
        },
        dateTimeArray: {
            type: Array as PropType< {dateTimeFrom: string, dateTimeTo: string}[]>,
            default: () => [] as {dateTimeFrom: string, dateTimeTo: string}[]
        },
      


    },
    watch: {
        'keywordArray.length': async function(newLength) {
            if (this.keywordArray.length > 0) {

                const usernameIndex = this.keywordArray.findIndex(item => item.isUser);

                if (usernameIndex !== -1) { // if username is in the array
                    const arrayWithoutUsername = this.keywordArray.filter((item, index) => index !== usernameIndex); // remove username from array

                    if (this.dateTimeArray.length == 0) {
                        const { data } = await useFetch('http://localhost:8080/search/fulltext', {
                            query: {
                                keywords: arrayWithoutUsername.length == 0 ? "" : arrayWithoutUsername.map(item => item.word).join(','), // if array is empty, send empty string
                                username: this.keywordArray[usernameIndex].word.slice(1), // remove @ from username
                                dateTimeFrom: null,
                                dateTimeTo: null,
                            }
                        });
                        const jsonData: any = data.value as Message[];
                        this.messages = jsonData.map((item: any[]): Message => ({
                            id: item[0],
                            customerId: item[1],
                            text: item[2],
                            dateTime: item[3],
                            isFlagged: item[4],
                            ogUsername: item[5],
                            username: item[6],
                        }));
                    } /* else if (this.keywordArray.length == 0) {
                        const dateTimeFrom = this.dateTimeArray[0].dateTimeFrom;
                        const dateTimeTo =  this.dateTimeArray[1].dateTimeTo;
                        console.log("filter", this.dateTimeArray);
                        const { data } = await useFetch('http://localhost:8080/search/datetime', {
                            query: {
                                dateTimeFrom: this.dateTimeArray[0].dateTimeFrom,
                                dateTimeTo: this.dateTimeArray[1].dateTimeTo,
                            }
                            
                        });
                        
                        const jsonData: any = data.value as Message[];

                        this.messages = jsonData.map((item: any[]): Message => ({
                            id: item[0],
                            customerId: item[1],
                            text: item[2],
                            dateTime: item[3],
                            isFlagged: item[4],
                            ogUsername: item[5],
                            username: item[6],
                        }));
                    } */ //else statement med null

                } else {
                    if (this.dateTimeArray.length == 0) {
                        const { data } = await useFetch('http://localhost:8080/search/fulltext', {
                            query: {
                                keywords: this.keywordArray.map(item => item.word).join(','),
                                dateTimeFrom: "",
                                dateTimeTo: "",
                                username: "",
                            }
                        });



                        const jsonData: any = data.value as Message[];
                        this.messages = jsonData.map((item: any[]): Message => ({
                            id: item[0],
                            customerId: item[1],
                            text: item[2],
                            dateTime: item[3],
                            isFlagged: item[4],
                            ogUsername: item[5],
                            username: item[6],
                        }));
                    } else {
                        console.log("dateTimeArray:", this.dateTimeArray);
                        const { data } = await useFetch('http://localhost:8080/search/fulltext', {
                            query: {
                                keywords: this.keywordArray.map(item => item.word).join(','),
                                dateTimeFrom: this.dateTimeArray[0].dateTimeFrom,
                                dateTimeTo: this.dateTimeArray[1].dateTimeTo,
                                username: null,
                            }
                        });
                    }  //else statement null
                    
                     
                }
            } else {
                this.messages = [];
            }
        },
        dateTimeArray: {
        handler: async function(newDateTimeArray, oldDateTimeArray) {
            console.log("filterChanged", newDateTimeArray[0]);
            const { data } = await useFetch('http://localhost:8080/search/datetime', {
                query: {
                    dateTimeFrom: newDateTimeArray[0],
                    dateTimeTo: newDateTimeArray[1],
                }
            });
            console.log(this.messages)
            const jsonData: any = data.value as Message[];

            this.messages = jsonData.map((item: any[]): Message => ({
                id: item[0],
                customerId: item[1],
                text: item[2],
                dateTime: item[3],
                isFlagged: item[4],
                ogUsername: item[5],
                username: item[6],
            }));

        },
        deep: true
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
                <div class="searchedMessage" v-for="(message) in messages" :key="message.id" @click="sendMessageId(message.id)"> 
                    <div class="messagesender">
                        {{ message.username }}:
                    </div>
                    <div classe="messageContent">
                        {{ message.text }}
                    </div> 
                    <div class="dateTime">
                        {{ new Date(message.dateTime).toLocaleString()  }}
                    </div>

                    <div v-if="message.isFlagged" class="flagged">
                        
                        <div class="icon">
                        <Icon name="material-symbols:warning-outline-rounded" class="icon"/>

                            <div class="flaggedText">
                                Flagged reason
                            </div>
                        </div>
                        


                    </div>
                    
                    <!-- {message.isFlagged ? <div class="flagged">
                        {{ message.isFlagged ? "Flagged" : "" }}
                    </div>} -->
                    <span>Customer Id: {{ message.customerId }},<br>OG Username: {{ message.ogUsername }}</span>
                </div>
            </div>
        </div>
    </div>

</template>