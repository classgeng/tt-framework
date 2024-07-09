<template>
  <div>
    <el-form :ref="formRef" :model="form" :rules="formRules" label-width="80px" label-position="top">

      <el-form-item label="内容" prop="content">
        <el-input v-model="form.content" placeholder="内容" maxlength="32" show-word-limit clearable />
      </el-form-item>
    </el-form>

    <div class="drawer-bottom-bar">
      <el-button type="primary" icon="el-icon-check" @click="submit">确定</el-button>
      <el-button type="default" icon="el-icon-close" @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import { addDataAuthTest, getDataAuthTestById, updateDataAuthTest } from '@/api/tt-server/experiment/data-auth-test'

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
        content: [
          { required: true, message: '请填写内容', trigger: 'blur' }
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
      const { data: systemParam } = await getDataAuthTestById(this.keyId)
      this.form = systemParam
    },
    submit() {
      this.$refs[this.formRef].validate(async valid => {
        if (!valid) return

        if (this.isUpdate) {
          await updateDataAuthTest(this.form)
          this.$message.success('系统参数更新成功')
        } else {
          await addDataAuthTest(this.form)
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
