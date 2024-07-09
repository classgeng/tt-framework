<template>
  <div v-loading="loadingFlag" style="padding: 15px">
    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col :span="$ui.layout.span.one" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>基础信息</span>
          </div>
          <p>最大文件句柄数量：{{ fileStoreInfo.maxFileDescriptors }}</p>
          <p>已打开句柄数量：{{ fileStoreInfo.openFileDescriptors }}</p>
          <p>已打开句柄百分比：{{ fileStoreInfo.fdUsageRate }}</p>
        </el-card>
      </el-col>

      <el-col v-for="(fileSys, idx) in fileStoreInfo.fileStores" :key="`fileSys${idx}`" :span="$ui.layout.span.two" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>{{ fileSys.name }}</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="openFileViewDrawer(fileSys)">浏览文件</el-button>
          </div>
          <el-descriptions title="" border :columns="3" style="margin-bottom: 20px">
            <el-descriptions-item label="挂载盘符">{{ fileSys.mount }}</el-descriptions-item>
            <el-descriptions-item label="存储类型">{{ fileSys.type }}</el-descriptions-item>
            <el-descriptions-item label="全部容量">{{ fileSys['totalSpace'] }}</el-descriptions-item>
            <el-descriptions-item label="使用容量">{{ fileSys['usedSpace'] }}</el-descriptions-item>
            <el-descriptions-item label="剩余容量">{{ fileSys['usableSpace'] }}</el-descriptions-item>
            <el-descriptions-item label="占用比">{{ fileSys['usageRate'] }}</el-descriptions-item>

            <el-descriptions-item label="存储UUID" :span="4">{{ fileSys['uuid'] }}</el-descriptions-item>
            <el-descriptions-item label="存储卷标" :span="4">{{ fileSys['volume'] }}</el-descriptions-item>
            <el-descriptions-item label="存储选项" :span="4">{{ fileSys['options'] }}</el-descriptions-item>
            <el-descriptions-item label="存储描述" :span="4">{{ fileSys['description'] }}</el-descriptions-item>
          </el-descriptions>

          <light-echarts :option="fileSys['fsChartOption']" :style-obj="chartsStyle" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 编辑抽屉 -->
    <el-drawer
      :title="fileView.title"
      :size="fileView.width"
      :append-to-body="true"
      :destroy-on-close="true"
      :modal="true"
      :visible.sync="fileView.visible"
      @close="closeAllDrawer"
    >
      <file-view v-if="fileView.visible" :param="fileView.param" />
    </el-drawer>
  </div>
</template>

<script>
import { getFileSystemInfo } from '@/api/tt-server/sys-monitor'
import LightEcharts from '@/components/LightEcharts'
import FileView from '@/views/tt-server/system-monitor/operate-system/file-system/file-view'

export default {
  name: 'Index',
  components: { FileView, LightEcharts },
  data() {
    return {
      loadingFlag: false,
      chartsStyle: {
        width: '100%',
        height: '400px'
      },
      fileStoreInfo: {
        maxFileDescriptors: '',
        openFileDescriptors: '',
        fileDescriptors: '',
        fdUsageRate: '',
        fileStores: []
      },
      fileView: {
        visible: false,
        width: '80vw',
        title: '',
        param: { path: '' }
      }
    }
  },
  async created() {
    this.loadingFlag = true
    await this.initialFileStoresInfo()
    this.loadingFlag = false
  },
  methods: {
    closeAllDrawer(val) {
      this.fileView.visible = false
    },
    openFileViewDrawer(val) {
      this.fileView.title = `${val.name}下的文件：`
      this.fileView.param = val
      this.fileView.visible = true
    },
    async initialFileStoresInfo() {
      const { data: fileStoreInfo } = await getFileSystemInfo()
      console.log(fileStoreInfo)
      this.fileStoreInfo = fileStoreInfo
      this.fileStoreInfo.fileStores.forEach((row, idx, arr) => {
        console.log(row)
        const fsChartOption = {
          title: {
            text: row.name,
            subtext: row.description,
            left: 'center'
          },
          tooltip: { trigger: 'item' },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              name: `${row.name}的占用情况`,
              type: 'pie',
              radius: '60%',
              label: {
                normal: {
                  formatter: '{b} {c}GB ({d}%)',
                  textStyle: {
                    fontWeight: 'normal',
                    fontSize: 12
                  }
                }
              },
              data: [
                { name: '已使用', value: (row.srcUsedSpace / 1024 / 1024 / 1024).toFixed(2) },
                { name: '还剩余', value: (row.freeSpace / 1024 / 1024 / 1024).toFixed(2) }
              ],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        }
        this.$set(this.fileStoreInfo.fileStores[idx], 'fsChartOption', fsChartOption)
      })
    }
  }
}
</script>
