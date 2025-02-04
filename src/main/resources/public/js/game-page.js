if (window.app) {
    window.app.component("game-page", {
        template: "#game-page",
        data: () => ({
            search: '',
            games: [
                {
                    img: 'https://cdn.vuetifyjs.com/docs/images/graphics/games/4.png',
                    title: 'The Sci-Fi Shooter Experience',
                    subtitle: 'Dive into a futuristic world of intense battles and alien encounters.',
                    advanced: false,
                    duration: '8 minutes',
                },
                {
                    img: 'https://cdn.vuetifyjs.com/docs/images/graphics/games/2.png',
                    title: 'Epic Adventures in Open Worlds',
                    subtitle: 'Embark on a journey through vast, immersive landscapes and quests.',
                    advanced: true,
                    duration: '10 minutes',
                }
            ],
        }),
        mounted() {
            console.log("✅ Game-Page Component Loaded");
        },
    });
} else {
    console.error("⚠️ Vue app is not defined before loading game-page.js");
}