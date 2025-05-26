import { createRouter, createWebHistory } from 'vue-router'
import CartView from '../views/cart/CartView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/auth/Login.vue')
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('../views/auth/Register.vue')
    },
    {
      path: '/home',
      name: 'Home',
      component: () => import('../views/home/index.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/user',
      name: 'User',
      component: () => import('../views/user/index.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/product/:id',
      name: 'ProductDetail',
      component: () => import('@/views/product/ProductDetail.vue')
    },
    {
      path: '/category/:id',
      name: 'Category',
      component: () => import('@/views/category/index.vue')
    },
    {
      path: '/cart',
      name: 'cart',
      component: CartView,
      meta: { requiresAuth: true }
    },
    {
      path: '/order/confirm',
      name: 'OrderConfirm',
      component: () => import('@/views/order/OrderConfirm.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/order/payment/:orderId',
      name: 'OrderPayment',
      component: () => import('@/views/order/OrderPayment.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/order/result/:orderId',
      name: 'OrderResult',
      component: () => import('@/views/order/OrderResult.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/order/logistics-tracking',
      component: () => import('@/views/order/LogisticsTracking.vue')
    },
    {
      path: '/orders',
      redirect: { path: '/user', query: { tab: 'orders' } }
    },
    {
      path: '/user/orders',
      redirect: { path: '/user', query: { tab: 'orders' } }
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: { path: '/user', query: { tab: 'orders' } }
    },
    {
      path: '/user/statistics',
      name: 'UserStatistics',
      component: () => import('@/views/user/UserStatistics.vue'),
      meta: {
        title: '数据统计',
        requiresAuth: true
      }
    },
    {
      path: '/search',
      name: 'SearchResult',
      component: () => import('@/views/search/SearchResult.vue')
    }
  ]
})

// 修改路由守卫实现方式
router.beforeEach(async (to, from, next) => {
  const { useUserStore } = await import('@/stores/user')
  const userStore = useUserStore()

  // 优先从 localStorage 读取 token，防止刷新丢失
  if (!userStore.token) {
    const token = localStorage.getItem('token')
    if (token) {
      userStore.setToken(token)
    }
  }

  if (to.meta.requiresAuth && !userStore.token) {
    next('/login')
  } else {
    next()
  }
})

export default router