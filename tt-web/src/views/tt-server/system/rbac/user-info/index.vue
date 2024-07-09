<template>
  <div>
    <!-- 条件表单 -->
    <div class="query-form">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="用户名称">
          <el-input v-model="queryForm.userName" placeholder="用户名称" clearable />
        </el-form-item>

        <el-form-item label="登录账号">
          <el-input v-model="queryForm.userUserName" placeholder="登录账号" clearable />
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
          <el-button v-permission="['user-info@insert']" type="primary" size="small" icon="el-icon-circle-plus-outline" @click="systemUserUpdateAction(null, null)">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div v-loading="tableLoadingFlag" class="query-table">
      <!-- 主查询列表组件 -->
      <el-table :data="tableData" stripe border :header-cell-style="$ui.table.headerCellStyle">
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column label="用户名称" prop="userName" show-overflow-tooltip />
        <el-table-column label="登录账号" prop="userUserName" show-overflow-tooltip />
        <el-table-column label="用户密码" prop="maskedPassword" show-overflow-tooltip />
        <el-table-column label="创建人" prop="creator" show-overflow-tooltip />
        <el-table-column label="更新人" prop="updater" show-overflow-tooltip />
        <el-table-column label="创建时间" prop="createTime" align="center" min-width="140px" show-overflow-tooltip />
        <el-table-column label="更新时间" prop="updateTime" align="center" min-width="140px" show-overflow-tooltip />
        <el-table-column fixed="right" label="操作" width="410px" align="center">
          <template slot-scope="{ $index, row }">
            <el-button v-permission="['user-info@data-auth']" type="default" size="small" icon="el-icon-view" @click="dataAuthAction($index, row)">数据授权</el-button>
            <el-button v-permission="['user-info@role-assign']" type="default" size="small" icon="el-icon-thumb" @click="roleAssignAction($index, row)">角色授予</el-button>
            <el-button v-permission="['user-info@update']" type="default" size="small" icon="el-icon-edit-outline" @click="systemUserUpdateAction($index, row)">更新</el-button>
            <el-button v-permission="['user-info@delete']" type="default" size="small" icon="el-icon-circle-close" @click="systemUserDeleteAction($index, row)">删除</el-button>
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
      <system-user-update v-if="editDrawerVisible" :ref="editDrawerRef" :key-id="currentId" />
    </el-drawer>

    <!-- 角色授予抽屉 -->
    <el-drawer
      :title="roleAssignDrawerTitle"
      :size="$ui.drawer.width.w480"
      :append-to-body="true"
      :destroy-on-close="true"
      :modal="true"
      :visible.sync="roleAssignVisible"
      @close="closeAllDrawer"
    >
      <system-user-role-assign v-if="roleAssignVisible" :ref="roleAssignRef" :key-id="currentId" />
    </el-drawer>

    <!-- 数据授权抽屉 -->
    <el-drawer
      :title="dataAuthDrawerTitle"
      :size="$ui.drawer.width.w960"
      :append-to-body="true"
      :destroy-on-close="true"
      :modal="true"
      :visible.sync="dataAuthVisible"
      @close="closeAllDrawer"
    >
      <system-user-data-auth v-if="dataAuthVisible" :ref="dataAuthRef" :key-id="currentId" />
    </el-drawer>

  </div>
</template>

<script>
import { deleteSystemUser, getSystemUserPage } from '@/api/tt-server/system/rbac/user-info'
import SystemUserUpdate from '@/views/tt-server/system/rbac/user-info/user-update'
import SystemUserRoleAssign from '@/views/tt-server/system/rbac/user-info/user-role-assign'
import SystemUserDataAuth from '@/views/tt-server/system/rbac/user-info/user-data-auth'
export default {
  name: 'Index',
  components: { SystemUserUpdate, SystemUserRoleAssign, SystemUserDataAuth },
  data() {
    return {
      /* 查询条件 */
      queryForm: {
        userName: '',
        userUserName: '',
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

      /* 角色授予抽屉 */
      roleAssignDrawerTitle: '',
      roleAssignVisible: false,
      roleAssignRef: 'roleAssignRefKey',

      /* 数据授权抽屉 */
      dataAuthDrawerTitle: '',
      dataAuthVisible: false,
      dataAuthRef: 'dataAuthRefKey',

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
      const { data: res } = await getSystemUserPage(this.queryForm)
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
    systemUserUpdateAction(idx, row) {
      const isUpdate = !!row
      this.currentId = isUpdate ? row.id : null
      this.editDrawerTitle = '系统用户编辑'
      this.editDrawerVisible = true
      console.log(idx, row)
    },
    closeAllDrawer() {
      this.editDrawerVisible = false
      this.roleAssignVisible = false
      this.dataAuthVisible = false
    },
    systemUserDeleteAction(idx, row) {
      console.log(idx, row)
      const confirmPromise = this.$confirm(`确定要删除系统用户[${row['userName']}]吗？`, '删除提示：', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
      confirmPromise.then(async() => {
        await deleteSystemUser(row)
        this.$message.success(`系统用户[${row['userName']}]删除成功`)
        await this.queryPage()
      })
    },
    roleAssignAction(idx, row) {
      this.currentId = row.id
      this.roleAssignDrawerTitle = `角色授予：[ ${row.userName} ]`
      this.roleAssignVisible = true
    },
    dataAuthAction(idx, row) {
      this.currentId = row.id
      this.dataAuthDrawerTitle = `数据授权：[ ${row.userName} ]`
      this.dataAuthVisible = true
    }
  }
}
</script>
