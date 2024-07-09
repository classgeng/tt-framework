<template>
  <div style="padding: 15px">
    <el-table border height="90vh" :data="pathList" style="width: 100% ">
      <el-table-column type="index" width="50" label="序号" align="center" />
      <el-table-column prop="path" label="路径" min-width="170" show-overflow-tooltip />
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'PathListView',
  props: {
    pathList: {
      type: Array,
      required: true,
      default: () => []
    }
  },
  created() {
    console.log(this.pathList)
  }
}
</script>
