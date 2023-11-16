<template>
  <div class="layout">
    <div>
      <Searchbar @updateKeywordArray="updateKeywordArray" />
      <Filterchat :keywordArray="keywordArray" />
    </div>
    <div>
      <Allchat>
        ikke dav
      </Allchat>
    </div>
  </div>
</template>

<script>

import('~/assets/css/main.css')
import Searchbar from './components/Searchbar.vue';
import Filterchat from './components/Filterchat.vue';

export default {
  name: 'App',
  components: {
    Searchbar,
    Filterchat
  },
  data() {
    return {
      keywordArray: []
    }
  },
  methods: {
    updateKeywordArray(newKeywordArray) {
      this.keywordArray = newKeywordArray;
      console.log('Received updateKeywordArray:', this.keywordArray);
    },
  },
};
</script>