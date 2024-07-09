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
