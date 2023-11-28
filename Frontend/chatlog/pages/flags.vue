<style scoped>
@import url("assets/css/flags.css");
</style>

<script lang="ts">
    import('~/assets/css/main.css')

    export default {
        name: 'Flags',
        data() {
            return {
                flags: Array<{ id: number, word: string, description: string }>(),
                newflags: Array<{ word: string, description: string }>(),
                newFlag: { word: "", description: "" }
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
                        await $fetch(`http://localhost:8080/flags/addflag`, {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json"
                            },
                            body: JSON.stringify({
                                word: this.newFlag.word,
                                description: this.newFlag.description
                            })
                        })
                        
                    } catch (error) {
                        console.log("This fails" + error)
                    }
                }
            },
            async removeFlag(flagword: String) {
                try {
                    this.flags = await $fetch(`http://localhost:8080/flags/removeflag?word=${flagword}`, {
                        method: "GET",
                    })
                } catch (error) {
                    console.log("This fails" + error)
                }
            }  
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
                <h1>Flags</h1>
                <p>Flags page</p>
            </div>
            <div class="flagForm">
                <input type="text" v-model="newFlag.word" placeholder="Flag" required />
                <input type="text" v-model="newFlag.description" placeholder="Description" />
                <button @click="postFlag()">Add flag</button>
            </div>
            <div class="flags">
                <div class="flag" @click="removeFlag(flag.word)" v-for="flag in flags" :key="flag.id">
                    <h3>{{ flag.word }}</h3>
                    <p>{{ flag.description }}</p>
                </div>
            </div>
        </div>
    </div>
</template>
