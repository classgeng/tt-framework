<template>
  <div :ref="ref" :style="styleObj" />
</template>

<script>
import debounce from 'lodash/debounce'
import { addListener, removeListener } from 'resize-detector'
export default {
  name: 'LightEcharts',
  props: {
    option: {
      type: Object,
      default: () => {}
    },
    styleObj: {
      type: Object,
      default() {
        return {
          width: '400px',
          height: '400px'
        }
      }
    }
  },
  data() {
    return {
      ref: 'LightEchartsRef',
      chart: {}
    }
  },
  watch: {
    option: {
      handler(val) {
        this.chart.setOption(val)
      },
      deep: true
    }
  },
  created() {
    this.resize = debounce(this.resize, 300)
  },
  mounted() {
    this.renderChart()
    addListener(this.$refs[this.ref], this.resize)
  },
  beforeDestroy() {
    removeListener(this.$refs[this.ref], this.resize)
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    resize() {
      this.chart.resize()
    },
    renderChart() {
      this.chart = this.$echarts.init(this.$refs[this.ref])
      this.chart.setOption(this.option)
    }
  }
}
</script>
