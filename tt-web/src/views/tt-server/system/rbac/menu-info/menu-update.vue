<template>
  <div>
    <el-form :ref="formRef" :model="form" :rules="formRules" label-width="80px" label-position="top">
      <el-form-item label="应用编号" prop="appCode">
        <el-input v-model="form.appCode" placeholder="应用编号" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="菜单名称" prop="menuName">
        <el-input v-model="form.menuName" placeholder="菜单名称" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="菜单赋值" prop="menuValue">
        <el-input v-model="form.menuValue" placeholder="菜单赋值" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="菜单图标" prop="menuIcon">
        <el-input v-model="form.menuIcon" placeholder="菜单图标" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="菜单路由" prop="menuRoute">
        <el-input v-model="form.menuRoute" placeholder="菜单路由" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="菜单路径" prop="menuPath">
        <el-input v-model="form.menuPath" placeholder="菜单路径" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="展示顺序" prop="menuSort">
        <el-input-number v-model="form.menuSort" :min="1" :max="10" label="展示顺序" :style="$ui.form.style.tile" />
      </el-form-item>

      <el-form-item label="是否展示" prop="isShow">
        <el-select v-model="form.isShow" placeholder="请选择" clearable :style="$ui.form.style.tile">
          <el-option v-for="(item, idx) in isShowList" :key="`isShow${idx}`" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item label="父级菜单" prop="parentId">
        <el-select v-model="form.parentId" :disabled="isUpdate" placeholder="请选择" clearable :style="$ui.form.style.tile">
          <el-option v-for="(item, idx) in menuList" :key="`parentId${idx}`" :label="item.menuName" :value="item.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="菜单类型" prop="menuType">
        <el-select v-model="form.menuType" :disabled="isUpdate" placeholder="请选择" clearable :style="$ui.form.style.tile">
          <el-option v-for="(item, idx) in menuTypeList" :key="`menuType${idx}`" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item label="菜单层级" prop="menuLevel">
        <el-select v-model="form.menuLevel" :disabled="isUpdate" placeholder="请选择" clearable :style="$ui.form.style.tile">
          <el-option v-for="(item, idx) in menuLevelList" :key="`menuLevel${idx}`" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

    </el-form>

    <div class="drawer-bottom-bar">
      <el-button type="primary" icon="el-icon-check" @click="submit">确定</el-button>
      <el-button type="default" icon="el-icon-close" @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import { addSystemMenu, getSystemMenuById, updateSystemMenu, getSystemMenuList } from '@/api/tt-server/system/rbac/menu-info'

export default {
  name: 'SystemMenuUpdate',
  props: {
    keyId: {
      type: [String, Number],
      require: false,
      default: undefined
    },
    options: {
      type: Object,
      require: true,
      default() {
        return {
          menuTypeList: [],
          menuLevelList: [],
          isShowList: []
        }
      }
    }
  },
  data() {
    return {
      formRef: 'formRefKey',
      isUpdate: false,
      menuList: [],
      menuTypeList: [],
      menuLevelList: [],
      isShowList: [],
      form: {
        id: '',
        appCode: '',
        menuName: '',
        menuValue: '',
        menuIcon: '',
        menuLevel: '',
        menuType: '',
        menuSort: '',
        menuRoute: '',
        menuPath: '',
        isShow: '',
        parentId: 0
      },
      formRules: {
        roleName: [
          { required: true, message: '请填写[角色名称]', trigger: 'blur' }
        ],
        roleValue: [
          { required: true, message: '请填写[角色赋值]', trigger: 'blur' }
        ]
      }
    }
  },
  async created() {
    this.isUpdate = !!this.keyId
    await this.initialOptions()
    await this.initialSystemMenuData()
  },
  methods: {
    async initialOptions() {
      const param = {
        menuType: 'Directory',
        menuIds: [this.isUpdate ? this.keyId : 0].toString()
      }
      const { data: menuList } = await getSystemMenuList(param)
      this.menuList = menuList
      this.menuList.unshift({ menuName: '无', id: 0 })

      this.menuTypeList = [... this.options.menuTypeList]
      this.menuLevelList = [... this.options.menuLevelList]
      this.isShowList = [... this.options.isShowList]
      this.menuTypeList.shift()
      this.menuLevelList.shift()
      this.isShowList.shift()
    },
    async initialSystemMenuData() {
      if (!this.isUpdate) return
      const { data: menu } = await getSystemMenuById(this.keyId)
      this.form = menu
    },
    submit() {
      this.$refs[this.formRef].validate(async valid => {
        if (!valid) return

        if (this.isUpdate) {
          await updateSystemMenu(this.form)
          this.$message.success('系统角色更新成功')
        } else {
          await addSystemMenu(this.form)
          this.$message.success('系统角色新增成功')
        }
        this.$parent.$parent['closeAllDrawer']()
        this.$parent.$parent['queryPage']()
      })
    },
    cancel() {
      this.$parent.$parent['closeAllDrawer']()
    }
  }
}
</script>
