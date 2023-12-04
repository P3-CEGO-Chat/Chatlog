<style scoped>
@import url('~/assets/css/filterchat.css');
</style>

<script lang="ts">
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
            messages: <Message[]>[],/* Array<{ id: string, customerId: string, text: string, dateTime: string, username: string, userId: string }>() */
            notiVisible: false,
            title: "Venter på søgning",
            TempTimeStart: {},
            TempTimeEnd: {},
            isFlagged: false,
            checked: false,
            messageReverseClass: "",
        };
    },

    props: {
        keywordArray: {
            type: Array as PropType<{ word: string, isUser: boolean, isCustomerId: boolean }[]>,
            default: () => [] as { word: string, isUser: boolean, isCustomerId: boolean }[]
        },
        dateTimeObject: {
            type: Object as PropType<{ dateTimeFrom: string, dateTimeTo: string }>,
            default: () => ({ dateTimeFrom: "", dateTimeTo: "" })
        },
    },

    watch: {
        keywordArray: {
            handler(newVal, oldVal) {
                // This function will be called when `keywordArray` changes
                console.log('keywordArray changed', newVal, oldVal);
                this.fetchData();
                this.title = "Søger efter: ";
            },
            deep: true // This ensures that the watcher will detect changes in the objects inside the array
        },
        dateTimeObject: {
            handler(newVal, oldVal) {
                // This function will be called when `dateTimeArray` changes
                console.log('dateTimeObject changed', newVal, oldVal);
                this.fetchData();
            },
            deep: true // This ensures that the watcher will detect changes in the objects inside the array
        },
        isFlagged: {
            handler(newVal, oldVal) {
                console.log('isFlagged changed', newVal, oldVal);

                if (newVal) {
                    // Checkbox is checked, perform your acti
                    this.findFlaggedMessages();
                } else {
                    // Checkbox is unchecked, perform your action here
                }
            }
        },
        messages: {
            handler() {
                // This function will be called when `messages` changes
                if (this.messages.length === 1) {
                }
            },
            deep: true // This ensures that the watcher will detect changes in the objects inside the array
        },
        checked:{
            handler(newVal, oldVal) {
            console.log(`Checkbox is now: ${newVal ? 'Checked' : 'Unchecked'}`);
            }
        }
    },

    methods: {

        async fetchData() {
            // This function will be called when `keywordArray` or `dateTimeArray` changes
            if (this.keywordArray.length > 0 || this.hasDateTimeData) {
                const usernameIndex = this.keywordArray.findIndex(item => item.isUser);
                const customerIdIndex = this.keywordArray.findIndex(item => item.isCustomerId);
                const arrayWithoutUsername = this.keywordArray.filter((item, index) => index !== usernameIndex && index !== customerIdIndex); // remove username from array
                // fetch data from backend
                console.log("kig her: ", this.dateTimeObject)
                const { data } = await useFetch('http://localhost:8080/search/fulltext', {
                    query: {
                        keywords: arrayWithoutUsername.length === 0 ? "" : arrayWithoutUsername.map(item => item.word).join(','),
                        username: usernameIndex !== -1 ? this.keywordArray[usernameIndex].word.slice(1) : "",
                        dateTimeFrom: this.hasDateTimeData ? this.dateTimeObject.dateTimeFrom : null,
                        dateTimeTo: this.hasDateTimeData ? this.dateTimeObject.dateTimeTo : null,
                        customerId: customerIdIndex !== -1 ? this.keywordArray[customerIdIndex].word : "",
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
                // reset messages
                this.messages = [];
                this.title = "Venter på søgning";
            }
        },

        // update messageId in parent component
        sendMessageId(messageId: Number) {
            this.$emit("updateMessageId", messageId);
        },

        // format datetime
        formatDateTime(datetime: string) {
            const dat = new Date(datetime);
            return `${dat.getDate()}/${dat.getMonth() + 1}/${dat.getFullYear()}` +
                ` ${dat.getHours()}:${dat.getMinutes()}`;
        },

        // notification handler
        notificationHandler(customerId: String) {
            this.notiVisible = true;
            if (this.notiVisible) {
                navigator.clipboard.writeText(customerId.toString());
            }
            setTimeout(() => {
                this.notiVisible = false;
            }, 3000);
        },

        async findFlaggedMessages() {
            const { data } = await useFetch(`http://localhost:8080/messages/find-flagged-messages`);
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

        console.log(this.messages); // Logs flagged messages to the console
        return this.messages;
        },

        messageHighestChecker(messageId: Number) {
            let highestId = 0;
            if (this.messages.length <= 1) {
                return false;
            }
            for (const message of this.messages) {
                if (message.id > highestId) {
                    highestId = message.id;
                }
            }
            if (messageId === highestId) {
                return true;
            } else {
                return false;
            }
        },
    },
    // compute the formatted datetime array
    computed: {
        /* formattedDateTimeArray() {
            return this.dateTimeArray.map(datetime => {
                const date = new Date(datetime);
                return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}` +
                    ` ${date.getHours()}:${date.getMinutes()}`;
            })
        }, */
        // check if the dateTimeObject has any data
        hasDateTimeData() {
            return Object.keys(this.dateTimeObject).length > 0;
        }
    },
    }
</script>

<template>
    <div class="container">
        <div class="SearchField">
            <div class="SearchTex">
                Viser resultat for tidsrummet: "{{ dateTimeObject.dateTimeFrom && dateTimeObject.dateTimeTo ? formatDateTime(dateTimeObject.dateTimeFrom) + ' - ' + formatDateTime(dateTimeObject.dateTimeTo) : '' }}"
                <div class="flaggedCheckBox">
                    <input type="checkbox" id="checkbox" v-model="checked">
                    <label for="checkbox">Flagged</label>
                </div>
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
                    <span @click="notificationHandler(message.customerId)">Customer Id: {{ message.customerId }},<br>OG
                        Username: {{ message.ogUsername }}</span>
                    <span :class="messageHighestChecker(message.id) ? 'highestId' : ''" @click="notificationHandler(message.customerId)" >Customer Id: {{ message.customerId }},<br>OG Username: {{ message.ogUsername }}</span>
                </div>
            </div>
        </div>
    </div>
    <Notification icon="/Tick.svg" notificationText="Kundenummer Kopieret" :activated="notiVisible"/>
</template>