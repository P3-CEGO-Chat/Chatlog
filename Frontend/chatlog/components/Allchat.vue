<style scoped>
@import url("~/assets/css/allChat.css");
</style>

<script lang="ts">

// Define the structure of a message
interface Message {
  id: number;
  customerId: string;
  dateTime: string;
  text: string;
  isFlagged: number;
  ogUsername: string;
  description?: string;
}

interface flagWord {
    id: number;
    word: string;
    description: string;
}

export default {
  data() {
    // Initialize data properties
    return {
      messages: <Message[]>[], // Array to hold chat messages
      currentPage: 1, // Current page of chat history
      originalPageCounter: 1, // Counter for original page
      initialLoad: true, // Flag for initial load
      HighestMessageId: 0, // ID of the highest message
      title: "Live Chat", // Title of the chat
      chatLive: true, // Flag for live chat
      newMessages: <Message[]>[], // Array to hold new messages
    };
  },

  async mounted() {
    // Fetch the highest ID and messages when the component is mounted
    await this.fetchHighestId();
    const { data } = await useFetch(`http://localhost:8080/messages/${this.currentPage}-${this.HighestMessageId}`);
    // Parse the fetched messages and store them in the messages array
    this.messages = this.parseMessage(data);

    this.originalPageCounter = this.currentPage;

    // Establish a WebSocket connection
    const socket = new WebSocket("ws://localhost:8080/websocket");

    socket.onopen = function (event) {
    }

    socket.onmessage = (event) => {
      
      // Handle incoming messages if chat is live
      if (this.chatLive === true) {
        const parsedData = JSON.parse(event.data);
        if (parsedData.event === "newMessage") {
          // Create a new message object from parsedData.data
          const newMessage = {
            id: -1,
            customerId: parsedData.data.customerId,
            text: parsedData.data.messageText,
            dateTime: parsedData.data.dateTime,
            isFlagged: parsedData.data.isFlagged,
            ogUsername: parsedData.data.ogusername,
            description: parsedData.data.description,
          };
          
          // Prepend the new message to the messages array
          this.messages = [...this.messages, newMessage];
          if (newMessage.isFlagged != null) {
            this.postMessageToSlack(newMessage);
          }
          
          // After the next DOM update, scroll to the bottom
          this.$nextTick(() => {
            this.scrollTobottom();
          });
        }
      }
      
      
    }

    // Handle WebSocket errors
    socket.onerror = function (error) {
      console.error("Socket encountered error:", error, "Closing socket");
      socket.close();
    }

    // Scroll to the bottom after the next DOM update
    this.$nextTick(() => {
      this.scrollTobottom();
    });
  },
  
  updated() {
    // Scroll to the bottom after the next DOM update
    this.$nextTick(() => {
      this.scrollTobottom();
    });
  },

  // Define props
  props: {
    messageId: {
      type: Number,
      default: 0,
    },
  },

  // Watch for changes in messageId
  watch: {
    async messageId() {
      if (this.messageId != 0) {
        this.findMessage();
      }
    },

    messages: {
      handler() {
        console.log("messages changed");
        this.getFlaggedData().then((data) => {
          for (const message of this.messages) {
              for (const flagWord of data) {
                  if (message.isFlagged == flagWord.id) {
                      message.description = flagWord.description;
                  }
              }
          }
        });
      },
    }

    
  },

  methods: {
    async postMessageToSlack(Message: any) {
      
      const message = { text: this.messages[this.messages.length-1].text,
      description: this.messages[this.messages.length-1].description
      };
      message.text = "(Flagged) " + this.messages[this.messages.length-1].ogUsername + ": " + message.text + message.description;

      try {
        const response = await $fetch(`http://localhost:8080/api/sendToSlack`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(message),
        })
      } catch (error) {
        console.error('Error posting message to Slack', error);
      }
    },

    async resetMessageId() {
      this.$emit('resetMessageId');
    },

    // Check the scroll position
    async checkScroll(event: Event) {
      const target = event.target as Element;
      const scrollTopBeforeLoad = target.scrollTop;
      const scrollHeightBeforeLoad = target.scrollHeight;
      const temp = Math.ceil(this.HighestMessageId / 25);

      // If the user has scrolled to the top of the scrollbar, fetch more messages
      if (target.scrollTop === 0 && target.scrollHeight - target.clientHeight > 0 && this.currentPage != temp) {
        this.currentPage++;
        this.chatLive = false;
        const { data } = await useFetch(`http://localhost:8080/messages/${this.currentPage}-${this.HighestMessageId}`);
        this.newMessages = this.parseMessage(data);
        this.messages = this.newMessages.concat(this.messages);
      }

      // After the next DOM update, check if the user has scrolled to the bottom of the scrollbar
      this.$nextTick(async () => {
        const scrollHeightAfterLoad = target.scrollHeight;
        const scrollHeightChange = scrollHeightAfterLoad - scrollHeightBeforeLoad;
        target.scrollTop = scrollTopBeforeLoad + scrollHeightChange;

        // If the user has scrolled to the bottom of the scrollbar, fetch previous messages
        if (target.scrollTop + target.clientHeight >= scrollHeightAfterLoad - 1) {
          if (!this.initialLoad && this.originalPageCounter != 1) {
            if (this.originalPageCounter === Math.ceil(this.HighestMessageId / 25)) {
              this.originalPageCounter--;
            }
            this.originalPageCounter--;

            const { data } = await useFetch(`http://localhost:8080/messages/${this.originalPageCounter}-${this.HighestMessageId}`);
            this.newMessages = this.parseMessage(data);
            // Add the previous messages to the end of the messages array
            this.messages = this.messages.concat(this.newMessages);
          }
        }
        this.initialLoad = false;
      });
    },

    // Clear the chat and scroll to the bottom
    async buttonClear() {
      await this.fetchHighestId();
      this.messages = [];
      this.currentPage = 1;
      this.originalPageCounter = this.currentPage;
      this.chatLive = true;
      const { data } = await useFetch(`http://localhost:8080/messages/${this.currentPage}-${this.HighestMessageId}`);
      this.messages = this.parseMessage(data);
      this.$emit('resetMessageId');
      this.$nextTick(() => {
        this.scrollTobottom();
        this.title = `Live Chat`;
      });


    },

    // Find a specific message
    async findMessage() {
      await this.fetchHighestId();
      this.findTheCurrentPage();
      this.chatLive = false;
      this.initialLoad = true;
      this.title = `Chat historik`;
      //find a specific message and update it
      this.messages = [];
      if (this.currentPage === 1) {
        const { data } = await useFetch(`http://localhost:8080/messages/${this.currentPage}-${this.HighestMessageId}`);
        this.messages = this.parseMessage(data);
      }
      else {
        const { data } = await useFetch(`http://localhost:8080/messages/message-id/${this.messageId}`);
        this.messages = this.parseMessage(data);
      }

    
      this.$nextTick(() => {
        this.scrollToMessage();
      });
    },

    // Find the current page
    findTheCurrentPage() {
      this.currentPage = Math.ceil((this.HighestMessageId - this.messageId + 1) / 25);
      this.originalPageCounter = this.currentPage;
    },

    // Scroll to the bottom of the scrollbar
    scrollTobottom() {
      const scrollBar = this.$el.querySelector('.scrollBar');
      if (scrollBar) {
        scrollBar.scrollTop = scrollBar.scrollHeight;
      }
    },

    // Scroll to a specific message
    scrollToMessage() {
      const messageRef = `message-${this.messageId}`;
      const messageElement = this.$refs[messageRef] as HTMLElement[];

      if (messageElement && messageElement[0]) {
        const scrollBar = this.$el.querySelector('.scrollBar');
        if (scrollBar) {
          scrollBar.scrollTop = messageElement[0].offsetTop - scrollBar.offsetTop;
        }
      }
    },

    // Fetch the highest ID
    async fetchHighestId() {
      try {
        const data = await $fetch(`http://localhost:8080/messages/find-highest-id`)
        this.HighestMessageId = Number(data);
      } catch (error) {
      }
    },

    // Parse the fetched messages
    parseMessage(data: any) {
      const messages = JSON.parse(data.value as string).map((item: any[]): Message => ({
        id: item[0],
        customerId: item[1],
        text: item[2],
        dateTime: item[3],
        isFlagged: item[4],
        ogUsername: item[5],
      }));
      return messages;
    },

    async getFlaggedData() {
      const { data } = await useFetch(`http://localhost:8080/flags/getflags`);
      const jsonData: flagWord[] = data.value as flagWord[];
      return jsonData;
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
  }
}
</script> 

<template>
  <div class="container">
    <div class="messageWindow">
      <div class="header">{{ title }}</div>
      <!-- Scroll event listener to check scroll position -->
      <div class="scrollBar" @scroll="checkScroll">
        <!-- Loop through messages and display them -->
        <div class="messageBox" 
        v-for="message in messages" 
        :key="message.id" 
        :ref="`message-${message.id}`"
        :class="{ 'highlightedMessage': message.id === messageId }">
          <div class="messageHeader">
            <div class="CustomerId">{{ message.ogUsername }}:&nbsp</div>
            <div class="messageContent">{{ message.text }}</div>
            <div class="Time">{{ new Date(message.dateTime).toLocaleString() }}</div>
            <!-- Display flagged icon if message is flagged -->
            <div v-if="message.isFlagged" class="flagged">
              <div class="icon">
                <Icon name="material-symbols:warning-outline-rounded" class="icon" />
                <div :class="messageHighestChecker(message.id) ? 'flaggedText highestIdFlagged' : 'flaggedText'">
                  {{ message.description ? message.description : "Ukendt grund" }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- Button to clear chat and scroll to bottom -->
      <button class="clearButton" v-if="!chatLive" @click="buttonClear">Se nyeste beskeder</button>
    </div>
  </div>
</template>
