import request from '@/utils/request'

export const PATH = '/db-conn'

/**
 * 获取连接列表
 * @returns {*}
 */
export function getDbConnList() {
  return request({
    url: `${PATH}/list`,
    method: 'get'
  })
}

/**
 * 获取连接约束列表
 * /{db-id}/schema-list
 * @returns {*}
 */
export function getDbSchemaList(dbId) {
  return request({
    url: `${PATH}/${dbId}/schema-list`,
    method: 'get'
  })
}

/**
 * 获取连接约束下的表列表
 * @param dbId
 * @param schemaName
 * @returns {*}
 */
export function getDbTableList(dbId, schemaName) {
  return request({
    url: `${PATH}/${dbId}/${schemaName}/table-list`,
    method: 'get'
  })
}

/**
 * 获取连接约束下的表分页
 * @param dbId
 * @param schemaName
 * @param data
 * @returns {*}
 */
export function getDbTablePage(dbId, schemaName, data) {
  return request({
    url: `${PATH}/${dbId}/${schemaName}/table-page`,
    method: 'post',
    data
  })
}

/**
 * Schema文档生成
 * @param dbId
 * @param schemaName
 * @param data
 * @returns {*}
 */
export function getDbSchemaDoc(dbId, schemaName, data) {
  return request({
    url: `${PATH}/${dbId}/${schemaName}/doc`,
    method: 'post',
    responseType: 'blob',
    data
  })
}

/**
 * Schema脚本导出
 * @param dbId
 * @param schemaName
 * @returns {*}
 */
export function exportDbSchema(dbId, schemaName) {
  return request({
    url: `${PATH}/${dbId}/${schemaName}/export`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * Schema备份
 * @param dbId
 * @param schemaName
 * @returns {*}
 */
export function backupDbSchema(dbId, schemaName) {
  return request({
    url: `${PATH}/${dbId}/${schemaName}/backup`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 获取建表语句
 * @param dbId
 * @param schemaName
 * @param tableName
 * @returns {*}
 */
export function getTableSchema(dbId, schemaName, tableName) {
  return request({
    url: `${PATH}/${dbId}/${schemaName}/${tableName}/schema-sql`,
    method: 'get'
  })
}

/**
 * 测试连接信息
 * @returns {*}
 */
export function testDbConn(params) {
  return request({
    url: `${PATH}/test`,
    method: 'get',
    params
  })
}

/**
 * 保存连接信息
 * @returns {*}
 */
export function saveDbConn(data) {
  return request({
    url: `${PATH}/save`,
    method: 'put',
    data
  })
}

/**
 * 更新连接信息
 * @returns {*}
 */
export function updateDbConn(data) {
  return request({
    url: `${PATH}/update`,
    method: 'post',
    data
  })
}

/**
 * 更新连接信息
 * @returns {*}
 */
export function getDbConnById(dbId) {
  return request({
    url: `${PATH}/get/${dbId}`,
    method: 'get'
  })
}

/**
 * 删除连接信息
 * @returns {*}
 */
export function deleteDbConnById(dbId) {
  return request({
    url: `${PATH}/delete/${dbId}`,
    method: 'delete'
  })
}

/**
 * 创建新的Schema
 * @returns {*}
 */
export function createNewDbSchema(dbId, schemaName) {
  return request({
    url: `${PATH}/${dbId}/${schemaName}/save`,
    method: 'put'
  })
}

/**
 * 删除Schema
 * @param dbId
 * @param schemaName
 * @returns {*}
 */
export function deleteDbSchemaById(dbId, schemaName) {
  return request({
    url: `${PATH}/${dbId}/${schemaName}/delete`,
    method: 'delete'
  })
}

/**
 * 删除Table
 * @param dbId
 * @param schemaName
 * @param tableName
 * @returns {*}
 */
export function deleteDbTableById(dbId, schemaName, tableName) {
  return request({
    url: `${PATH}/${dbId}/${schemaName}/${tableName}/delete`,
    method: 'delete'
  })
}
