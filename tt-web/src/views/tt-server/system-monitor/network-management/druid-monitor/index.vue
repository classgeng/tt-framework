<template>
  <light-frame :src="druidMonitorUrl" />
</template>

<script>
import LightFrame from '@/components/LightFrame'
export default {
  name: 'Index',
  components: { LightFrame },
  data() {
    return {
      druidMonitorUrl: `${process.env.VUE_APP_BASE_DOMAIN}${process.env.VUE_APP_BASE_API}/druid/index.html`
    }
  }
}
</script>
