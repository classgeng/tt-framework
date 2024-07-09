<template>
  <div>
    <el-form :ref="formRef" :model="form" :rules="formRules" label-width="80px" label-position="top">
      <el-form-item label="用户名称" prop="userName">
        <el-input v-model="form.userName" placeholder="用户名称" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="登录账号" prop="userUserName">
        <el-input v-model="form.userUserName" placeholder="登录账号" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="用户密码" prop="userPassword">
        <el-input v-model="form.userPassword" placeholder="用户密码" maxlength="32" show-word-limit clearable />
      </el-form-item>
    </el-form>

    <div class="drawer-bottom-bar">
      <el-button type="primary" icon="el-icon-check" @click="submit">确定</el-button>
      <el-button type="default" icon="el-icon-close" @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import { addSystemUser, getSystemUserById, updateSystemUser } from '@/api/tt-server/system/rbac/user-info'

export default {
  name: 'SystemUserUpdate',
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
        userName: '',
        userUserName: '',
        userPassword: ''
      },
      formRules: {
        userName: [
          { required: true, message: '请填写[用户名称]', trigger: 'blur' }
        ],
        userUserName: [
          { required: true, message: '请填写[登录账号]', trigger: 'blur' }
        ],
        userPassword: [
          { required: true, message: '请填写[用户密码]', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.isUpdate = !!this.keyId
    this.initialSystemUserData()
  },
  methods: {
    async initialSystemUserData() {
      if (!this.isUpdate) return
      const { data: sysUser } = await getSystemUserById(this.keyId)
      this.form = sysUser
    },
    submit() {
      this.$refs[this.formRef].validate(async valid => {
        if (!valid) return

        if (this.isUpdate) {
          await updateSystemUser(this.form)
          this.$message.success('系统用户更新成功')
        } else {
          await addSystemUser(this.form)
          this.$message.success('系统用户新增成功')
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
