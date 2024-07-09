<template>
  <div style="padding: 15px">
    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col :span="$ui.layout.span.two">
        <el-calendar v-model="today">
          <!-- 这里使用的是 2.5 slot 语法，对于新项目请使用 2.6 slot 语法-->
          <template
            slot="dateCell"
            slot-scope="{date, data}"
          >
            {{ data.day.split('-').slice(1).join('-') }}{{ data.isSelected ? '✔' : '' }}
          </template>
        </el-calendar>
      </el-col>

      <el-col :span="$ui.layout.span.two">
        <light-echarts :option="timeChartOpt" :style-obj="timeChartStyle" />
      </el-col>
    </el-row>

  </div>
</template>

<script>
import LightEcharts from '@/components/LightEcharts'
export default {
  name: 'Index',
  components: { LightEcharts },
  data() {
    return {
      today: new Date(),
      timeChartInterval: null,
      timeChartStyle: {
        width: '100%',
        height: '630px'
      },
      timeChartOpt: {
        backgroundColor: '',
        title: {
          show: true,
          x: 'center',
          y: '62%',
          text: '', // 幸运值取代码置于值于此处
          // subtext: '幸运指数',
          textStyle: {
            fontSize: 26,
            fontWeight: 'normal',
            fontStyle: 'normal',
            color: '#4B68CD'
          }
        },
        tooltip: {
          show: true,
          formatter: '{a}{b}：{c}',
          backgroundColor: '#6eba44',
          borderColor: '#6eba44',
          borderWidth: '1px',
          textStyle: {
            color: 'white'
          }
        },
        series: [
          {
            name: '小时',
            type: 'gauge',
            min: 0,
            max: 12,
            startAngle: 90,
            endAngle: -269.9999,
            splitNumber: 12, // 刻度数量
            radius: '90%', // 图表尺寸
            animation: 0,
            pointer: { // 仪表盘指针
              length: '55%',
              width: 10
            },
            itemStyle: { // 仪表盘指针样式
              normal: {
                color: '#4b68cd',
                shadowColor: 'rgba(0, 0, 0, 0.5)',
                shadowBlur: 10,
                shadowOffsetX: 2,
                shadowOffsetY: 2
              }
            },
            axisLine: { // 仪表盘轴线样式
              show: 0,
              lineStyle: {
                color: [
                  [1, '#4158B0']
                ],
                width: 50,
                shadowColor: 'rgba(75,104,205,0.6)',
                shadowBlur: 12,
                shadowOffsetX: 3,
                shadowOffsetY: 3
              }
            },
            splitLine: { // 分割线样式
              length: 50,
              lineStyle: {
                width: 3,
                color: ['#ffffff']
              }
            },
            axisTick: { // 仪表盘刻度样式
              show: true,
              splitNumber: 5, // 分隔线之间分割的刻度数
              length: 15, // 刻度线长
              lineStyle: {
                color: ['#ffffff']
              }
            },
            /* axisTick: {
                show: true,
                lineStyle: {
                    color: "#6eba44",
                    width: 1
                },
                length: 10,
                splitNumber: 2
            },*/
            /* splitLine: {
                  show: true,
                  length: 20,
                  lineStyle: {
                      color: '#6eba44'
                  }
              },*/
            axisLabel: { // 刻度标签
              distance: 0,
              textStyle: {
                color: '#2D2D2D', /* '#4b68cd', */
                fontSize: '22',
                padding: 10
              },
              formatter: function(e) {
                switch (e + '') {
                  case '0':
                    return '12'
                  default:
                    return e
                }
              }
            },
            detail: { // 指针评价
              show: false
            },
            title: { // 仪表盘标题
              show: 0,
              textStyle: {
                fontSize: 30,
                fontWeight: 'bolder',
                fontStyle: 'normal',
                color: '#6eba44'
              },
              offsetCenter: [0, 15]
            },
            data: [{ name: '', value: 12 }]
          }, {
            name: '分钟',
            type: 'gauge',
            min: 0,
            max: 60,
            startAngle: 90,
            endAngle: -269.9999,
            splitNumber: 12, // 刻度数量
            radius: '90%', // 图表尺寸
            animation: 0,
            pointer: { // 仪表盘指针
              length: '83%',
              width: 6
            },
            itemStyle: { // 仪表盘指针样式
              normal: {
                color: '#EE6A50',
                shadowColor: 'rgba(0, 0, 0, 0.5)',
                shadowBlur: 10,
                shadowOffsetX: 2,
                shadowOffsetY: 2
              }
            },
            axisLine: { // 仪表盘轴线样式
              show: 0,
              lineStyle: {
                color: [
                  [1, '#4158B0']
                ],
                width: 50,
                shadowColor: 'rgba(75,104,205,0.6)',
                shadowBlur: 12,
                shadowOffsetX: 3,
                shadowOffsetY: 3
              }
            },
            splitLine: { // 分割线样式
              length: 50,
              lineStyle: {
                width: 3,
                color: ['#ffffff']
              }
            },
            axisTick: { // 仪表盘刻度样式
              show: true,
              splitNumber: 5, // 分隔线之间分割的刻度数
              length: 15, // 刻度线长
              lineStyle: {
                color: ['#ffffff']
              }
            },
            /* axisTick: {
                show: true,
                lineStyle: {
                    color: "#6eba44",
                    width: 1
                },
                length: 10,
                splitNumber: 2
            },*/
            /* splitLine: {
                  show: true,
                  length: 20,
                  lineStyle: {
                      color: '#6eba44'
                  }
              },*/
            axisLabel: { // 刻度标签
              show: 0,
              distance: 0,
              textStyle: {
                color: '#4B68CD',
                fontSize: '24',
                padding: 10
              },
              formatter: function(e) {
                switch (e + '') {
                  case '0':
                    return '12'
                  default:
                    return e
                }
              }
            },
            detail: { // 指针评价
              show: false
            },
            title: { // 仪表盘标题
              show: 0,
              textStyle: {
                fontSize: 30,
                fontWeight: 'bolder',
                fontStyle: 'normal',
                color: '#4B68CD'
              },
              offsetCenter: [0, 15]
            },
            data: [{
              name: '',
              value: 5
            }]
          }, {
            name: '秒',
            type: 'gauge',
            min: 0,
            max: 60,
            startAngle: 90,
            endAngle: -269.9999,
            splitNumber: 12, // 刻度数量
            radius: '90%', // 图表尺寸
            animation: 0,
            pointer: { // 仪表盘指针
              length: '97%',
              width: 4
            },
            itemStyle: { // 仪表盘指针样式
              normal: {
                color: '#36BF44',
                shadowColor: 'rgba(0, 0, 0, 0.5)',
                shadowBlur: 10,
                shadowOffsetX: 2,
                shadowOffsetY: 2
              }
            },
            axisLine: { // 仪表盘轴线样式
              show: 0,
              lineStyle: {
                color: [
                  [1, '#4158B0']
                ],
                width: 50,
                shadowColor: 'rgba(75,104,205,0.6)',
                shadowBlur: 12,
                shadowOffsetX: 3,
                shadowOffsetY: 3
              }
            },
            splitLine: { // 分割线样式
              length: 50,
              lineStyle: {
                width: 3,
                color: ['#ffffff']
              }
            },
            axisTick: { // 仪表盘刻度样式
              show: true,
              splitNumber: 5, // 分隔线之间分割的刻度数
              length: 15, // 刻度线长
              lineStyle: {
                color: ['#ffffff']
              }
            },
            /* axisTick: {
                show: true,
                lineStyle: {
                    color: "#6eba44",
                    width: 1
                },
                length: 10,
                splitNumber: 2
            },*/
            /* splitLine: {
                  show: true,
                  length: 20,
                  lineStyle: {
                      color: '#6eba44'
                  }
              },*/
            axisLabel: { // 刻度标签
              show: 0,
              distance: 0,
              textStyle: {
                color: '#4B68CD',
                fontSize: '24',
                padding: 10
              },
              formatter: function(e) {
                switch (e + '') {
                  case '0':
                    return '12'
                  default:
                    return e
                }
              }
            },
            detail: { // 指针评价
              show: false
            },
            title: { // 仪表盘标题
              show: 0,
              textStyle: {
                fontSize: 30,
                fontWeight: 'bolder',
                fontStyle: 'normal',
                color: '#4B68CD'
              },
              offsetCenter: [0, 15]
            },
            data: [{
              name: '',
              value: 10
            }]
          }
        ]
      }
    }
  },
  created() {

  },
  mounted() {
    this.startTimeChartInterval(1000)
  },
  beforeDestroy() {
    clearInterval(this.timeChartInterval)
    this.memoryChartsInterval = null
  },
  methods: {
    startTimeChartInterval(val) {
      clearInterval(this.timeChartInterval)
      this.timeChartInterval = setInterval(async() => {
        const date = new Date()
        const second = date.getSeconds()
        const minute = date.getMinutes() + second / 60
        const hour = (date.getHours() % 12) + minute / 60
        this.timeChartOpt.animationDurationUpdate = 300
        this.timeChartOpt = {
          series: [
            {
              name: 'hour',
              animation: hour !== 0,
              data: [{ value: hour }]
            },
            {
              name: 'minute',
              animation: minute !== 0,
              data: [{ value: minute }]
            },
            {
              animation: second !== 0,
              name: 'second',
              data: [{ value: second }]
            }
          ]
        }
      }, val)
    }
  }
}
</script>
