<template>
  <div :style="{ height: height, overflow: 'auto' }">

    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col :span="$ui.layout.span.one" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>设置项</span>
          </div>
          <el-form :inline="true">
            <el-form-item label="刷新间隔">
              <el-select v-model="intervalValue" placeholder="请选择" @change="startIntervalUpdate">
                <el-option v-for="(item, idx) in intervalList" :key="`intervalValue${idx}`" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>

            <el-form-item label="时长范围">
              <el-select v-model="rangeValue" placeholder="请选择">
                <el-option v-for="(item, idx) in rangeList" :key="`rangeValue${idx}`" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>

            <el-form-item label="">
              <el-button type="info" icon="el-icon-video-pause" plain @click="stopFresh">暂停</el-button>
              <el-button type="primary" icon="el-icon-video-play" plain @click="startFresh">开始</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col :span="$ui.layout.span.two" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>物理内存使用情况</span>
          </div>

          <el-col :span="8">
            <el-descriptions title="" border :columns="3">
              <el-descriptions-item :label-style="labelStyle" label="总大小" :span="3">{{ memoryInfo.total }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle" label="使用大小" :span="3">{{ memoryInfo['used'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle" label="剩余大小" :span="3">{{ memoryInfo['available'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle" label="使用率" :span="3">{{ memoryInfo['usageRate'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle" label="剩余率" :span="3">{{ memoryInfo['freeRate'] }}</el-descriptions-item>
            </el-descriptions>
          </el-col>

          <el-col :span="16">
            <light-echarts :option="memoryChartsOption" :style-obj="memoryChartsStyle" />
          </el-col>

          <el-col :span="$ui.layout.span.one" style="margin-bottom: 15px">
            <light-echarts :option="memoryChartsOption2" :style-obj="memoryChartsStyle" />
          </el-col>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.two" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>虚拟内存</span>
          </div>
          <el-col :span="8">
            <el-descriptions title="" border :columns="3">
              <el-descriptions-item :label-style="labelStyle" label="总大小" :span="3">{{ memoryInfo['virtualMemory']['virtualMax'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle" label="使用大小" :span="3">{{ memoryInfo['virtualMemory']['virtualInUse'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle" label="剩余大小" :span="3">{{ memoryInfo['virtualMemory']['virtualFree'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle" label="使用率" :span="3">{{ memoryInfo['virtualMemory']['virtualUsageRate'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle" label="剩余率" :span="3">{{ memoryInfo['virtualMemory']['virtualFreeRate'] }}</el-descriptions-item>
            </el-descriptions>
          </el-col>

          <el-col :span="16">
            <light-echarts :option="virtualMemoryChartsOption" :style-obj="memoryChartsStyle" />
          </el-col>

          <el-col :span="$ui.layout.span.one" style="margin-bottom: 15px">
            <light-echarts :option="virtualMemoryChartsOption2" :style-obj="memoryChartsStyle" />
          </el-col>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.two" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>交换部分</span>
          </div>
          <el-col :span="8">
            <el-descriptions title="" border :columns="3">
              <el-descriptions-item :label-style="labelStyle2" label="交换总大小" :span="3">{{ memoryInfo['virtualMemory']['swapTotal'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle2" label="交换使用大小" :span="3">{{ memoryInfo['virtualMemory']['swapUsed'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle2" label="交换剩余大小" :span="3">{{ memoryInfo['virtualMemory']['swapFree'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle2" label="交换使用率" :span="3">{{ memoryInfo['virtualMemory']['swapUsageRate'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle2" label="交换剩余率" :span="3">{{ memoryInfo['virtualMemory']['swapFreeRate'] }}</el-descriptions-item>
            </el-descriptions>
          </el-col>

          <el-col :span="16">
            <light-echarts :option="swapMemoryChartsOption" :style-obj="memoryChartsStyle" />
          </el-col>

          <el-col :span="$ui.layout.span.one" style="margin-bottom: 15px">
            <light-echarts :option="swapMemoryChartsOption2" :style-obj="memoryChartsStyle" />
          </el-col>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.two" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>JVM内存</span>
          </div>
          <el-col :span="8">
            <el-descriptions title="" border :columns="3">
              <!-- <el-descriptions-item :label-style="labelStyle" label="最大" :span="2">{{ jvmMemoryInfo['max'] }}</el-descriptions-item>-->
              <el-descriptions-item :label-style="labelStyle" label="总大小" :span="3">{{ jvmMemoryInfo['total'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle" label="使用大小" :span="3">{{ jvmMemoryInfo['used'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle" label="剩余大小" :span="3">{{ jvmMemoryInfo['free'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle" label="使用率" :span="3">{{ jvmMemoryInfo['usageRate'] }}</el-descriptions-item>
              <el-descriptions-item :label-style="labelStyle" label="剩余率" :span="3">{{ jvmMemoryInfo['freeRate'] }}</el-descriptions-item>
            </el-descriptions>
          </el-col>

          <el-col :span="16">
            <light-echarts :option="jvmMemoryChartsOption" :style-obj="memoryChartsStyle" />
          </el-col>

          <el-col :span="$ui.layout.span.one" style="margin-bottom: 15px">
            <light-echarts :option="jvmMemoryChartsOption2" :style-obj="memoryChartsStyle" />
          </el-col>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col :span="$ui.layout.span.one">
        <el-card>
          <div slot="header">
            <span>物理内存列表</span>
          </div>
          <el-table v-loading="loadingFlag" border :data="memoryInfo['physicalMemoryList']" style="width: 100% ">
            <el-table-column type="index" width="50" label="序号" align="center" />
            <el-table-column prop="manufacturer" label="厂商" width="120px" show-overflow-tooltip />
            <el-table-column prop="capacity" label="大小" width="100px" show-overflow-tooltip />
            <el-table-column prop="clockSpeed" label="频率" width="100px" show-overflow-tooltip />
            <el-table-column prop="memoryType" label="类型" width="100px" align="center" />
            <el-table-column prop="bankLabel" label="插槽位" width="100px" align="center" />
            <el-table-column prop="toString" label="完整打印" min-width="180" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getMemoryInfo, getJvmMemory } from '@/api/tt-server/sys-monitor'
import LightEcharts from '@/components/LightEcharts'

export default {
  name: 'Index',
  components: { LightEcharts },
  data() {
    return {
      loadingFlag: false,
      labelStyle: { width: '90px' },
      labelStyle2: { width: '110px' },
      height: `${document.documentElement.clientHeight - 130}px`,
      /* 设置时间间隔 */
      intervalValue: 3000,
      intervalList: [
        { label: '1秒', value: 1000 },
        { label: '2秒', value: 2000 },
        { label: '3秒', value: 3000 },
        { label: '4秒', value: 4000 },
        { label: '5秒', value: 5000 },
        { label: '10秒', value: 10000 }
      ],
      /* 设置时长范围 */
      rangeValue: 1000 * 30,
      rangeList: [
        { label: '10秒', value: 1000 * 10 },
        { label: '20秒', value: 1000 * 20 },
        { label: '30秒', value: 1000 * 30 },
        { label: '40秒', value: 1000 * 40 },
        { label: '50秒', value: 1000 * 50 },
        { label: '1分钟', value: 1000 * 60 }
      ],
      chartRange: 0,
      memoryInfo: {
        physicalMemoryList: [],
        virtualMemory: {
          srcVirtualMax: '',
          srcVirtualInUse: '',
          srcVirtualFree: '',
          virtualMax: '',
          virtualInUse: '',
          virtualFree: '',
          virtualUsageRate: '',
          virtualFreeRate: '',

          srcSwapTotal: '',
          srcSwapUsed: '',
          srcSwapFree: '',
          swapTotal: '',
          swapUsed: '',
          swapFree: '',
          swapUsageRate: '',
          swapFreeRate: ''
        }
      },
      jvmMemoryInfo: {
        srcTotal: '',
        srcUsed: '',
        srcFree: '',
        max: '',
        total: '',
        used: '',
        free: '',
        usageRate: '',
        freeRate: ''
      },
      memoryChartsStyle: {
        width: '100%',
        height: '250px'
      },
      memoryChartsInterval: null,
      /* 物理内存图参数 */
      memoryChartsOption: {
        title: {
          text: '物理内存',
          subtext: 'Physical Memory',
          left: 'center'
        },
        tooltip: { trigger: 'item' },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '物理内存使用情况',
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
            data: [],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      },
      memoryChartsOption2: {
        title: { text: '物理内存占用' },
        xAxis: {
          /* 是否留出边界 */
          boundaryGap: false,
          type: 'category',
          data: []
        },
        yAxis: { type: 'value', max: 0 },
        series: [
          {
            data: [],
            type: 'line',
            /* 面积样式设置 */
            areaStyle: {},
            /* 折线是否平滑 */
            smooth: true
          }
        ]
      },
      /* 虚拟内存图参数 */
      virtualMemoryChartsOption: {
        title: {
          text: '虚拟内存',
          subtext: 'Virtual Memory',
          left: 'center'
        },
        tooltip: { trigger: 'item' },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '虚拟内存使用情况',
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
            data: [],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      },
      virtualMemoryChartsOption2: {
        title: { text: '虚拟内存占用' },
        xAxis: {
          /* 是否留出边界 */
          boundaryGap: false,
          type: 'category',
          data: []
        },
        yAxis: { type: 'value', max: 0 },
        series: [
          {
            data: [],
            type: 'line',
            /* 面积样式设置 */
            areaStyle: {},
            /* 折线是否平滑 */
            smooth: true
          }
        ]
      },
      /* 交换内存图参数 */
      swapMemoryChartsOption: {
        title: {
          text: '交换内存',
          subtext: 'Swap Memory',
          left: 'center'
        },
        tooltip: { trigger: 'item' },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '交换内存使用情况',
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
            data: [],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      },
      swapMemoryChartsOption2: {
        title: { text: '交换内存占用' },
        xAxis: {
          /* 是否留出边界 */
          boundaryGap: false,
          type: 'category',
          data: []
        },
        yAxis: { type: 'value', max: 0 },
        series: [
          {
            data: [],
            type: 'line',
            /* 面积样式设置 */
            areaStyle: {},
            /* 折线是否平滑 */
            smooth: true
          }
        ]
      },
      /* JVM内存图参数 */
      jvmMemoryChartsOption: {
        title: {
          text: 'JVM内存',
          subtext: 'JVM Memory',
          left: 'center'
        },
        tooltip: { trigger: 'item' },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: 'JVM内存使用情况',
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
            data: [],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      },
      jvmMemoryChartsOption2: {
        title: { text: 'JVM内存占用' },
        xAxis: {
          /* 是否留出边界 */
          boundaryGap: false,
          type: 'category',
          data: []
        },
        yAxis: { type: 'value', max: 0 },
        series: [
          {
            data: [],
            type: 'line',
            /* 面积样式设置 */
            areaStyle: {},
            /* 折线是否平滑 */
            smooth: true
          }
        ]
      }
    }
  },
  async created() {
    this.loadingFlag = true
    await this.initialMemoryInfo()
    this.loadingFlag = false
  },
  mounted() {
    this.startIntervalUpdate(this.intervalValue)
  },
  beforeDestroy() {
    clearInterval(this.memoryChartsInterval)
    this.memoryChartsInterval = null
  },
  methods: {
    stopFresh() {
      clearInterval(this.memoryChartsInterval)
    },
    startFresh() {
      this.startIntervalUpdate(this.intervalValue)
    },
    startIntervalUpdate(val) {
      clearInterval(this.memoryChartsInterval)
      this.memoryChartsInterval = setInterval(async() => {
        await this.initialMemoryInfo()
      }, val)
    },
    async initialMemoryInfo() {
      const { data: memoryInfo } = await getMemoryInfo()
      this.memoryInfo = memoryInfo
      const { data: jvmMemoryInfo } = await getJvmMemory()
      this.jvmMemoryInfo = jvmMemoryInfo

      const inUseVal = (this.memoryInfo['srcUsed'] / 1024 / 1024).toFixed(2)
      const srcVirtualInUse = (this.memoryInfo['virtualMemory']['srcVirtualInUse'] / 1024 / 1024).toFixed(2)
      const srcSwapUsed = (this.memoryInfo['virtualMemory']['srcSwapUsed'] / 1024 / 1024).toFixed(2)
      const srcJvmUsed = (this.jvmMemoryInfo['srcUsed'] / 1024 / 1024).toFixed(2)
      /* 饼图数据设置 */
      this.memoryChartsOption.series[0].data = [
        { name: '已占用', value: (this.memoryInfo['srcUsed'] / 1024 / 1024 / 1024).toFixed(2) },
        { name: '还剩余', value: (this.memoryInfo['srcAvailable'] / 1024 / 1024 / 1024).toFixed(2) }
      ]
      this.virtualMemoryChartsOption.series[0].data = [
        { name: '已占用', value: (this.memoryInfo['virtualMemory']['srcVirtualInUse'] / 1024 / 1024 / 1024).toFixed(2) },
        { name: '还剩余', value: (this.memoryInfo['virtualMemory']['srcVirtualFree'] / 1024 / 1024 / 1024).toFixed(2) }
      ]
      this.swapMemoryChartsOption.series[0].data = [
        { name: '已占用', value: (this.memoryInfo['virtualMemory']['srcSwapUsed'] / 1024 / 1024 / 1024).toFixed(2) },
        { name: '还剩余', value: (this.memoryInfo['virtualMemory']['srcSwapFree'] / 1024 / 1024 / 1024).toFixed(2) }
      ]
      this.jvmMemoryChartsOption.series[0].data = [
        { name: '已占用', value: (this.jvmMemoryInfo['srcUsed'] / 1024 / 1024 / 1024).toFixed(2) },
        { name: '还剩余', value: (this.jvmMemoryInfo['srcFree'] / 1024 / 1024 / 1024).toFixed(2) }
      ]
      /* ----------------------------- 折线图y轴最大值设置 ----------------------------- */
      this.chartRange = Math.trunc(this.rangeValue / this.intervalValue)
      console.log(this.chartRange)
      /* ----------------------------- 折线图数据设置 ----------------------------- */
      this.memoryChartsOption2.yAxis.max = Math.trunc(this.memoryInfo['srcTotal'] / 1024 / 1024)
      this.virtualMemoryChartsOption2.yAxis.max = Math.trunc(this.memoryInfo['virtualMemory']['srcVirtualMax'] / 1024 / 1024)
      this.swapMemoryChartsOption2.yAxis.max = Math.trunc(this.memoryInfo['virtualMemory']['srcSwapTotal'] / 1024 / 1024)
      this.jvmMemoryChartsOption2.yAxis.max = Math.trunc(this.jvmMemoryInfo['srcTotal'] / 1024 / 1024)
      if (this.memoryChartsOption2.xAxis.data.length === this.chartRange) {
        this.clearLastTimeData()
      } else if (this.memoryChartsOption2.xAxis.data.length > this.chartRange) {
        let diff = this.memoryChartsOption2.xAxis.data.length - this.chartRange
        while (diff !== 0) {
          this.clearLastTimeData()
          --diff
        }
      }
      const timeLine = this.$moment().format('HH:mm:ss')
      this.memoryChartsOption2.xAxis.data.push(timeLine)
      this.memoryChartsOption2.series[0].data.push(inUseVal)
      this.virtualMemoryChartsOption2.xAxis.data.push(timeLine)
      this.virtualMemoryChartsOption2.series[0].data.push(srcVirtualInUse)
      this.swapMemoryChartsOption2.xAxis.data.push(timeLine)
      this.swapMemoryChartsOption2.series[0].data.push(srcSwapUsed)
      this.jvmMemoryChartsOption2.xAxis.data.push(timeLine)
      this.jvmMemoryChartsOption2.series[0].data.push(srcJvmUsed)
    },
    clearLastTimeData() {
      this.memoryChartsOption2.series[0].data.shift()
      this.memoryChartsOption2.xAxis.data.shift()
      this.virtualMemoryChartsOption2.series[0].data.shift()
      this.virtualMemoryChartsOption2.xAxis.data.shift()
      this.swapMemoryChartsOption2.xAxis.data.shift()
      this.swapMemoryChartsOption2.series[0].data.shift()
      this.jvmMemoryChartsOption2.xAxis.data.shift()
      this.jvmMemoryChartsOption2.series[0].data.shift()
    }
  }
}
</script>
