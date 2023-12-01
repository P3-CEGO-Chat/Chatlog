<style scoped>
@import url("assets/css/flags.css");
</style>

<script lang="ts">

    interface addFlagResponse {
        success: string;
        id: number;
    }

    export default {
        name: 'Flags',
        data() {
            return {
                flags: Array<{ id: number, word: string, description: string }>(),
                newflags: Array<{ word: string, description: string }>(),
                newFlag: { word: "", description: "" },
                modalOpen: false,
                modal2Open: false,
                currentModalFlag: { id: 0, word: "", description: "" },
                notiVisible: false,
            }
        },
        methods: {
            async fetchAllFlags() {
                try {
                    this.flags = await $fetch(`http://localhost:8080/flags/getflags`)
                } catch (error) {
                    console.log("Hallo mfer" + error)
                }
            },
            async postFlag() {
                if (this.newFlag.word == "") {
                    alert("Please enter a flag");
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
                        
                        console.log(response.id);

                        this.flags.push({
                            id: response.id,
                            word: this.newFlag.word,
                            description: this.newFlag.description
                        });
                        
                        this.newFlag.word = "";
                        this.newFlag.description = "";

                    } catch (error) {
                        console.log("This fails" + error)
                    }
                }
            },
            async removeFlag(flagword: String) {
                try {
                    await $fetch(`http://localhost:8080/flags/removeflag?word=${flagword}`);
                    this.flags = this.flags.filter((flag: { word: String }) => flag.word !== flagword)
                    this.modalOpen = !this.modalOpen;
                } catch (error) {
                    console.log("This fails" + error)
                }
            },

            async editFlag(flagId: number, newWord: string, newDescription: string) {
                console.log("FlagId: ", flagId)
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
                    /* this.flags = this.flags.filter((flag: { id: number }) => flag.id !== flagId)
                    this.flags.push({
                        id: flagId,
                        word: this.newFlag.word,
                        description: this.newFlag.description
                    }); */
                    this.handleModal2({ id: flagId, word: newWord, description: newDescription });
                } catch (error) {
                    console.log("This fails" + error)
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
            notificationHandler(customerId: String) {
                this.notiVisible = true;
                if (this.notiVisible) {
                    console.log("Her" + customerId.toString());
                }
                setTimeout(() => {
                    this.notiVisible = false;
                }, 3000);
            },
            
        },
        async mounted() {
         await this.fetchAllFlags();
         this.flags.forEach(flag => {
             console.log(flag.word)
         });
        }
    }
</script>

<template>
    <div class="layoutFlag">
        
        <div class="container">
            <div class="title">
                <h1>Flag ord</h1>
                <p>Her kan du tilføje, redigere og slette flag ord.</p>
            </div>
            <div class="flagForm">
                <input type="text" v-model="newFlag.word" placeholder="Flag" required v-on:input="detectSpace" />
                <input type="text" v-model="newFlag.description" placeholder="Begrundelse" />
                <button @click="postFlag()">Tilføj flag</button>
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
    <Notification icon="/Tick.svg" notificationText="Nortification State" :activated="true"/>
</template>
