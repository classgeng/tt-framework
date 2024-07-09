<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item v-for="(p, idx) in stored" :key="`path${idx}`"><a @click="changeDir(p)">{{ p }}</a></el-breadcrumb-item>
    </el-breadcrumb>

    <el-table v-loading="loadingFlag" border :data="fileList">
      <el-table-column type="index" width="50" label="序号" align="center" />
      <el-table-column prop="name" label="文件名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="sizeRead" label="文件大小" width="110" />
      <el-table-column prop="lastModified2" label="修改时间" width="160" align="center" />
      <!-- <el-table-column prop="canonicalPath" label="规范路径" min-width="100" show-overflow-tooltip /> -->
      <el-table-column prop="directory" label="目录" min-width="100" show-overflow-tooltip />
      <el-table-column prop="file" label="文件" min-width="100" show-overflow-tooltip />
      <el-table-column fixed="right" label="操作" align="center" width="300px">
        <template slot-scope="{ $index, row }">
          <el-button v-if="row.file" size="mini">下载</el-button>
          <el-button v-else size="mini" @click="toSubDir(row)">访问</el-button>
          <el-button size="mini">重命名</el-button>
          <el-button size="mini">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { listFiles } from '@/api/tt-server/tool-lib/file-view'

export default {
  name: 'FileView',
  props: {
    param: {
      type: Object,
      required: true,
      default: () => {}
    }
  },
  data() {
    return {
      loadingFlag: false,
      fileList: [],
      stored: []
    }
  },
  async created() {
    this.stored.push(this.param.mount)
    await this.listFilesFrom(this.param.mount)
  },
  methods: {
    async listFilesFrom(path) {
      path = path.replaceAll('\\', '\\\\')
      this.loadingFlag = true
      const { data: files } = await listFiles({ path })
      this.fileList = files
      this.loadingFlag = false
    },
    async changeDir(val) {
      await this.listFilesFrom(val)
    },
    async toSubDir(val) {
      this.stored.push(val.name)
      await this.listFilesFrom(val.canonicalPath)
    }
  }
}
</script>
