<template>
  <div style="padding: 20px">
    <el-descriptions :title="form.module" direction="vertical" :column="1" border>
      <el-descriptions-item label="业务类型">{{ form.businessType }}</el-descriptions-item>
      <el-descriptions-item label="模块描述">{{ form.module }}</el-descriptions-item>
      <el-descriptions-item label="接口方法">{{ form.method }}</el-descriptions-item>
      <el-descriptions-item label="接口地址">{{ form.url }}</el-descriptions-item>
      <el-descriptions-item label="请求IP">{{ form.ip }}</el-descriptions-item>
      <el-descriptions-item label="操作系统">{{ form.address }}</el-descriptions-item>
      <el-descriptions-item label="操作人">{{ form.operator }}</el-descriptions-item>
      <el-descriptions-item label="请求参数">{{ form.param }}</el-descriptions-item>
      <el-descriptions-item label="操作结果">{{ form.result }}</el-descriptions-item>
      <el-descriptions-item label="状态">{{ form.status }}</el-descriptions-item>
      <el-descriptions-item label="错误信息">{{ form.errorMessage }}</el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ form.createTime }}</el-descriptions-item>
      <el-descriptions-item label="完成时间">{{ form.finishTime }}</el-descriptions-item>
      <el-descriptions-item label="耗时计算">{{ form.duration }}</el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
import { getSystemLogById } from '@/api/tt-server/system/common/system-log'

export default {
  name: 'SystemLogDetail',
  props: {
    keyId: {
      type: [String, Number],
      require: true,
      default: undefined
    }
  },
  data() {
    return {
      form: {}
    }
  },
  async created() {
    const { data: res } = await getSystemLogById(this.keyId)
    this.form = res
  }
}
</script>
