<template>
  <div>
    <!-- 条件表单 -->
    <div class="query-form">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="内容">
          <el-input v-model="queryForm.content" placeholder="内容" clearable />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="small" icon="el-icon-search" @click="querySubmit">查询</el-button>
          <el-button type="default" size="small" icon="el-icon-refresh" @click="resetSubmit">重置</el-button>
        </el-form-item>

        <el-form-item>
          <el-button v-permission="['data-auth-test@insert']" type="primary" size="small" icon="el-icon-circle-plus-outline" @click="dataAuthTestUpdateAction(null, null)">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div v-loading="tableLoadingFlag" class="query-table">
      <!-- 主查询列表组件 -->
      <el-table :data="tableData" stripe border :header-cell-style="$ui.table.headerCellStyle">
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column label="内容" prop="content" show-overflow-tooltip />
        <el-table-column label="创建人" prop="creator" show-overflow-tooltip />
        <el-table-column label="创建时间" prop="createTime" min-width="140px" align="center" show-overflow-tooltip />
        <el-table-column label="更新人" prop="updater" show-overflow-tooltip />
        <el-table-column label="更新时间" prop="updateTime" min-width="140px" align="center" show-overflow-tooltip />
        <el-table-column fixed="right" label="操作" align="center" width="180px">
          <template slot-scope="{ $index, row }">
            <el-button v-permission="['data-auth-test@update']" type="default" size="small" icon="el-icon-edit-outline" @click="dataAuthTestUpdateAction($index, row)">更新</el-button>
            <el-button v-permission="['data-auth-test@delete']" type="default" size="small" icon="el-icon-circle-close" @click="dataAuthTestDeleteAction($index, row)">删除</el-button>
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
      <data-auth-test-update v-if="editDrawerVisible" :ref="editDrawerRef" :key-id="currentId" />
    </el-drawer>

  </div>
</template>

<script>
import { deleteDataAuthTest, getDataAuthTestPage } from '@/api/tt-server/experiment/data-auth-test'
import DataAuthTestUpdate from '@/views/tt-server/experiment/data-auth-test/data-auth-test-update'

export default {
  name: 'Index',
  components: { DataAuthTestUpdate },
  data() {
    return {
      /* 查询条件 */
      queryForm: {
        content: ''
      },

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
      const { data: res } = await getDataAuthTestPage(this.queryForm)
      this.tableData = res.records
      this.page.total = res.total
      this.tableLoadingFlag = false
    },
    querySubmit() {
      this.queryPage()
    },
    resetSubmit() {
      this.queryForm = {
        content: ''
      }
    },
    dataAuthTestUpdateAction(idx, row) {
      const isUpdate = !!row
      this.currentId = isUpdate ? row.id : null
      this.editDrawerTitle = '数据授权测试编辑'
      this.editDrawerVisible = true
      console.log(idx, row)
    },
    dataAuthTestDeleteAction(idx, row) {
      console.log(idx, row)
      const confirmPromise = this.$confirm(`确定要删除数据授权测试[${row['paramName']}]吗？`, '删除提示：', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
      confirmPromise.then(async() => {
        await deleteDataAuthTest(row)
        this.$message.success(`数据授权测试[${row['paramName']}]删除成功`)
        await this.queryPage()
      })
    },
    closeAllDrawer() {
      this.editDrawerVisible = false
    }
  }
}
</script>
