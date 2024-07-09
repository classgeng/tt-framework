<template>
  <div>
    <!-- 条件表单 -->
    <div class="query-form">
      <el-form :inline="true" :model="queryForm" size="small">

        <el-form-item label="处理器方法名">
          <el-input v-model="queryForm.handlerMethodName" placeholder="处理器方法名" clearable />
        </el-form-item>

        <el-form-item label="处理器方法所在包">
          <el-input v-model="queryForm.handlerPackage" placeholder="处理器方法所在包" clearable />
        </el-form-item>

        <el-form-item label="处理器方法所在类">
          <el-input v-model="queryForm.handlerController" placeholder="处理器方法所在类" clearable />
        </el-form-item>

        <el-form-item label="处理器接口地址">
          <el-input v-model="queryForm.apiPath" placeholder="处理器接口地址" clearable />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="small" icon="el-icon-search" @click="querySubmit">查询</el-button>
          <el-button type="default" size="small" icon="el-icon-refresh" @click="resetSubmit">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div v-loading="tableLoadingFlag" class="query-table">
      <!-- 主查询列表组件 -->
      <el-table :data="tableData" stripe border :header-cell-style="$ui.table.headerCellStyle">
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column label="处理器方法名" prop="handlerMethodName" align="left" show-overflow-tooltip />
        <el-table-column label="处理器方法所在包" prop="handlerPackage" align="left" min-width="160px" show-overflow-tooltip />
        <el-table-column label="处理器方法所在类" prop="handlerController" align="left" min-width="90px" show-overflow-tooltip />
        <el-table-column label="处理器接口地址" prop="apiPath" align="left" show-overflow-tooltip />
        <el-table-column label="处理器请求方式" prop="methodType" align="left" show-overflow-tooltip />
        <el-table-column fixed="right" label="操作" align="center" width="100px">
          <template slot-scope="{ $index, row }">
            <el-button v-permission="['system-api@query']" type="default" size="small" icon="el-icon-document" @click="openDetailDrawer($index, row)">详情</el-button>
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

    <!-- 详情抽屉 -->
    <el-drawer
      :title="detailDrawerTitle"
      :size="$ui.drawer.width.w480"
      :append-to-body="true"
      :destroy-on-close="true"
      :modal="false"
      :visible.sync="detailDrawerVisible"
      @close="closeDetailDrawer"
    >
      <system-api-detail v-if="detailDrawerVisible" :ref="detailDrawerRef" :detail="currentRow" />
    </el-drawer>
  </div>
</template>

<script>
import { getSystemApiPage } from '@/api/tt-server/system/common/system-api'
import SystemApiDetail from '@/views/tt-server/system/common/system-api/system-api-detail'

export default {
  name: 'Index',
  components: { SystemApiDetail },
  data() {
    return {
      /* 查询条件 */
      queryForm: {
        handlerMethodName: '',
        handlerPackage: '',
        handlerController: '',
        apiPath: ''
      },
      busTypeList: [],
      requestMethodList: [
        { label: '全部', value: '' },
        { label: 'GET', value: 'GET' },
        { label: 'POST', value: 'POST' },
        { label: 'PUT', value: 'PUT' },
        { label: 'DELETE', value: 'DELETE' }
      ],
      statusList: [
        { label: '全部', value: '' },
        { label: '成功', value: '成功' },
        { label: '失败', value: '失败' }
      ],
      /* 翻页属性 */
      tableLoadingFlag: false,
      tableData: [],
      page: {
        size: 10,
        current: 1,
        total: 0
      },

      /* 详情抽屉 */
      detailDrawerTitle: '系统日志详情',
      detailDrawerVisible: false,
      detailDrawerRef: 'detailDrawerRefKey',
      currentRow: null
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
      const { data: res } = await getSystemApiPage(this.queryForm)
      this.tableData = res.records
      this.page.total = res.total
      this.tableLoadingFlag = false
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
    },
    openDetailDrawer(idx, row) {
      this.currentRow = row
      this.detailDrawerVisible = true
    },
    closeDetailDrawer() {
      this.detailDrawerVisible = false
    }
  }
}
</script>
