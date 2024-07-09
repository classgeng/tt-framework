<template>
  <div>
    <el-row :gutter="$ui.layout.gutter.g10">
      <el-col :span="$ui.layout.span.one" style="background-color: #FFFFFF; padding: 10px;">
        <el-form ref="form" :model="form" label-width="80px" size="small" :inline="true">
          <el-form-item label="生成数量">
            <el-input-number v-model="form.number" :min="1" :max="999" label="描述文字" />
          </el-form-item>

          <el-form-item label="数值范围">
            <el-input-number v-model="form.start" :min="1" :max="9999999999" label="描述文字" /> ~
            <el-input-number v-model="form.end" :min="1" :max="9999999999" label="描述文字" />
          </el-form-item>

          <el-form-item>
            <el-button size="mini" type="primary" @click="spawn">生成</el-button>
          </el-form-item>
        </el-form>

      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  name: 'Index',
  data() {
    return {
      form: {
        number: 1,
        start: 1,
        end: 100
      }
    }
  },
  methods: {
    spawn() {

    }
  }
}
</script>
