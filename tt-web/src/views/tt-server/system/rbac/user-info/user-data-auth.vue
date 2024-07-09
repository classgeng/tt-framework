<template>
  <div v-loading="loadingFlag">
    <el-form :ref="formRef" :rules="formRules">
      <el-table :ref="tableRef" :data="dataAuthConfigList" highlight-current-row height="calc(100vh - 160px)" border stripe :span-method="spanMethod" @selection-change="selectChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column label="功能名称" prop="identName" width="140px" align="center" show-overflow-tooltip />
        <el-table-column label="授权名称" prop="authName" width="140px" align="center" show-overflow-tooltip />
        <el-table-column label="授权值" prop="authValue" align="center">
          <template slot-scope="{ $index, row }">
            <el-form-item v-if="isEqualsDataSrc(row['authType'], 'date')" label="" :prop="`dataAuthConfigList.${$index}.authValue`">
              <el-date-picker v-model="row.dateTimeRange" value-format="yyyy-MM-dd hh:mm:ss" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" @change="dataTimeRangeChange($index, row)" />
            </el-form-item>
          </template>
        </el-table-column>
      </el-table>
    </el-form>

    <div class="drawer-bottom-bar">
      <el-button type="primary" icon="el-icon-check" @click="submit">确定</el-button>
      <el-button type="default" icon="el-icon-close" @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import { getAssignableDataAuthList, getAssignedDataAuthList, dataAuthAssign } from '@/api/tt-server/experiment/data-auth-config'

export default {
  name: 'UserDataAuth',
  props: {
    keyId: {
      type: [String, Number],
      required: true,
      default: undefined
    }
  },
  data() {
    return {
      formRef: 'formRefKey',
      tableRef: 'tableRef',
      loadingFlag: false,
      dataAuthConfigList: [],
      dateTimeRange: [],
      selects: [],
      span: {
        array: [],
        idx: 0
      },
      formRules: {
        authValue: []
      }
    }
  },
  async created() {
    this.loadingFlag = true
    await this.initialDataAuthConfigList()
    await this.spanArrayCalculate()
    await this.echoCheckedRow()
    this.loadingFlag = false
  },
  methods: {
    dataTimeRangeChange(idx, row) {
      this.$set(this.dataAuthConfigList[idx], 'authValue', JSON.stringify(row.dateTimeRange))
    },
    async initialDataAuthConfigList() {
      const { data: dataAuthConfigList } = await getAssignableDataAuthList(this.keyId)
      dataAuthConfigList.forEach((row, idx) => {
        if (this.isEqualsDataSrc(row['authType'], 'date')) {
          row.dateTimeRange = []
        }
      })
      console.log(dataAuthConfigList)
      this.dataAuthConfigList = dataAuthConfigList
    },
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
    spanArrayCalculate() {
      /* 每次加载重置合并行计算 */
      this.span.array = []
      this.span.idx = 0
      this.dataAuthConfigList.forEach((row, idx, array) => {
        /* 动态合并列 执行计算 */
        if (idx === 0) {
          this.span.array.push(1)
          this.span.idx = 0
          return
        }
        /*  判断当前元素与上一个元素是否相同 ,menuName是后台返回的标识符 */
        if (row.identKey === array[idx - 1].identKey) {
          this.span.array[this.span.idx] += 1
          this.span.array.push(0)
        } else {
          this.span.array.push(1)
          this.span.idx = idx
        }
      })
    },
    /* 回显勾选的行 */
    async echoCheckedRow() {
      const { data: assignedDataAuths } = await getAssignedDataAuthList(this.keyId)
      if (!assignedDataAuths || assignedDataAuths.length === 0) return
      const totalData = this.dataAuthConfigList
      totalData.forEach((row, idx, arr) => {
        const sameOne = assignedDataAuths.find(checkedRow => checkedRow.id === row.id)
        if (!sameOne) return
        this.$refs[this.tableRef].toggleRowSelection(arr[idx])
        const authValue = JSON.parse(sameOne.authValue)
        if (this.isEqualsDataSrc(sameOne['authType'], 'date')) {
          this.$set(totalData[idx], 'dateTimeRange', authValue)
        } else {
          this.$set(totalData[idx], 'authValue', authValue)
        }
      })
    },
    isEqualsDataSrc(val, declareVal) {
      return val === declareVal
    },
    async submit() {
      const param = {
        id: this.keyId,
        dataAuthConfigs: this.selects
      }
      await dataAuthAssign(param)
      this.$message.success('权限配置成功')
      this.$parent.$parent['closeAllDrawer']()
    },
    cancel() {
      this.$parent.$parent['closeAllDrawer']()
    }
  }
}
</script>
