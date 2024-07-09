<template>
  <div>
    <!-- 条件表单 -->
    <div class="query-form">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="模块功能">
          <el-input v-model="queryForm.module" placeholder="模块功能" clearable />
        </el-form-item>

        <el-form-item label="IP地址">
          <el-input v-model="queryForm.ip" placeholder="IP地址" clearable />
        </el-form-item>

        <el-form-item label="操作人">
          <el-input v-model="queryForm.operator" placeholder="操作人" clearable />
        </el-form-item>

        <el-form-item label="业务类型">
          <el-select v-model="queryForm.businessType" placeholder="请选择" clearable>
            <el-option v-for="(item, idx) in busTypeList" :key="`busType${idx}`" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="请求方式">
          <el-select v-model="queryForm.requestMethod" placeholder="请选择" clearable>
            <el-option v-for="(item, idx) in requestMethodList" :key="`requestMethod${idx}`" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option v-for="(item, idx) in statusList" :key="`status${idx}`" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="small" icon="el-icon-search" @click="querySubmit">查询</el-button>
          <el-button type="default" size="small" icon="el-icon-refresh" @click="resetSubmit">重置</el-button>
          <el-button v-permission="['system-log@delete']" type="default" size="small" icon="el-icon-delete" @click="flushLogSubmit">清空日志</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div v-loading="tableLoadingFlag" class="query-table">
      <!-- 主查询列表组件 -->
      <el-table :data="tableData" stripe border :header-cell-style="$ui.table.headerCellStyle">
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column label="业务类型" prop="businessType" align="center" show-overflow-tooltip />
        <el-table-column label="模块功能" prop="module" min-width="120px" show-overflow-tooltip />
        <el-table-column label="请求方式" prop="requestMethod" align="center" show-overflow-tooltip />
        <el-table-column label="IP" prop="ip" align="center" show-overflow-tooltip />
        <el-table-column label="地址" prop="address" align="center" show-overflow-tooltip />
        <el-table-column label="状态" prop="status" align="center" show-overflow-tooltip />
        <el-table-column label="操作人" prop="operator" show-overflow-tooltip />
        <el-table-column label="创建时间" prop="createTime" min-width="120px" align="center" show-overflow-tooltip />
        <el-table-column fixed="right" label="操作" align="center" width="100px">
          <template slot-scope="{ $index, row }">
            <el-button type="default" size="small" icon="el-icon-document" @click="openDetailDrawer($index, row)">详情</el-button>
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
      <system-log-detail v-if="detailDrawerVisible" :ref="detailDrawerRef" :key-id="currentId" />
    </el-drawer>
  </div>
</template>

<script>
import { getSystemLogPage, getBusinessTypeList, flushSystemLog } from '@/api/tt-server/system/common/system-log'
import SystemLogDetail from '@/views/tt-server/system/common/system-log/system-log-detail'

export default {
  name: 'Index',
  components: { SystemLogDetail },
  data() {
    return {
      /* 查询条件 */
      queryForm: {
        businessType: '',
        requestMethod: '',
        status: '',
        module: '',
        ip: '',
        operator: ''
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
      currentId: null
    }
  },
  created() {
    this.initialize()
  },
  methods: {
    initialize() {
      this.queryPage()
      this.initBusinessTypeSelect()
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
      const { data: res } = await getSystemLogPage(this.queryForm)
      this.tableData = res.records
      this.page.total = res.total
      this.tableLoadingFlag = false
    },
    async initBusinessTypeSelect() {
      const { data: busTypeList } = await getBusinessTypeList()
      this.busTypeList = busTypeList.map(bt => { return { label: bt, value: bt } })
      this.busTypeList.unshift({ label: '全部', value: '' })
    },
    querySubmit() {
      this.queryPage()
    },
    resetSubmit() {
      this.queryForm = {
        businessType: '',
        requestMethod: '',
        status: '',
        module: '',
        ip: '',
        operator: ''
      }
    },
    openDetailDrawer(idx, row) {
      this.currentId = row.id
      this.detailDrawerVisible = true
    },
    closeDetailDrawer() {
      this.detailDrawerVisible = false
    },
    flushLogSubmit() {
      const confirmPromise = this.$confirm(`确定要清空系统日志吗？`, '警告提示：', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
      confirmPromise.then(async() => {
        await flushSystemLog()
        await this.queryPage()
        this.$message['success']('系统日志清空完毕')
      })
    }
  }
}
</script>
