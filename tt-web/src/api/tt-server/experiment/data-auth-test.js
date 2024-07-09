import request from '@/utils/request'

const PATH = '/api/data-auth-test'

/**
 * 获取数据授权测试翻页
 * @param data
 * @returns {*}
 */
export function getDataAuthTestPage(data) {
  return request({
    url: `${PATH}/page`,
    method: 'post',
    data
  })
}

/**
 * 按Id获取数据授权测试
 * @param id
 * @returns {*}
 */
export function getDataAuthTestById(id) {
  return request({
    url: `${PATH}/${id}`,
    method: 'get'
  })
}

/**
 * 新增数据授权测试
 * @param data
 * @returns {*}
 */
export function addDataAuthTest(data) {
  return request({
    url: `${PATH}/add`,
    method: 'put',
    data
  })
}

/**
 * 更新数据授权测试
 * @param data
 * @returns {*}
 */
export function updateDataAuthTest(data) {
  return request({
    url: `${PATH}/update`,
    method: 'post',
    data
  })
}

/**
 * 删除数据授权测试
 * @param data
 * @returns {*}
 */
export function deleteDataAuthTest(data) {
  return request({
    url: `${PATH}/delete`,
    method: 'delete',
    data
  })
}
