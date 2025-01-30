<template id="mice-page" dark>
  <app-frame>
    <v-data-iterator :items="mice" :items-per-page="itemsPerPage">
      <template v-slot:header="{ page, pageCount, prevPage, nextPage }">
        <h1 class="text-h4 font-weight-bold d-flex justify-space-between mb-4 align-center">
          <div></div>
          <div class="d-flex align-center">
            <!-- Botón para abrir el formulario de inserción -->
            <v-btn class="me-8" color="black" @click="showInsertForm = true">
              <span class="text-none">Agregar Mouse</span>
            </v-btn>

            <v-btn class="me-8" variant="text" @click="onClickSeeAll">
              <span class="text-decoration-underline text-none">Ver Todos</span>
            </v-btn>

            <div class="d-inline-flex">
              <v-btn :disabled="page === 1" class="me-2" icon="mdi-arrow-left" size="small" variant="tonal"
                @click="prevPage"></v-btn>

              <v-btn :disabled="page === pageCount" icon="mdi-arrow-right" size="small" variant="tonal"
                @click="nextPage"></v-btn>
            </div>
          </div>
        </h1>
      </template>

      <!-- Resto del código del template (formulario y lista de ratones) -->
      <template v-slot:default="{ items }">
        <v-row>
          <v-col v-for="(item, i) in items" :key="i" cols="12" sm="6" xl="3">
            <v-sheet border>
              <v-img :gradient="`to top right, rgba(255, 255, 255, .1), rgba(${item.raw.color}, .15)`"
                :src="item.raw.imageUrl" height="150" cover v-if="item.raw.imageUrl"></v-img>

              <v-container>
                <v-card class="d-flex flex-column align-center">
                  <v-list-item :title="item.raw.name" density="comfortable" lines="two" subtitle="Vista previa"
                    class="text-center">
                    <template v-slot:title>
                      <strong class="text-h6">
                        {{ item.raw.name }}
                      </strong>
                    </template>
                  </v-list-item>

                  <v-table class="text-caption" density="compact">
                    <tbody>
                      <tr align="center" class="my-3">
                        <th>DPI:</th>
                        <td>{{ item.raw.dpi }}</td>
                      </tr>

                      <tr align="center" class="my-3">
                        <th>Botones:</th>
                        <td>{{ item.raw.buttons }}</td>
                      </tr>

                      <tr align="center" class="my-3">
                        <th>Peso:</th>
                        <td>{{ item.raw.weight }}</td>
                      </tr>

                      <tr align="center" class="my-3">
                        <th>Wireless:</th>
                        <td>{{ item.raw.wireless ? 'Yes' : 'No' }}</td>
                      </tr>

                      <tr align="center" class="my-3">
                        <th>Precio:</th>
                        <td>${{ item.raw.price }}MX</td>
                      </tr>

                      <!-- botones -->
                      <div class="mt-4 mb-4">
                        <v-row no-gutters class="justify-center">
                          <v-col cols="auto" class="mx-4">
                            <v-btn rounded="lg" color="black">Borrar</v-btn>
                          </v-col>

                          <v-col cols="auto" class="mx-4">
                            <v-btn rounded="lg" color="black" @click="editMouse(item.raw)">Modificar</v-btn>
                          </v-col>
                        </v-row>
                      </div>
                    </tbody>
                  </v-table>
                </v-card>
              </v-container>
            </v-sheet>
          </v-col>
        </v-row>
      </template>

      <template v-slot:footer="{ page, pageCount }">
        <v-footer class="justify-space-between text-body-2 mt-4" color="black">
          Total mice: {{ mice.length }}
          <div>
            Page {{ page }} of {{ pageCount }}
          </div>
        </v-footer>
      </template>
    </v-data-iterator>

    <!-- Formulario para insertar datos -->
    <v-dialog v-model="showInsertForm" max-width="600px" color="black">
      <v-card>
        <v-card-title>
          <span class="text-h4 text-center">Datos del Mouse</span>
        </v-card-title>
        <v-card-text>
          <v-form @submit.prevent="submitMouse">
            <v-container>
              <v-row>
                <v-col cols="12">
                  <v-text-field v-model="newMouse.name" label="Nombre" required></v-text-field>
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field v-model="newMouse.dpi" label="DPI" type="number" required></v-text-field>
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field v-model="newMouse.buttons" label="No.Botones" type="number" required></v-text-field>
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field v-model="newMouse.weight" label="Peso" required></v-text-field>
                </v-col>
                <v-col cols="12" md="6">
                  <v-checkbox v-model="newMouse.wireless" label="Wireless"></v-checkbox>
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field v-model="newMouse.price" label="Precio" type="number" step="0.01"
                    required></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field v-model="newMouse.imageUrl" label="Imagen URL" required></v-text-field>
                </v-col>
              </v-row>
            </v-container>
            <v-card-actions>
              <v-btn type="submit" color="primary">Cargar</v-btn>
              <v-btn @click="showInsertForm = false" color="error">Cancelar</v-btn>
            </v-card-actions>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </app-frame>
</template>
<script>
app.component("mice-page", {
  template: "#mice-page",
  data: () => ({
    itemsPerPage: 4,
    mice: [], // Lista de ratones obtenidos del backend
    newMouse: {
      id: "",  // 🔹 Se necesita para actualizar correctamente
      name: "",
      dpi: 0,
      buttons: 0,
      weight: "",
      wireless: false,
      price: 0.0,
      imageUrl: "",  // 🔹 Para guardar la URL de la imagen
    },
    showInsertForm: false,  // Muestra u oculta el formulario de agregar/editar
    loadingOverlay: false,  // Indicador de carga mientras se obtiene la lista
    isEditing: false,       // 🔹 Variable para saber si estamos editando un mouse
  }),

  methods: {
    // 🔹 Alternar entre "ver todos" y paginar por 4 elementos
    onClickSeeAll() {
      this.itemsPerPage = this.itemsPerPage === 4 ? this.mice.length : 4;
    },

    // 🔹 Obtener lista de ratones desde el backend
    async fetchMice() {
      try {
        this.loadingOverlay = true;
        const response = await fetch("/api/mice");

        if (!response.ok) {
          throw new Error("Error al obtener los ratones");
        }

        const data = await response.json();
        console.log("🐭 Datos recibidos de la API:", data); // 🔹 Depuración
        this.mice = data;
      } catch (error) {
        console.error(error);
        alert("Error al cargar los ratones: " + error.message);
      } finally {
        this.loadingOverlay = false;
      }
    },

    // 🔹 Llenar el formulario con los datos de un mouse para editarlo
    editMouse(mouse) {
      this.newMouse = { ...mouse }; // Copia los datos del mouse seleccionado
      this.showInsertForm = true;   // Muestra el formulario
      this.isEditing = true;        // Indica que estamos editando
    },

    // 🔹 Crear o actualizar un mouse
    async submitMouse() {
      try {
        const method = this.isEditing ? "PUT" : "POST";
        const url = this.isEditing ? `/api/mice/${this.newMouse.id}` : "/api/mice";

        const response = await fetch(url, {
          method: method,
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(this.newMouse),
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText || "Error al registrar el mouse");
        }

        alert(this.isEditing ? "Mouse actualizado exitosamente" : "Mouse registrado exitosamente");

        if (this.isEditing) {
          // 🔹 Actualiza la lista sin recargar la página
          const index = this.mice.findIndex(m => m.id === this.newMouse.id);
          if (index !== -1) this.mice[index] = { ...this.newMouse };
        } else {
          const newMouseId = await response.text();
          this.mice.push({ ...this.newMouse, id: newMouseId });
        }

        // 🔹 Resetear formulario
        this.newMouse = {
          id: "",
          name: "",
          dpi: 0,
          buttons: 0,
          weight: "",
          wireless: false,
          price: 0.0,
          imageUrl: "",
        };
        this.isEditing = false;
        this.showInsertForm = false;
      } catch (error) {
        console.error("Error:", error);
        alert(`Ocurrió un error: ${error.message}`);
      }
    },
  },

  // 🔹 Cargar la lista de ratones al montar el componente
  mounted() {
    this.fetchMice();
  },
});
</script>
