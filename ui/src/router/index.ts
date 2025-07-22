import { createRouter, createWebHistory } from 'vue-router';
import Layout from '../layout/Layout.vue';
import Login from '../views/Login.vue';
import Documents from '../views/Documents.vue';

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Home',
        component: Documents,
      },
      {
        path: '/documents',
        name: 'Documents',
        component: Documents,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router; 