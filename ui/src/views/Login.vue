<template>
  <div class="login">
    <h1>Login</h1>
    <form @submit.prevent="login">
      <div>
        <label for="username">Username:</label>
        <input type="text" id="username" v-model="username" required />
      </div>
      <div>
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="password" required />
      </div>
      <button type="submit">Login</button>
    </form>
  </div>
</template>

<script lang="ts">
export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: '',
    };
  },
  methods: {
    login() {
      // 这里可以添加登录逻辑
      console.log('Logging in with:', this.username, this.password);
      this.$router.push('/');
    },
  },
};
</script>

<style scoped>
.login {
  max-width: 300px;
  margin: 0 auto;
  padding: 2rem;
  text-align: center;
}

.login form div {
  margin-bottom: 1rem;
}

.login label {
  display: block;
  margin-bottom: 0.5rem;
}

.login input {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 1rem;
}

.login button {
  padding: 0.5rem 1rem;
  background-color: #42b983;
  color: white;
  border: none;
  cursor: pointer;
}

.login button:hover {
  background-color: #3aa876;
}
</style> 