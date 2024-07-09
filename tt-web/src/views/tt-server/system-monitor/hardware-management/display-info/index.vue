<template>
  <div>
    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col v-for="(monitor, idx) in displayInfos" :key="`monitor${idx}`" :span="$ui.layout.span.two">
        <el-card>
          <div slot="header">
            <span>显示器[{{ idx + 1 }}]的信息：</span>
          </div>

          <el-descriptions title="" border :columns="3">
            <el-descriptions-item :label-style="labelStyle" label="显示器名称" :span="3">{{ monitor['monitorName'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="序列数值" :span="3">{{ monitor['serialNumber'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="产品Id" :span="3">{{ monitor['productId'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="数码显示" :span="3">{{ monitor['isDigital'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="厂商Id" :span="3">{{ monitor['manufacturerId'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="Edid版本" :span="3">{{ monitor['version'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="范围限制" :span="3">{{ monitor['rangeLimits'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="最佳设置" :span="3">{{ monitor['preferredTiming'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="未识别信息" :span="3">{{ monitor['unspecifiedText'] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDisplayInfo } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      labelStyle: { width: '110px' },
      loadingFlag: false,
      displayInfos: []
    }
  },
  created() {
    this.initialDisplayInfo()
  },
  methods: {
    async initialDisplayInfo() {
      this.loadingFlag = true
      const { data: displayInfos } = await getDisplayInfo()
      this.displayInfos = displayInfos
      this.loadingFlag = false
    }
  }
}
</script>
