<template>
  <div>
    <el-form :ref="formRef" :model="form" :rules="formRules" label-width="80px" label-position="top">
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="form.roleName" placeholder="角色名称" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="角色赋值" prop="roleValue">
        <el-input v-model="form.roleValue" placeholder="角色赋值" maxlength="32" show-word-limit clearable />
      </el-form-item>
    </el-form>

    <div class="drawer-bottom-bar">
      <el-button type="primary" icon="el-icon-check" @click="submit">确定</el-button>
      <el-button type="default" icon="el-icon-close" @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import { addSystemRole, getSystemRoleById, updateSystemRole } from '@/api/tt-server/system/rbac/role-info'

export default {
  name: 'SystemRoleUpdate',
  props: {
    keyId: {
      type: [String, Number],
      require: false,
      default: undefined
    }
  },
  data() {
    return {
      formRef: 'formRefKey',
      isUpdate: false,
      form: {
        id: '',
        roleName: '',
        roleValue: ''
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
  created() {
    this.isUpdate = !!this.keyId
    this.initialSystemRoleData()
  },
  methods: {
    async initialSystemRoleData() {
      if (!this.isUpdate) return
      const { data: role } = await getSystemRoleById(this.keyId)
      this.form = role
    },
    submit() {
      this.$refs[this.formRef].validate(async valid => {
        if (!valid) return

        if (this.isUpdate) {
          await updateSystemRole(this.form)
          this.$message.success('系统角色更新成功')
        } else {
          await addSystemRole(this.form)
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
