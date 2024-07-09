import request from '@/utils/request'

const PATH = '/api/area'

/**
 * 获取区域树
 * @param data
 * @returns {*}
 */
export function getAreaTree(data) {
  return request({
    url: `${PATH}/tree`,
    method: 'get',
    data
  })
}

/**
 * 获取区域翻页
 * @param data
 * @returns {*}
 */
export function getAreaPage(data) {
  return request({
    url: `${PATH}/page`,
    method: 'post',
    data
  })
}
