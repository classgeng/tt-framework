<template>
  <div>
    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col :span="$ui.layout.span.three" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>Server 服务：</span>
          </div>
          <p>TCP端口号：{{ serverInfo.tcp_port }}</p>
          <p>进程ID：{{ serverInfo.process_id }}</p>
          <p>版本：{{ serverInfo.redis_version }}</p>
          <p>构建ID：{{ serverInfo.redis_build_id }}</p>
          <p>配置文件：{{ serverInfo.config_file.replaceAll('\\', '\&#92;') }}</p>
          <p>模式：{{ serverInfo.redis_mode }}</p>
          <p>运行ID：{{ serverInfo.run_id }}</p>
          <p>正常运行时间(秒)：{{ serverInfo.uptime_in_seconds }}</p>
          <p>正常运行时间(天)：{{ serverInfo.uptime_in_days }}</p>
          <p>操作系统：{{ serverInfo.os }}</p>
          <p>系统架构位数：{{ serverInfo.arch_bits }}</p>
          <p>多路复用接口：{{ serverInfo.multiplexing_api }}</p>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.three" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>Clients 客户端：</span>
          </div>
          <p>客户端连接数：{{ clientsInfo.connected_clients }}</p>
          <p>集群连接数：{{ clientsInfo.cluster_connections }}</p>
          <p>最大客户端连接数：{{ clientsInfo.maxclients }}</p>
          <p>已连接的从节点：{{ clientsInfo.redis_build_id }}</p>
          <p>最大输入缓冲：{{ clientsInfo.client_recent_max_input_buffer }}</p>
          <p>阻塞的客户端数：{{ clientsInfo.blocked_clients }}</p>
          <p>跟踪的客户端数：{{ clientsInfo.tracking_clients }}</p>
          <p>超时的客户端数：{{ clientsInfo.clients_in_timeout_table }}</p>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.three" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>Memory 内存：</span>
          </div>
          <p>内存占用：{{ memoryInfo.used_memory_human }}</p>
          <p>峰值内存占用：{{ clientsInfo.used_memory_peak_human }}</p>
          <p>内存分配器：{{ memoryInfo.mem_allocator }}</p>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { getRedisStatistic } from '@/api/tt-server/redis-monitor'

export default {
  name: 'Index',
  data() {
    return {
      serverInfo: {
        tcp_port: '',
        process_id: '',
        redis_git_sha1: '',
        run_id: '',
        os: '',
        redis_mode: '',
        uptime_in_seconds: '',
        redis_git_dirty: '',
        uptime_in_days: '',
        hz: '',
        arch_bits: '',
        redis_version: '',
        redis_build_id: '',
        multiplexing_api: '',
        config_file: '',
        lru_clock: ''
      },
      clientsInfo: {
        connected_clients: 2,
        cluster_connections: 0,
        maxclients: 10000,
        client_recent_max_input_buffer: 20480,
        client_recent_max_output_buffer: 0,
        blocked_clients: 0,
        tracking_clients: 0,
        clients_in_timeout_table: 0
      },
      memoryInfo: {
        used_memory_human: '740.36K',
        used_memory_rss: '758048',
        used_memory_peak_human: '741.33K',
        mem_fragmentation_ratio: '1.00',
        used_memory_lua: '36864',
        used_memory: '758128',
        mem_allocator: 'jemalloc-3.6.0',
        used_memory_peak: '759120'
      },
      statsInfo: {
      }
    }
  },
  async created() {
    await this.initialRedisServerInfo()
    await this.initialRedisClientsInfo()
    await this.initialRedisMemoryInfo()
    await this.initialPersistInfo()
    await this.initialStatsInfo()
  },
  methods: {
    async initialRedisServerInfo() {
      const { data: serverInfo } = await getRedisStatistic({ key: 'server' })
      console.log(serverInfo)
      this.serverInfo = serverInfo
    },
    async initialRedisClientsInfo() {
      const { data: clientsInfo } = await getRedisStatistic({ key: 'clients' })
      console.log(clientsInfo)
      this.clientsInfo = clientsInfo
    },
    async initialRedisMemoryInfo() {
      const { data: memoryInfo } = await getRedisStatistic({ key: 'memory' })
      console.log(memoryInfo)
      this.memoryInfo = memoryInfo
    },
    async initialPersistInfo() {
      const { data: persistInfo } = await getRedisStatistic({ key: 'persistence' })
      console.log(persistInfo)
      this.persistInfo = persistInfo
    },
    async initialStatsInfo() {
      const { data: statsInfo } = await getRedisStatistic({ key: 'stats' })
      console.log(statsInfo)
      this.statsInfo = statsInfo
    }
  }
}
</script>

