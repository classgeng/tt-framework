import request from '@/utils/request'

const PATH = '/system-api'

/**
 * 获取系统接口翻页
 * @param data
 * @returns {*}
 */
export function getSystemApiPage(data) {
  return request({
    url: `${PATH}/page`,
    method: 'post',
    data
  })
}
