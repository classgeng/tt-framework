import request from '@/utils/request'
const Mock = require('mockjs')

export function searchUser(name) {
  return request({
    url: '/vue-element-admin/search/user',
    method: 'get',
    params: { name }
  })
}

export function transactionList(query) {
  // return request({
  //   url: '/vue-element-admin/transaction/list',
  //   method: 'get',
  //   params: query
  // })
  return {
    code: 20000,
    data: {
      total: 20,
      'items|20': [{
        order_no: '@guid()',
        timestamp: +Mock.Random.date('T'),
        username: '@name()',
        price: '@float(1000, 15000, 0, 2)',
        'status|1': ['success', 'pending']
      }]
    }
  }
}
