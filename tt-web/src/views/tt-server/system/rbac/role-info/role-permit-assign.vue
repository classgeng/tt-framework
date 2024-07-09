<template>
  <div>
    <div v-loading="loadingFlag" style="padding: 15px;">
      <el-table :ref="tableRef" :data="rolePermitList" highlight-current-row height="calc(100vh - 160px)" border stripe :span-method="spanMethod" @selection-change="selectChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column label="菜单名称" prop="menuName" min-width="100px" align="center" show-overflow-tooltip />
        <el-table-column label="权限名称" prop="permitName" min-width="100px" show-overflow-tooltip />
        <el-table-column label="权限赋值" prop="permitValue" min-width="140px" show-overflow-tooltip />
      </el-table>
    </div>

    <div class="drawer-bottom-bar">
      <el-button type="primary" icon="el-icon-check" @click="submit">确定</el-button>
      <el-button type="default" icon="el-icon-close" @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import {
  assignSystemPermits,
  getAssignableSystemPermitList,
  getAssignedSystemPermitList
} from '@/api/tt-server/system/rbac/privilege-info'

export default {
  name: 'RolePermitAssign',
  props: {
    keyId: {
      type: [String, Number],
      require: false,
      default: undefined
    }
  },
  data() {
    return {
      tableRef: 'tableRefKey',
      loadingFlag: false,
      rolePermitList: [],
      selects: [],
      span: {
        array: [],
        idx: 0
      }
    }
  },
  created() {
    this.initialAssignablePermits()
  },
  methods: {
    selectChange(val) {
      console.log(val)
      this.selects = val
    },
    spanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 2) {
        const _row = this.span.array[rowIndex]
        const _col = 1
        /* console.log(`rowspan:${_row} colspan:${_col}`) */
        return {
          // [0,0] 表示这一行不显示， [2,1]表示行的合并数
          rowspan: _row,
          colspan: _col
        }
      }
    },
    async initialAssignablePermits() {
      this.loadingFlag = true
      const { data: assignablePermits } = await getAssignableSystemPermitList(this.keyId)
      console.log(assignablePermits)
      this.rolePermitList = assignablePermits
      await this.spanArrayCalculate()
      await this.echoCheckedRow()
      this.loadingFlag = false
    },
    /* 回显勾选的行 */
    async echoCheckedRow() {
      const { data: assignedPermits } = await getAssignedSystemPermitList(this.keyId)
      if (!assignedPermits || assignedPermits.length === 0) return
      const totalData = this.rolePermitList
      totalData.forEach((row, idx, arr) => {
        const sameOne = assignedPermits.find(checkedRow => checkedRow.id === row.id)
        if (!sameOne) return
        this.$refs[this.tableRef].toggleRowSelection(arr[idx])
      })
    },
    spanArrayCalculate() {
      /* 每次加载重置合并行计算 */
      this.span.array = []
      this.span.idx = 0
      this.rolePermitList.forEach((row, idx, array) => {
        /* 动态合并列 执行计算 */
        if (idx === 0) {
          this.span.array.push(1)
          this.span.idx = 0
          return
        }
        /*  判断当前元素与上一个元素是否相同 ,menuName是后台返回的标识符 */
        if (row.menuName === array[idx - 1].menuName) {
          this.span.array[this.span.idx] += 1
          this.span.array.push(0)
        } else {
          this.span.array.push(1)
          this.span.idx = idx
        }
      })
    },
    async submit() {
      console.log(this.selects)

      const param = {
        id: this.keyId,
        mePmList: this.selects
      }
      await assignSystemPermits(param)

      this.$message.success('权限授予配置成功！')
      this.$parent.$parent['closeAllDrawer']()
    },
    cancel() {
      this.$parent.$parent['closeAllDrawer']()
    }
  }
}
</script>

<style scoped>
.el-table__body tr.current-row>td{
  background-color: #69A8EA !important;
  color: #fff;
}
</style>
