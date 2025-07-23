// src/composables/useAuth.ts
import { reactive, computed } from 'vue'

const state = reactive({
    token: localStorage.getItem('token'),
    userInfo: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo') as string) : null,
})

export const useAuth = () => {
    // 获取 token
    const getToken = computed(() => state.token)

    // 获取 userInfo
    const getUserInfo = computed(() => state.userInfo)

    // 设置 token
    const setToken = (token: string) => {
        state.token = token
        localStorage.setItem('token', token)
    }

    // 设置 userInfo
    const setUserInfo = (userInfo: any) => {
        state.userInfo = userInfo
        localStorage.setItem('userInfo', JSON.stringify(userInfo))
    }

    // 清除 token 和 userInfo
    const clearAuth = () => {
        state.token = null
        state.userInfo = null
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
    }

    return {
        getToken,
        getUserInfo,
        setToken,
        setUserInfo,
        clearAuth,
    }
}
