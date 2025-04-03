<script setup>
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <div class="app">
    <header>
      <nav>
        <router-link to="/">首頁</router-link> |
        <router-link to="/projects">項目列表</router-link>
      </nav>
    </header>
    
    <main>
      <router-view />
    </main>
  </div>
</template>

<style>
.app {
  max-width: 1280px;
  margin: 0 auto;
  padding: 2rem;
}

header {
  margin-bottom: 2rem;
  text-align: center;
}

nav {
  padding: 1rem 0;
}

nav a {
  margin: 0 0.5rem;
  color: #646cff;
  text-decoration: none;
}

nav a.router-link-active {
  font-weight: bold;
}
</style>
