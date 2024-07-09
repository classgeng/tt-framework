import axios from 'axios'
import { MessageBox, Message, Notification } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

/**
 * create an axios instance
 * 创建 axios实例
 * @type {AxiosInstance} axios实例
 */
const service = axios.create({
  baseURL: process.env.VUE_APP_PROXY_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // 请求超时时间 request timeout
})

const fileHeaders = [
  'text/plain',
  'text/html',
  'application/vnd.ms-word2006ml',
  'text/x-web-markdown',
  'application/x-rar-compressed',
  'application/x-zip-compressed',
  'application/octet-stream',
  'application/zip',
  'multipart/x-zip',
  'text/x-sql'
]

/**
 * request interceptor
 * request 拦截器
 */
service.interceptors.request.use(
  config => {
    // do something before request is sent

    /**
     * 让每个请求携带token--['X-Token']为自定义key
     * 请根据实际情况自行修改
     */
    if (store.getters.token) {
      /* config.headers['X-Token'] = getToken() */
      config.headers['user-token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

/**
 * response interceptor
 * response 拦截器
 */
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    /* 通过response自定义code来标示请求状态 */
    const res = response.data

    const headers = response.headers
    const ct = headers['content-type']
    if (ct && fileHeaders.some(h => ct.indexOf(h) > -1)) return response

    // if the custom code is not 20000, it is judged as an error.
    if (res.code !== 200) {
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      /* 401 未授权的请求 */
      if (res.code === 401) {
        // to re-login
        MessageBox.confirm('您已注销，您可以取消停留在此页面，或重新登录', '确认退出', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    const response = error['response']
    // Message({
    //   message: response['data']['message'],
    //   type: 'error',
    //   duration: 5 * 1000
    // })
    Notification.error({
      title: '操作失败',
      message: response['data']['message'],
      duration: 5 * 1000
    })

    return Promise.reject(error)
  }
)

export default service
