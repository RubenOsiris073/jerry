<template id="app-frame">
<v-app>
   <v-navigation-drawer v-model="drawer" color="black">
      <v-list-item class="scrollable" width="256">
            <v-list-item density="compact"
               prepend-avatar="https://media1.tenor.com/m/R9E4h1vKHr8AAAAd/mewing-mew.gif"
               title="Osiris" subtitle="admin">
            </v-list-item>
         </v-list-item>
         <v-divider thickness="0"></v-divider>
         <v-list>
            <v-list-item v-for="mnu in menu" :prepend-icon="mnu.icon" :title="mnu.text" :href="mnu.href"
               link></v-list-item>
         </v-list>
      </v-navigation-drawer>
      <v-app-bar color="black">
         <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
         <v-app-bar-title>
            <span class="white--text">Inventario</span>
         </v-app-bar-title>
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

<script>
app.component("app-frame", {
   template: "#app-frame",
   data: () => ({
      drawer: null,
      menu: [
         { icon: 'mdi-database-edit-outline', text: 'Ver Inventario', href: 'dessert' },
         { icon: 'mdi-inbox-arrow-down', text: 'Avisos', href: '/' },
         { icon: 'mdi-delete', text: 'Eliminar Producto', href: '/' },
         { icon: 'mdi-alert-octagon', text: 'Spam', href: '/' },
         { icon: 'mdi-text-box-edit-outline', text: 'Editar Inventario', href: '/' },
         { icon: 'mdi-clipboard-text-clock-outline', text: 'Historial de Productos', href: '/' },
         { icon: 'mdi-account-group-outline', text: 'Usuarios y Permisos', href: '/' },
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