import request from '@/utils/request'

const PATH = '/api/sys-param'

/**
 * 获取系统参数翻页
 * @param data
 * @returns {*}
 */
export function getSystemParamPage(data) {
  return request({
    url: `${PATH}/page`,
    method: 'post',
    data
  })
}

/**
 * 按Id获取系统参数
 * @param sysParamId
 * @returns {*}
 */
export function getSystemParamById(sysParamId) {
  return request({
    url: `${PATH}/${sysParamId}`,
    method: 'get'
  })
}

/**
 * 新增系统参数
 * @param data
 * @returns {*}
 */
export function addSystemParam(data) {
  return request({
    url: `${PATH}/add`,
    method: 'put',
    data
  })
}

/**
 * 更新系统参数
 * @param data
 * @returns {*}
 */
export function updateSystemParam(data) {
  return request({
    url: `${PATH}/update`,
    method: 'post',
    data
  })
}

/**
 * 删除系统参数
 * @param data
 * @returns {*}
 */
export function deleteSystemParam(data) {
  return request({
    url: `${PATH}/delete`,
    method: 'delete',
    data
  })
}
