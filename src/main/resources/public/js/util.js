(function () {
    // Definir la variable global app
    if (typeof app === "undefined") {
        window.app = Vue.createApp({});
    }

    // Global Const para JWT
    app.config.globalProperties.$jwt = '';

    // Fetch Text or JSON objects
    app.config.globalProperties.$fetch = async (input, init) => {
        const response = await fetch(input, init);
        if (!response.ok) throw Error(await response.text());
        const contentType = response.headers.get("content-type");
        return contentType.includes("text/plain") ? await response.text() : await response.json();
    };

    // Mostrar errores con SweetAlert
    app.config.globalProperties.$showError = (title, text) => {
        Swal.fire({ icon: 'error', title: title, text: text });
    };

    // Confirmación con SweetAlert
    app.config.globalProperties.$swalConfirm = async (title, text, icon) => {
        return await Swal.fire({
            title: title,
            text: text,
            icon: icon,
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Confirmar'
        });
    };

    // Exponer Vue a la consola
    window.app = app;
    console.log("✅ Vue está inicializado y accesible en window.app");
})();
