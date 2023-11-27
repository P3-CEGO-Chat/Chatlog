<style scoped>
@import url("assets/css/flags.css");
</style>

<script lang="ts">
    import('~/assets/css/main.css')

    export default {
        name: 'Flags',
        data() {
            return {
                flags: Array<{ id: number, word: string, description: string }>()
            }
        },
        methods: {
            async fetchAllFlags() {
                try {
                    this.flags = await $fetch(`http://localhost:8080/flags/getflags`)
                } catch (error) {
                    console.log(error)
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
                <input type="text" placeholder="Flag" />
                <input type="text" placeholder="Description" />
                <button>Add flag</button>
            </div>
            <div class="flags">
                <div class="flag" v-for="flag in flags" :key="flag.id">
                    <h3>{{ flag.word }}</h3>
                    <p>{{ flag.description }}</p>
                </div>
            </div>
        </div>
    </div>
</template>
