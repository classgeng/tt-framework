<template>
  <div>
    <el-form :ref="formRef" :model="form" :rules="formRules" label-width="80px" label-position="top">

      <el-form-item label="系统参数名称" prop="paramName">
        <el-input v-model="form.paramName" placeholder="系统参数名称" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="系统参数值" prop="paramValue">
        <el-input v-model="form.paramValue" placeholder="系统参数值" maxlength="32" show-word-limit clearable />
      </el-form-item>
    </el-form>

    <div class="drawer-bottom-bar">
      <el-button type="primary" icon="el-icon-check" @click="submit">确定</el-button>
      <el-button type="default" icon="el-icon-close" @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import { addSystemParam, getSystemParamById, updateSystemParam } from '@/api/tt-server/system/common/system-param'

export default {
  name: 'SysParamUpdate',
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
        paramName: '',
        paramValue: ''
      },
      formRules: {
        paramName: [
          { required: true, message: '请填写系统参数名称', trigger: 'blur' }
        ],
        paramValue: [
          { required: true, message: '请填写系统参数值', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.isUpdate = !!this.keyId
    this.initialSystemParamData()
  },
  methods: {
    async initialSystemParamData() {
      if (!this.isUpdate) return
      const { data: systemParam } = await getSystemParamById(this.keyId)
      this.form = systemParam
    },
    submit() {
      this.$refs[this.formRef].validate(async valid => {
        if (!valid) return

        if (this.isUpdate) {
          await updateSystemParam(this.form)
          this.$message.success('系统参数更新成功')
        } else {
          await addSystemParam(this.form)
          this.$message.success('系统参数新增成功')
        }
        this.$parent.$parent['closeAllDrawer']()
        this.$parent.$parent['queryPage']()
      })
    },
    cancel() {
      this.$parent.$parent.closeEditDrawer()
    }
  }
}
</script>
