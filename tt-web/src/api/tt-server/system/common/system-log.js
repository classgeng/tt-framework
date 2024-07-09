import request from '@/utils/request'

const PATH = '/api/sys-log'

/**
 * 获取系统日志翻页
 * @param data
 * @returns {*}
 */
export function getSystemLogPage(data) {
  return request({
    url: `${PATH}/page`,
    method: 'post',
    data
  })
}

/**
 * 获取系统日志翻页
 * @returns {*}
 */
export function getBusinessTypeList() {
  return request({
    url: `${PATH}/bus-type-list`,
    method: 'get'
  })
}

/**
 * 按ID获取系统日志
 * @returns {*}
 */
export function getSystemLogById(sysLogId) {
  return request({
    url: `${PATH}/${sysLogId}`,
    method: 'get'
  })
}

/**
 * 清空日志信息
 */
export function flushSystemLog() {
  return request({
    url: `${PATH}/flush`,
    method: 'delete'
  })
}
