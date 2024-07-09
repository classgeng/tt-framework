<template>
  <div>
    <el-form :ref="formRef" :model="form" :rules="formRules" :inline="false" size="small" label-position="right" label-width="120px">
      <el-row :gutter="$ui.layout.gutter.g10">
        <el-col :span="$ui.layout.span.six">
          <el-form-item label="作者" prop="author" label-width="50px">
            <el-input v-model="form.author" placeholder="作者" clearable maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.four">
          <el-form-item label="项目名称" prop="author" label-width="80px">
            <el-input v-model="form.projectName" placeholder="项目名称" clearable maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>

        <el-col :span="$ui.layout.span.two">
          <el-form-item label="基础包路径" prop="author" label-width="95px">
            <el-input v-model="form.basePackagePath" placeholder="项目名称" clearable maxlength="128" show-word-limit @input="basePackagePathChange" />
          </el-form-item>
        </el-col>

        <el-col :span="2">
          <el-button size="small" icon="el-icon-folder-opened" @click="downloadAllCodeFile">下载全部</el-button>
        </el-col>
      </el-row>

      <el-tabs v-model="activeTabLabel" type="border-card" @tab-click="handleTabClick">
        <el-tab-pane label="Model实体类" name="modelTab">
          <el-row :gutter="$ui.layout.gutter.g10">
            <el-col :span="$ui.layout.span.two">
              <el-form-item label="实体类名称" prop="modelName" label-width="100px">
                <el-input v-model="form.modelName" placeholder="实体类名称" clearable maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two">
              <el-form-item label="包路径" prop="modelPath" label-width="70px">
                <el-input v-model="form.modelPath" placeholder="包路径" clearable maxlength="128" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two">
              <el-form-item label="实体类描述" prop="modelDescription" label-width="100px">
                <el-input v-model="form.modelDescription" placeholder="实体类描述" clearable maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two" align="right">
              <el-button size="small" icon="el-icon-refresh-right" @click="initialTemplateContent(1001)">刷新</el-button>
              <el-button v-clipboard:copy="templateTxt" v-clipboard:success="clipboardSuccess" size="small" icon="el-icon-document">复制</el-button>
              <el-button size="small" icon="el-icon-folder-opened" @click="downloadCodeFile(1001)">下载</el-button>
            </el-col>
          </el-row>

          <pre v-loading="loadingFlag" v-highlightjs>
            <code class="java" v-text="templateTxt" />
          </pre>
        </el-tab-pane>

        <el-tab-pane label="Mapper映射接口" name="mapperTab">
          <el-row :gutter="$ui.layout.gutter.g10">
            <el-col :span="$ui.layout.span.two">
              <el-form-item label="Mapper名称" prop="mapperName" label-width="100px">
                <el-input v-model="form.mapperName" placeholder="Mapper名称" clearable maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two">
              <el-form-item label="包路径" prop="mapperPath" label-width="70px">
                <el-input v-model="form.mapperPath" placeholder="包路径" clearable maxlength="128" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two">
              <el-form-item label="Mapper描述" prop="mapperDescription" label-width="100px">
                <el-input v-model="form.mapperDescription" placeholder="Mapper描述" clearable maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two" align="right">
              <el-button size="small" icon="el-icon-refresh-right" @click="initialTemplateContent(1002)">刷新</el-button>
              <el-button v-clipboard:copy="templateTxt" v-clipboard:success="clipboardSuccess" size="small" icon="el-icon-document">复制</el-button>
              <el-button size="small" icon="el-icon-folder-opened" @click="downloadCodeFile(1002)">下载</el-button>
            </el-col>
          </el-row>

          <pre v-loading="loadingFlag" v-highlightjs>
            <code class="java" v-text="templateTxt" />
          </pre>
        </el-tab-pane>

        <el-tab-pane label="Service服务接口" name="iServiceTab">
          <el-row :gutter="$ui.layout.gutter.g10">
            <el-col :span="$ui.layout.span.two">
              <el-form-item label="服务接口名称" prop="iServiceName">
                <el-input v-model="form.iServiceName" placeholder="Mapper名称" clearable maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two">
              <el-form-item label="包路径" prop="mapperPath" label-width="70px">
                <el-input v-model="form.iServicePath" placeholder="包路径" clearable maxlength="128" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two">
              <el-form-item label="服务接口描述" prop="iServiceDescription">
                <el-input v-model="form.iServiceDescription" placeholder="服务接口描述" clearable maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two" align="right">
              <el-button size="small" icon="el-icon-refresh-right" @click="initialTemplateContent(1003)">刷新</el-button>
              <el-button v-clipboard:copy="templateTxt" v-clipboard:success="clipboardSuccess" size="small" icon="el-icon-document">复制</el-button>
              <el-button size="small" icon="el-icon-folder-opened" @click="downloadCodeFile(1003)">下载</el-button>
            </el-col>
          </el-row>

          <pre v-loading="loadingFlag" v-highlightjs>
            <code class="java" v-text="templateTxt" />
          </pre>
        </el-tab-pane>

        <el-tab-pane label="Impl服务实现" name="serviceImplTab">
          <el-row :gutter="$ui.layout.gutter.g10">
            <el-col :span="$ui.layout.span.two">
              <el-form-item label="服务实现名称" prop="serviceImplName">
                <el-input v-model="form.serviceImplName" placeholder="服务实现名称" clearable maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two">
              <el-form-item label="包路径" prop="serviceImplPath" label-width="70px">
                <el-input v-model="form.serviceImplPath" placeholder="包路径" clearable maxlength="128" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two">
              <el-form-item label="服务实现描述" prop="serviceImplDescription">
                <el-input v-model="form.serviceImplDescription" placeholder="服务实现描述" clearable maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two" align="right">
              <el-button size="small" icon="el-icon-refresh-right" @click="initialTemplateContent(1004)">刷新</el-button>
              <el-button v-clipboard:copy="templateTxt" v-clipboard:success="clipboardSuccess" size="small" icon="el-icon-document">复制</el-button>
              <el-button size="small" icon="el-icon-folder-opened" @click="downloadCodeFile(1004)">下载</el-button>
            </el-col>
          </el-row>

          <pre v-loading="loadingFlag" v-highlightjs>
            <code class="java" v-text="templateTxt" />
          </pre>
        </el-tab-pane>

        <el-tab-pane label="Controller控制器" name="controllerTab">
          <el-row :gutter="$ui.layout.gutter.g10">
            <el-col :span="$ui.layout.span.two">
              <el-form-item label="控制器名称" prop="serviceImplName" label-width="100px">
                <el-input v-model="form.controllerName" placeholder="控制器名称" clearable maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two">
              <el-form-item label="控制器路径" prop="urlPath" label-width="100px">
                <el-input v-model="form.urlPath" placeholder="控制器路径" clearable maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two">
              <el-form-item label="包路径" prop="controllerPath" label-width="100px">
                <el-input v-model="form.controllerPath" placeholder="包路径" clearable maxlength="128" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two">
              <el-form-item label="控制器描述" prop="controllerDescription" label-width="100px">
                <el-input v-model="form.controllerDescription" placeholder="控制器描述" clearable maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two" align="left">
              <el-button size="small" icon="el-icon-refresh-right" @click="initialTemplateContent(1005)">刷新</el-button>
              <el-button v-clipboard:copy="templateTxt" v-clipboard:success="clipboardSuccess" size="small" icon="el-icon-document">复制</el-button>
              <el-button size="small" icon="el-icon-folder-opened" @click="downloadCodeFile(1005)">下载</el-button>
            </el-col>
          </el-row>

          <pre v-loading="loadingFlag" v-highlightjs>
            <code class="java" v-text="templateTxt" />
          </pre>
        </el-tab-pane>

        <el-tab-pane label="ApiJs接口" name="apiJsTab">
          <el-row :gutter="$ui.layout.gutter.g10">
            <el-col :span="$ui.layout.span.two">
              <el-form-item label="接口文件名" prop="apiName" label-width="100px">
                <el-input v-model="form.apiName" placeholder="接口文件名" clearable maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>

            <el-col :span="$ui.layout.span.two" align="right">
              <el-button v-clipboard:copy="templateTxt" v-clipboard:success="clipboardSuccess" size="small" icon="el-icon-document">复制</el-button>
              <el-button size="small" icon="el-icon-folder-opened" @click="downloadCodeFile(2001)">下载</el-button>
            </el-col>
          </el-row>

          <pre v-loading="loadingFlag" v-highlightjs>
            <code class="java" v-text="templateTxt" />
          </pre>
        </el-tab-pane>

        <el-tab-pane label="View组件" name="viewTab">
          Api JS接口
          <pre v-loading="loadingFlag" v-highlightjs>
            <code class="java" v-text="templateTxt" />
          </pre>
        </el-tab-pane>
      </el-tabs>
    </el-form>
  </div>
</template>

<script>
import { axiosDownloadFile, horizonBarToLine, toHump } from '@/utils'
import {
  downloadAllTemplate,
  downloadTemplate,
  getTemplateContent,
  getTemplateTypeList
} from '@/api/tt-server/tool-lib/code-template'
import clipboard from '@/directive/clipboard'

export default {
  name: 'CodeTemplateView',
  directives: { clipboard },
  props: {
    param: {
      type: Object,
      required: true,
      default: () => {}
    }
  },
  data() {
    return {
      activeTabLabel: 'modelTab',
      formRef: 'formRefKey',
      form: {
        author: 'OnCloud9',
        projectName: 'tt-server',
        basePackagePath: '',
        /* model */
        modelPath: '',
        modelDescription: '',
        modelName: '',

        /* mapper */
        mapperPath: '',
        mapperDescription: '',
        mapperName: '',

        /* iService */
        iServicePath: '',
        iServiceDescription: '',
        iServiceName: '',

        /* serviceImpl */
        serviceImplPath: '',
        serviceImplDescription: '',
        serviceImplName: '',

        /* controller */
        controllerPath: '',
        controllerDescription: '',
        controllerName: '',
        urlPath: '',

        /* ApiJs */
        apiName: ''
      },
      formRules: {
        author: [{ required: true, trigger: 'blur', message: '请填写作者名称' }],
        projectName: [{ required: true, trigger: 'blur', message: '请填写项目名称' }],
        basePackagePath: [{ required: true, trigger: 'blur', message: '请填写基础包路径' }],

        modelPath: [{ required: true, trigger: 'blur', message: '包路径' }],
        modelName: [{ required: true, trigger: 'blur', message: '请填写实体类名称' }],
        mapperPath: [{ required: true, trigger: 'blur', message: '包路径' }],
        mapperName: [{ required: true, trigger: 'blur', message: '请填写Mapper名称' }],
        iServicePath: [{ required: true, trigger: 'blur', message: '包路径' }],
        iServiceName: [{ required: true, trigger: 'blur', message: '请填写服务接口名称' }],
        serviceImplPath: [{ required: true, trigger: 'blur', message: '包路径' }],
        serviceImplName: [{ required: true, trigger: 'blur', message: '请填写服务实现名称' }],
        controllerPath: [{ required: true, trigger: 'blur', message: '包路径' }],
        controllerName: [{ required: true, trigger: 'blur', message: '请填写控制器名称' }],
        urlPath: [{ required: true, trigger: 'blur', message: '请填写控制器路径' }],
        apiName: [{ required: true, trigger: 'blur', message: '请填写控制器路径' }]
      },
      /* templateCodeMap */
      templateCodeMap: {
        modelTab: 1001,
        mapperTab: 1002,
        iServiceTab: 1003,
        serviceImplTab: 1004,
        controllerTab: 1005,
        apiJsTab: 2001,
        viewTab: 2002
      },
      inputStyle: { width: '240px' },
      ttList: [],
      templateTxt: '',
      loadingFlag: false
    }
  },
  async created() {
    console.log(this.param)
    this.initialBasicParam()
    await this.initialTemplateContent(1001)
  },
  methods: {
    async initialTemplateContent(templateCode) {
      this.loadingFlag = true
      const param = { ... this.form, ... this.param, templateCode }
      const { data: content } = await getTemplateContent(param)
      this.templateTxt = content
      this.loadingFlag = false
    },
    async downloadAllCodeFile() {
      const param = { ... this.form, ... this.param }
      const res = await downloadAllTemplate(param)
      axiosDownloadFile(res)
    },
    async downloadCodeFile(templateCode) {
      const param = { ... this.form, ... this.param, templateCode }
      const res = await downloadTemplate(param)
      console.log(res)
      axiosDownloadFile(res)
    },
    clipboardSuccess() {
      this.$message.success('复制成功')
    },
    handleTabClick(instance) {
      console.log(this.templateCodeMap)
      console.log(this.activeTabLabel)
      const tc = this.templateCodeMap[this.activeTabLabel]
      this.initialTemplateContent(tc)
    },
    basePackagePathChange() {
      this.form.modelPath = `${this.form.basePackagePath}.dto`
      this.form.mapperPath = `${this.form.basePackagePath}.mapper`
      this.form.iServicePath = `${this.form.basePackagePath}.service`
      this.form.serviceImplPath = `${this.form.basePackagePath}.serviceImpl`
      this.form.controllerPath = `${this.form.basePackagePath}.controller`
    },
    initialBasicParam() {
      // dbConnId: 3
      // schemaName: "nacos-config"
      // tableName: "config_info"
      const humpedTableName = toHump(this.param.tableName)
      const firstChar = humpedTableName.charAt(0)
      const className = humpedTableName.replace(firstChar, firstChar.toUpperCase())
      console.log(className)

      /* 类名称设置 */
      this.form.modelName = `${className}DTO`
      this.form.mapperName = `${className}Mapper`
      this.form.iServiceName = `I${className}Service`
      this.form.serviceImplName = `${className}ServiceImpl`
      this.form.controllerName = `${className}Controller`
      this.form.apiName = className

      /* 控制器路径设置 */
      this.form.urlPath = `/${horizonBarToLine(humpedTableName)}`

      /* 基础包路劲设置 */
      this.form.basePackagePath = `cn.cloud9.server.${className.toLowerCase()}`

      /* 包路径设置 */
      this.basePackagePathChange()

      /* 描述设置 */
      this.form.modelDescription =
        this.form.mapperDescription =
        this.form.iServiceDescription =
        this.form.serviceImplDescription =
        this.form.controllerDescription =
        '用于....'
    },
    async initialTemplateTypeList() {
      const { data: ttList } = await getTemplateTypeList()
      this.ttList = ttList
    }
  }
}
</script>

<style scoped>
  pre {
    margin: 0 auto;
    padding: 0 !important;
  }
  code {
    border-radius: 5px;
  }
</style>
