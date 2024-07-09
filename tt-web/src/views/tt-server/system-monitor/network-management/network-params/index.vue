<template>
  <div style="padding: 15px">
    <el-row :gutter="10">
      <el-col :span="$ui.layout.span.one">
        <el-card>
          <div slot="header">
            网络参数信息
          </div>

          <p>主机名：{{ networkParams.hostName }}</p>
          <p>域名：{{ networkParams.domainName }}</p>
          <p>IPv6网关：{{ networkParams.ipv6Gateway }}</p>
          <p>IPv4网关：{{ networkParams.ipv4Gateway }}</p>
          <h4>DNS解析服务器列表</h4>
          <p v-for="(dns, idx) in networkParams.dnsServers" :key="`dns${idx}`">{{ dns }}</p>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { getNetworkParamsInfo } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      loadingFlag: false,
      networkParams: {}
    }
  },
  created() {
    this.initialNetworkParamsInfo()
  },
  methods: {
    async initialNetworkParamsInfo() {
      this.loadingFlag = true
      const { data: networkParams } = await getNetworkParamsInfo()
      this.networkParams = networkParams
      this.loadingFlag = false
    }
  }
}
</script>
