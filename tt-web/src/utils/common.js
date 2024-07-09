/**
 * 检查入参值的数据类型
 * @param val 入参值
 * @returns {string}
 */
export function typeCheck(val) {
  return Object.prototype.toString.call(val)
}
