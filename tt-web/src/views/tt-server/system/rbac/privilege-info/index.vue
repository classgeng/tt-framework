<template>
  <div>
    <!-- 条件表单 -->
    <div class="query-form">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="权限名称">
          <el-input v-model="queryForm.permitName" placeholder="权限名称" clearable />
        </el-form-item>

        <el-form-item label="权限赋值">
          <el-input v-model="queryForm.permitValue" placeholder="权限赋值" clearable />
        </el-form-item>

        <el-form-item label="创建时间">
          <el-date-picker
            v-model="createTimeRange"
            value-format="yyyy-MM-dd hh:mm:ss"
            type="datetimerange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="createTimeRangeChange"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="small" icon="el-icon-search" @click="querySubmit">查询</el-button>
          <el-button type="default" size="small" icon="el-icon-refresh" @click="resetSubmit">重置</el-button>
        </el-form-item>

        <el-form-item>
          <el-button v-permission="['permit-info@insert']" type="primary" size="small" icon="el-icon-circle-plus-outline" @click="systemMenuUpdateAction(null, null)">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div v-loading="tableLoadingFlag" class="query-table">
      <!-- 主查询列表组件 -->
      <el-table :data="tableData" stripe border :header-cell-style="$ui.table.headerCellStyle">
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column label="权限名称" prop="permitName" min-width="100px" sortable show-overflow-tooltip />
        <el-table-column label="权限赋值" prop="permitValue" min-width="100px" sortable show-overflow-tooltip />
        <el-table-column label="创建时间" prop="createTime" align="center" min-width="140px" sortable show-overflow-tooltip />
        <el-table-column label="更新时间" prop="updateTime" align="center" min-width="140px" sortable show-overflow-tooltip />
        <el-table-column label="创建人" prop="creator" align="center" min-width="120px" sortable show-overflow-tooltip />
        <el-table-column label="更新人" prop="updater" align="center" min-width="120px" sortable show-overflow-tooltip />
        <el-table-column fixed="right" label="操作" width="180px" align="center">
          <template slot-scope="{ $index, row }">
            <el-button v-permission="['permit-info@update']" type="default" size="small" icon="el-icon-edit-outline" @click="systemMenuUpdateAction($index, row)">更新</el-button>
            <el-button v-permission="['permit-info@delete']" type="default" size="small" icon="el-icon-circle-close" @click="systemMenuDeleteAction($index, row)">删除</el-button>
          </template>
        </el-table-column>
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

    <!-- 编辑抽屉 -->
    <el-drawer
      :title="editDrawerTitle"
      :size="$ui.drawer.width.w480"
      :append-to-body="true"
      :destroy-on-close="true"
      :modal="true"
      :visible.sync="editDrawerVisible"
      @close="closeAllDrawer"
    >
      <system-permit-update v-if="editDrawerVisible" :ref="editDrawerRef" :key-id="currentId" />
    </el-drawer>

  </div>
</template>

<script>
import { deleteSystemPermit, getSystemPermitPage } from '@/api/tt-server/system/rbac/privilege-info'
import SystemPermitUpdate from '@/views/tt-server/system/rbac/privilege-info/privilege-update'

export default {
  name: 'Index',
  components: { SystemPermitUpdate },
  data() {
    return {
      /* 查询条件 */
      queryForm: {
        permitName: '',
        permitValue: '',
        startCreateTime: '',
        endCreateTime: ''
      },
      createTimeRange: [],

      /* 翻页属性 */
      tableLoadingFlag: false,
      tableData: [],
      page: {
        size: 10,
        current: 1,
        total: 0
      },

      /* 抽屉 */
      editDrawerTitle: '',
      editDrawerVisible: false,
      editDrawerRef: 'editDrawerRefKey',
      currentId: ''
    }
  },
  created() {
    this.initialize()
  },
  methods: {
    createTimeRangeChange(range) {
      const isClear = !range
      this.queryForm.startCreateTime = isClear ? '' : range[0]
      this.queryForm.endCreateTime = isClear ? '' : range[1]
    },
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
      this.tableLoadingFlag = true
      this.queryForm.page = this.page
      console.log(this.queryForm)
      const { data: res } = await getSystemPermitPage(this.queryForm)
      this.tableData = res.records
      this.page.total = res.total
      this.tableLoadingFlag = false
    },
    querySubmit() {
      this.queryPage()
    },
    resetSubmit() {
      this.createTimeRange = ['', '']
      this.queryForm = {
        permitName: '',
        permitValue: '',
        startCreateTime: '',
        endCreateTime: ''
      }
    },
    systemMenuUpdateAction(idx, row) {
      const isUpdate = !!row
      this.currentId = isUpdate ? row.id : null
      this.editDrawerTitle = '系统权限编辑'
      this.editDrawerVisible = true
      console.log(idx, row)
    },
    systemMenuDeleteAction(idx, row) {
      console.log(idx, row)
      const confirmPromise = this.$confirm(`确定要删除系统权限[ ${row['permitName']} ]吗？`, '删除提示：', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
      confirmPromise.then(async() => {
        await deleteSystemPermit(row)
        this.$message.success(`系统权限[ ${row['permitName']} ]删除成功`)
        await this.queryPage()
      })
    },
    closeAllDrawer() {
      this.editDrawerVisible = false
    }
  }
}
</script>
