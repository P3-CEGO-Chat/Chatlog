<template>
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
  </template>

<script>
export default {
  data() {
    return {
      searchKeyword: '',
      keywordArray: [], // New data property
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
  },
};
</script>
  
  

<style scoped>
.input-field {
  display: flex;
  flex-wrap: wrap;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 15px;
}

.keyword-box {
  background-color: #f2f2f2;
  padding: 5px 10px;
  margin-right: 5px;
  margin-bottom: 5px;
  border-radius: 5px;
}

.input-field input {
  flex-grow: 1;
  border: none;
  outline: none;
}

  .div {
    display: flex;
    flex-direction: column;
  }
  .div-2 {
    border-radius: 1.25rem;
    background-color: #fff;
    display: flex;
    width: 100%;
    flex-direction: column;
    align-items: start;
    padding: 20px 15px;
  }
  @media (max-width: 991px) {
    .div-2 {
      max-width: 100%;
    }
  }
  .div-3 {
    border-radius: 0.625rem;
    border: 1px solid #6ca5fc;
    display: flex;
    width: 585px;
    gap: 16px;
    padding: 21px 272px 21px 11px;
  }
  @media (max-width: 991px) {
    .div-3 {
      max-width: 100%;
      flex-wrap: wrap;
      padding-right: 20px;
    }
  }
  .div-4 {
    justify-content: center;
    color: var(--label-colors-lcl-secondary, rgba(60, 60, 67, 0.6));
    text-align: center;
    font: 500 1.13rem/278% SF Pro Display, -apple-system, Roboto, Helvetica,
      sans-serif;
  }
  .div-5 {
    color: var(--label-colors-lcl-secondary, rgba(60, 60, 67, 0.6));
    font-feature-settings: "clig" off, "liga" off;
    letter-spacing: -0.0255rem;
    align-self: start;
    flex-grow: 1;
    flex-basis: auto;
    font: 400 1.06rem/129% Inter, sans-serif;
  }
  </style>