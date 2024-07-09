import request from '@/utils/request'

const PATH = '/sys-monitor'

/**
 * 获取系统硬件信息
 * @returns {*}
 */
export function getSystemHardwareInfo() {
  return request({
    url: `${PATH}/info`,
    method: 'get'
  })
}

export function getCpuInfo() {
  return request({
    url: `${PATH}/cpuInfo`,
    method: 'get'
  })
}

export function getMemoryInfo() {
  return request({
    url: `${PATH}/memoryInfo`,
    method: 'get'
  })
}

export function getGraphicsCardsInfo() {
  return request({
    url: `${PATH}/graphicsCardsInfo`,
    method: 'get'
  })
}

export function getSoundCardInfo() {
  return request({
    url: `${PATH}/soundCardInfo`,
    method: 'get'
  })
}

export function getDisplayInfo() {
  return request({
    url: `${PATH}/getDisplayInfo`,
    method: 'get'
  })
}

export function getFileSystemInfo() {
  return request({
    url: `${PATH}/fileSystemInfo`,
    method: 'get'
  })
}

export function getDiskStoreInfo() {
  return request({
    url: `${PATH}/diskStoreInfo`,
    method: 'get'
  })
}

export function getLogicalVolumeGroupInfo() {
  return request({
    url: `${PATH}/logicalVolumeGroupInfo`,
    method: 'get'
  })
}

export function getUsbDevicesInfo() {
  return request({
    url: `${PATH}/usbDevicesInfo`,
    method: 'get'
  })
}

export function getProcessesInfo() {
  return request({
    url: `${PATH}/processesInfo`,
    method: 'get',
    timeout: 1000 * 30
  })
}

export function getIpStatistics() {
  return request({
    url: `${PATH}/ipStatistics`,
    method: 'get'
  })
}

export function getNetWorkInterfaces() {
  return request({
    url: `${PATH}/netWorkInterfaces`,
    method: 'get'
  })
}

export function getNetworkParamsInfo() {
  return request({
    url: `${PATH}/networkParamsInfo`,
    method: 'get'
  })
}

export function getOperateSystemInfo() {
  return request({
    url: `${PATH}/operateSystemInfo`,
    method: 'get'
  })
}

export function getPowerSourceInfo() {
  return request({
    url: `${PATH}/powerSourceInfo`,
    method: 'get'
  })
}

export function getSensorInfo() {
  return request({
    url: `${PATH}/sensorInfo`,
    method: 'get'
  })
}

export function getJvmMemory() {
  return request({
    url: `${PATH}/jvm-memory`,
    method: 'get'
  })
}

export function getAppThreadInfo() {
  return request({
    url: `${PATH}/thread-info`,
    method: 'get'
  })
}

export function getSystemServicePage(data) {
  return request({
    url: `${PATH}/systemServicePage`,
    method: 'post',
    data
  })
}

export function getJvmInfo() {
  return request({
    url: `${PATH}/jvmInfo`,
    method: 'get'
  })
}

export function getServerSystemInfo() {
  return request({
    url: `${PATH}/serverSysInfo`,
    method: 'get'
  })
}

