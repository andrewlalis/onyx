import {createRouter, createWebHistory} from 'vue-router'
import MainView from "@/views/MainView.vue";
import GatewayView from "@/views/GatewayView.vue";
import LoginForm from "@/components/gateway/LoginForm.vue";
import RegisterForm from "@/components/gateway/RegisterForm.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: MainView
    },
    {
      path: "/gateway",
      name: "gateway",
      component: GatewayView,
      children: [
        {
          path: "login",
          component: LoginForm
        },
        {
          path: "register",
          component: RegisterForm
        }
      ]
    }
  ]
})

export default router
