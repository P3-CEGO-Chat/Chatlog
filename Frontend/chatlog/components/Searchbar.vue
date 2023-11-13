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
      return this.keywordArray.length > 0 ? 'Tilføje kriterier' : 'Søg efter brugernavn/kundenummer eller nøgleord';
    }
  },

  methods: {
    submitForm() {
      // You can add your search logic here
      this.keywordArray.push(this.searchKeyword)
      console.log(this.searchKeyword);
      console.log(this.keywordArray);
    },
    removeKeyword(index) {
      this.keywordArray.splice(index, 1);
    },

    onEnter() {
      if (this.searchKeyword[0] === "@") {
        this.wordObject = {"word": this.searchKeyword, "isUser": true};
      } else {
        this.wordObject = {"word": this.searchKeyword, "isUser": false};
      }
      this.keywordArray.push(this.wordObject);
      this.searchKeyword = "";
    }
  },
};
</script>

<template>
  <div class="container">
    <div class="searchBox">
      <Icon name="heroicons-solid:search" color="#D9D9D9" class="searchIcon" />
      <div class="keyword" v-for="(keyword, index) in keywordArray" :key="index" @click="removeKeyword(index)" :style="{ backgroundColor: keyword.isUser ? '#6CA5FC' : '#FFB84B' }">
          {{ keyword.word }}
      </div>
      <input class="searchField" type="text" v-model="searchKeyword" :placeholder="placeholderText"  v-on:keyup.enter="onEnter"/>
    </div>
    <button class="calendarButton">
      <Icon name="heroicons-solid:calendar-days" color="grey" class="calendarIcon" size="1.5em"/>
    </button>
  </div>
</template>

  
  

