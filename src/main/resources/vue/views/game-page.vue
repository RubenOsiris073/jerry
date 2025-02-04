<template id="game-page">
    <v-card>
        <v-data-iterator :items="games" :items-per-page="itemsPerPage" :search="search">
            <template v-slot:header>
                <v-toolbar class="px-2 d-flex align-center">
                    <v-app-bar-nav-icon @click.stop="toggleDrawer"></v-app-bar-nav-icon>
                    <v-text-field v-model="search" density="comfortable" placeholder="Search"
                        prepend-inner-icon="mdi-magnify" style="max-width: 300px;" variant="solo" clearable
                        hide-details></v-text-field>
                    <div class="d-flex justify-end" style="flex: 1;">
                        <v-btn height="48" @click="showInsertForm = true">
                            Agregar
                        </v-btn>
                    </div>
                </v-toolbar>
            </template>

            <template v-slot:default="{ items }">
                <v-container class="pa-2" fluid>
                    <v-row dense>
                        <v-col v-for="item in items" :key="item.title" cols="auto" md="4">
                            <v-card class="pb-3" border flat>
                                <v-img :src="item.raw.img"></v-img>

                                <v-list-item :subtitle="item.raw.subtitle" class="mb-2">
                                    <template v-slot:title>
                                        <strong class="text-h6 mb-2">{{ item.raw.title }}</strong>
                                    </template>
                                </v-list-item>

                                <div class="d-flex justify-space-between px-4">
                                    <div class="d-flex align-center text-caption text-medium-emphasis me-1">
                                        <v-icon icon="mdi-clock" start></v-icon>

                                        <div class="text-truncate">{{ item.raw.duration }}</div>
                                    </div>
                                    <v-btn :loading="loading" class="" height="40" width="100" variant="tonal"
                                        @click="console.log('🗑️ Game seleccionado:', item.raw.id); deleteGame(item.raw.id)">
                                        Borrar
                                    </v-btn>

                                    <v-btn :loading="loading" class="" height="40" width="100" variant="tonal"
                                        @click="console.log('🗑️ Game seleccionado:', item.raw.id); editGame(item.raw)">
                                        Modificar
                                    </v-btn>

                                </div>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-container>
            </template>

            <template v-slot:footer="{ page, pageCount, prevPage, nextPage }">
                <div class="d-flex align-center justify-center pa-4">
                    <v-btn :disabled="page === 1" density="comfortable" icon="mdi-arrow-left" variant="tonal" rounded
                        @click="prevPage"></v-btn>

                    <div class="mx-2 text-caption">
                        Page {{ page }} of {{ pageCount }}
                    </div>

                    <v-btn :disabled="page >= pageCount" density="comfortable" icon="mdi-arrow-right" variant="tonal"
                        rounded @click="nextPage"></v-btn>
                </div>
            </template>
        </v-data-iterator>

        <app-collapse-frame>
            <template v-slot:header>
                <!-- Aquí puedes colocar lo que necesites dentro del app-collapse-frame -->
            </template>
        </app-collapse-frame>

        <!-- Formulario para insertar datos -->
        <v-dialog v-model="showInsertForm" max-width="600px" color="black">
            <v-card>
                <v-card-title>
                    <span class="text-h4 text-center">Datos del Game</span>
                </v-card-title>
                <v-card-text>
                    <v-form @submit.prevent="submitGame">
                        <v-container>
                            <v-row>
                                <v-col cols="12">
                                    <v-text-field v-model="newGame.title" label="Nombre" required></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="newGame.descripcion" label="Descripcion"
                                        required></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="newGame.tiempo" label="Tiempo" type="number"
                                        required></v-text-field>
                                </v-col>

                                <v-col cols="12" md="6">
                                    <v-text-field v-model="newGame.price" label="Precio" type="number" step="0.01"
                                        required></v-text-field>
                                </v-col>
                                <v-col cols="12">
                                    <v-text-field v-model="newGame.imageUrl" label="Imagen URL" required></v-text-field>
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

    </v-card>
</template>

<script>
app.component("game-page", {
    template: "#game-page",
    data: () => ({
        search: '',
        itemsPerPage: 2,
        games: [], // 🔹 Arreglo de juegos
        drawer: false,
        showInsertForm: false,
        newGame: {
            id: "", // 🔹 Propiedad id
            title: '',
            descripcion: '',
            tiempo: 0,
            price: 0,
            imageUrl: '', // 🔹 Propiedad imageUrl
        },
    }),
    mounted() {
        this.fetchGame();
        window.app = this; // 🔹 Exponer Vue manualmente en la consola
        console.log("🌍 Game-Page ahora es accesible en window.app");
    },
    methods: {
        toggleDrawer() {
            this.drawer = !this.drawer;
        },
        onClickSeeAll() {
            this.itemsPerPage = this.itemsPerPage === 2 ? this.game.length : 2;
        },

        // 🔹 Obtener lista de ratones desde el backend
        async fetchGame() {
            try {
                console.log("🔄 Fetching game from API...");
                const response = await fetch("/api/game");
                if (!response.ok) {
                    throw new Error("Error al obtener los juegos");
                }
                this.game = await response.json();
                console.log("🐭 Datos recibidos y almacenados en `game`:", this.game);
            } catch (error) {
                console.error("❌ Error al cargar los juegos:", error);
            }
        },

        // 🔹 Llenar el formulario con los datos para editarlo
        editGame(game) {
            this.newGame = { ...game }; // Copia los datos del seleccionado
            this.showInsertForm = true;   // Muestra el formulario
            this.isEditing = true;        // Indica que estamos editando
        },

        // 🔹 Crear o actualizar un juego
        async submitGame() {
            console.log("✏️ Modificando/creando game:", this.newGame);
            try {
                const method = this.isEditing ? "PUT" : "POST";
                const url = this.isEditing ? `/api/game/${this.newGame.id}` : "/api/game";

                const response = await fetch(url, {
                    method: method,
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(this.newGame),
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(errorText || "Error al registrar el game");
                }

                alert(this.isEditing ? "Game actualizado exitosamente" : "registrado exitosamente");

                if (this.isEditing) {
                    // 🔹 Actualiza la lista sin recargar la página
                    console.log("✏️ Modificando game:", this.newGame);
                    const index = this.game.findIndex(m => m.id === this.newGame.id);
                    if (index !== -1) this.game[index] = { ...this.newGame };
                } else {
                    console.log("➕ Creando nuevo game:", this.newGame);
                    const newGameId = await response.text();
                    this.game.push({ ...this.newGame, id: newGameId });
                }

                // 🔹 Resetear formulario
                this.newGame = {
                    id: "", // 🔹 Propiedad id
                    title: '',
                    descripcion: '',
                    tiempo: 0,
                    price: 0,
                    imageUrl: '',
                };
                this.isEditing = false;
                this.showInsertForm = false;
            } catch (error) {
                console.error("Error:", error);
                alert(`Ocurrió un error: ${error.message}`);
            }
        },

        // 🔹 Eliminar un juego de la lista y del backend
        async deleteGame(id) {
            console.log("🗑️ Intentando eliminar el game con ID:", id);
            if (!id) {
                console.error("❌ Error: ID no definido");
                alert("Error: No se encontró el ID del game.");
                return;
            }

            try {
                const response = await fetch(`/api/game/${id}`, { method: "DELETE" });

                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(errorText || "Error al eliminar el game");
                }

                alert("Juego eliminado exitosamente");

                // 🔄 Remover de la lista localmente sin recargar la API
                this.game = this.game.filter(game => game.id !== id);
                console.log("📋 Lista de juegos después de eliminar:", this.game);
            } catch (error) {
                console.error("❌ Error al eliminar:", error);
                alert(`Ocurrió un error al eliminar: ${error.message}`);
            }
        }
    }
});
</script>