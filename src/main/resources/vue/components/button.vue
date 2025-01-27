<template id="button-mode">
    <v-col cols="auto">
      <v-btn icon @click="toggleTheme" :title="isDark ? 'Modo Claro' : 'Modo Oscuro'">
        <v-icon>{{ isDark ? 'mdi-weather-night' : 'mdi-weather-sunny' }}</v-icon>
      </v-btn>
    </v-col>
  </template>
  
  <script>
  export default {
    data() {
      return {
        isDark: false,
      };
    },
    methods: {
      toggleTheme() {
        this.isDark = !this.isDark; 
        this.$vuetify.theme.global.name.value = this.isDark ? 'dark' : 'light';
      },
    },
  };
  </script>
  