<style scoped>
@import url("assets/css/foruroligendeOrd.css");
</style>

<script lang="ts">

    interface addFlagResponse {
        success: string;
        id: number;
    }

    export default {
        name: 'ForuroligendeOrd',
        data() {
            return {
                flags: Array<{ id: number, word: string, description: string }>(),
                newflags: Array<{ word: string, description: string }>(),
                newFlag: { word: "", description: "" },
                modalOpen: false,
                modal2Open: false,
                currentModalFlag: { id: 0, word: "", description: "" },
                notificationData: { visible: false, text: "", alert: false },
                emptyFlagInput: false,
            }
        },
        methods: {
            async fetchAllFlags() {
                console.log("Fetching flags")
                try {
                    this.flags = await $fetch(`http://localhost:8080/flags/getflags`)
                } catch (error) {
                    console.error(error)
                }
            },
            async postFlag() {
                console.log("Posting flag")
                if (this.newFlag.word == "") {
                    this.notificationHandler("Indtast venligst et flag", true);
                    return;
                } else {
                    try {
                        const response: addFlagResponse = await $fetch(`http://localhost:8080/flags/addflag`, {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json"
                            },
                            body: JSON.stringify({
                                word: this.newFlag.word,
                                description: this.newFlag.description
                            })
                        })

                        this.flags.push({
                            id: response.id,
                            word: this.newFlag.word,
                            description: this.newFlag.description
                        });
                        
                        this.newFlag.word = "";
                        this.newFlag.description = "";
                        this.notificationHandler("Flag tilføjet", false);
                    } catch (error) {
                        this.notificationHandler("Flag eksisterer allerede", true);

                    }
                }
            },
            async removeFlag(flagword: String) {
                try {
                    console.log("Removing flag")
                    await $fetch(`http://localhost:8080/flags/removeflag?word=${flagword}`);
                    this.flags = this.flags.filter((flag: { word: String }) => flag.word !== flagword)
                    this.modalOpen = !this.modalOpen;
                    this.notificationHandler("Flag fjernet", false);
                } catch (error) {
                    console.error(error)
                }
            },

            async editFlag(flagId: number, newWord: string, newDescription: string) {
                try {
                    await $fetch(`http://localhost:8080/flags/updateflag`, {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            id: flagId,
                            word: newWord,
                            description: newDescription
                        })
                    })
                    this.handleModal2({ id: flagId, word: newWord, description: newDescription });
                    this.notificationHandler("Flag redigeret", false);
                } catch (error) {
                    console.error(error)
                }
            },

            detectSpace() {
                if (this.newFlag.word.includes(" ")) {
                    // Remove all whitespace from the string
                    this.newFlag.word = this.newFlag.word.replace(/\s/g, "");
                }
            },

            handleModal(flag: { id: number, word: string, description: string }) {
                this.currentModalFlag = flag;
                this.modalOpen = !this.modalOpen;
            },

            handleModal2(flag: { id: number, word: string, description: string }) {
                this.currentModalFlag = flag;
                this.modal2Open = !this.modal2Open;
            },
            notificationHandler(text: string, alert: boolean) {
                this.notificationData = { visible: true, text: text, alert: alert}
                if (alert) {
                    this.emptyFlagInput = true;
                }
                setTimeout(() => {
                    this.notificationData = { visible: false, text: "", alert: false}
                    if (alert) {
                        this.emptyFlagInput = false;
                    }
                }, 3000);
            },
        },
        async mounted() {
         await this.fetchAllFlags();
        }
    }
</script>

<template>
    <div class="layoutFlag">
        
        <div class="container">
            <div class="title">
                <h1>Foruroligende Ord</h1>
                <p>Her kan du tilføje, redigere og slette foruroligende ord.</p>
            </div>
            <div class="flagForm">
                <input type="text" v-model="newFlag.word" placeholder="Foruroligende ord" required class="input" :class="emptyFlagInput ? 'error': ''" v-on:input="detectSpace" />
                <input type="text" v-model="newFlag.description" class="input" placeholder="Begrundelse" />
                <button @click="postFlag()">Tilføj Foruroligende ord</button>
            </div>
            <div class="flags">
                <div class="flag"  v-for="flag in flags" :key="flag.id">
                    <Modal @handleModal="handleModal" @removeFlag="removeFlag" RightButtonEmit="handleModal" LeftButtonEmit="removeFlag" ModalText="Ønsker du at slette dette ord?" :activated="modalOpen" :ModalPicked="[currentModalFlag]"  />
                    <Modal @handleModal="handleModal2" @editFlag="editFlag" RightButtonEmit="handleModal" LeftButtonEmit="editFlag" ModalText="Rediger herunder" :activated="modal2Open" :ModalPicked="[currentModalFlag]"  />
                    
                    <h3>{{ flag.word }}</h3>
                    <p>{{ flag.description }}</p>
                    <div class="buttonContainer">
                        <button class="editButton" @click="handleModal2(flag)">Rediger</button>
                        <button class="removeButton" @click="handleModal(flag)">Fjern</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <Notification :icon="notificationData.alert ? '/Alert.svg' : '/Tick.svg'" :notificationText="notificationData.text" :activated="notificationData.visible" :alert="notificationData.alert"/>
</template>
