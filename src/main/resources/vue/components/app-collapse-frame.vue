<template id="app-collapse-frame">
    <v-navigation-drawer v-model="drawer" temporary>
        <v-app-bar collapse :elevation="2">
            <template v-slot:prepend>
                <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
            </template>
        </v-app-bar>

        <v-main>
            <v-container fluid>
                <v-list>
                    <v-list-item v-for="(item, index) in items" :key="index" @click="selectItem(item)">
                        <v-btn block variant="text" :prepend-icon="item.icon">
                            {{ item.title }}
                        </v-btn>
                    </v-list-item>
                </v-list>
            </v-container>
        </v-main>
    </v-navigation-drawer>
</template>

<script>
app.component("app-collapse-frame", {
    template: "#app-collapse-frame",
    data() {
        return {
            drawer: false,
            items: [
                { title: "Opción 1", icon: "mdi-star" },
                { title: "Opción 2", icon: "mdi-heart" },
                { title: "Opción 3", icon: "mdi-cog" },
                { title: "Opción 4", icon: "mdi-account" },
            ],
        };
    },
});
</script>