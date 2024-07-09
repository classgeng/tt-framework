<template>
  <div v-loading="loadingFlag" style="padding: 15px">
    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col :span="$ui.layout.span.two" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>计算机系统</span>
          </div>
          <el-descriptions title="" border :columns="3">
            <el-descriptions-item label="系统信息" :span="3">{{ osInfo.osName }}</el-descriptions-item>
            <el-descriptions-item label="启动时间" :span="3">{{ osInfo.booted }}</el-descriptions-item>
            <el-descriptions-item label="序列号" :span="3">{{ osInfo.computerSystem['serialNumber'] }}</el-descriptions-item>
            <el-descriptions-item label="硬件UUID" :span="3">{{ osInfo.computerSystem['hardwareUUID'] }}</el-descriptions-item>
            <el-descriptions-item label="型号" :span="3">{{ osInfo.computerSystem['model'] }}</el-descriptions-item>
            <el-descriptions-item label="厂商" :span="3"> {{ osInfo.computerSystem['manufacturer'] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.two" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>系统会话列表</span>
          </div>

          <el-table border :data="osInfo.sessionList" style="width: 100% ">
            <el-table-column type="index" width="50" label="序号" align="center" />
            <el-table-column prop="userName" label="用户名" min-width="170" show-overflow-tooltip />
            <el-table-column prop="terminalDevice" label="终端设备" min-width="100" show-overflow-tooltip />
            <el-table-column prop="loginTime" label="登录时间" min-width="140" show-overflow-tooltip>
              <template slot-scope="{ row }">
                {{ $moment(row['loginTime']).format('YYYY-MM-DD HH:mm:ss') }}
              </template>
            </el-table-column>
            <el-table-column prop="host" label="主机" min-width="140" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.two" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>主板信息</span>
          </div>
          <el-descriptions title="" border :columns="3">
            <el-descriptions-item label="厂商" :span="3">{{ osInfo.baseboard['manufacturer'] }}</el-descriptions-item>
            <el-descriptions-item label="型号" :span="3">{{ osInfo.baseboard['model'] }}</el-descriptions-item>
            <el-descriptions-item label="序列号" :span="3">{{ osInfo.baseboard['serialNumber'] }}</el-descriptions-item>
            <el-descriptions-item label="版本" :span="3"> {{ osInfo.baseboard['version'] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.two" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>固件信息</span>
          </div>
          <el-descriptions title="" border :columns="3">
            <el-descriptions-item label="名称" :span="3">{{ osInfo.firmware['name'] }}</el-descriptions-item>
            <el-descriptions-item label="厂商" :span="3">{{ osInfo.firmware['manufacturer'] }}</el-descriptions-item>
            <el-descriptions-item label="版本" :span="3">{{ osInfo.firmware['version'] }}</el-descriptions-item>
            <el-descriptions-item label="描述" :span="3">{{ osInfo.firmware['description'] }}</el-descriptions-item>
            <el-descriptions-item label="发行日期" :span="3">{{ osInfo.firmware['releaseDate'] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.two" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>应用系统信息</span>
          </div>

          <el-descriptions title="" :column="2" :size="size" border>
            <el-descriptions-item>
              <template slot="label">系统名称</template>
              {{ serverSysInfo['osName'] }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">系统版本</template>
              {{ serverSysInfo['osVersion'] }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">系统架构</template>
              {{ serverSysInfo['osArch'] }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">登录用户</template>
              {{ serverSysInfo['userName'] }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">用户语言</template>
              {{ serverSysInfo['userLanguage'] }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">用户时区</template>
              {{ serverSysInfo['userTimezone'] }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">用户根目录</template>
              {{ serverSysInfo['userHome'] }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">用户变量</template>
              <el-button plain size="mini" @click="openPathDialog(serverSysInfo['userVariant'], '用户变量')">点击查看</el-button>
            </el-descriptions-item>

            <el-descriptions-item :span="2">
              <template slot="label">应用所在目录</template>
              {{ serverSysInfo['userDir'] }}
            </el-descriptions-item>

            <el-descriptions-item :span="2">
              <template slot="label">应用启动时间</template>
              {{ serverSysInfo['startTime'] }}
            </el-descriptions-item>

            <el-descriptions-item :span="2">
              <template slot="label">应用运行时长</template>
              {{ durationFormat(serverSysInfo['duration']['sec'] * 1000) }}
            </el-descriptions-item>

            <el-descriptions-item :span="2">
              <template slot="label">应用启动参数</template>
              <el-button plain size="mini" @click="openPathDialog(serverSysInfo['bootParam'], '启动参数')">点击查看</el-button>
            </el-descriptions-item>

            <el-descriptions-item :span="1">
              <template slot="label">系统环境变量</template>
              <el-button plain size="mini" @click="openSysVarDialog(serverSysInfo['sysEnv'])">点击查看</el-button>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :span="$ui.layout.span.two" style="margin-bottom: 15px">
        <el-card>
          <div slot="header">
            <span>JVM虚拟机信息</span>
          </div>

          <el-descriptions title="" :column="2" :size="size" border direction="vertical">
            <el-descriptions-item>
              <template slot="label">Java版本</template>
              {{ jvmInfo.version }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">Class版本</template>
              {{ jvmInfo.classVersion }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">Java规格名称</template>
              {{ jvmInfo.javaSpecificationName }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">Java规格版本</template>
              {{ jvmInfo.javaSpecificationVersion }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">Jvm名称</template>
              {{ jvmInfo.jvmName }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">Jvm版本</template>
              {{ jvmInfo.jvmVersion }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">Jvm模式</template>
              {{ jvmInfo.jvmMode }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">Jvm规格名称</template>
              {{ jvmInfo.jvmSpecificationName }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">Jvm规格版本</template>
              {{ jvmInfo.jvmSpecificationVersion }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">Jvm规格供应商</template>
              {{ jvmInfo.jvmSpecificationVendor }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">Jvm供应商</template>
              {{ jvmInfo.jvmVendor }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">Jvm供应商地址</template>
              {{ jvmInfo.jvmVendorUrl }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">运行时名称</template>
              {{ jvmInfo.runtimeName }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">运行时版本</template>
              {{ jvmInfo.runtimeVersion }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">IO临时目录</template>
              {{ jvmInfo.ioTmpDir }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">执行命令</template>
              {{ jvmInfo.execCommand }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">编译器名称</template>
              {{ jvmInfo.compiler }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">数据模型架构</template>
              {{ jvmInfo.archDataModel }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">JavaHome路径</template>
              {{ jvmInfo.home }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">JreBin路径</template>
              {{ jvmInfo.jreBinPath }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">JavaLib路径</template>
              <el-button plain size="mini" @click="openPathDialog(jvmInfo['libPath'], 'JavaLib路径')">点击查看</el-button>
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">Class路径</template>
              <el-button plain size="mini" @click="openPathDialog(jvmInfo['classPath'], 'Class路径')">点击查看</el-button>
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">BootClass路径</template>
              <el-button plain size="mini" @click="openPathDialog(jvmInfo['bootClassPath'], 'BootClass路径')">点击查看</el-button>
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">扩展路径</template>
              <el-button plain size="mini" @click="openPathDialog(jvmInfo['extendsDir'], '扩展路径')">点击查看</el-button>
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">启动器</template>
              {{ jvmInfo.launcher }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <!-- 路径列表抽屉 -->
    <el-drawer
      :title="pathDrawerTitle"
      :size="$ui.drawer.width.w960"
      :append-to-body="true"
      :destroy-on-close="true"
      :modal="true"
      :visible.sync="pathDrawerVisible"
      @close="closeAllDrawer"
    >
      <path-list-view :path-list="pathList" />
    </el-drawer>

    <!-- 系统变量弹窗 -->
    <el-dialog
      :title="systemVariantTitle"
      :visible.sync="systemVariantVisible"
      width="1480px"
      :append-to-body="true"
      :modal="true"
      @close="closeAllDrawer"
    >
      <system-variant-view :sys-var="systemVariant" />
    </el-dialog>

  </div>
</template>

<script>
import { getJvmInfo, getOperateSystemInfo, getServerSystemInfo } from '@/api/tt-server/sys-monitor'
import PathListView from '@/views/tt-server/system-monitor/operate-system/os-info/path-list-view'
import SystemVariantView from '@/views/tt-server/system-monitor/operate-system/os-info/system-variant-view'

export default {
  name: 'Index',
  components: { SystemVariantView, PathListView },
  data() {
    return {
      loadingFlag: false,
      osInfo: {
        booted: '',
        osName: '',
        sessionList: [],
        baseboard: {
          manufacturer: '',
          model: '',
          serialNumber: '',
          version: ''
        },
        firmware: {
          description: '',
          manufacturer: '',
          name: '',
          releaseDate: '',
          version: ''
        },
        computerSystem: {}
      },
      serverSysInfo: {},
      jvmInfo: {},

      /* 路径列表变量 */
      pathList: [],
      pathDrawerTitle: '',
      pathDrawerVisible: false,

      /* 系统变量弹窗 */
      systemVariant: {},
      systemVariantTitle: '',
      systemVariantVisible: false,

      size: 'medium'
    }
  },
  async created() {
    this.loadingFlag = true
    await this.initialSystemInfo()
    await this.initialServerSystemInfo()
    await this.initialJvmInfo()
    this.loadingFlag = false
  },
  methods: {
    async initialSystemInfo() {
      const { data: osInfo } = await getOperateSystemInfo()
      console.log(osInfo)
      this.osInfo = osInfo
    },
    async initialServerSystemInfo() {
      const { data: serverSysInfo } = await getServerSystemInfo()
      console.log(serverSysInfo)
      this.serverSysInfo = serverSysInfo
    },
    async initialJvmInfo() {
      const { data: jvmInfo } = await getJvmInfo()
      console.log(jvmInfo)
      this.jvmInfo = jvmInfo
    },
    openPathDialog(pathList, pathTitle) {
      this.pathList = pathList.map(el => { return { path: el } })
      this.pathDrawerTitle = `${pathTitle}的列表`
      this.pathDrawerVisible = true
    },
    openSysVarDialog(sysVar) {
      this.systemVariant = sysVar
      this.systemVariantTitle = `系统变量`
      this.systemVariantVisible = true
    },
    closeAllDrawer() {
      this.pathDrawerVisible = false
      this.systemVariantVisible = false
    },
    durationFormat(milliSeconds) {
      if (milliSeconds < 0) milliSeconds = -milliSeconds
      const timeObj = {
        '天': Math.floor(milliSeconds / 86400000),
        '小时': Math.floor(milliSeconds / 3600000) % 24,
        '分钟': Math.floor(milliSeconds / 60000) % 60,
        '秒': Math.floor(milliSeconds / 1000) % 60
        /* milliSecond: Math.floor(milliSeconds / 1000) */
      }
      return Object.entries(timeObj).filter(val => val[1] !== 0).map(([key, val]) => `${val}${key}`).join('')
    }
  }
}
</script>
