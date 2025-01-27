<template id="app-frame">
   <v-app>
      <v-navigation-drawer v-model="drawer" color="blue-grey-lighten-3">
         <v-list-item class="scrollable" width="256">
            <v-list-item density="compact"
               prepend-avatar="https://imgs.search.brave.com/1ftC1scxB7ynu8usYk7yFpcDH1qS8Nq2K0z3fWYmbLo/rs:fit:500:0:0:0/g:ce/aHR0cHM6Ly9pMC53/cC5jb20vZWx0YWxs/ZXJkZWhlY3Rvci5j/b20vd3AtY29udGVu/dC91cGxvYWRzLzIw/MjIvMDYvOGIyNDQt/Ym9iLWVzcG9uamEt/Y2VycmFuZG8tdW4t/b2pvLXBuZy5wbmc_/cmVzaXplPTQ1Niw1/MDAmc3NsPTE"
               title="Osiris" subtitle="admin">
            </v-list-item>
         </v-list-item>
         <v-divider thickness="3"></v-divider>
         <v-list>
            <v-list-item v-for="mnu in menu" :prepend-icon="mnu.icon" :title="mnu.text" :href="mnu.href"
               link></v-list-item>
         </v-list>
      </v-navigation-drawer>
      <v-app-bar color="blue-grey-lighten-3">
         <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
         <v-app-bar-title>Inventario</v-app-bar-title>
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