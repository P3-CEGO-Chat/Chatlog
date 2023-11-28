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


export default {
    data() {
        return {
            keyword: "" as string | unknown, // explicitly define the type of keyword
            ObjectArray: [],
            messages : <Message[]>[],/* Array<{ id: string, customerId: string, text: string, dateTime: string, username: string, userId: string }>() */
            notiVisible: false,
        };
    },

    props: {
        keywordArray: {
            type: Array as PropType<{ word: string, isUser: boolean }[]>,
            default: () => [] as { word: string, isUser: boolean }[]
        },
        dateTimeArray: {
            type: Array as PropType<{ dateTimeFrom: string, dateTimeTo: string }[]>,
            default: () => [] as { dateTimeFrom: string, dateTimeTo: string }[]
        },
    },

    watch: {
        keywordArray: {
            handler(newVal, oldVal) {
                // This function will be called when `keywordArray` changes
                console.log('keywordArray changed', newVal, oldVal);
                this.fetchData();
            },
            deep: true // This ensures that the watcher will detect changes in the objects inside the array
        },
        dateTimeArray: {
            handler(newVal, oldVal) {
                // This function will be called when `dateTimeArray` changes
                console.log('dateTimeArray changed', newVal, oldVal);
                this.fetchData();
            },
            deep: true // This ensures that the watcher will detect changes in the objects inside the array
        }
    },

    methods: {
        async fetchData() {
            
            if (this.keywordArray.length > 0 || this.dateTimeArray.length > 0) {
                console.log("i got here");
                const usernameIndex = this.keywordArray.findIndex(item => item.isUser);
                const arrayWithoutUsername = this.keywordArray.filter((item, index) => index !== usernameIndex); // remove username from array
                const { data } = await useFetch('http://localhost:8080/search/fulltext', {
                    query: {
                        keywords: arrayWithoutUsername.length === 0 ? "" : arrayWithoutUsername.map(item => item.word).join(','),
                        username: usernameIndex !== -1 ? this.keywordArray[usernameIndex].word.slice(1) : "",
                        dateTimeFrom: this.dateTimeArray.length !== 0 ? this.dateTimeArray[0] : null,
                        dateTimeTo: this.dateTimeArray.length !== 0 ? this.dateTimeArray[1] : null,
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
                this.messages = [];
            }
        },

        sendMessageId(messageId: Number) {
            this.$emit("updateMessageId", messageId);
        },

        notificationHandler(customerId: String) {
            this.notiVisible = true;
            if (this.notiVisible) {
                console.log("Her" + customerId.toString());
                navigator.clipboard.writeText(customerId.toString());
            }
            setTimeout(() => {
                this.notiVisible = false;
            }, 3000);
        },
    },
}
</script>

<template>
    <div class="container">
        <div class="SearchField">
            <div class="SearchTex">

                Viser resultat for: "{{ keywordArray.map(keyword => keyword.word).join(', ') }}"
            </div>
            <div class="scrollBar">
                <div class="searchedMessage" v-for="(message) in messages" :key="message.id"
                    @click="sendMessageId(message.id)">
                    <div class="messagesender">
                        {{ message.username }}:
                    </div>
                    <div classe="messageContent">
                        {{ message.text }}
                    </div>
                    <div class="dateTime">
                        {{ new Date(message.dateTime).toLocaleString() }}
                    </div>

                    <div v-if="message.isFlagged" class="flagged">
                        <div class="icon">
                            <Icon name="material-symbols:warning-outline-rounded" class="icon" />
                            <div class="flaggedText">
                                Flagged reason
                            </div>
                        </div>
                    </div>

                    <!-- {message.isFlagged ? <div class="flagged">
                        {{ message.isFlagged ? "Flagged" : "" }}
                    </div>} -->
                    <span @click="notificationHandler(message.customerId)" >Customer Id: {{ message.customerId }},<br>OG Username: {{ message.ogUsername }}</span>
                </div>
            </div>
        </div>
    </div>
    <Notification icon="/Tick.svg" notificationText="CustomerId copied" :activated="notiVisible"/>
</template>