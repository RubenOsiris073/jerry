<template id="app-frame">
   <v-app>
      <v-navigation-drawer v-model="drawer" color="black">

         <v-list-item class="scrollable" width="256">
            <v-list-item density="compact" prepend-avatar="https://media1.tenor.com/m/R9E4h1vKHr8AAAAd/mewing-mew.gif"
               title="Osiris" subtitle="admin">
            </v-list-item>
         </v-list-item>

         <v-divider thickness="0"></v-divider>
         <v-list>
            <v-list-item v-for="mnu in menu" :prepend-icon="mnu.icon" :title="mnu.text"
               :href="mnu.text === 'Avisos' || mnu.text === 'Eliminar Producto' ? null : mnu.href"
               @click="mnu.text === 'Avisos' ? openAvisos() : mnu.text === 'Eliminar Producto' ? openEliminar() : null"
               link>
            </v-list-item>

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

      <!-- Modal de Eliminar Producto -->
      <v-dialog v-model="eliminarDialog" max-width="500px">
         <v-card class="pa-4 text-center">
            <v-card-title class="text-h5">Eliminar Productos</v-card-title>
            <v-card-text>
               <v-list>
                  <v-list-item v-for="mouse in mice" :key="mouse.id">
                     <v-list-item-content>
                        <v-list-item-title>{{ mouse.name }}</v-list-item-title>
                     </v-list-item-content>
                     <v-list-item-action>
                        <v-btn color="red" @click="deleteMouse(mouse.id)">Eliminar</v-btn>
                     </v-list-item-action>
                  </v-list-item>
               </v-list>
            </v-card-text>
            <v-card-actions>
               <v-spacer></v-spacer>
               <v-btn color="primary" @click="closeEliminar">Cerrar</v-btn>
            </v-card-actions>
         </v-card>
      </v-dialog>


      <!-- Modal de Avisos -->
      <v-dialog v-model="avisosDialog" max-width="400px">
         <v-card class="pa-4 text-center">
            <v-card-title class="text-h5">Avisos</v-card-title>
            <v-card-text class="text-body-1">Sin avisos</v-card-text>
            <v-card-actions>
               <v-spacer></v-spacer>
               <v-btn color="primary" @click="closeAvisos">Cerrar</v-btn>
            </v-card-actions>
         </v-card>
      </v-dialog>
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
      avisosDialog: false,
      eliminarDialog: false,
      menu: [
         { icon: 'mdi-database-edit-outline', text: 'Ver Inventario', href: '/' },
         { icon: 'mdi-inbox-arrow-down', text: 'Avisos', action: () => this.openAvisos() },
         { icon: 'mdi-delete', text: 'Eliminar Producto', action: () => this.openEliminar() },
         { icon: 'mdi-alert-octagon', text: 'Spam', href: '/' },
         { icon: 'mdi-text-box-edit-outline', text: 'Editar Inventario', href: '/' },
         { icon: 'mdi-clipboard-text-clock-outline', text: 'Historial de Productos', href: '/' },
         { icon: 'mdi-account-group-outline', text: 'Usuarios y Permisos', href: '/' },
      ],
      avisosDialog: false,
   }),
   //
   methods: {
      async deleteMouse(id) {
         console.log(`🗑️ Eliminando mouse con ID: ${id}`);
         console.log(`🗑️ Eliminando mouse con ID: ${id}`);
         try {
            const response = await fetch(`/api/mice/${id}`, { method: 'DELETE' });
            if (!response.ok) {
               throw new Error('Error al eliminar el mouse');
            }
            await this.fetchMice(); // 🔹 Recargar la lista después de eliminar
            console.log("✅ Mouse eliminado correctamente");
         } catch (error) {
            console.error("❌ Error al eliminar el mouse:", error);
         }
      },
      async fetchMice() {
         try {
            console.log("🔄 Cargando lista de mice desde Firebird...");
            const response = await fetch("/api/mice");
            if (!response.ok) {
               throw new Error("Error al obtener los ratones desde Firebird");
            }
            this.mice = await response.json();
            console.log("🐭 Mice cargados desde Firebird:", this.mice);
         } catch (error) {
            console.error("❌ Error al cargar los mice desde Firebird:", error);
         }
      },
      openEliminar() {
         this.eliminarDialog = true;
         this.fetchMice().then(() => {
            console.log("🗑️ Modal de Eliminar abierto y mice cargados");
         });
         this.eliminarDialog = true;
         this.fetchMice().then(() => {
            console.log("🗑️ Modal de Eliminar abierto y mice cargados");
         });
         this.eliminarDialog = true;
         this.fetchMice(); // 🔹 Cargar mice desde Firebird al abrir el modal
         console.log("🗑️ Modal de Eliminar abierto y mice cargados");
         this.eliminarDialog = true;
         console.log("🗑️ Modal de Eliminar abierto");
      },
      closeEliminar() {
         this.eliminarDialog = false;
         console.log("❌ Modal de Eliminar cerrado");
      },
      signOut() {
         this.$swalConfirm('Cerrar sesión', '¿Desea Salir de la Sesión?', 'info')
            .then(result => {
               if (result.isConfirmed) {
                  this.$fetch("api/sign-out", { method: 'POST' })
                     .then(result => window.location = 'login')
                     .catch(err => this.$toast.fire({ icon: 'error', title: err }))
               }
            })
      },
      openAvisos() {
         this.avisosDialog = true;
         console.log("📢 Modal de Avisos abierto");
      },
      closeAvisos() {
         this.avisosDialog = false;
         console.log("❌ Modal de Avisos cerrado");
      }
   },
   mounted() {
      window.app = this; // 🔹 Exponer Vue correctamente
      console.log("🌍 App-Frame ahora es accesible en window.app");
   }
})
</script>