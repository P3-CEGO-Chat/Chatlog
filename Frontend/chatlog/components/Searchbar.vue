<style scoped>
@import url("assets/css/searchBar.css");
</style>

<script lang="ts">
import Calendar from './Calendar.vue';

export default {
  data() {
    return {
      searchKeyword: '',
      keywordArray: Array<{ word: string, isUser: boolean, isCustomerId: boolean }>(), // New data property
      wordObject: {word: "", isUser: false, isCustomerId: false},
      showInfoBox: false,
      infoBoxLeft: '0px',
      showCalendar: true,
      dateTimeObject: { dateTimeFrom: "", dateTimeTo: "" },
      firstKeywordEntered: false,
    };
  },
  
  methods: {
    removeKeyword(index: number) {
      this.keywordArray.splice(index, 1);
      this.updateKeywords();
    },

    updateKeywords() {
      this.$emit("updateKeywordArray", this.keywordArray);
    },

    // om enter is pressed, add the keyword to the keywordArray
    onEnter() {
      if (this.searchKeyword.trim() === '') {
        this.showInfoBox = true;
        const offset = -45; // Adjust this value as needed
        this.infoBoxLeft = `${(this.$refs.searchInput as HTMLInputElement).getBoundingClientRect().left + offset}px`;

        setTimeout(() => {
          this.showInfoBox = false;
        }, 2000); // 3000 milliseconds = 3 seconds
        return;
      }

      // Check if the keyword is a user or a keyword
      if (this.searchKeyword[0] === "@") {
        this.wordObject = { "word": this.searchKeyword, "isUser": true, "isCustomerId": false };
      } else if (this.searchKeyword.startsWith("SN") && ["0", "1", "2", "3", "4"].includes(this.searchKeyword[2])) {
        this.wordObject = { "word": this.searchKeyword, "isUser": false, "isCustomerId": true };
      } else {
        this.wordObject = { "word": this.searchKeyword, "isUser": false, "isCustomerId": false };
      }

      const newKeywordLowercase = this.wordObject.word.toLowerCase();
      const keywordExists = this.keywordArray.some(keyword => keyword.word.toLowerCase() === newKeywordLowercase);
      
      // if the keyword doesn't already exist, add it to the keywordArray
      if (!keywordExists) {
        this.keywordArray.push(this.wordObject);
        if (this.keywordArray.length === 1) {
          this.firstKeywordEntered = true;
        }
      }

      // Sort the keywordArray so that usernames are first
      this.keywordArray.sort((a, b) => {
        if (a.word.startsWith("@") && !b.word.startsWith("@")) {
          return -1;
        } else if (!a.word.startsWith("@") && b.word.startsWith("@")) {
          return 1;
        } else {
          return 0;
        }
      });

      this.searchKeyword = "";
      this.updateKeywords();
    },

    // if the user types a space, remove it
    detectSpace() {
      if (this.searchKeyword.includes(" ")) {
        // Remove all whitespace from the string
        this.searchKeyword = this.searchKeyword.replace(/\s/g, "");
        // Check if the string is empty
        if (this.searchKeyword.length > 0) {
          this.onEnter();
        }
      }
    },

    // update the dateTimeArray
    updateDateTimeObject(newDateTimeObject: { dateTimeFrom: string, dateTimeTo: string }) {
      this.dateTimeObject = newDateTimeObject;
      this.$emit("updateDateTimeObject", this.dateTimeObject);
    }
  },
  
  computed: {
    placeholderText() {
      return this.keywordArray.length > 0 ? 'Tilføj kriterier' : 'Søg efter brugernavn/kundenummer eller nøgleord';
    }
  },
}
</script>

<template>
  <div :class="['container', firstKeywordEntered ? 'containerExpanded' : '']">
    <div class="topContainer">
      <div class="searchBox">
        <Icon name="heroicons-solid:search" color="#D9D9D9" class="searchIcon" />

        <div class="infoBox" :class="{ show: showInfoBox }" :style="{ left: infoBoxLeft }">
          <span class="infoText">Intet skrevet</span>
        </div>

        <input ref="searchInput" class="searchField" type="text" v-model="searchKeyword" :placeholder="placeholderText"
          v-on:keyup.enter="onEnter" v-on:input="detectSpace" />

        <div class="infoDiv">
          <Icon name="humbleicons:info-circle" color="#6CA5FC" class="infoIcon" />
          <span class="infoText">Brug @ foran brugernavn</span>
        </div>
      </div>

      <div v-if="showCalendar" class="calendar">
        <Calendar @updateDateTimeObject="updateDateTimeObject" />

      </div>
    </div>

    <div class="bottomContainer">
      <div class="keywordContainer">
        <div class="keyword" v-for="(keyword, index) in keywordArray" :key="index" @click="removeKeyword(index)"
          :style="{ backgroundColor: keyword.isUser ? '#6CA5FC' : keyword.isCustomerId ? '#4ed378' : '#FFB84B' }">
          {{ keyword.word }}
          <Icon name="ri:close-circle-line" color="white"></Icon>
        </div>
      </div>
    </div>
  </div>
</template>


  

