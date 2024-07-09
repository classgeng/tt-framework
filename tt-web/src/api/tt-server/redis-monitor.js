import request from '@/utils/request'

const PATH = '/redis-monitor'

/**
 * 获取Redis信息
 * @returns {*}
 */
export function getRedisInfo() {
  return request({
    url: `${PATH}/info`,
    method: 'get'
  })
}

/**
 * 获取Redis统计信息，按key提供
 * @returns {*}
 */
export function getRedisStatistic(params) {
  return request({
    url: `${PATH}/statistic`,
    method: 'get',
    params
  })
}
