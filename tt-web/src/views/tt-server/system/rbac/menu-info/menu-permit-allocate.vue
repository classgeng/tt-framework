<template>
  <div>
    <div v-loading="loadingFlag" class="transfer-wrapper">
      <el-transfer
        v-model="allocatedPermitList"
        :data="allPermitList"
        :titles="label.titles"
        :button-texts="label.btnTexts"
        filterable
        filter-placeholder="请输入权限名称"
      />
    </div>

    <div class="drawer-bottom-bar">
      <el-button type="primary" icon="el-icon-check" @click="submit">确定</el-button>
      <el-button type="default" icon="el-icon-close" @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>

import {
  getSystemPermitList,
  getAllocatedSystemPermitList,
  allocateSystemPermitList
} from '@/api/tt-server/system/rbac/privilege-info'

export default {
  name: 'MenuPermitAllocate',
  props: {
    keyId: {
      type: [Number, String],
      required: true,
      default: undefined
    }
  },
  data() {
    return {
      loadingFlag: false,
      allPermitList: [],
      allocatedPermitList: [],
      label: {
        titles: ['未选权限', '已有权限'],
        btnTexts: ['移除已有权限', '分配新的权限']
      }
    }
  },
  async created() {
    if (!this.keyId) return
    this.loadingFlag = true
    await this.initialAllPermitList()
    await this.initialAllocatedPermitList()
    this.loadingFlag = false
  },
  methods: {
    async initialAllPermitList() {
      const { data: permitList } = await getSystemPermitList()
      this.allPermitList = permitList.map(permit => { return { ...permit, key: permit.id, label: `${permit.permitName} - ${permit.permitValue}` } })
    },
    async initialAllocatedPermitList() {
      const { data: allocatedPermitList } = await getAllocatedSystemPermitList(this.keyId)
      this.allocatedPermitList = allocatedPermitList.map(permit => { return permit.id })
    },
    async submit() {
      const menuData = {
        id: this.keyId,
        permitIds: this.allocatedPermitList
      }
      await allocateSystemPermitList(menuData)
      this.$message.success('权限配置成功')
      this.$parent.$parent['closeAllDrawer']()
    },
    cancel() {
      this.$parent.$parent['closeAllDrawer']()
    }
  }
}
</script>

<style scoped>
.transfer-wrapper {
  padding: 20px
}
/* 设置穿梭框的大小 */
.transfer-wrapper >>> div.el-transfer-panel {
  width: 100%;
  height: 40vh;
}
/* 设置穿梭框内的筛选框 */
.transfer-wrapper >>> div.el-transfer-panel__list.is-filterable {
  height: 39.5vh;
}
.transfer-wrapper >>> div.el-transfer__buttons {
  margin-top: 20px;
  margin-bottom: 10px;
  padding-left: 75px;
}

</style>
