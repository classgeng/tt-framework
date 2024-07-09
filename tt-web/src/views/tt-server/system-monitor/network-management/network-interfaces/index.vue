<template>
  <div style="padding: 15px">
    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col v-for="(nwIntf, idx) in netWorkInterfaces" :key="`networkIntf${idx}`" :span="$ui.layout.span.two" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>网路接口 [{{ idx }}]</span>
          </div>
          <el-descriptions title="" border :columns="3">
            <el-descriptions-item :label-style="labelStyle" label="接口展示名称" :span="3">{{ nwIntf.displayName }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="接口名称" :span="1">{{ nwIntf.name }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="接口别名" :span="2">{{ nwIntf.ifAlias }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="接口全称" :span="3">{{ nwIntf.networkInterface }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="IPv4地址" :span="3">{{ nwIntf.iPv4addr }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="IPv6地址" :span="3"> {{ nwIntf.iPv6addr }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="Mac地址" :span="3"> {{ nwIntf.macAddr }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getNetWorkInterfaces } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      loadingFlag: false,
      labelStyle: { width: '120px' },
      netWorkInterfaces: []
    }
  },
  created() {
    this.initialNetWorkInterfaces()
  },
  methods: {
    async initialNetWorkInterfaces() {
      this.loadingFlag = true
      const { data: netWorkInterfaces } = await getNetWorkInterfaces()
      this.netWorkInterfaces = netWorkInterfaces
      this.loadingFlag = false
    }
  }
}
</script>
