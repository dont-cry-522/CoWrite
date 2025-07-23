import { createRouter, createWebHistory } from 'vue-router';
import Layout from '../layout/Layout.vue';
import Login from '../views/Login.vue';
import Documents from '../views/Documents.vue';
import BackLayout from "../layout/BackLayout.vue";
import StatsView from "../views/back/StatsView.vue"
import AIPluginsView from "../views/back/AIPluginsView.vue"
import PreferencesView from "../views/back/PreferencesView.vue"
import ProfileView from "../views/back/ProfileView.vue"
import NotificationSettingsView from "../views/back/NotificationSettingsView.vue"
import PersonalLogsView from "../views/back/PersonalLogsView.vue"
import AccountManagementView from "../views/back/AccountManagementView.vue"
import SettingsView from "../views/back/SettingsView.vue"
import UserListView from "../views/back/UserListView.vue"
import HomeView from "../views/back/HomeView.vue"

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
  {
    path: '/back',
    component: BackLayout,
    children: [
      { path: '', component: HomeView },
      { path: 'stats', component: StatsView },
      { path: 'ai-plugins', component: AIPluginsView },
      { path: 'preferences', component: PreferencesView },
      { path: 'profile', component: ProfileView },
      { path: 'notifications', component: NotificationSettingsView },
      { path: 'logs', component: PersonalLogsView },
      { path: 'account', component: AccountManagementView },
      { path: 'settings', component: SettingsView },
      { path: 'users', component: UserListView }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router; 