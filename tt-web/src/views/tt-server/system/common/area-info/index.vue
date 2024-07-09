<template>
  <div>

    <el-row :gutter="10">
      <el-col :span="4">
        <div v-loading="treeLoadingFlag" class="tree-panel">
          <el-tree node-key="id" :data="treeData" :props="treeParam" @node-click="handleNodeClick" />
        </div>
      </el-col>

      <el-col :span="20">
        <!-- 条件表单 -->
        <div class="query-form">
          <el-form :inline="true" :model="queryForm" size="small">
            <el-form-item label="区域名称">
              <el-input v-model="queryForm.areaName" placeholder="区域名称" clearable />
            </el-form-item>

            <el-form-item label="区域层级">
              <el-select v-model="queryForm.areaLevel" placeholder="区域层级" clearable>
                <el-option v-for="(item, idx) in areaLevelList" :key="`area${idx}`" :label="item.label" :value="item.value" />
              </el-select>
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
            <el-table-column label="区域名称" prop="areaName" show-overflow-tooltip />
            <el-table-column label="区域层级" prop="areaLevel" show-overflow-tooltip />
            <el-table-column label="上级主键" prop="parentId" show-overflow-tooltip />
            <el-table-column label="区域路径" prop="areaPath" show-overflow-tooltip />
            <el-table-column label="连接地址" prop="areaUrl" show-overflow-tooltip />
            <el-table-column label="创建时间" prop="createTime" show-overflow-tooltip />
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
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { getAreaPage, getAreaTree } from '@/api/tt-server/system/common/area-info'

export default {
  name: 'Index',
  data() {
    return {
      /* 查询条件 */
      queryForm: {
        areaName: '',
        areaLevel: ''
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
      /* 树组件属性 */
      treeLoadingFlag: false,
      treeData: [],
      treeParam: {
        label: 'areaName'
      }
    }
  },
  created() {
    this.initialize()
  },
  methods: {
    initialize() {
      this.queryAreaTree()
      this.queryAreaPage()
    },
    pageSizeChange(val) {
      console.log(val)
      this.page.size = val
      this.queryAreaPage()
    },
    pageCurrentChange(val) {
      console.log(val)
      this.page.current = val
      this.queryAreaPage()
    },
    async queryAreaPage() {
      this.tableLoadingFlag = true
      this.queryForm.page = this.page
      console.log(this.queryForm)
      const { data: res } = await getAreaPage(this.queryForm)
      this.tableData = res.records
      this.page.total = res.total
      this.tableLoadingFlag = false
    },
    querySubmit() {
      this.queryAreaPage()
    },
    resetSubmit() {
      this.queryForm = {
        areaName: '',
        areaLevel: ''
      }
    },
    handleNodeClick(val) {
      console.log(val)
      this.queryForm.id = val.id
      this.queryAreaPage()
    },
    async queryAreaTree() {
      this.treeLoadingFlag = true
      const { data: res } = await getAreaTree()
      console.log(res)
      this.treeData = res
      this.treeLoadingFlag = false
    }
  }
}
</script>
