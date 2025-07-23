<template>
  <div class="login-wrapper" :style="{ backgroundImage: `url(${bgImage})` }">
    <transition name="fade">
      <div class="login-card" key="card">
        <h1 class="logo">CoWrite</h1>

        <!-- 登录方式切换 -->
        <div class="tab-selector">
          <button
              :class="{ active: mode === 'email' }"
              @click="switchMode('email')"
          >
            <MailIcon class="icon" />
            邮箱登录
          </button>
          <button
              :class="{ active: mode === 'account' }"
              @click="switchMode('account')"
          >
            <UserIcon class="icon" />
            账号登录
          </button>
        </div>

        <transition name="slide-fade" mode="out-in">
          <form :key="mode" @submit.prevent="handleLogin">
            <div v-if="mode === 'email'" class="mode-block">
              <div class="form-group">
                <input type="email" placeholder="邮箱" v-model="email" required />
              </div>
              <div class="form-group code-row">
                <input
                    type="text"
                    placeholder="验证码"
                    v-model="code"
                    maxlength="6"
                    pattern="\d*"
                    required
                />
                <button
                    type="button"
                    class="code-button"
                    :disabled="codeCooldown > 0 || !email"
                    @click="sendCode"
                >
                  <svg v-if="codeCooldown > 0" class="ring" viewBox="0 0 36 36">
                    <path
                        class="progress"
                        :stroke-dasharray="`${(60 - codeCooldown) / 60 * 100}, 100`"
                        d="M18 2 a 16 16 0 1 1 0 32 a 16 16 0 1 1 0 -32"
                    />
                  </svg>
                  <span>
                    {{ codeCooldown > 0 ? `${codeCooldown}s` : '发送验证码' }}
                  </span>
                </button>
              </div>
            </div>

            <div v-else class="mode-block">
              <div class="form-group">
                <input type="text" placeholder="用户名" v-model="username" required />
              </div>
              <div class="form-group">
                <input type="password" placeholder="密码" v-model="password" required />
              </div>
              <div class="forgot-password">
                <a href="#" @click.prevent="onForgotPassword">忘记密码？</a>
              </div>
            </div>

            <button type="submit" class="submit-button" :disabled="loading">
              <LoaderIcon v-if="loading" class="icon spin" />
              <span v-else>登录</span>
            </button>
          </form>
        </transition>

        <div class="divider">或</div>

        <button class="github-button" @click="loginWithGitHub">
          <GithubIcon class="icon" /> GitHub 登录
        </button>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import bgImage from '../assets/login_background.png'
import {
  MailIcon,
  UserIcon,
  GithubIcon,
  LoaderIcon,
} from 'lucide-vue-next'

const router = useRouter()

const mode = ref<'email' | 'account'>('email')
const email = ref('')
const code = ref('')
const username = ref('')
const password = ref('')
const loading = ref(false)
const codeCooldown = ref(0)
let timer: any = null

const switchMode = (m: 'email' | 'account') => {
  mode.value = m
}

const sendCode = async () => {
  if (!email.value) return
  try {
    loading.value = true
    await fetch('/api/send-code', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: email.value }),
    })

    codeCooldown.value = 60
    timer = setInterval(() => {
      if (codeCooldown.value > 0) {
        codeCooldown.value--
      } else {
        clearInterval(timer)
      }
    }, 1000)
  } catch (e) {
    alert('发送失败')
  } finally {
    loading.value = false
  }
}

const validateEmail = (val: string): boolean => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val)
}

const validateCode = (val: string): boolean => {
  return /^\d{6}$/.test(val)
}

const handleLogin = async () => {
  if (mode.value === 'email') {
    if (!validateEmail(email.value)) {
      alert('请输入有效邮箱')
      return
    }
    if (!validateCode(code.value)) {
      alert('验证码必须为 6 位数字')
      return
    }
  } else {
    if (!username.value.trim()) {
      alert('请输入用户名')
      return
    }
    if (!password.value.trim()) {
      alert('请输入密码')
      return
    }
  }
  loading.value = true
  try {
    const payload =
        mode.value === 'email'
            ? { email: email.value, code: code.value }
            : { username: username.value, password: password.value }

    const res = await fetch('/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })

    const data = await res.json()
    if (!res.ok) throw new Error(data.message || '登录失败')
    localStorage.setItem('token', data.token)
    router.push('/')
  } catch (err: any) {
    alert(err.message)
  } finally {
    loading.value = false
  }
}

const loginWithGitHub = () => {
  window.location.href =
      'https://github.com/login/oauth/authorize?client_id=YOUR_CLIENT_ID'
}

const onForgotPassword = () => {
  alert('请联系管理员重置密码或前往找回页面')
}
</script>

<style scoped>
.login-wrapper {
  height: 100vh;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-card {
  background: rgba(255, 255, 255, 0.92);
  padding: 2.5rem;
  border-radius: 16px;
  width: 100%;
  max-width: 400px;
  text-align: center;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
  animation: scaleIn 0.4s ease;
}

@keyframes scaleIn {
  from { transform: scale(0.9); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

.logo {
  font-size: 32px;
  font-weight: bold;
  color: #0ee2e9;
  margin-bottom: 1rem;
}

.tab-selector {
  display: flex;
  margin-bottom: 1.2rem;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #ddd;
}

.tab-selector button {
  flex: 1;
  padding: 0.6rem;
  font-weight: 500;
  background: #f0f9ff;
  color: #0c4a6e;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.tab-selector button.active {
  background-color: #0ee2e9;
  color: white;
}

.mode-block {
  transition: all 0.3s ease;
}

.form-group {
  width: 95%;
  text-align: left;
  margin-bottom: 1rem;
}

input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #bbb;
  border-radius: 6px;
  font-size: 14px;
  transition: border 0.3s ease;
}

input:focus {
  border-color: #0ea5e9;
  outline: none;
}

.code-row {
  display: flex;
  gap: 8px;
  align-items: center;
}

.code-button {
  position: relative;
  padding: 0.5rem 0.95rem;
  font-size: 13px;
  background: #0ea5e9;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  min-width: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.code-button .ring {
  width: 20px;
  height: 20px;
  margin-right: 4px;
}

.code-button .progress {
  fill: none;
  stroke: white;
  stroke-width: 3;
  stroke-linecap: round;
}

.forgot-password {
  text-align: right;
  font-size: 13px;
  margin-top: -0.5rem;
  margin-bottom: 1rem;
}

.forgot-password a {
  color: #0ea5e9;
  text-decoration: none;
}

.submit-button {
  width: 100%;
  padding: 0.6rem;
  background-color: #0ea5e9;
  color: white;
  font-size: 16px;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
}

.submit-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.github-button {
  width: 100%;
  padding: 0.6rem;
  background-color: #1f2937;
  color: white;
  font-size: 15px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
}

.github-button:hover {
  background-color: #111827;
}

.divider {
  margin: 1rem 0;
  font-size: 13px;
  color: #777;
}

.icon {
  width: 18px;
  height: 18px;
}

.spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0); }
  100% { transform: rotate(360deg); }
}

input:invalid {
  animation: shake 0.3s ease;
  border-color: red;
}

@keyframes shake {
  0% { transform: translateX(0); }
  25% { transform: translateX(-4px); }
  50% { transform: translateX(4px); }
  75% { transform: translateX(-4px); }
  100% { transform: translateX(0); }
}

</style>
