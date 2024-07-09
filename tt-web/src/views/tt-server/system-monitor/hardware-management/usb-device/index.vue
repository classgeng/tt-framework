<template>
  <div>
    <el-table
      v-loading="loadingFlag"
      :tree-props="treeTableProps"
      row-key="uniqueDeviceId"
      default-expand-all
      :height="height"
      border
      :data="usbDevices"
      style="width: 100% "
    >
      <el-table-column type="index" width="50" label="序号" align="center" />
      <el-table-column prop="name" label="设备名称" min-width="440" show-overflow-tooltip />
      <el-table-column prop="vendor" label="供应商" min-width="140" show-overflow-tooltip />
      <el-table-column prop="vendorId" label="供应商Id" min-width="100" show-overflow-tooltip />
      <el-table-column prop="productId" label="产品Id" min-width="100" show-overflow-tooltip />
      <el-table-column prop="uniqueDeviceId" label="唯一设备id" min-width="180" show-overflow-tooltip />
      <el-table-column prop="serialNumber" label="序列号" min-width="180" show-overflow-tooltip />
    </el-table>

  </div>
</template>

<script>
import { getUsbDevicesInfo } from '@/api/tt-server/sys-monitor'

export default {
  name: 'Index',
  data() {
    return {
      loadingFlag: false,
      height: `${document.documentElement.clientHeight - 140}px`,
      treeTableProps: { children: 'connectedDevices', hasChildren: 'hasChildren' },
      usbDevices: []
    }
  },
  created() {
    this.initialUsbDevicesInfo()
  },
  methods: {
    async initialUsbDevicesInfo() {
      this.loadingFlag = true
      const { data: usbDevices } = await getUsbDevicesInfo()
      this.usbDevices = usbDevices
      this.loadingFlag = false
    }
  }
}
</script>
