import request from '@/utils/request'

const PATH = '/template'

/**
 * 列出模板类型集合
 * @returns {*}
 */
export function getTemplateTypeList() {
  return request({
    url: `${PATH}/list`,
    method: 'get'
  })
}

/**
 * 获取模板输出的内容
 * @param data
 * @returns {*}
 */
export function getTemplateContent(data) {
  return request({
    url: `${PATH}/content`,
    method: 'post',
    data
  })
}

/**
 * 下载模板文件
 * @param data
 * @returns {*}
 */
export function downloadTemplate(data) {
  return request({
    url: `${PATH}/download`,
    method: 'post',
    responseType: 'blob',
    data
  })
}

/**
 * 下载全部文件
 * @param data
 * @returns {*}
 */
export function downloadAllTemplate(data) {
  return request({
    url: `${PATH}/download-all`,
    method: 'post',
    responseType: 'blob',
    data
  })
}

