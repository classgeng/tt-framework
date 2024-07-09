<template>
  <div>
    <!--    <el-row :gutter="$ui.layout.gutter.g10">-->
    <!--      <el-col :span="$ui.layout.span.one">-->
    <!--        <div class="top-bar">-->
    <!--          <el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="openDbConnDialog('')">新建</el-button>-->
    <!--          <el-button size="mini" plain icon="el-icon-refresh-right" @click="reloadDbConn">刷新</el-button>-->
    <!--        </div>-->
    <!--      </el-col>-->
    <!--    </el-row>-->

    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col :span="4">
        <div class="top-bar">
          <el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="openDbConnDialog('')">新建</el-button>
          <el-button size="mini" plain icon="el-icon-refresh-right" @click="reloadDbConn">刷新</el-button>
        </div>

        <div class="menu-panel">
          <el-collapse v-model="activeDbConnList" accordion @change="collapseItemChange">
            <el-collapse-item v-for="(dbConn, idx) in dbConnList" :key="`dbConn${idx}`" :title="dbConn.connName" :name="dbConn.id">
              <!-- 设置菜单头 -->
              <template slot="title">
                <el-dropdown>
                  <span class="el-dropdown-link">
                    <i class="el-icon-link txt-space" />{{ dbConn.connName }}
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="openSchemaSaveDialog(dbConn)"><i class="el-icon-plus" />创建Schema</el-dropdown-item>
                    <el-dropdown-item divided @click.native="openDbConnDialog(dbConn)"><i class="el-icon-edit-outline" />更新连接</el-dropdown-item>
                    <el-dropdown-item divided @click.native="removeDbConn(dbConn)"><i class="el-icon-delete" />删除连接</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
              <el-menu>
                <el-menu-item v-for="(schema, schemaIdx) in schemaList" :key="`schema${schemaIdx}`" :index="schema['schema_name']" @click="dbSchemaClick(dbConn, schema)">
                  <el-dropdown>
                    <span class="el-dropdown-link">
                      <i class="el-icon-coin" /><span slot="title">{{ schema['schema_name'] }}</span>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item @click.native="createDbSchemaDoc(schema, dbConn)"><i class="el-icon-tickets" />生成文档</el-dropdown-item>
                      <el-dropdown-item divided @click.native="exportDbSchema(schema, dbConn)"><i class="el-icon-printer" />导出脚本</el-dropdown-item>
                      <el-dropdown-item divided @click.native="backupDbSchema(schema, dbConn)"><i class="el-icon-receiving" />完整备份</el-dropdown-item>
                      <el-dropdown-item divided @click.native="removeDbSchema(schema, dbConn)"><i class="el-icon-delete" />删除Schema</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>

                </el-menu-item>
              </el-menu>
            </el-collapse-item>
          </el-collapse>
        </div>
      </el-col>
      <el-col :span="20">
        <el-tabs v-model="activeTabLabel" type="border-card" @tab-click="handleTabClick">
          <el-tab-pane label="表对象列表" class="main-panel" name="tables">
            <el-form :inline="true" :model="queryForm" size="small">
              <el-form-item label="表名称">
                <el-input v-model="queryForm.tableName" placeholder="表名称" clearable />
              </el-form-item>

              <el-form-item label="表注释">
                <el-input v-model="queryForm.tableComment" placeholder="表注释" clearable />
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
            </el-form>

            <el-table v-loading="tableLoadingFlag" border height="calc(100% - 115px)" :data="tableList">
              <el-table-column type="index" width="50" label="序号" align="center" />
              <el-table-column prop="table_schema" label="所属约束" width="120px" show-overflow-tooltip />
              <el-table-column prop="table_name" label="表名称" min-width="100px" show-overflow-tooltip />
              <el-table-column prop="table_comment" label="表注释" min-width="100px" show-overflow-tooltip />
              <el-table-column prop="engine" label="引擎" width="100px" show-overflow-tooltip />
              <el-table-column prop="table_collation" label="字符集" min-width="80px" show-overflow-tooltip />
              <el-table-column prop="version" label="版本" width="80px" align="center" />
              <el-table-column prop="create_time" label="创建时间" width="160px" align="center">
                <template slot-scope="{ row }">{{ $moment(row['create_time']).format('YYYY-MM-DD HH:mm:ss') }}</template>
              </el-table-column>
              <el-table-column fixed="right" label="操作" width="100px" align="center">
                <template slot-scope="{ row }">
                  <el-dropdown size="small" type="primary" trigger="click">
                    <el-button type="primary" size="mini">操作</el-button>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item @click.native="openCreateSqlDialog(row)"><i class="el-icon-document" />建表语句</el-dropdown-item>
                      <el-dropdown-item divided @click.native="openCodeTemplateDialog(row)"><i class="el-icon-document-copy" />生成代码</el-dropdown-item>
                      <el-dropdown-item divided @click.native="removeThisTable(row)"><i class="el-icon-delete" />删除此表</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </template>
              </el-table-column>
            </el-table>

            <!-- 翻页组件 -->
            <el-pagination
              background
              layout="->, total, sizes, prev, pager, next, jumper"
              :current-page="queryForm.page.current"
              :page-sizes="[10, 15, 20, 25, 30, 35, 40, 50, 100]"
              :page-size="queryForm.page.size"
              :total="queryForm.page.total"
              @size-change="pageSizeChange"
              @current-change="pageCurrentChange"
            />

          </el-tab-pane>
          <el-tab-pane label="存储过程" class="main-panel">存储过程</el-tab-pane>
          <el-tab-pane label="触发器" class="main-panel">触发器</el-tab-pane>
          <el-tab-pane label="自定义函数" class="main-panel">自定义函数</el-tab-pane>
          <el-tab-pane label="定时任务" class="main-panel">定时任务</el-tab-pane>
          <el-tab-pane label="系统变量" class="main-panel">全局变量 / 会话变量</el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>

    <!-- 连接参数配置弹窗 -->
    <el-dialog
      :title="dbConnDialog.title"
      :width="dbConnDialog.width"
      :modal="true"
      :visible.sync="dbConnDialog.visible"
      :append-to-body="true"
      @close="closeAllDrawer"
    >
      <db-conn-view v-if="dbConnDialog.visible" :db-conn-id="dbConnDialog.dbConnId" />
    </el-dialog>

    <!-- 连接参数配置弹窗 -->
    <el-dialog
      :title="schemaDialog.title"
      :width="schemaDialog.width"
      :modal="true"
      :visible.sync="schemaDialog.visible"
      :append-to-body="true"
      @close="closeAllDrawer"
    >
      <el-form :ref="schemaFormRef" :model="schemaForm" :rules="schemaFormRules" label-width="120px">
        <el-form-item label="Schema名称" prop="name">
          <el-input v-model="schemaForm.name" placeholder="Schema名称" clearable maxlength="32" show-word-limit />
        </el-form-item>
      </el-form>

      <div align="center">
        <el-button size="mini" type="primary" icon="el-icon-check" @click="schemaFormSubmit">确定</el-button>
        <el-button size="mini" type="default" plain icon="el-icon-close" @click="closeAllDrawer">取消</el-button>
      </div>
    </el-dialog>

    <!-- 建表SQL预览弹窗 -->
    <el-dialog
      :title="createSqlDialog.title"
      :width="createSqlDialog.width"
      :modal="true"
      :visible.sync="createSqlDialog.visible"
      :append-to-body="true"
      @close="closeAllDrawer"
    >
      <create-sql-view v-if="createSqlDialog.visible" :param="createSqlDialog.param" />
    </el-dialog>

    <!-- 代码模板弹窗 -->
    <el-dialog
      :title="codeTemplateDialog.title"
      :width="codeTemplateDialog.width"
      :modal="true"
      :visible.sync="codeTemplateDialog.visible"
      :append-to-body="true"
      @close="closeAllDrawer"
    >
      <code-template-view v-if="codeTemplateDialog.visible" :param="codeTemplateDialog.param" />
    </el-dialog>

    <!-- 文档生成参数弹窗 -->
    <el-dialog
      :title="createDbSchemaDialog.title"
      :width="createDbSchemaDialog.width"
      :modal="true"
      :visible.sync="createDbSchemaDialog.visible"
      :append-to-body="true"
      @close="closeAllDrawer"
    >
      <create-doc-view v-if="createDbSchemaDialog.visible" :param="createDbSchemaDialog.param" />
    </el-dialog>
  </div>
</template>

<script>
import {
  backupDbSchema,
  createNewDbSchema,
  deleteDbConnById,
  deleteDbSchemaById,
  deleteDbTableById,
  exportDbSchema,
  getDbConnList,
  getDbSchemaList,
  getDbTablePage
} from '@/api/tt-server/tool-lib/mysql-visualize'

import DbConnView from '@/views/tt-server/research-lab/tool-lib/mysql-visualize/db-conn-view'
import CreateSqlView from '@/views/tt-server/research-lab/tool-lib/mysql-visualize/table/create-sql-view'
import CodeTemplateView from '@/views/tt-server/research-lab/tool-lib/mysql-visualize/table/code-template-view'
import { axiosDownloadFile } from '@/utils'
import CreateDocView from '@/views/tt-server/research-lab/tool-lib/mysql-visualize/schema/create-doc-view'

export default {
  name: 'Index',
  components: { CreateDocView, CodeTemplateView, CreateSqlView, DbConnView },
  data() {
    return {
      activeTabLabel: 'tables',
      height: `${document.documentElement.clientHeight - 130}px`,
      schemaFormRef: 'schemaFormRefKey',
      schemaForm: {
        name: '',
        dbId: ''
      },
      schemaFormRules: {
        name: [{ required: true, message: '请输入Schema名称', trigger: 'blur' }]
      },
      dbConnDialog: {
        visible: false,
        title: '',
        width: '480px',
        dbConnId: 0
      },
      schemaDialog: {
        visible: false,
        title: '',
        width: '480px'
      },
      createDbSchemaDialog: {
        visible: false,
        title: '',
        width: '780px',
        param: {}
      },
      createSqlDialog: {
        visible: false,
        title: '',
        width: '780px',
        param: {}
      },
      codeTemplateDialog: {
        visible: false,
        title: '',
        width: '1376px',
        param: {}
      },
      dbConnList: [],
      schemaList: [],
      tableLoadingFlag: false,
      queryForm: {
        tableName: '',
        tableComment: '',
        page: {
          current: 1,
          size: 10,
          total: 0
        }
      },
      createTimeRange: [],
      tableList: [],
      activeDbConnList: [],
      dbConnId: '',
      schemaName: ''
    }
  },
  created() {
    this.initialDbConnList()
  },
  methods: {
    createTimeRangeChange(range) {
      const isClear = !range
      this.queryForm.startCreateTime = isClear ? '' : range[0]
      this.queryForm.endCreateTime = isClear ? '' : range[1]
    },
    querySubmit() {
      this.queryPage()
    },
    resetSubmit() {
      this.createTimeRange = ['', '']
      this.queryForm = {
        tableName: '',
        tableComment: '',
        page: {
          current: 1,
          size: 10,
          total: 0
        }
      }
    },
    pageSizeChange(val) {
      console.log(val)
      this.queryForm.page.size = val
      this.queryPage()
    },
    pageCurrentChange(val) {
      console.log(val)
      this.queryForm.page.current = val
      this.queryPage()
    },
    async queryPage() {
      if (!this.dbConnId || !this.schemaName) return this.$message.warning('请先选择Schema!')
      this.tableLoadingFlag = true
      console.log(this.queryForm)
      const { data: res } = await getDbTablePage(this.dbConnId, this.schemaName, this.queryForm)
      this.tableList = res.records
      this.queryForm.page.total = res.total
      this.tableLoadingFlag = false
    },
    removeThisTable(table) {
      const confirmPromise = this.$confirm(`确定要删除Schema[${table['table_name']}]吗?`, '删除提示：', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
      confirmPromise.then(async() => {
        await deleteDbTableById(this.dbConnId, this.schemaName, table['table_name'])
        this.$message.success(`表[${table['table_name']}]已被成功删除`)
        await this.queryPage()
      })
    },
    openCodeTemplateDialog(table) {
      const tableName = table['table_name']
      this.codeTemplateDialog.title = `表[ ${tableName} ]生成的代码：`
      this.codeTemplateDialog.param = {
        dbConnId: this.dbConnId,
        schemaName: this.schemaName,
        tableName: tableName
      }
      this.codeTemplateDialog.visible = true
    },
    openCreateSqlDialog(table) {
      const tableName = table['table_name']
      this.createSqlDialog.title = `[ ${tableName} ]的建表语句：`
      this.createSqlDialog.param = {
        dbConnId: this.dbConnId,
        schemaName: this.schemaName,
        tableName: tableName
      }
      this.createSqlDialog.visible = true
    },
    reloadDbConn() {
      this.dbConnId = ''
      this.schemaName = ''
      this.dbConnList = []
      this.schemaList = []
      this.tableList = []
      this.activeDbConnList = []
      this.queryForm = {
        tableName: '',
        tableComment: '',
        page: {
          current: 1,
          size: 10,
          total: 0
        }
      }
      this.initialDbConnList()
    },
    handleTabClick(tabLabel, event) {
      console.log(tabLabel)
    },
    async dbSchemaClick(dbConn, schema) {
      this.dbConnId = dbConn.id
      this.schemaName = schema['schema_name']
      await this.queryPage()
    },
    async collapseItemChange(val) {
      if (!val) return
      await this.initialDbSchemaList(val)
    },
    async initialDbSchemaList(val) {
      const { data: schemaList } = await getDbSchemaList(val)
      console.log(schemaList)
      this.schemaList = schemaList
    },
    async initialDbConnList() {
      const { data: dbConnList } = await getDbConnList()
      this.dbConnList = dbConnList
    },
    openDbConnDialog(val) {
      const isExist = !!val
      if (isExist) {
        this.dbConnDialog.title = `${val.connName}的连接配置`
        this.dbConnDialog.dbConnId = val.id
      } else {
        this.dbConnDialog.title = '新建连接配置'
        this.dbConnDialog.dbConnId = 0
      }
      this.dbConnDialog.visible = true
    },
    openSchemaSaveDialog(val) {
      this.schemaForm.name = ''
      this.schemaForm.dbId = val.id
      this.schemaDialog.title = '新建Schema'
      this.schemaDialog.visible = true
    },
    closeAllDrawer() {
      this.schemaDialog.visible = false
      this.dbConnDialog.visible = false
      this.createSqlDialog.visible = false
      this.codeTemplateDialog.visible = false
      this.createDbSchemaDialog.visible = false
    },
    async createDbSchemaDoc(schema, dbConn) {
      this.createDbSchemaDialog.title = `[ ${schema['schema_name']} ]的文档参数：`
      this.createDbSchemaDialog.param = { dbConnId: dbConn.id, schemaName: schema['schema_name'] }
      this.createDbSchemaDialog.visible = true
    },
    async backupDbSchema(schema, dbConn) {
      const res = await backupDbSchema(dbConn.id, schema['schema_name'])
      axiosDownloadFile(res)
    },
    async exportDbSchema(schema, dbConn) {
      // location.href = `${PATH}/${dbConn.id}/${schema['schema_name']}/export`
      const res = await exportDbSchema(dbConn.id, schema['schema_name'])
      axiosDownloadFile(res)
    },
    removeDbSchema(schema, dbConn) {
      const confirmPromise = this.$confirm(`确定要删除Schema[${schema['schema_name']}]吗?`, '删除提示：', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
      confirmPromise.then(async() => {
        await deleteDbSchemaById(dbConn.id, schema['schema_name'])
        await this.initialDbSchemaList(dbConn.id)
        this.$message.success(`Schema[${schema.connName}]已被成功删除`)
      })
    },
    removeDbConn(val) {
      const confirmPromise = this.$confirm(`确定要删除连接[${val.connName}]吗?`, '删除提示：', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
      confirmPromise.then(async() => {
        await deleteDbConnById(val.id)
        await this.initialDbConnList()
        this.$message.success(`连接[${val.connName}]已被成功删除`)
      })
    },
    schemaFormSubmit() {
      this.$refs[this.schemaFormRef].validate(async(isValid) => {
        if (!isValid) return
        await createNewDbSchema(this.schemaForm.dbId, this.schemaForm.name)
        await this.initialDbSchemaList(this.schemaForm.dbId)
        this.$message.success(`Schema[${this.schemaForm.name}]创建成功`)
        this.closeAllDrawer()
      })
    }
  }
}
</script>

<style scoped>
.top-bar, .menu-panel, .main-panel {
  background-color: white;
  padding: 10px;
  margin-bottom: 5px;
  border-radius: 5px;
}
.menu-panel {
  height: calc(100vh - 170px);
  overflow-x: auto;
}
.main-panel {
  height: calc(100vh - 190px);
  padding: 5px;
  overflow-x: auto;
}
.txt-space {
  margin-right: 5px;
}

</style>
