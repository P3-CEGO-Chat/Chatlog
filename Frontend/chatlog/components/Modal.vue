<style scoped>
    @import url('~/assets/css/modal.css');
</style>

<script lang="ts">

    export default{
        props: {
            ModalText: {
                type: String,
                default: "Test text"
            },
            ModalPicked: {
                type: Array<{ id: number, word: string, description: string }>,
                default: "Test"
            },
            
            LeftButtonText: {   
                type: String,
                default: "Yes"
            },
            LeftButtonEmit: {
                type: String,
                default: ""
            },
            RightButtonEmit: {
                type: String,
                default: ""
            },
            RightButtonText: {
                type: String,
                default: "No"
            },
            activated: {
                type: Boolean,
                default: false
            }
        },
        methods: {
            rightButtonEmit() {
                this.$emit(`${this.RightButtonEmit}`);
            },
            leftButtonEmit() {
                if (this.LeftButtonEmit == "removeFlag") {
                    this.$emit(`${this.LeftButtonEmit}`, this.ModalPicked[0].word);
                } else if (this.LeftButtonEmit == "editFlag") {
                    this.$emit(`${this.LeftButtonEmit}`, this.ModalPicked[0].id, this.ModalPicked[0].word, this.ModalPicked[0].description);
                }
            },
            detectSpace() {
                if (this.ModalPicked[0].word.includes(" ")) {
                    // Remove all whitespace from the string
                    this.ModalPicked[0].word = this.ModalPicked[0].word.replace(/\s/g, "");
                }
                console.log(this.ModalPicked);
            },
        }
    }

</script>

<template>
    <div class="container" v-if="activated">
        <div class="modal">
            <div class="modal-text">
                <h3>{{ ModalText }}</h3>
            </div>
            <div v-if="LeftButtonEmit == 'editFlag'" class="editFlag">
                <div class="inputFields">
                    <span>Flag ord</span>
                    <input type="text" v-model="ModalPicked[0].word" placeholder="Flag" required v-on:input="detectSpace" />
                    <span>Begrundelse</span>
                    <input type="text" v-model="ModalPicked[0].description" placeholder="Begrundelse" />
                </div>
                <div class ="modalButtons">
                    <button class="leftButton" @click="leftButtonEmit">{{ LeftButtonText }}</button>    
                    <button class="rightButton" @click="rightButtonEmit">{{ RightButtonText }}</button>
                </div>
            </div>
            <div v-if="LeftButtonEmit == 'removeFlag'">
                <div class="modal-picked">
                    <p>{{ ModalPicked[0].word }}</p>
                </div>
                <div class ="modalButtons">
                    <button class="leftButton" @click="leftButtonEmit">{{ LeftButtonText }}</button>    
                    <button class="rightButton" @click="rightButtonEmit">{{ RightButtonText }}</button>
                </div>
            </div>
        </div>
    </div>
</template>