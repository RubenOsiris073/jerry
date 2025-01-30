<template id="mice-page">
  <app-frame>
    <v-data-iterator :items="mice" :items-per-page="itemsPerPage">
      <template v-slot:header="{ page, pageCount, prevPage, nextPage }">
        <h1 class="text-h4 font-weight-bold d-flex justify-space-between mb-4 align-center">
          <div class="text-truncate">
            Most popular mice
          </div>

          <div class="d-flex align-center">
            <!-- Botón para abrir el formulario de inserción -->
            <v-btn class="me-8" color="primary" @click="showInsertForm = true">
              <span class="text-none">Add Mouse</span>
            </v-btn>

            <v-btn class="me-8" variant="text" @click="onClickSeeAll">
              <span class="text-decoration-underline text-none">See all</span>
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
                :src="item.raw.src" height="150" cover></v-img>

              <v-container>
                <v-card class="d-flex flex-column align-center">
                  <v-list-item :title="item.raw.name" density="comfortable" lines="two"
                    subtitle="Lorem ipsum dil orei namdie dkaf" class="text-center">
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
                        <th>Buttons:</th>
                        <td>{{ item.raw.buttons }}</td>
                      </tr>

                      <tr align="center" class="my-3">
                        <th>Weight:</th>
                        <td>{{ item.raw.weight }}</td>
                      </tr>

                      <tr align="center" class="my-3">
                        <th>Wireless:</th>
                        <td>{{ item.raw.wireless ? 'Yes' : 'No' }}</td>
                      </tr>

                      <tr align="center" class="my-3">
                        <th>Price:</th>
                        <td>${{ item.raw.price }}</td>
                      </tr>

                      <!-- botones -->
                      <div class="mt-4 mb-4">
                        <v-row no-gutters class="justify-center">
                          <v-col cols="auto" class="mx-4">
                            <v-btn rounded="lg" color="black">Borrar</v-btn>
                          </v-col>

                          <v-col cols="auto" class="mx-4">
                            <v-btn rounded="lg" color="black">Modificar</v-btn>
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
        <v-footer class="justify-space-between text-body-2 mt-4" color="surface-variant">
          Total mice: {{ mice.length }}
          <div>
            Page {{ page }} of {{ pageCount }}
          </div>
        </v-footer>
      </template>
    </v-data-iterator>

    <!-- Formulario para insertar datos -->
    <v-dialog v-model="showInsertForm" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="text-h5">Add Mouse</span>
        </v-card-title>
        <v-card-text>
          <v-form @submit.prevent="submitMouse">
            <v-container>
              <v-row>
                <v-col cols="12">
                  <v-text-field v-model="newMouse.name" label="Name" required></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field v-model="newMouse.dpi" label="DPI" type="number" required></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field v-model="newMouse.buttons" label="Buttons" type="number" required></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field v-model="newMouse.weight" label="Weight" required></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-checkbox v-model="newMouse.wireless" label="Wireless"></v-checkbox>
                </v-col>
                <v-col cols="12">
                  <v-text-field v-model="newMouse.price" label="Price" type="number" step="0.01" required></v-text-field>
                </v-col>
              </v-row>
            </v-container>
            <v-card-actions>
              <v-btn type="submit" color="primary">Submit</v-btn>
              <v-btn @click="showInsertForm = false" color="error">Cancel</v-btn>
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
    mice: [], // Lista de ratones
    newMouse: {
      name: "",
      dpi: 0,
      buttons: 0,
      weight: "",
      wireless: false,
      price: 0.0,
    },
    showInsertForm: false,
    loadingOverlay: false, // Indicador de carga
  }),
  methods: {
    onClickSeeAll() {
      this.itemsPerPage = this.itemsPerPage === 4 ? this.mice.length : 4;
    },
    async submitMouse() {
    try {
      // Enviar los datos sin el campo ID
      const newMouse = {
        name: this.newMouse.name,
        dpi: this.newMouse.dpi,
        buttons: this.newMouse.buttons,
        weight: this.newMouse.weight,
        wireless: this.newMouse.wireless,
        price: this.newMouse.price,
      };

      // Enviar la solicitud POST al backend
      const response = await fetch("/api/mice", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newMouse),
      });

      if (!response.ok) throw new Error("Error al registrar el mouse");

      // Obtener el nuevo mouse desde el backend
      const createdMouse = await response.json();

      // Agregar el nuevo mouse a la lista
      this.mice.push(createdMouse);

      // Cerrar el formulario y resetear los campos
      this.showInsertForm = false;
      this.newMouse = { name: "", dpi: 0, buttons: 0, weight: "", wireless: false, price: 0.0 };

      // Mostrar mensaje de éxito
      alert("Mouse registrado exitosamente");
    } catch (error) {
      console.error(error);
      alert("Error al registrar el mouse: " + error.message);
    }
  },
  async fetchMice() {
      try {
        // Obtener la lista de ratones desde el backend
        this.loadingOverlay = true;
        const response = await fetch("/api/mice");
        if (!response.ok) {
          throw new Error("Error al obtener los ratones");
        }
        this.mice = await response.json();
      } catch (error) {
        console.error(error);
        alert("Error al cargar los ratones: " + error.message);
      } finally {
        this.loadingOverlay = false;
      }
    },
  },
  mounted() {
    this.fetchMice(); // Cargar la lista de ratones al montar el componente
  },
});
</script>