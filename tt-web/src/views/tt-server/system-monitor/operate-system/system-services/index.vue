<template>
  <div style="padding: 15px">
    <!-- 条件表单 -->
    <div class="query-form">
      <el-form :inline="true" :model="queryForm" size="small">

        <el-form-item label="服务名称">
          <el-input v-model="queryForm.name" placeholder="服务名称" clearable />
        </el-form-item>

        <el-form-item label="服务状态">
          <el-select ref="select" v-model="queryForm.state" clearable placeholder="请选择">
            <el-option v-for="item in stateList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="small" icon="el-icon-search" @click="querySubmit">查询</el-button>
          <el-button type="default" size="small" icon="el-icon-refresh" @click="resetSubmit">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div v-loading="loadingFlag" class="query-table">
      <!-- 主查询列表组件 -->
      <el-table :data="tableData" stripe border :header-cell-style="$ui.table.headerCellStyle">
        <el-table-column type="index" width="50" label="序号" align="center" />
        <el-table-column prop="name" label="服务名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="pid" label="进程号" min-width="180" show-overflow-tooltip />
        <el-table-column prop="state" label="进程状态" min-width="180" show-overflow-tooltip />
      </el-table>

      <!-- 翻页组件 -->
      <el-pagination
        background
        layout="->, total, sizes, prev, pager, next, jumper"
        :current-page="page.current"
        :page-sizes="[10, 15, 20, 25, 30, 35, 40, 50, 100]"
        :page-size="page.size"
        :total="page.total"
        @size-change="pageSizeChange"
        @current-change="pageCurrentChange"
      />
    </div>

  </div>
</template>

<script>
import { getSystemServicePage } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      loadingFlag: false,
      sysServices: [],
      /* 查询条件 */
      queryForm: {
        name: '',
        state: ''
      },
      page: {
        size: 10,
        current: 1,
        total: 0
      },
      tableData: [],
      stateList: [
        { label: '运行中', value: 'RUNNING' },
        { label: '已停止', value: 'STOPPED' },
        { label: '其他', value: 'OTHER' }
      ]
    }
  },
  created() {
    this.initialize()
  },
  methods: {
    initialize() {
      this.queryPage()
    },
    pageSizeChange(val) {
      console.log(val)
      this.page.size = val
      this.queryPage()
    },
    pageCurrentChange(val) {
      console.log(val)
      this.page.current = val
      this.queryPage()
    },
    async queryPage() {
      this.loadingFlag = true
      this.queryForm.page = this.page
      console.log(this.queryForm)
      const { data: res } = await getSystemServicePage(this.queryForm)
      this.tableData = res.records
      this.page.total = res.total
      this.loadingFlag = false
    },
    querySubmit() {
      this.queryPage()
    },
    resetSubmit() {
      this.queryForm = {
        handlerMethodName: '',
        handlerPackage: '',
        handlerController: '',
        apiPath: ''
      }
    }
  }
}
</script>
