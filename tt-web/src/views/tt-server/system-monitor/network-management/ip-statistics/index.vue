<template>
  <div style="padding: 15px">

    <el-row :gutter="$ui.layout.gutter.g10" style="margin-bottom: 15px">
      <el-col :span="$ui.layout.span.two">
        <el-card>
          <div slot="header">
            <span>TcpV4</span>
          </div>
          <el-descriptions title="" border :columns="5">
            <el-descriptions-item :label-style="labelStyle" label="发送段数" :span="2">{{ ipStatistics['tcpV4']['segmentsSent'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="接收段数" :span="2">{{ ipStatistics['tcpV4']['segmentsReceived'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="重传段数" :span="2">{{ ipStatistics['tcpV4']['segmentsRetransmitted'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="重置段数" :span="2">{{ ipStatistics['tcpV4']['outResets'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="接收错误数" :span="2">{{ ipStatistics['tcpV4']['inErrors'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="连接失败" :span="2">{{ ipStatistics['tcpV4']['connectionFailures'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="连接建立" :span="2">{{ ipStatistics['tcpV4']['connectionsEstablished'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="连接被动总数" :span="2">{{ ipStatistics['tcpV4']['connectionsPassive'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="连接激活总数" :span="2">{{ ipStatistics['tcpV4']['connectionsActive'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="连接重置总数" :span="2">{{ ipStatistics['tcpV4']['connectionsReset'] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.two">
        <el-card>
          <div slot="header">
            <span>TcpV6</span>
          </div>
          <el-descriptions title="" border :columns="5">
            <el-descriptions-item :label-style="labelStyle" label="发送段数" :span="2">{{ ipStatistics['tcpV6']['segmentsSent'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="接收段数" :span="2">{{ ipStatistics['tcpV6']['segmentsReceived'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="重传段数" :span="2">{{ ipStatistics['tcpV6']['segmentsRetransmitted'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="重置段数" :span="2">{{ ipStatistics['tcpV6']['outResets'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="接收错误数" :span="2">{{ ipStatistics['tcpV6']['inErrors'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="连接失败" :span="2">{{ ipStatistics['tcpV6']['connectionFailures'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="连接建立" :span="2">{{ ipStatistics['tcpV6']['connectionsEstablished'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="连接被动总数" :span="2">{{ ipStatistics['tcpV6']['connectionsPassive'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="连接激活总数" :span="2">{{ ipStatistics['tcpV6']['connectionsActive'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle" label="连接重置总数" :span="2">{{ ipStatistics['tcpV6']['connectionsReset'] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="$ui.layout.gutter.g10" style="margin-bottom: 15px">
      <el-col :span="$ui.layout.span.two">
        <el-card>
          <div slot="header">
            <span>UdpV4</span>
          </div>
          <el-descriptions title="" border :columns="4">
            <el-descriptions-item :label-style="labelStyle2" label="收到的报文错误" :span="2">{{ ipStatistics['udpV4']['datagramsReceivedErrors'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle2" label="发送的报文" :span="2">{{ ipStatistics['udpV4']['datagramsSent'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle2" label="无端口应用收到的报文" :span="2">{{ ipStatistics['udpV4']['datagramsNoPort'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle2" label="接收到的报文" :span="2">{{ ipStatistics['udpV4']['datagramsReceived'] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.two">
        <el-card>
          <div slot="header">
            <span>UdpV6</span>
          </div>
          <el-descriptions title="" border :columns="4">
            <el-descriptions-item :label-style="labelStyle2" label="收到的报文错误" :span="2">{{ ipStatistics['udpV6']['datagramsReceivedErrors'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle2" label="发送的报文" :span="2">{{ ipStatistics['udpV6']['datagramsSent'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle2" label="无端口应用收到的报文" :span="2">{{ ipStatistics['udpV6']['datagramsNoPort'] }}</el-descriptions-item>
            <el-descriptions-item :label-style="labelStyle2" label="接收到的报文" :span="2">{{ ipStatistics['udpV6']['datagramsReceived'] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="$ui.layout.gutter.g10" style="margin-bottom: 15px">
      <el-col :span="$ui.layout.span.one">
        <el-card>
          <div slot="header">
            <span>IP连接列表</span>
          </div>
          <el-table v-loading="loadingFlag" border :data="ipStatistics['ipConnections']" style="width: 100% ">
            <el-table-column type="index" width="50" label="序号" align="center" />
            <el-table-column prop="owningProcessId" label="所属进程：PID" min-width="180" show-overflow-tooltip />
            <el-table-column prop="state" label="状态" min-width="180" show-overflow-tooltip />
            <el-table-column prop="type" label="类型" min-width="180" show-overflow-tooltip />
            <el-table-column prop="localPort" label="本地端口" min-width="180" show-overflow-tooltip />
            <el-table-column prop="localAddress" label="本地地址" min-width="180" show-overflow-tooltip />
            <el-table-column prop="foreignPort" label="外部端口" min-width="180" show-overflow-tooltip />
            <el-table-column prop="foreignAddress" label="外部地址" min-width="180" show-overflow-tooltip />
            <el-table-column prop="receiveQueue" label="接收队列数" min-width="180" show-overflow-tooltip />
            <el-table-column prop="transmitQueue" label="传输队列数" min-width="180" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getIpStatistics } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      loadingFlag: false,
      ipStatistics: {},
      labelStyle: { width: '120px' },
      labelStyle2: { width: '190px' }
    }
  },
  created() {
    this.initialIpStatistics()
  },
  methods: {
    async initialIpStatistics() {
      this.loadingFlag = true
      const { data: ipStatistics } = await getIpStatistics()
      this.ipStatistics = ipStatistics
      this.loadingFlag = false
    }
  }
}
</script>
