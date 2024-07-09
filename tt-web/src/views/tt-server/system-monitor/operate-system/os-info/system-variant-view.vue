<template>
  <div>
    <el-descriptions title="" :column="4" direction="vertical" :size="size" border>
      <el-descriptions-item v-for="(value, key, idx) in sysVar" :key="`variant${idx}`">
        <template slot="label">{{ key }}</template>
        <el-button v-if="['PSModulePath', 'Path'].includes(key)" plain size="mini" @click="openPathDialog(value, key)">点击查看</el-button>
        <span v-else>{{ value }}</span>
      </el-descriptions-item>
    </el-descriptions>

    <!-- 路径列表抽屉 -->
    <el-drawer
      :title="pathDrawerTitle"
      :size="$ui.drawer.width.w960"
      :append-to-body="true"
      :destroy-on-close="true"
      :modal="true"
      :visible.sync="pathDrawerVisible"
      @close="pathDrawerVisible = false"
    >
      <path-list-view :path-list="pathList" />
    </el-drawer>
  </div>
</template>

<script>
import PathListView from '@/views/tt-server/system-monitor/operate-system/os-info/path-list-view'
export default {
  name: 'SystemVariantView',
  components: { PathListView },
  props: {
    sysVar: {
      type: Object,
      required: true,
      default: () => {}
    }
  },
  data() {
    return {
      /* 路径列表变量 */
      pathList: [],
      pathDrawerTitle: '',
      pathDrawerVisible: false,

      size: 'medium'
    }
  },
  methods: {
    openPathDialog(pathList, pathTitle) {
      this.pathList = pathList.map(el => { return { path: el } })
      this.pathDrawerTitle = `${pathTitle}的列表`
      this.pathDrawerVisible = true
    }
  }
}
</script>

