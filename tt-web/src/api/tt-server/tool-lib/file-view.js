import request from '@/utils/request'

const PATH = '/file-view'

/**
 * 列出模板类型集合
 * @returns {*}
 */
export function listFiles(data) {
  return request({
    url: `${PATH}/path`,
    method: 'get',
    params: data
  })
}
