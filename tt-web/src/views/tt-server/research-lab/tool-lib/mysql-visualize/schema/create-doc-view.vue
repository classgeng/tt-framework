<template>
  <div>
    <el-form :ref="formRef" :model="form" :rules="formRules" size="small">
      <el-row :gutter="$ui.layout.gutter.g10">
        <el-col :span="$ui.layout.span.two">
          <el-form-item label="文件名" prop="docName">
            <el-input v-model="form.docName" placeholder="文件名" clearable maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="文档标题" prop="docTitle">
            <el-input v-model="form.docTitle" placeholder="文档标题" clearable maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="文档版本" prop="version">
            <el-input v-model="form.version" placeholder="文档版本" clearable maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="文档描述" prop="description">
            <el-input v-model="form.description" placeholder="文档描述" clearable maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="组织机构" prop="organization">
            <el-input v-model="form.organization" placeholder="组织机构" clearable maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="组织机构链接" prop="organizationUrl">
            <el-input v-model="form.organizationUrl" placeholder="组织机构链接" clearable maxlength="64" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="指定生成的表" prop="specifyTables">
            <el-input v-model="form.specifyTables" placeholder="指定生成的表" clearable maxlength="128" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="指定生成的表前缀" prop="specifyTablePrefixes">
            <el-input v-model="form.specifyTablePrefixes" placeholder="指定生成的表前缀" clearable maxlength="128" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="指定生成的表后缀" prop="specifyTableSuffixes">
            <el-input v-model="form.specifyTableSuffixes" placeholder="指定生成的表后缀" clearable maxlength="128" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="要忽略的表" prop="specifyTables">
            <el-input v-model="form.ignoreTables" placeholder="要忽略的表" clearable maxlength="128" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="要忽略的表前缀" prop="specifyTablePrefixes">
            <el-input v-model="form.ignoreTablePrefixes" placeholder="要忽略的表前缀" clearable maxlength="128" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="要忽略的表后缀" prop="specifyTableSuffixes">
            <el-input v-model="form.ignoreTableSuffixes" placeholder="要忽略的表后缀" clearable maxlength="128" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="文件类型" prop="docType">
            <el-select v-model="form.docType" placeholder="请选择" clearable style="width: 100%">
              <el-option v-for="item in docTypes" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div align="center">
      <el-button size="mini" type="primary" icon="el-icon-check" @click="formSubmit">确定</el-button>
      <el-button size="mini" type="default" plain icon="el-icon-close" @click="formCancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import { getDbSchemaDoc } from '@/api/tt-server/tool-lib/mysql-visualize'
import { axiosDownloadFile } from '@/utils'

export default {
  name: 'CreateDocView',
  props: {
    param: {
      type: Object,
      required: true,
      default: () => {}
    }
  },
  data() {
    return {
      formRef: 'formRefKey',
      form: {
        docName: '',
        docTitle: '',
        version: 'V1.0.0',
        docType: 1,
        description: '',
        organization: 'OnCloud9',
        organizationUrl: 'https://www.cnblogs.com/mindzone/',

        /* 输出的表指定参数 */
        specifyTables: '',
        specifyTablePrefixes: '',
        specifyTableSuffixes: '',
        ignoreTables: '',
        ignoreTablePrefixes: '',
        ignoreTableSuffixes: ''
      },
      formRules: {
        docName: [{ required: true, message: '请填写文件名', trigger: 'blur' }],
        docTitle: [{ required: true, message: '请填写文档标题', trigger: 'blur' }],
        version: [{ required: true, message: '请填写文档版本', trigger: 'blur' }],
        description: [{ required: true, message: '请填写文档描述', trigger: 'blur' }],
        organization: [{ required: true, message: '请填写组织机构', trigger: 'blur' }],
        organizationUrl: [{ required: true, message: '请填写组织机构链接', trigger: 'blur' }],
        docType: [{ required: true, message: '请选择文件类型', trigger: 'blur' }]
      },
      docTypes: [
        { label: 'html | .html', value: 1 },
        { label: 'word | .docx', value: 2 },
        { label: 'markdown | .md', value: 3 }
      ]
    }
  },
  methods: {
    formSubmit() {
      this.$refs[this.formRef].validate(async(isValid) => {
        if (!isValid) return
        const docParam = JSON.parse(JSON.stringify(this.form))
        docParam.specifyTables = docParam.specifyTables ? docParam.specifyTables.split(',') : ''
        docParam.specifyTablePrefixes = docParam.specifyTablePrefixes ? docParam.specifyTablePrefixes.split(',') : ''
        docParam.specifyTableSuffixes = docParam.specifyTableSuffixes ? docParam.specifyTableSuffixes.split(',') : ''
        docParam.ignoreTables = docParam.ignoreTables ? docParam.ignoreTables.split(',') : ''
        docParam.ignoreTablePrefixes = docParam.ignoreTablePrefixes ? docParam.ignoreTablePrefixes.split(',') : ''
        docParam.ignoreTableSuffixes = docParam.ignoreTableSuffixes ? docParam.ignoreTableSuffixes.split(',') : ''
        const res = await getDbSchemaDoc(this.param.dbConnId, this.param.schemaName, docParam)
        axiosDownloadFile(res)
      })
    },
    formCancel() {
      this.$parent.$parent['closeAllDrawer']()
    }
  }
}
</script>
