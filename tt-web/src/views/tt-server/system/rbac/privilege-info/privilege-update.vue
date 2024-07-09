<template>
  <div>
    <el-form :ref="formRef" :model="form" :rules="formRules" label-width="80px" label-position="top">
      <el-form-item label="权限名称" prop="permitName">
        <el-input v-model="form.permitName" placeholder="权限名称" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="权限赋值" prop="permitValue">
        <el-input v-model="form.permitValue" placeholder="权限赋值" maxlength="32" show-word-limit clearable />
      </el-form-item>
    </el-form>

    <div class="drawer-bottom-bar">
      <el-button type="primary" icon="el-icon-check" @click="submit">确定</el-button>
      <el-button type="default" icon="el-icon-close" @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import { addSystemPermit, getSystemPermitById, updateSystemPermit } from '@/api/tt-server/system/rbac/privilege-info'

export default {
  name: 'PrivilegeUpdate',
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
        permitName: '',
        permitValue: ''
      },
      formRules: {
        permitName: [
          { required: true, message: '请填写权限名称', trigger: 'blur' }
        ],
        permitValue: [
          { required: true, message: '请填写权限赋值', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.isUpdate = !!this.keyId
    this.initialSystemPermitData()
  },
  methods: {
    async initialSystemPermitData() {
      if (!this.isUpdate) return
      const { data: dict } = await getSystemPermitById(this.keyId)
      this.form = dict
    },
    submit() {
      this.$refs[this.formRef].validate(async valid => {
        if (!valid) return

        if (this.isUpdate) {
          await updateSystemPermit(this.form)
          this.$message.success('权限更新成功')
        } else {
          await addSystemPermit(this.form)
          this.$message.success('权限新增成功')
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
