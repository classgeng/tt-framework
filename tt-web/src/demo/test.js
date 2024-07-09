/* 规约计算处理， 累计每个对象的属性值， prev是上一次操作返回的值，next是迭代的每个元素，对象或者值，0表示初始值 */
const arr = [{ a: 10, b: '' }, { a: 13, b: '' }, { a: 22, b: '' }]
const b = arr.reduce((prev, next) => prev + next.a, 0)

/* Promise.allSettled使用 */
// https://blog.csdn.net/alessia_wang/article/details/127394177
const getData1 = () => { console.log('接口1获取数据') }
const getData2 = () => { console.log('接口2获取数据') }
const toDoList = [getData1, getData2]

// const results = Promise.allSettled(toDoList)
const [res1, res2] = Promise.allSettled(toDoList)

/* 通过fulfilled, 回绝rejected */
const ok = 'fulfilled'
const no = 'rejected' /* 回绝状态 */
if (res1.status === ok && res2.status === ok) {
    // 当列表每个返回的Promise对象的结果都是已完成时，执行
}

/* URL参数封装成对象 https://www.bilibili.com/video/BV1gy4y1M77k/ */ 
const getQueryParams = () => {
    const queryObj = {}

    // 获取url参数 ?A=B&C=D
    const queryString = window.location.search

    // 匹配的正则表达式
    const regexp = /[?&][^?&]+=[^?&]+/g
    
    // 获取匹配的集合 ['?A=B', '&C=D']
    const matched = queryString.match(regexp)

    if (!matched || matched.length === 0) return queryObj

    matched.forEach(m => {
        // 去掉第一个url拼接符号，用等号分割
        let itemArr = m.substring(1).split('=')
        let key = itemArr[0]
        let value = itemArr[1]
        queryObj[key] = value
    })

    return queryObj
}

/* storage命名空间存取 https://www.bilibili.com/video/BV1gy4y1M77k?p=2 */
const saveItemByNameSpace = (nameSpace, key, value) => {
    let storedObj = window.localStorage.getItem(nameSpace)

    // 当store没有时创建，反之活化本地存储
    storedObj = !storedObj ? {} : JSON.parse(storedObj)

    // 赋值
    storedObj[key] = value

    // 序列化
    storedObj = JSON.stringify(storedObj)

    // 存储回去
    window.localStorage.setItem(nameSpace, storedObj)
}

const getItemByNameSpace = (nameSpace, key, value = '') => {
    let storedObj = window.localStorage.getItem(nameSpace)
    if (!storedObj) return value
    storedObj = JSON.parse(storedObj)
    return storedObj[key] || value
}

// 多值包含判断
const x = 'a1'
if (x === 'a1' || x === 'a2' || x === 'a3') {
    // todo ......
}
if (['a1', 'a2', 'a3'].includes(x)) {
    // todo ......
}

// 不同变量赋值处理
let a = 1, b = 2, c = 3
const [a, b, c] = [1, 2, 3]

// 判断执行
if (true) {
    callMethod()
}
true && calMethod()

// 
const test1 = () => console.log('test1')
const test2 = () => console.log('test2')
const test3 = true

if (test3) test1()
else test2()

// 三元赋值匿名函数对象调用
(test3 ? test1 : test2) ()





