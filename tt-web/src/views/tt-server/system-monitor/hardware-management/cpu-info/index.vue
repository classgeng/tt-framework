<template>
  <div v-loading="loadingFlag">
    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col :span="$ui.layout.span.three" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>处理器信息</span>
          </div>
          <el-descriptions title="" border :columns="3">
            <el-descriptions-item :label-style="labelStyle" label="名称" :span="3">{{ processorIdentifier['name'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="64位" :span="3">{{ processorIdentifier['cpu64bit'] ? '是' : '否' }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="标识" :span="3">{{ processorIdentifier['identifier'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="架构" :span="3">{{ processorIdentifier['microarchitecture'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="零件编号" :span="3">{{ processorIdentifier['model'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="处理器ID" :span="3">{{ processorIdentifier['processorID'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="步进" :span="3">{{ processorIdentifier['stepping'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="供应商" :span="3">{{ processorIdentifier['vendor'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="供应商频率" :span="3">{{ processorIdentifier['vendorFreq'] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.three" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>物理处理器列表</span>
          </div>

          <el-table border :data="physicalProcessors">
            <el-table-column type="index" width="50" label="序号" align="center" />
            <el-table-column prop="physicalProcessorNumber" label="处理器Id" min-width="100" show-overflow-tooltip />
            <el-table-column prop="idString" label="平台标识ID" min-width="100" show-overflow-tooltip />
            <el-table-column prop="physicalPackageNumber" label="物理包Id" min-width="100" show-overflow-tooltip />
            <el-table-column prop="efficiency" label="能效值" min-width="100" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.three" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>处理器缓存列表</span>
          </div>

          <el-table border :data="processorCaches">
            <el-table-column type="index" width="50" label="序号" align="center" />
            <el-table-column prop="level" label="缓存等级" min-width="100" show-overflow-tooltip />
            <el-table-column prop="cacheSize" label="缓存大小" min-width="100" show-overflow-tooltip />
            <el-table-column prop="type" label="类型" min-width="100" show-overflow-tooltip />
            <el-table-column prop="lineSize" label="缓存行" min-width="100" show-overflow-tooltip />
            <el-table-column prop="associativity" label="缓存路数" min-width="100" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col :span="$ui.layout.span.three" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>逻辑处理器列表</span>
          </div>

          <el-table border :data="logicalProcessors">
            <el-table-column type="index" width="50" label="序号" align="center" />
            <el-table-column prop="processorNumber" label="处理器Id" min-width="100" show-overflow-tooltip />
            <el-table-column prop="processorGroup" label="处理器分组" min-width="100" show-overflow-tooltip />
            <el-table-column prop="physicalPackageNumber" label="物理包Id" min-width="100" show-overflow-tooltip />
            <el-table-column prop="numaNode" label="NUMA节点号" min-width="100" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { getCpuInfo } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      labelStyle: { width: '110px' },
      loadingFlag: false,
      processorIdentifier: {},
      cpuInfo: {},
      processorCpuLoadTicks: [],
      processorCaches: [],
      physicalProcessors: [],
      logicalProcessors: []
    }
  },
  created() {
    this.initialCpuInfo()
  },
  methods: {
    async initialCpuInfo() {
      this.loadingFlag = true
      const { data: cpuInfo } = await getCpuInfo()
      console.log(cpuInfo)
      this.cpuInfo = cpuInfo
      this.processorIdentifier = cpuInfo.instance.processorIdentifier
      this.logicalProcessors = cpuInfo.instance.logicalProcessors
      this.physicalProcessors = cpuInfo.instance.physicalProcessors
      this.processorCaches = cpuInfo.instance.processorCaches
      this.processorCpuLoadTicks = cpuInfo.instance.processorCpuLoadTicks
      this.currentFreq = cpuInfo.instance.currentFreq
      this.systemCpuLoadTicks = cpuInfo.instance.systemCpuLoadTicks
      this.loadingFlag = false
    }
  }
}
</script>
