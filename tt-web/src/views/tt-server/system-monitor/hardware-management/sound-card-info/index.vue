<template>
  <div v-loading="loadingFlag">
    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col v-for="(sound, idx) in soundsCardList" :key="`sound${idx}`" :span="$ui.layout.span.three" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>声卡[{{ idx + 1 }}]的信息：</span>
          </div>
          <el-descriptions title="" border :columns="3">
            <el-descriptions-item :label-style="labelStyle" label="名称" :span="3">{{ sound['name'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="编码器" :span="3">{{ sound['codec'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="驱动版本" :span="3">{{ sound['driverVersion'] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { getSoundCardInfo } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      loadingFlag: false,
      soundsCardList: [],
      labelStyle: { width: '90px' }
    }
  },
  created() {
    this.initialSoundCardsInfo()
  },
  methods: {
    async initialSoundCardsInfo() {
      this.loadingFlag = true
      const { data: soundsCardList } = await getSoundCardInfo()
      this.soundsCardList = soundsCardList
      this.loadingFlag = false
    }
  }
}
</script>
