<template>
  <div v-loading="loadingFlag">
    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col v-for="(power, idx) in powerSources" :key="`power${idx}`" :span="$ui.layout.span.three">
        <el-card>
          <div slot="header">
            <span>电池[{{ idx + 1 }}]的信息：</span>
          </div>
          <el-descriptions title="" border :columns="3">
            <el-descriptions-item :label-style="labelStyle" label="电池名称" :span="3">{{ power['name'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="温度" :span="3">{{ power['temperature'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="充电次数" :span="3">{{ power['cycleCount'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="当前容量" :span="3">{{ power['currentCapacity'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="容量单位" :span="3">{{ power['capacityUnits'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="正在放电" :span="3">{{ power['isDischarging'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="设计容量" :span="3">{{ power['designCapacity'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="最大容量" :span="3">{{ power['maxCapacity'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="剩余电量百分比" :span="3">{{ power['remainingCapacityPercent'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="正在充电" :span="3">{{ power['isCharging'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="安倍数" :span="3">{{ power['amperage'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="电压" :span="3">{{ power['voltage'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="使用率" :span="3">{{ power['powerUsageRate'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="厂商" :span="3">{{ power['manufacturer'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="设备名称" :span="3">{{ power['deviceName'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="化学成分" :span="3">{{ power['chemistry'] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getPowerSourceInfo } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      labelStyle: { width: '130px' },
      loadingFlag: false,
      powerSources: []
    }
  },
  created() {
    this.initialGraphicsCardsInfo()
  },
  methods: {
    async initialGraphicsCardsInfo() {
      this.loadingFlag = true
      const { data: powerSources } = await getPowerSourceInfo()
      this.powerSources = powerSources
      this.loadingFlag = false
    }
  }
}
</script>
