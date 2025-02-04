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
         <v-card class="pa-4">
            <v-card-title class="text-h5 font-weight-bold primary--text">Eliminar Productos</v-card-title>
            <v-divider class="mb-4"></v-divider>
            <v-card-text>
               <v-list class="pt-0">
                  <v-list-subheader class="text-subtitle-1 font-weight-bold grey--text text--darken-2">LISTA DE
                     PRODUCTOS</v-list-subheader>
                  <v-list-item v-for="mouse in mice" :key="mouse.id" class="d-flex align-center justify-space-between">
                     <v-list-item-content class="py-2">
                        <v-list-item-title class="text-left text-h6 font-weight-medium">{{ mouse.name
                           }}</v-list-item-title>
                     </v-list-item-content>
                     <v-list-item-action class="ml-auto">
                        <v-btn color="error" class="text-white font-weight-bold" rounded @click="deleteMouse(mouse.id)">
                           ELIMINAR
                        </v-btn>
                     </v-list-item-action>
                  </v-list-item>
               </v-list>
            </v-card-text>
            <v-divider class="mt-4"></v-divider>
            <v-card-actions class="justify-end pa-4">
               <v-btn color="blue darken-2" class="text-white font-weight-bold" rounded @click="closeEliminar">
                  CERRAR
               </v-btn>
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

      <!-- Modal de Modificar Producto -->
      <v-dialog v-model="modificarDialog" max-width="500px">
         <v-card class="pa-4">
            <v-card-title class="text-h5 font-weight-bold primary--text">
               Modificar Producto
            </v-card-title>
            <v-divider class="mb-4"></v-divider>
            <v-card-text>
               <v-form ref="form">
                  <v-text-field v-model="selectedMouse.name" label="Nombre" outlined dense></v-text-field>
                  <v-text-field v-model="selectedMouse.dpi" label="DPI" outlined dense type="number"></v-text-field>
                  <v-text-field v-model="selectedMouse.buttons" label="Botones" outlined dense
                     type="number"></v-text-field>
                  <v-text-field v-model="selectedMouse.weight" label="Peso" outlined dense></v-text-field>
                  <v-switch v-model="selectedMouse.wireless" label="Inalámbrico"></v-switch>
               </v-form>
            </v-card-text>
            <v-divider class="mt-4"></v-divider>
            <v-card-actions class="justify-end pa-4">
               <v-btn color="green darken-2" class="text-white font-weight-bold" rounded @click="submitMouse">
                  GUARDAR
               </v-btn>
               <v-btn color="blue darken-2" class="text-white font-weight-bold" rounded @click="closeModificar">
                  CERRAR
               </v-btn>
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

<script>app.component("app-frame", {
   template: "#app-frame",
   data: () => ({
      drawer: null,
      avisosDialog: false,
      eliminarDialog: false,
      modificarDialog: false,
      mice: [], // Lista de productos
      selectedMouse: { // Estado inicial del objeto seleccionado
         id: null,
         name: "",
         dpi: null,
         buttons: null,
         weight: "",
         wireless: false
      },
      menu: [
         { icon: 'mdi-database-edit-outline', text: 'Ver Inventario', href: '/' },
         { icon: 'mdi-inbox-arrow-down', text: 'Avisos', action: () => this.openAvisos() },
         { icon: 'mdi-delete', text: 'Eliminar Producto', action: () => this.openEliminar() },
         { icon: 'mdi-alert-octagon', text: 'Spam', href: '/' },
         { icon: 'mdi-text-box-edit-outline', text: 'Editar Inventario', action: () => this.openModificar(null) }, // 🔹 Ahora se evita el error al abrir
         { icon: 'mdi-clipboard-text-clock-outline', text: 'Historial de Productos', href: '/' },
         { icon: 'mdi-account-group-outline', text: 'Usuarios y Permisos', href: '/' },
      ],
   }),
   methods: {
      async fetchMice() {
         try {
            console.log("🔄 Cargando lista de mice...");
            const response = await fetch("/api/mice");
            if (!response.ok) {
               throw new Error("Error al obtener los ratones");
            }
            this.mice = await response.json();
            console.log("🐭 Mice cargados:", this.mice);
         } catch (error) {
            console.error("❌ Error al cargar los mice:", error);
         }
      },

      async deleteMouse(id) {
         console.log(`🗑️ Eliminando mouse con ID: ${id}`);
         try {
            const response = await fetch(`/api/mice/${id}`, { method: 'DELETE' });
            if (!response.ok) {
               throw new Error('Error al eliminar el mouse');
            }
            await this.fetchMice(); // Recargar lista después de eliminar
            console.log("✅ Mouse eliminado correctamente");
         } catch (error) {
            console.error("❌ Error al eliminar el mouse:", error);
         }
      },

      openEliminar() {
         this.eliminarDialog = true;
         this.fetchMice();
         console.log("🗑️ Modal de Eliminar abierto");
      },

      closeEliminar() {
         this.eliminarDialog = false;
         console.log("❌ Modal de Eliminar cerrado");
      },

      openModificar(mouse) {
         if (!mouse) {
            console.error("❌ No se pudo abrir el modal: No hay mouse seleccionado");
            return;
         }

         this.selectedMouse = { ...mouse }; // Clonar el objeto seleccionado
         this.modificarDialog = true;
         console.log("✏️ Modal de Modificar abierto con:", this.selectedMouse);
      },

      closeModificar() {
         this.modificarDialog = false;
         this.selectedMouse = { id: null, name: "", dpi: null, buttons: null, weight: "", wireless: false };
         console.log("❌ Modal de Modificar cerrado");
      },

      async submitMouse() {
         if (!this.selectedMouse.id) {
            console.error("❌ Error: No se ha seleccionado un mouse válido para modificar");
            return;
         }

         try {
            const response = await fetch(`/api/mice/${this.selectedMouse.id}`, {
               method: 'PUT',
               headers: { "Content-Type": "application/json" },
               body: JSON.stringify(this.selectedMouse),
            });

            if (!response.ok) {
               throw new Error("Error al modificar el mouse");
            }

            console.log("✅ Mouse modificado correctamente");
            this.modificarDialog = false;
            await this.fetchMice(); // Recargar lista después de modificar
         } catch (error) {
            console.error("❌ Error al modificar el mouse:", error);
         }
      },

      signOut() {
         this.$swalConfirm('Cerrar sesión', '¿Desea salir de la sesión?', 'info')
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
      window.app = this;
      this.fetchMice(); // Cargar lista de mice al iniciar
      console.log("🌍 App-Frame ahora es accesible en window.app");
   }
});
</script>