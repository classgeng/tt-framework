<template>
  <div>
    <div v-loading="loadingFlag" style="padding: 10px">
      <el-tree
        :ref="menuTreeRef"
        :data="menuTreeData"
        :props="configProps"
        show-checkbox
        default-expand-all
        node-key="id"
        highlight-current
      />
    </div>

    <div class="drawer-bottom-bar">
      <el-button type="primary" icon="el-icon-check" @click="submit">确定</el-button>
      <el-button type="default" icon="el-icon-close" @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import { assignSystemMenus, getCheckedSystemMenus, getSystemMenuTree } from '@/api/tt-server/system/rbac/menu-info'
import { typeCheck } from '@/utils/common'

export default {
  name: 'RoleMenuAssign',
  props: {
    keyId: {
      type: [String, Number],
      require: false,
      default: undefined
    }
  },
  data() {
    return {
      loadingFlag: false,
      menuTreeRef: 'menuTreeRefKey',
      menuTreeData: [],
      configProps: {
        label: this.customLabel
      }

    }
  },
  async created() {
    this.loadingFlag = true
    await this.initialMenuTree()
    await this.initialCheckedMenus()
    this.loadingFlag = false
  },
  methods: {
    async initialMenuTree() {
      const { data: menuTreeData } = await getSystemMenuTree()
      this.menuTreeData = menuTreeData
    },
    async initialCheckedMenus() {
      const { data: checkedMenus } = await getCheckedSystemMenus(this.keyId)
      const checkedKeys = checkedMenus.map(menu => menu.id)
      const treeRef = this.$refs[this.menuTreeRef]

      /* 回显操作 */
      checkedKeys.forEach(key => {
        const node = treeRef.getNode(key)
        if (!node.isLeaf) return
        treeRef.setChecked(node, true)
      })
    },
    customLabel(data, node) {
      return `${data.menuName}`
    },
    async submit() {
      const treeRef = this.$refs[this.menuTreeRef]
      // const checkedNodes = treeRef.getCheckedNodes()
      // console.log(`选中的节点 ${JSON.stringify(checkedNodes)}`)

      const checkedKeys = treeRef.getCheckedKeys()
      console.log(`选中的Key ${checkedKeys} 类型 ${typeCheck(checkedKeys)}`)

      const halfCheckedKeys = treeRef.getHalfCheckedKeys()
      console.log(`半选中的Key ${halfCheckedKeys} 类型 ${typeCheck(checkedKeys)}`)

      const allCheckedKeys = [... checkedKeys, ... halfCheckedKeys].sort()
      console.log(allCheckedKeys)

      const param = {
        id: this.keyId,
        menuIds: allCheckedKeys
      }
      await assignSystemMenus(param)

      this.$message.success('菜单授予配置成功！')
      this.$parent.$parent['closeAllDrawer']()
    },
    cancel() {
      this.$parent.$parent['closeAllDrawer']()
    }
  }
}
</script>

<style scoped>

</style>
