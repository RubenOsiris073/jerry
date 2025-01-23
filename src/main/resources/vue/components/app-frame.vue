<template id="app-frame">
   <v-app>
      <v-navigation-drawer v-model="drawer">
         <v-list-item class="text-center">
            <v-avatar size="74">
               <v-img
                  src="https://imgs.search.brave.com/1ftC1scxB7ynu8usYk7yFpcDH1qS8Nq2K0z3fWYmbLo/rs:fit:500:0:0:0/g:ce/aHR0cHM6Ly9pMC53/cC5jb20vZWx0YWxs/ZXJkZWhlY3Rvci5j/b20vd3AtY29udGVu/dC91cGxvYWRzLzIw/MjIvMDYvOGIyNDQt/Ym9iLWVzcG9uamEt/Y2VycmFuZG8tdW4t/b2pvLXBuZy5wbmc_/cmVzaXplPTQ1Niw1/MDAmc3NsPTE"></v-img>
            </v-avatar>
         </v-list-item>
         <v-list-item class="text-center">
            <v-list-item-content>
               <v-list-item-title class="text-h6">osiris</v-list-item-title>
               <v-list-item-subtitle class="text-lowercase">admin</v-list-item-subtitle>
            </v-list-item-content>
         </v-list-item>
         <v-divider thickness="2"></v-divider>

         <v-list>
            <v-list-item v-for="mnu in menu" :prepend-icon="mnu.icon" :title="mnu.text" :href="mnu.href"
               link></v-list-item>
         </v-list>
      </v-navigation-drawer>

      <v-app-bar class="blurred-app-bar">
         <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
         <v-app-bar-title>Application</v-app-bar-title>
         <v-spacer></v-spacer>
         <v-btn icon @click="signOut">
            <v-icon>mdi-exit-to-app</v-icon>
         </v-btn>
      </v-app-bar>

      <v-main>
         <v-container fluid>
            <slot></slot>
         </v-container>
      </v-main>
   </v-app>
</template>

<style scoped>
.blurred-app-bar {
   position: sticky;
   /* Para asegurarse de que siempre esté visible */
   top: 0;
   z-index: 10;
   background-color: rgba(255, 255, 255, 0.1);
   /* Fondo semitransparente */
   backdrop-filter: blur(9px) saturate(100%);
   -webkit-backdrop-filter: blur(9px) saturate(100%);
   border-bottom: 1px solid rgba(255, 255, 255, 0.2);
   /* Borde semitransparente */
}
</style>

<script>
app.component("app-frame", {
   template: "#app-frame",
   data: () => ({
      drawer: null,
      menu: [
         { icon: 'mdi-inbox-arrow-down', text: 'Inbox', href: '/' },
         { icon: 'mdi-ice-cream', text: 'Desserts', href: 'dessert' },
         { icon: 'mdi-delete', text: 'Trash', href: '/' },
         { icon: 'mdi-alert-octagon', text: 'Spam', href: '/' },
      ],
   }),
   //
   methods: {
      signOut() {
         this.$swalConfirm('Cerrar sesión', '¿Desea Salir de la Sesión?', 'info')
            .then(result => {
               if (result.isConfirmed) {
                  this.$fetch("api/sign-out", { method: 'POST' })
                     .then(result => window.location = 'login')
                     .catch(err => this.$toast.fire({ icon: 'error', title: err }))
               }
            })
      }
   }
})
</script>