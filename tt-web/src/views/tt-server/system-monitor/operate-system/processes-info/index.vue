<template>
  <div style="padding: 15px">
    <el-table v-loading="loadingFlag" border :data="processesInfo['osProcessMapList']" style="width: 100% ">
      <el-table-column type="index" width="50" label="序号" align="center" />
      <el-table-column type="expand">
        <template slot-scope="{ $index, row }">
          <el-form label-position="left" inline>
            <el-form-item label="bitness">
              <span>{{ row['bitness'] }}</span>
            </el-form-item>

            <el-form-item label="affinityMask">
              <span>{{ row['affinityMask'] }}</span>
            </el-form-item>

            <el-form-item label="groupId">
              <span>{{ row['groupId'] }}</span>
            </el-form-item>

            <el-form-item label="processCpuLoadBetweenTicks">
              <span>{{ row['processCpuLoadBetweenTicks'] }}</span>
            </el-form-item>

            <el-form-item label="pid">
              <span>{{ row['pid'] }}</span>
            </el-form-item>

            <el-form-item label="openFiles">
              <span>{{ row['openFiles'] }}</span>
            </el-form-item>

            <el-form-item label="currentWorkingDirectory">
              <span>{{ row['currentWorkingDirectory'] }}</span>
            </el-form-item>

            <el-form-item label="processCpuLoadCumulative">
              <span>{{ row['processCpuLoadCumulative'] }}</span>
            </el-form-item>

            <el-form-item label="path">
              <span>{{ row['path'] }}</span>
            </el-form-item>

            <el-form-item label="processName">
              <span>{{ row['processName'] }}</span>
            </el-form-item>

            <el-form-item label="virtualMemSize">
              <span>{{ row['virtualMemSize'] }}</span>
            </el-form-item>

            <el-form-item label="majorFaults">
              <span>{{ row['majorFaults'] }}</span>
            </el-form-item>

            <el-form-item label="startTime">
              <span>{{ row['startTime'] }}</span>
            </el-form-item>

            <el-form-item label="group">
              <span>{{ row['group'] }}</span>
            </el-form-item>

            <el-form-item label="bytesWritten">
              <span>{{ row['bytesWritten'] }}</span>
            </el-form-item>

            <el-form-item label="userTime">
              <span>{{ row['userTime'] }}</span>
            </el-form-item>

            <el-form-item label="residentSetSize">
              <span>{{ row['residentSetSize'] }}</span>
            </el-form-item>

            <el-form-item label="bytesRead">
              <span>{{ row['bytesRead'] }}</span>
            </el-form-item>

            <el-form-item label="threadCount">
              <span>{{ row['threadCount'] }}</span>
            </el-form-item>

            <el-form-item label="priority">
              <span>{{ row['priority'] }}</span>
            </el-form-item>

            <el-form-item label="userId">
              <span>{{ row['userId'] }}</span>
            </el-form-item>

            <el-form-item label="upTime">
              <span>{{ row['upTime'] }}</span>
            </el-form-item>

            <el-form-item label="kernelTime">
              <span>{{ row['kernelTime'] }}</span>
            </el-form-item>

            <el-form-item label="memUsageRate">
              <span>{{ row['memUsageRate'] }}</span>
            </el-form-item>

            <el-form-item label="hardOpenFileLimit">
              <span>{{ row['hardOpenFileLimit'] }}</span>
            </el-form-item>

            <el-form-item label="minorFaults">
              <span>{{ row['minorFaults'] }}</span>
            </el-form-item>

            <el-form-item label="softOpenFileLimit">
              <span>{{ row['softOpenFileLimit'] }}</span>
            </el-form-item>

            <el-form-item label="user">
              <span>{{ row['user'] }}</span>
            </el-form-item>

            <el-form-item label="cpuUsageRate">
              <span>{{ row['cpuUsageRate'] }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column prop="processName" label="进程名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="virtualMemSize" label="虚拟内存大小" min-width="180" show-overflow-tooltip />
      <el-table-column prop="pid" label="进程ID(PID)" min-width="180" show-overflow-tooltip />
      <el-table-column prop="threadCount" label="线程计数" min-width="180" show-overflow-tooltip />
      <el-table-column prop="priority" label="权重值" min-width="180" show-overflow-tooltip />
    </el-table>

  </div>
</template>

<script>
import { getProcessesInfo } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      loadingFlag: false,
      processesInfo: {}
    }
  },
  created() {
    this.initialProcessesInfo()
  },
  methods: {
    async initialProcessesInfo() {
      this.loadingFlag = true
      const { data: processesInfo } = await getProcessesInfo()
      this.processesInfo = processesInfo
      this.loadingFlag = false
    }
  }
}
</script>
