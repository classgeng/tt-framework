<template>
  <div>
    <el-table
      v-loading="loadingFlag"
      border
      :data="diskStores"
      style="width: 100% "
    >
      <el-table-column type="index" width="50" label="序号" align="center" />
      <el-table-column type="expand">
        <template slot-scope="{ $index, row }">
          <div style="padding: 15px">
            <el-table border :data="row['partitionList']">
              <el-table-column prop="name" label="分区名称" min-width="180" show-overflow-tooltip />
              <el-table-column prop="mountPoint" label="分区挂载点" min-width="180" show-overflow-tooltip />
              <el-table-column prop="identification" label="分区标识" min-width="180" show-overflow-tooltip />
              <el-table-column prop="size" label="分区大小" min-width="180" show-overflow-tooltip />
              <el-table-column prop="major" label="Major" min-width="180" show-overflow-tooltip />
              <el-table-column prop="minor" label="Minor" min-width="180" show-overflow-tooltip />
            </el-table>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="磁盘名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="model" label="磁盘型号" min-width="180" show-overflow-tooltip />
      <el-table-column prop="size" label="磁盘大小" min-width="180" show-overflow-tooltip />
      <el-table-column prop="reads" label="读取" min-width="180" show-overflow-tooltip />
      <el-table-column prop="readBytes" label="读取字节" min-width="180" show-overflow-tooltip />
      <el-table-column prop="writes" label="写入" min-width="180" show-overflow-tooltip />
      <el-table-column prop="writeBytes" label="写入字节" min-width="180" show-overflow-tooltip />
      <el-table-column prop="currentQueueLength" label="当前队列长度" min-width="180" show-overflow-tooltip />
      <el-table-column prop="toString" label="完整打印" min-width="180" show-overflow-tooltip />
    </el-table>

  </div>
</template>

<script>
import { getDiskStoreInfo } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      loadingFlag: false,
      diskStores: []
    }
  },
  created() {
    this.initialDiskStoreInfo()
  },
  methods: {
    async initialDiskStoreInfo() {
      this.loadingFlag = true
      const { data: diskStores } = await getDiskStoreInfo()
      this.diskStores = diskStores
      this.loadingFlag = false
    }
  }
}
</script>
