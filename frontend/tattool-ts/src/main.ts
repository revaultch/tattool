import { createApp } from 'vue'
import App from './App.vue'
import router from "./router/router";
import VueClipboard from 'vue3-clipboard';
import { Vue } from 'vue-class-component';



createApp(App)
    .use(router)

    .use(VueClipboard, {
        autoSetContainer: true,
        appendToBody: true,
    })
    .mount('#app')
