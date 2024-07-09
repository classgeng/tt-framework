<template>
  <div v-loading="loadingFlag">
    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col v-for="(graphic, idx) in graphicCards" :key="`graphic${idx}`" :span="$ui.layout.span.three">
        <el-card>
          <div slot="header">
            <span>显卡[{{ idx + 1 }}]的信息：</span>
          </div>
          <el-descriptions title="" border :columns="3">
            <el-descriptions-item :label-style="labelStyle" label="名称" :span="3">{{ graphic['name'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="供应商" :span="3">{{ graphic['vendor'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="驱动版本" :span="3">{{ graphic['versionInfo'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="可用显存大小" :span="3">{{ graphic['vRamFormat'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="设备Id" :span="3">{{ graphic['deviceId'] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { getGraphicsCardsInfo } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      loadingFlag: false,
      graphicCards: [],
      labelStyle: { width: '105px' }
    }
  },
  created() {
    this.initialGraphicsCardsInfo()
  },
  methods: {
    async initialGraphicsCardsInfo() {
      this.loadingFlag = true
      const { data: graphicCards } = await getGraphicsCardsInfo()
      this.graphicCards = graphicCards
      this.loadingFlag = false
    }
  }
}
</script>
