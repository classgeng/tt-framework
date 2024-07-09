<template>
  <div>
    <el-table v-loading="loadingFlag" border :data="threadList">
      <el-table-column type="index" width="50" label="序号" align="center" />
      <el-table-column type="expand">
        <template slot-scope="{ $index, row }">
          <div style="padding: 15px">
            <el-row :gutter="$ui.layout.gutter.g10">
              <el-col :span="$ui.layout.span.three" style="margin-bottom: 15px">
                <el-card>
                  <div slot="header">
                    <span>堆栈跟踪列表</span>
                  </div>
                  <el-table border :data="row['stackTrace']">
                    <el-table-column prop="className" label="类名称" min-width="100" show-overflow-tooltip />
                    <el-table-column prop="fileName" label="源文件名称" min-width="100" show-overflow-tooltip />
                    <el-table-column prop="lineNumber" label="lineNumber" min-width="100" show-overflow-tooltip />
                    <el-table-column prop="methodName" label="方法名" min-width="100" show-overflow-tooltip />
                    <el-table-column prop="nativeMethod" label="本地方法" min-width="100">
                      <template slot-scope="subScope">
                        {{ subScope.row['nativeMethod'] ? '是' : '否' }}
                      </template>
                    </el-table-column>
                  </el-table>
                </el-card>
              </el-col>

              <el-col v-if="!!row['threadGroup']" :span="$ui.layout.span.three" style="margin-bottom: 15px">
                <el-card>
                  <div slot="header">
                    <span>线程分组信息</span>
                  </div>
                  <p>名称 {{ row['threadGroup']['name'] }}</p>
                  <p>已销毁 {{ row['threadGroup']['destroyed'] ? '是' : '否' }}</p>
                  <p>最大权重 {{ row['threadGroup']['maxPriority'] }}</p>
                  <p>是否守护线程 {{ row['threadGroup']['daemon'] ? '是' : '否' }}</p>
                </el-card>
              </el-col>

            </el-row>

          </div>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="线程ID" min-width="100" show-overflow-tooltip />
      <el-table-column prop="name" label="线程名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="priority" label="权重值" min-width="100" show-overflow-tooltip />
      <el-table-column prop="state" label="状态" min-width="100" show-overflow-tooltip />
      <el-table-column prop="interrupted" label="是否阻塞" min-width="100">
        <template slot-scope="{ row }">
          {{ row['interrupted'] ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column prop="daemon" label="是否守护进程" min-width="100">
        <template slot-scope="{ row }">
          {{ row['daemon'] ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column prop="alive" label="是否活跃" min-width="100">
        <template slot-scope="{ row }">
          {{ row['alive'] ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column prop="creationTime" label="创建时间" min-width="100">
        <template slot-scope="{ row }">
          {{ $moment(row.creationTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
      </el-table-column>

    </el-table>
  </div>
</template>

<script>
import { getAppThreadInfo } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      loadingFlag: false,
      threadList: []
    }
  },
  created() {
    this.initialAppThreadInfo()
  },
  methods: {
    async initialAppThreadInfo() {
      this.loadingFlag = true
      const { data: res } = await getAppThreadInfo()
      console.log(res)
      this.threadList = res['activeThread']
      this.loadingFlag = false
    }
  }
}
</script>

