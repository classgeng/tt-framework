<template>
  <div v-loading="loadingFlag" style="padding: 15px">
    <p>CPU温度： {{ sensorInfo.instance.cpuTemperature }}</p>
    <p>CPU电压： {{ sensorInfo.instance.cpuVoltage }}</p>
    <p>风扇速度： {{ sensorInfo.instance.fanSpeeds }}</p>

  </div>
</template>

<script>
import { getSensorInfo } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      loadingFlag: false,
      sensorInfo: {}
    }
  },
  created() {
    this.initialSensorInfo()
  },
  methods: {
    async initialSensorInfo() {
      this.loadingFlag = true
      const { data: sensorInfo } = await getSensorInfo()
      this.sensorInfo = sensorInfo
      this.loadingFlag = false
    }
  }
}
</script>
