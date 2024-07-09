<template>
  <div>
    <div v-loading="loadingFlag" class="transfer-wrapper">
      <el-transfer
        v-model="assignedRoleList"
        :data="allRoleList"
        :titles="label.titles"
        :button-texts="label.btnTexts"
        filterable
        filter-placeholder="请输入角色名称"
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
  assignSystemRoleList,
  getAssignedSystemRoleList,
  getSystemRoleList
} from '@/api/tt-server/system/rbac/role-info'

export default {
  name: 'UserRoleAssign',
  props: {
    keyId: {
      type: [String, Number],
      required: true,
      default: undefined
    }
  },
  data() {
    return {
      loadingFlag: false,
      allRoleList: [],
      assignedRoleList: [],
      label: {
        titles: ['未选角色', '已有角色'],
        btnTexts: ['移除已有角色', '分配新的角色']
      }
    }
  },
  async created() {
    if (!this.keyId) return
    this.loadingFlag = true
    await this.initialAllRoleList()
    await this.initialAssignedRoleList()
    this.loadingFlag = false
  },
  methods: {
    async initialAllRoleList() {
      const { data: roleList } = await getSystemRoleList()
      this.allRoleList = roleList.map(role => { return { ...role, key: role.id, label: `${role.roleName} - ${role.roleValue}` } })
    },
    async initialAssignedRoleList() {
      const { data: assignedRoles } = await getAssignedSystemRoleList(this.keyId)
      this.assignedRoleList = assignedRoles.map(role => { return role.id })
    },
    async submit() {
      const userData = {
        id: this.keyId,
        roleIds: this.assignedRoleList
      }
      await assignSystemRoleList(userData)
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
