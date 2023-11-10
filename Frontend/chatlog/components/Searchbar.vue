
<script>

import('~/assets/css/searchBar.css')


export default {
  data() {
    return {
      searchKeyword: '',
      keywordArray: [], // New data property
      wordObject: {word: "", isUser: false}
    };
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
    }
  },
};
</script>

<template>
  <div class="container">
    <div class="searchBox">
      <Icon name="heroicons-solid:search" color="#D9D9D9" class="searchIcon" />
      <div class="keyword" v-for="(keyword, index) in keywordArray" :key="index" @click="removeKeyword(index)">
          {{ keyword.word }} - {{ keyword.isUser ? "User" : "Not User" }}
      </div>
      <input class="searchField" type="text" v-model="searchKeyword" placeholder="Søg efter brugernavn/kundenummer og nøgleord"  v-on:keyup.enter="onEnter"/>
    </div>
    <button class="calendarButton">
      <Icon name="heroicons-solid:calendar-days" color="grey" class="calendarIcon" size="1.5em"/>
    </button>
  </div>
</template>






<!-- <template>
    <div class="div">
      <div class="div-2">
        <div class="div-3">
          <div class="div-4">􀊫</div>
          <div class="div-5"> 
            <form @submit.prevent="submitForm">
              <div class="input-field">
                <span class="keyword-box" v-for="(keyword, index) in keywordArray" :key="index" @click="removeKeyword(index)">
                {{ keyword }}
              </span>
              <input type="text" v-model="searchKeyword" placeholder="Search" />
            </div>
            <input type="submit" value="Search" />
            </form>
          </div>
        </div>
      </div>
    </div>
  </template> -->



  
  

