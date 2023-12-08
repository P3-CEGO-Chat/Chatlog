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
    isFlagged: number;
    ogUsername: string;
    username: string;
    description?: string;
}

interface flagWord {
    id: number;
    word: string;
    description: string;
}

export default {
    emits: ["updateMessageId"],
    data() {
        return {
            keyword: "" as string | unknown, // explicitly define the type of keyword
            ObjectArray: [],
            messages: <Message[]>[],/* Array<{ id: string, customerId: string, text: string, dateTime: string, username: string, userId: string }>() */
            notificationVisible: false,
            title: "Venter på søgning",
            TempTimeStart: {},
            TempTimeEnd: {},
            isFlagged: false,
            checked: false,
            messageReverseClass: "",
            flagWord: {} as flagWord,
        };
    },

    props: {
        // This is the array of keywords that will be used to search for messages
        keywordArray: {
            type: Array as PropType<{ word: string, isUser: boolean, isCustomerId: boolean }[]>,
            default: () => [] as { word: string, isUser: boolean, isCustomerId: boolean }[]
        },
        // This is the object of datetime that will be used to search for messages
        dateTimeObject: {
            type: Object as PropType<{ dateTimeFrom: string, dateTimeTo: string }>,
            default: () => ({ dateTimeFrom: "", dateTimeTo: "" })
        },
    },

    watch: {
        keywordArray: {
            handler() {
                // This function will be called when `keywordArray` changes
                this.fetchData();
                this.title = "Søger efter: ";
                console.log("hey")
            },
            deep: true // This ensures that the watcher will detect changes in the objects inside the array
        },
        dateTimeObject: {
            handler() {
                // This function will be called when `dateTimeObject` changes
                this.fetchData();

            },
            deep: true // This ensures that the watcher will detect changes in the objects inside the array
        },
        isFlagged: {
            handler(newVal) {
                if (newVal) {
                    // Checkbox is checked, perform your acti
                    this.findFlaggedMessages();
                } else {
                    // Checkbox is unchecked, perform your action
                }
            }
        },
        messages: {
            handler() {
                // This function will be called when `messages` changes
                this.getFlaggedData().then((data) => {
                    for (const message of this.messages) {
                        for (const flagWord of data) {
                            if (message.isFlagged == flagWord.id) {
                                message.description = flagWord.description;
                                console.log(message.description);
                            }
                        }
                    }
                });
            },
            deep: true // This ensures that the watcher will detect changes in the objects inside the array
        },
        checked:{
            handler() {
                this.fetchData();
            }
        }
    },

    mounted(){
            if (this.$route.query.messageid) {
            this.sendMessageId(Number(this.$route.query.messageid));
            }
            console.log(this.$route.query.messageid);
        },

    methods: {

        async fetchData() {
            // This function will be called when `keywordArray` or `dateTimeArray` changes
            if (this.keywordArray.length > 0 || this.hasDateTimeData || this.checked === true) {
                const usernameIndex = this.keywordArray.findIndex(item => item.isUser);
                const customerIdIndex = this.keywordArray.findIndex(item => item.isCustomerId);
                const arrayWithoutUsername = this.keywordArray.filter((item, index) => index !== usernameIndex && index !== customerIdIndex); // remove username from array
                // fetch data from backend
                const { data } = await useFetch('http://localhost:8080/search/fulltext', {
                    query: {
                        keywords: arrayWithoutUsername.length === 0 ? "" : arrayWithoutUsername.map(item => item.word).join(','),
                        username: usernameIndex !== -1 ? this.keywordArray[usernameIndex].word.slice(1) : "",
                        dateTimeFrom: this.hasDateTimeData ? this.dateTimeObject.dateTimeFrom : null,
                        dateTimeTo: this.hasDateTimeData ? this.dateTimeObject.dateTimeTo : null,
                        customerId: customerIdIndex !== -1 ? this.keywordArray[customerIdIndex].word : "",
                        isFlagged: this.checked === true ? true : false,
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
            this.notificationVisible = true;
            if (this.notificationVisible) {
                navigator.clipboard.writeText(customerId.toString());
            }
            setTimeout(() => {
                this.notificationVisible = false;
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

        return this.messages;
        },

        // check if the message is the highest id
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

        async getFlaggedData() {
            const { data } = await useFetch(`http://localhost:8080/flags/getflags`);
            const jsonData: flagWord[] = data.value as flagWord[];
            return jsonData;
        },
    },
    // compute the formatted datetime array
    computed: {
        hasDateTimeData() {
            if (this.dateTimeObject.dateTimeFrom == "") {
                return false;
            }
            return Object.keys(this.dateTimeObject).length > 0;
        },

        checkedAsInt() {
            return this.checked ? 1 : 0;
        },
    },
    }
</script>

<template>
    <div class="container">
        <div class="SearchField">
            <div class="SearchTex">
                Viser resultat for tidsrummet: "{{ dateTimeObject.dateTimeFrom && dateTimeObject.dateTimeTo ? formatDateTime(dateTimeObject.dateTimeFrom) + ' - ' + formatDateTime(dateTimeObject.dateTimeTo) : '' }}"
                <div class="flaggedCheckBox">
                    <label for="checkbox">Foruroligende Ord</label>
                    <input type="checkbox" id="checkbox" v-model="checked">
                </div>
            </div>

            <div class="scrollBar">
                <div class="searchedMessage" v-for="(message) in messages" :key="message.id"
                    @click="sendMessageId(message.id)">
                    <div class="messagesender">
                        {{ message.ogUsername }}:
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
                            <div :class="messageHighestChecker(message.id) ? 'flaggedText highestIdFlagged' : 'flaggedText'">
                                {{ message.description ? message.description : 'Ukendt grund' }}
                            </div>
                        </div>
                    </div>
                    <span :class="messageHighestChecker(message.id) ? 'highestId' : ''" @click="notificationHandler(message.customerId)" >Kundenummer: {{ message.customerId }},<br>Aktivt brugernavn: {{ message.username }}</span>
                </div>
            </div>
        </div>
    </div>
    <Notification icon="/Tick.svg" notificationText="Kundenummer Kopieret" :activated="notificationVisible"/>
</template>