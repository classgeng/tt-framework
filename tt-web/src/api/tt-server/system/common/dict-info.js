import request from '@/utils/request'

const PATH = '/api/dict'

/**
 * 获取字典翻页
 * @param data
 * @returns {*}
 */
export function getDictPage(data) {
  return request({
    url: `${PATH}/page`,
    method: 'post',
    data
  })
}

/**
 * 按Id获取字典
 * @param dictId
 * @returns {*}
 */
export function getDictById(dictId) {
  return request({
    url: `${PATH}/${dictId}`,
    method: 'get'
  })
}

/**
 * 新增字典
 * @param data
 * @returns {*}
 */
export function addDict(data) {
  return request({
    url: `${PATH}/add`,
    method: 'put',
    data
  })
}

/**
 * 更新字典
 * @param data
 * @returns {*}
 */
export function updateDict(data) {
  return request({
    url: `${PATH}/update`,
    method: 'post',
    data
  })
}

/**
 * 删除字典
 * @param data
 * @returns {*}
 */
export function deleteDict(data) {
  return request({
    url: `${PATH}/delete`,
    method: 'delete',
    data
  })
}
