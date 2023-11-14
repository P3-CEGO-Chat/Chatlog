<style scoped>
@import url("assets/css/searchBar.css");
</style>

<script>
export default {
  data() {
    return {
      searchKeyword: '',
      keywordArray: [], // New data property
      wordObject: {word: "", isUser: false}
    };
  },
  computed: {
    placeholderText() {
      return this.keywordArray.length > 0 ? 'Tilføj kriterier' : 'Søg efter brugernavn/kundenummer eller nøgleord';
    }
  },

  methods: {
    removeKeyword(index) {
      this.keywordArray.splice(index, 1);
      this.updateKeywords();
    },

    updateKeywords() {
      this.$emit("updateKeywordArray", this.keywordArray);
    },

    onEnter() {
      if (this.searchKeyword[0] === "@") {
        this.wordObject = {"word": this.searchKeyword, "isUser": true};
      } else {
        this.wordObject = {"word": this.searchKeyword, "isUser": false};
      }
      const newKeywordLowercase = this.wordObject.word.toLowerCase(); 
      const keywordExists = this.keywordArray.some(keyword => keyword.word.toLowerCase() === newKeywordLowercase);
      if (!keywordExists) {
      this.keywordArray.push(this.wordObject);
      }      
      this.searchKeyword = "";
      this.updateKeywords();
    },

    detectSpace() {
      if (this.searchKeyword.includes(" ")) {
        // Remove all whitespace from the string
        const wordWithoutSpace = this.searchKeyword.replace(/\s/g, "");
        if (wordWithoutSpace.length > 0) {
          console.log(wordWithoutSpace, wordWithoutSpace.length);
          this.onEnter();
        }
      }
    }
  },
};
</script>

<template>
  <div class="container">
    <div class="searchBox">
      <Icon name="heroicons-solid:search" color="#D9D9D9" class="searchIcon" />
      <div class="keywordContainer">
        <div class="keyword" v-for="(keyword, index) in keywordArray" :key="index" @click="removeKeyword(index)" :style="{ backgroundColor: keyword.isUser ? '#6CA5FC' : '#FFB84B' }">
            {{ keyword.word }}
            <Icon name="ri:close-circle-line" color="white"></Icon>
        </div>
      </div>

      <input class="searchField" type="text" v-model="searchKeyword" :placeholder="placeholderText"  v-on:keyup.enter="onEnter" v-on:input="detectSpace"/>
      <div class="infoDiv">
        <Icon name="humbleicons:info-circle" color="#6CA5FC" class="infoIcon" />
        <span class="infoText">Brug @ foran brugernavn</span>
      </div>
        
    </div>
    <button class="calendarButton">
      <Icon name="heroicons-solid:calendar-days" color="grey" class="calendarIcon" size="1.5em"/>
    </button>
  </div>
</template>


  

