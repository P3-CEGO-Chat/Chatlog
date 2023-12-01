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
            activated: {
                type: Boolean,
                default: false
            },
            alert: {
                type: Boolean,
                default: true
            },
        },
        computed: {
            notificationClass() {
                return {
                    "alert": this.alert
                }
            }
        }
    }

</script>

<template>
    <div class="notification" v-if="activated" :class="notificationClass">
        <div class="notification-icon">
            <img :src="icon" alt="notification-icon">
        </div>
        <div class="notification-text">
            <p>{{ notificationText }}</p>
        </div>
    </div>
</template>