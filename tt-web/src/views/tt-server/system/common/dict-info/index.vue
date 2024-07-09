<template>
  <div>
    <!-- 条件表单 -->
    <div class="query-form">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="字典名称">
          <el-input v-model="queryForm.dictName" placeholder="字典编号/字典名称" clearable />
        </el-form-item>

        <el-form-item label="字典类别">
          <el-input v-model="queryForm.dictCateName" placeholder="字典类别/字典列别名称" clearable />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="small" icon="el-icon-search" @click="querySubmit">查询</el-button>
          <el-button type="default" size="small" icon="el-icon-refresh" @click="resetSubmit">重置</el-button>
        </el-form-item>

        <el-form-item>
          <el-button v-permission="['dict-info@insert']" type="primary" size="small" icon="el-icon-circle-plus-outline" @click="dictUpdateAction(null, null)">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div v-loading="tableLoadingFlag" class="query-table">
      <!-- 主查询列表组件 -->
      <el-table :data="tableData" stripe border :header-cell-style="$ui.table.headerCellStyle">
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column label="字典编号" prop="dictCode" align="center" show-overflow-tooltip />
        <el-table-column label="字典名称" prop="dictName" show-overflow-tooltip />
        <el-table-column label="字典类别" prop="dictCategory" align="center" show-overflow-tooltip />
        <el-table-column label="字典列别名称" prop="dictCateName" show-overflow-tooltip />
        <el-table-column fixed="right" label="操作" width="180px" align="center">
          <template slot-scope="{ $index, row }">
            <el-button v-permission="['dict-info@update']" type="default" size="small" icon="el-icon-edit-outline" @click="dictUpdateAction($index, row)">更新</el-button>
            <el-button v-permission="['dict-info@delete']" type="default" size="small" icon="el-icon-circle-close" @click="dictDeleteAction($index, row)">删除</el-button>
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
      @close="closeEditDrawer"
    >
      <dict-update v-if="editDrawerVisible" :ref="editDrawerRef" :key-id="currentId" />
    </el-drawer>

  </div>
</template>

<script>
import { deleteDict, getDictPage } from '@/api/tt-server/system/common/dict-info'
import DictUpdate from '@/views/tt-server/system/common/dict-info/dict-update'

export default {
  name: 'Index',
  components: { DictUpdate },
  data() {
    return {
      /* 查询条件 */
      queryForm: {
        dictName: '',
        dictCateName: ''
      },
      areaLevelList: [
        { label: '全部', value: '' },
        { label: '一级', value: '1' },
        { label: '二级', value: '2' },
        { label: '三级', value: '3' }
      ],

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
      const { data: res } = await getDictPage(this.queryForm)
      this.tableData = res.records
      this.page.total = res.total
      this.tableLoadingFlag = false
    },
    querySubmit() {
      this.queryPage()
    },
    resetSubmit() {
      this.queryForm = {
        dictName: '',
        dictCateName: ''
      }
    },
    dictUpdateAction(idx, row) {
      const isUpdate = !!row
      this.currentId = isUpdate ? row.id : null
      this.editDrawerTitle = '字典编辑'
      this.editDrawerVisible = true
      console.log(idx, row)
    },
    dictDeleteAction(idx, row) {
      console.log(idx, row)
      const confirmPromise = this.$confirm(`确定要删除字典[${row['dictCode']}]吗？`, '删除提示：', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
      confirmPromise.then(async() => {
        await deleteDict(row)
        this.$message.success(`字典[${row['dictCode']}]删除成功`)
        await this.queryPage()
      })
    },
    closeEditDrawer() {
      this.editDrawerVisible = false
    }
  }
}
</script>
