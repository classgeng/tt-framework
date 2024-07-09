<template>
  <div style="padding: 0 !important;">
    <pre v-loading="loadingFlag" v-highlightjs>
      <code class="sql" v-text="createSQL" />
    </pre>

    <div class="dialog-bottom-bar">
      <el-button v-clipboard:copy="createSQL" v-clipboard:success="clipboardSuccess" type="primary" size="mini" icon="el-icon-document">复制</el-button>
      <el-button type="default" size="mini" icon="el-icon-close" @click="closeDialog">取消</el-button>
    </div>
  </div>
</template>

<script>
import { getTableSchema } from '@/api/tt-server/tool-lib/mysql-visualize'
import clipboard from '@/directive/clipboard' // use clipboard by v-directive

export default {
  name: 'CreateSqlView',
  directives: { clipboard },
  props: {
    param: {
      type: Object,
      required: true,
      default: () => {}
    }
  },
  data() {
    return {
      createSQL: ''
    }
  },
  async created() {
    this.loadingFlag = true
    const { data: res } = await getTableSchema(this.param['dbConnId'], this.param['schemaName'], this.param['tableName'])
    console.log(res)
    this.createSQL = res['create table']
    this.loadingFlag = false
  },
  methods: {
    clipboardSuccess() {
      this.$message.success('复制成功')
    },
    closeDialog() {
      this.$parent.$parent['closeAllDrawer']()
    }
  }
}
</script>

<style scoped>
  div.el-dialog__body {
    padding-top: 0 !important;
  }
  pre {
    margin: 0 auto;
    padding: 0 !important;
  }
  code {
    border-radius: 5px;
  }
</style>
