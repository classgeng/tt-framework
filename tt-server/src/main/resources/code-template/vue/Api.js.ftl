import request from '@/utils/request'

const PATH = '/api${urlPath}'

/**
 * 翻页
 * @param data
 * @returns {*}
 */
export function get${modelName}Page(data) {
  return request({
    url: `${r"${PATH}"}/page`,
    method: 'post',
    data
  })
}

/**
 * 按Id获取
 * @param Id
 * @returns {*}
 */
export function get${modelName}ById(id) {
  return request({
    url: `${r"${PATH}"}/get/${r"${id}"}`,
    method: 'get'
  })
}

/**
 * 保存
 * @param data
 * @returns {*}
 */
export function save${modelName}(data) {
  return request({
    url: `${r"${PATH}"}/save`,
    method: 'put',
    data
  })
}

/**
 * 更新
 * @param data
 * @returns {*}
 */
export function update${modelName}ById(data) {
  return request({
    url: `${r"${PATH}"}/update`,
    method: 'post',
    data
  })
}

/**
 * 删除
 * @param data
 * @returns {*}
 */
export function delete${modelName}ById(data) {
  return request({
    url: `${r"${PATH}"}/delete`,
    method: 'delete',
    data
  })
}
