<style scoped>
    @import url('~/assets/css/navBar.css');
</style>

<script lang="ts">
    import type { NuxtLink } from '#build/components';

    export default {
        data() {
            return {
                title: '',
            };
        },

        methods: {
            isCurrentPage(route: string) {
                this.title = this.$route.path === '/' ? 'SpilNu' : 
                this.$route.path === '/flags' ? 'flag ord' : route.charAt(1).toUpperCase() + route.slice(2);
                return this.$route.path === route;
            },
        },
    }
</script>

<template>
    <div class="navbar">
        <div class="title">
            <NuxtLink to="/">{{ "Chatlog - " + title }}</NuxtLink>
        </div>
        <div class="navs">
            <NuxtLink to="/" class="link" :disabled="isCurrentPage('/')">
                SpilNu
                <hr/>
            </NuxtLink>
            <NuxtLink to="/happytiger" class="link" :disabled="isCurrentPage('/happytiger')" >
                HappyTiger
                <hr/>
            </NuxtLink>
            <NuxtLink to="/lyckost" class="link" :disabled="isCurrentPage('/lyckost')">
                Lyckost
                <hr/>
            </NuxtLink>
            <NuxtLink to="/foruroligende-ord" class="link" :disabled="isCurrentPage('/foruroligende-ord')">
                Foruroligende ord
                <hr/>
            </NuxtLink>
        </div>
    </div>
</template>