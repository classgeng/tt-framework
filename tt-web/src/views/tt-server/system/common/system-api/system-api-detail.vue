<template>
  <div style="padding: 20px">
    <el-descriptions :title="''" direction="vertical" :column="1" border>
      <el-descriptions-item label="路径列表">{{ detail.apiPath }}</el-descriptions-item>
      <el-descriptions-item label="请求方式列表">{{ detail.methodType }}</el-descriptions-item>
      <el-descriptions-item label="请求头要求">{{ detail.requireHeaders }}</el-descriptions-item>
      <el-descriptions-item label="请求参数要求">{{ detail.requireParams }}</el-descriptions-item>
      <el-descriptions-item label="响应类型">{{ detail.produceMimeType }}</el-descriptions-item>
      <el-descriptions-item label="包路径">{{ detail.handlerPackage }}</el-descriptions-item>
      <el-descriptions-item label="控制器">{{ detail.handlerController }}</el-descriptions-item>
      <el-descriptions-item label="方法">{{ detail.handlerMethodName }}</el-descriptions-item>
      <el-descriptions-item label="方法参数">{{ detail.handlerMethodParams }}</el-descriptions-item>
      <el-descriptions-item label="方法返回">{{ detail.handlerMethodReturn }}</el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
export default {
  name: 'SystemApiDetail',
  props: {
    detail: {
      type: Object,
      require: true,
      default: () => {
        return {
          apiPath: '[/system-api/page]',
          consumeMimeType: '[]',
          handlerController: 'SystemApiController',
          handlerMethodName: 'getSystemApiPage',
          handlerMethodParams: 'cn.cloud9.server.struct.api.SystemApiDTO',
          handlerMethodReturn: 'com.baomidou.mybatisplus.core.metadata.IPage',
          handlerPackage: 'cn.cloud9.server.struct.api',
          methodType: '[POST]',
          page: null,
          produceMimeType: '[]',
          requireHeaders: '[]',
          requireParams: '[]'
        }
      }
    }
  },
  data() {
    return {
    }
  },
  async created() {
    console.log(this.detail)
  }
}
</script>
