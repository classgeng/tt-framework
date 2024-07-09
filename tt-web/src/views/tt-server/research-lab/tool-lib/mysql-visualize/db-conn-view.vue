<template>
  <div>
    <el-form :ref="formRef" :model="form" :rules="formRules" label-width="80px">
      <el-form-item label="连接名称" prop="connName">
        <el-input v-model="form.connName" placeholder="连接名称" clearable maxlength="32" show-word-limit />
      </el-form-item>

      <el-form-item label="连接主机" prop="host">
        <el-input v-model="form.host" placeholder="连接主机" clearable maxlength="64" show-word-limit />
      </el-form-item>

      <el-form-item label="连接端口" prop="host">
        <el-input v-model="form.port" placeholder="连接端口" clearable maxlength="6" show-word-limit />
      </el-form-item>

      <el-form-item label="连接账户" prop="username">
        <el-input v-model="form.username" placeholder="连接账户" clearable maxlength="32" show-word-limit />
      </el-form-item>

      <el-form-item label="连接密码" prop="username">
        <el-input v-model="form.password" placeholder="连接密码" clearable maxlength="32" show-word-limit />
      </el-form-item>

    </el-form>

    <el-row>
      <el-col :span="12">
        <el-button size="mini" type="default" plain icon="el-icon-refresh" @click="dbConnTest">连接测试</el-button>
      </el-col>
      <el-col :span="12" align="right">
        <el-button size="mini" type="primary" icon="el-icon-check" @click="formSubmit">确定</el-button>
        <el-button size="mini" type="default" plain icon="el-icon-close" @click="formClose">取消</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDbConnById, saveDbConn, testDbConn, updateDbConn } from '@/api/tt-server/tool-lib/mysql-visualize'

export default {
  name: 'DbConnView',
  props: {
    dbConnId: {
      type: [String, Number],
      required: true,
      default: 0
    }
  },
  data() {
    return {
      formRef: 'formRefKey',
      formRules: {
        connName: [{ required: true, message: '请输入连接名称', trigger: 'blur' }],
        host: [{ required: true, message: '请输入连接主机', trigger: 'blur' }],
        port: [{ required: true, message: '请输入连接端口', trigger: 'blur' }],
        username: [{ required: true, message: '请输入连接账户', trigger: 'blur' }],
        password: [{ required: true, message: '请输入连接密码', trigger: 'blur' }]
      },
      form: {
        id: '',
        connName: '',
        host: 'localhost',
        port: '3306',
        username: 'root',
        password: '123456'
      },
      isUpdate: false
    }
  },
  created() {
    this.isUpdate = !!this.dbConnId
    this.initialFormData()
  },
  methods: {
    async initialFormData() {
      if (this.isUpdate) {
        const { data: dto } = await getDbConnById(this.dbConnId)
        this.form = dto
      } else {
        this.form = { ... this.form }
      }
    },
    dbConnTest() {
      this.$refs[this.formRef].validate(async(isValid) => {
        if (!isValid) return this.$message.error('请完善连接信息！')
        const { data: res } = await testDbConn(this.form)
        this.$message({ type: res ? 'success' : 'error', message: res ? '连接成功' : '连接失败' })
      })
    },
    formSubmit() {
      this.$refs[this.formRef].validate(async(isValid) => {
        if (!isValid) return this.$message.error('请完善连接信息！')
        if (this.isUpdate) {
          await updateDbConn(this.form)
          this.$message.success('更新成功')
        } else {
          await saveDbConn(this.form)
          this.$message.success('保存成功')
        }
        this.formClose()
      })
    },
    formClose() {
      this.$parent.$parent['initialDbConnList']()
      this.$parent.$parent['closeAllDrawer']()
    }
  }
}
</script>
