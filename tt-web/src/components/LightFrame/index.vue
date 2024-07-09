<template>
  <div v-loading="loading" style="height: calc(100vh - 110px)">
    <iframe class="iframe-panel" :src="src" />
  </div>
</template>
<script>
export default {
  name: 'LightFrame',
  props: {
    src: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      height: document.documentElement.clientHeight - 94.5 + 'px',
      loading: true,
      url: this.src
    }
  },
  mounted() {
    setTimeout(() => { this.loading = false }, 300)
  }
}
</script>

<style scoped>
.iframe-panel {
  width: 100%;
  height: 100%;
  border: none;
  overflow-scrolling: auto;
}
</style>
