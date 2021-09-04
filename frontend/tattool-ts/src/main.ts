import { createApp } from 'vue'
import App from './App.vue'
import router from "./router/router";
import VueClipboard from 'vue3-clipboard';
import useSessionContext from "@/state/useSessionContext";

const { defaultValidationService, defaultContextService } =
    useSessionContext();

createApp(App)
    .use(router)

    .use(VueClipboard, {
        autoSetContainer: true,
        appendToBody: true,
    })
    .provide("validationService", defaultValidationService)
    .provide("contextService", defaultContextService)
    .mount('#app')
