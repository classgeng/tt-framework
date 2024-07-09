<template>
  <div>
    <el-form :ref="formRef" :model="form" :rules="formRules" label-width="80px" label-position="top">
      <el-form-item label="字典编号" prop="dictCode">
        <el-input v-model="form.dictCode" placeholder="字典编号" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="字典名称" prop="dictName">
        <el-input v-model="form.dictName" placeholder="字典名称" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="字典类别" prop="dictCategory">
        <el-input v-model="form.dictCategory" placeholder="字典类别" maxlength="32" show-word-limit clearable />
      </el-form-item>

      <el-form-item label="字典类别名称" prop="dictCateName">
        <el-input v-model="form.dictCateName" placeholder="字典类别名称" maxlength="32" show-word-limit clearable />
      </el-form-item>
    </el-form>

    <div class="drawer-bottom-bar">
      <el-button type="primary" icon="el-icon-check" @click="submit">确定</el-button>
      <el-button type="default" icon="el-icon-close" @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import { addDict, getDictById, updateDict } from '@/api/tt-server/system/common/dict-info'

export default {
  name: 'DictUpdate',
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
        dictCode: '',
        dictName: '',
        dictCategory: '',
        dictCateName: ''
      },
      formRules: {
        dictCode: [
          { required: true, message: '请填写字典编号', trigger: 'blur' }
        ],
        dictName: [
          { required: true, message: '请填写字典名称', trigger: 'blur' }
        ],
        dictCategory: [
          { required: true, message: '请填写字典类别', trigger: 'blur' }
        ],
        dictCateName: [
          { required: true, message: '请填写字典类别名称', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.isUpdate = !!this.keyId
    this.initialDictData()
  },
  methods: {
    async initialDictData() {
      if (!this.isUpdate) return
      const { data: dict } = await getDictById(this.keyId)
      this.form = dict
    },
    submit() {
      this.$refs[this.formRef].validate(async valid => {
        if (!valid) return

        if (this.isUpdate) {
          await updateDict(this.form)
          this.$message.success('字典更新成功')
        } else {
          await addDict(this.form)
          this.$message.success('字典新增成功')
        }
        this.$parent.$parent.closeEditDrawer()
        this.$parent.$parent['queryPage']()
      })
    },
    cancel() {
      this.$parent.$parent.closeEditDrawer()
    }
  }
}
</script>
