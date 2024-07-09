<template>
  <div>
    <!-- 条件表单 -->
    <div class="query-form">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="应用编号">
          <el-input v-model="queryForm.appCode" placeholder="应用编号" clearable />
        </el-form-item>

        <el-form-item label="菜单名称">
          <el-input v-model="queryForm.menuName" placeholder="菜单名称" clearable />
        </el-form-item>

        <el-form-item label="菜单赋值">
          <el-input v-model="queryForm.menuValue" placeholder="菜单赋值" clearable />
        </el-form-item>

        <el-form-item label="菜单类型">
          <el-select v-model="queryForm.menuType" placeholder="请选择" clearable>
            <el-option v-for="(item, idx) in options.menuTypeList" :key="`menuType${idx}`" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="菜单层级">
          <el-select v-model="queryForm.menuLevel" placeholder="请选择" clearable>
            <el-option v-for="(item, idx) in options.menuLevelList" :key="`menuLevel${idx}`" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="是否展示">
          <el-select v-model="queryForm.isShow" placeholder="请选择" clearable>
            <el-option v-for="(item, idx) in options.isShowList" :key="`isShow${idx}`" :label="item.label" :value="item.value" />
          </el-select>
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
          <el-button v-permission="['menu-info@insert']" type="primary" size="small" icon="el-icon-circle-plus-outline" @click="systemMenuUpdateAction(null, null)">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div v-loading="tableLoadingFlag" class="query-table">
      <!-- 主查询列表组件 -->
      <el-table :data="tableData" stripe border :header-cell-style="$ui.table.headerCellStyle">
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column label="应用编号" prop="appCode" min-width="120px" show-overflow-tooltip />
        <el-table-column label="菜单名称" prop="menuName" min-width="120px" show-overflow-tooltip />
        <el-table-column label="菜单赋值" prop="menuValue" min-width="130px" show-overflow-tooltip />
        <el-table-column label="菜单层级" prop="menuLevel" min-width="80px" show-overflow-tooltip />
        <el-table-column label="菜单类型" prop="menuTypeName" min-width="80px" show-overflow-tooltip />
        <el-table-column label="展示排序" prop="menuSort" min-width="80px" show-overflow-tooltip />
        <el-table-column label="是否展示" prop="isShowName" min-width="80px" show-overflow-tooltip />
        <el-table-column label="创建时间" prop="createTime" align="center" min-width="160px" show-overflow-tooltip />
        <el-table-column fixed="right" label="操作" width="290px" align="center">
          <template slot-scope="{ $index, row }">
            <el-button v-permission="['menu-info@permit-allocate']" :disabled="isDirectory(row)" type="default" size="small" icon="el-icon-edit-outline" @click="systemPermitAllocateAction($index, row)">配置权限</el-button>
            <el-button v-permission="['menu-info@update']" type="default" size="small" icon="el-icon-edit-outline" @click="systemMenuUpdateAction($index, row)">更新</el-button>
            <el-button v-permission="['menu-info@delete']" type="default" size="small" icon="el-icon-circle-close" @click="systemMenuDeleteAction($index, row)">删除</el-button>
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
      <system-menu-update v-if="editDrawerVisible" :ref="editDrawerRef" :options="options" :key-id="currentId" />
    </el-drawer>

    <!-- 权限配置抽屉 -->
    <el-drawer
      :title="permitAllocateDrawerTitle"
      :size="$ui.drawer.width.w480"
      :append-to-body="true"
      :destroy-on-close="true"
      :modal="true"
      :visible.sync="permitAllocateVisible"
      @close="closeAllDrawer"
    >
      <menu-permit-allocate v-if="permitAllocateVisible" :ref="permitAllocateRef" :key-id="currentId" />
    </el-drawer>

  </div>
</template>

<script>
import { deleteSystemMenu, getSystemMenuPage } from '@/api/tt-server/system/rbac/menu-info'
import SystemMenuUpdate from '@/views/tt-server/system/rbac/menu-info/menu-update'
import MenuPermitAllocate from '@/views/tt-server/system/rbac/menu-info/menu-permit-allocate'

export default {
  name: 'Index',
  components: { MenuPermitAllocate, SystemMenuUpdate },
  data() {
    return {
      /* 查询条件 */
      queryForm: {
        menuName: '',
        menuValue: '',
        menuType: '',
        menuLevel: '',
        isShow: '',
        startCreateTime: '',
        endCreateTime: ''
      },
      options: {
        menuTypeList: [
          { label: '全部', value: '' },
          { label: '菜单', value: 'Function' },
          { label: '目录', value: 'Directory' }
        ],
        menuLevelList: [
          { label: '全部', value: '' },
          { label: '一级', value: '1' },
          { label: '二级', value: '2' },
          { label: '三级', value: '3' }
        ],
        isShowList: [
          { label: '全部', value: '' },
          { label: '是', value: true },
          { label: '否', value: false }
        ]
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

      /* 编辑抽屉 */
      editDrawerTitle: '',
      editDrawerVisible: false,
      editDrawerRef: 'editDrawerRefKey',

      /* 权限配置抽屉 */
      permitAllocateDrawerTitle: '',
      permitAllocateVisible: false,
      permitAllocateRef: 'permitAllocateRefKey',
      currentId: ''
    }
  },
  created() {
    this.initialize()
  },
  methods: {
    isDirectory(row) {
      return row.menuType === 'Directory'
    },
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
      const { data: res } = await getSystemMenuPage(this.queryForm)
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
        menuName: '',
        menuValue: '',
        menuType: '',
        menuLevel: '',
        isShow: '',
        startCreateTime: '',
        endCreateTime: ''
      }
    },
    systemMenuUpdateAction(idx, row) {
      const isUpdate = !!row
      this.currentId = isUpdate ? row.id : null
      this.editDrawerTitle = '系统菜单编辑'
      this.editDrawerVisible = true
      console.log(idx, row)
    },
    closeAllDrawer() {
      this.editDrawerVisible = false
      this.permitAllocateVisible = false
    },
    systemMenuDeleteAction(idx, row) {
      console.log(idx, row)
      const confirmPromise = this.$confirm(`确定要删除系统菜单[ ${row['menuName']} ] 吗？`, '删除提示：', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
      confirmPromise.then(async() => {
        await deleteSystemMenu(row)
        this.$message.success(`系统菜单[ ${row['menuName']} ]删除成功`)
        await this.queryPage()
      })
    },
    systemPermitAllocateAction(idx, row) {
      this.permitAllocateDrawerTitle = `权限配置：[ ${row.menuName} ]`
      this.currentId = row.id
      this.permitAllocateVisible = true
    }
  }
}
</script>
