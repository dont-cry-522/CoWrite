import { createApp } from 'vue';
import './style.css';
import App from './App.vue';
import router from './router';
import Notify from './plugins/notify'

createApp(App).use(router).use(Notify).mount('#app').$nextTick(() => {
  // Use contextBridge
  window.ipcRenderer.on('main-process-message', (_event, message) => {
    console.log(message);
  });
});
