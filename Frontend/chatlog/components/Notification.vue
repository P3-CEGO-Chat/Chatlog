<style scoped>
    @import url('~/assets/css/notification.css');
</style>

<script lang="ts">
    export default{
        props: {
            icon: {
                type: String,
                default: "Icon here "
            },
            notificationText: {
                type: String,
                default: "Test notifcation text"
            },
        },


    }
</script>

<template>
    <div class="notification">
        <div class="notification-icon">
            <img :src="icon" alt="notification-icon">
        </div>
        <div class="notification-text">
            <p>{{ notificationText }}</p>
        </div>
    </div>
</template>