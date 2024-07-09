<template>
  <div>
    <!-- 条件表单 -->
    <div class="query-form">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="角色名称">
          <el-input v-model="queryForm.roleName" placeholder="角色名称" clearable />
        </el-form-item>

        <el-form-item label="角色赋值">
          <el-input v-model="queryForm.roleValue" placeholder="角色赋值" clearable />
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
          <el-button v-permission="['role-info@insert']" type="primary" size="small" icon="el-icon-circle-plus-outline" @click="systemRoleUpdateAction(null, null)">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div v-loading="tableLoadingFlag" class="query-table">
      <!-- 主查询列表组件 -->
      <el-table :data="tableData" stripe border :header-cell-style="$ui.table.headerCellStyle">
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column label="角色名称" prop="roleName" show-overflow-tooltip />
        <el-table-column label="角色赋值" prop="roleValue" show-overflow-tooltip />
        <el-table-column label="创建人" prop="creator" show-overflow-tooltip />
        <el-table-column label="更新人" prop="updater" show-overflow-tooltip />
        <el-table-column label="创建时间" prop="createTime" align="center" min-width="140px" show-overflow-tooltip />
        <el-table-column label="更新时间" prop="updateTime" align="center" min-width="140px" show-overflow-tooltip />
        <el-table-column fixed="right" label="操作" width="410px" align="center">
          <template slot-scope="{ $index, row }">
            <el-button v-permission="['role-info@menu-assign']" type="default" size="small" icon="el-icon-copy-document" @click="menuAssignAction($index, row)">菜单授予</el-button>
            <el-button v-permission="['role-info@permit-assign']" type="default" size="small" icon="el-icon-connection" @click="permitAssignAction($index, row)">权限授予</el-button>
            <el-button v-permission="['role-info@update']" type="default" size="small" icon="el-icon-edit-outline" @click="systemRoleUpdateAction($index, row)">更新</el-button>
            <el-button v-permission="['role-info@delete']" type="default" size="small" icon="el-icon-circle-close" @click="systemRoleDeleteAction($index, row)">删除</el-button>
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
      <system-role-update v-if="editDrawerVisible" :ref="editDrawerRef" :key-id="currentId" />
    </el-drawer>

    <!-- 菜单授予抽屉 -->
    <el-drawer
      :title="menuAssignDrawerTitle"
      :size="$ui.drawer.width.w480"
      :append-to-body="true"
      :destroy-on-close="true"
      :modal="true"
      :visible.sync="menuAssignVisible"
      @close="closeAllDrawer"
    >
      <role-menu-assign v-if="menuAssignVisible" :ref="menuAssignDrawerRef" :key-id="currentId" />
    </el-drawer>

    <!-- 权限授予抽屉 -->
    <el-drawer
      :title="permitAssignDrawerTitle"
      :size="$ui.drawer.width.w640"
      :append-to-body="true"
      :destroy-on-close="true"
      :modal="true"
      :visible.sync="permitAssignVisible"
      @close="closeAllDrawer"
    >
      <role-permit-assign v-if="permitAssignVisible" :ref="permitAssignDrawerRef" :key-id="currentId" />
    </el-drawer>

  </div>
</template>

<script>
import { deleteSystemRole, getSystemRolePage } from '@/api/tt-server/system/rbac/role-info'
import SystemRoleUpdate from '@/views/tt-server/system/rbac/role-info/role-update'
import RoleMenuAssign from '@/views/tt-server/system/rbac/role-info/role-menu-assign'
import RolePermitAssign from '@/views/tt-server/system/rbac/role-info/role-permit-assign'

export default {
  name: 'Index',
  components: { RolePermitAssign, RoleMenuAssign, SystemRoleUpdate },
  data() {
    return {
      /* 查询条件 */
      queryForm: {
        roleName: '',
        roleValue: '',
        startCreateTime: '',
        endCreateTime: ''
      },
      createTimeRange: [],
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

      /* 编辑抽屉 */
      editDrawerTitle: '',
      editDrawerVisible: false,
      editDrawerRef: 'editDrawerRefKey',
      /* 菜单授予抽屉 */
      menuAssignDrawerTitle: '',
      menuAssignVisible: false,
      menuAssignDrawerRef: 'menuAssignDrawerRefKey',
      /* 菜单授予抽屉 */
      permitAssignDrawerTitle: '',
      permitAssignVisible: false,
      permitAssignDrawerRef: 'permitAssignDrawerRef',
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
      const { data: res } = await getSystemRolePage(this.queryForm)
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
        userName: '',
        userUserName: '',
        startCreateTime: '',
        endCreateTime: ''
      }
    },
    systemRoleUpdateAction(idx, row) {
      const isUpdate = !!row
      this.currentId = isUpdate ? row.id : null
      this.editDrawerTitle = '系统角色编辑'
      this.editDrawerVisible = true
      console.log(idx, row)
    },
    systemRoleDeleteAction(idx, row) {
      console.log(idx, row)
      const confirmPromise = this.$confirm(`确定要删除系统角色[ ${row['roleName']} ]吗？`, '删除提示：', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
      confirmPromise.then(async() => {
        await deleteSystemRole(row)
        this.$message.success(`系统角色[ ${row['roleName']} ]删除成功`)
        await this.queryPage()
      })
    },
    closeAllDrawer() {
      this.editDrawerVisible = false
      this.menuAssignVisible = false
      this.permitAssignVisible = false
    },
    menuAssignAction(idx, row) {
      this.currentId = row.id
      this.menuAssignDrawerTitle = `菜单授予：[ ${row.roleName} ]`
      this.menuAssignVisible = true
    },
    permitAssignAction(idx, row) {
      this.currentId = row.id
      this.permitAssignDrawerTitle = `权限授予：[ ${row.roleName} ]`
      this.permitAssignVisible = true
    }
  }
}
</script>
