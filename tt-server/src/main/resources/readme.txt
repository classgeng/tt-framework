
- 基础框架部分：
    - SSO单点登录 [JWT实现, 提供免登录注解，参数自动注入当前用户注解]
    - 基础支持资源：
        1、BaseController 继承后默认提供 Request & Response 对象
        2、BaseDTO 自带自增主键，创建人，更新人，创建时间，更新时间
        3、Constant 基础常量接口
        4、ResultMessage 统一的结果信息枚举
    - Cors 跨域配置
    - CXF服务集成，暂定
    - Dict字典 Redis缓存 & 注解自动翻译
    - Dozer Bean对象转换工具
    - 加密解密框架， 需求不明确，暂定
    - 枚举类 Redis 缓存 自动翻译， 半成品
    - Controller异常拦截处理 [ExceptionAdvice]
    - 业务异常 ServiceException
    - 普通文件上传下载 与 大文件上传(逻辑暂定)
    - Lambda学习 暂定
    - Aop 实现日志收集
    - DataMasking 数据脱敏框架实现
    - SpringContextHolder Bean持有器工具类
    - SSE 单向消息推送Socket
    - WebSocket　
    - Response 统一的响应体配置，支持解除响应体包裹
    - 工具包：
        1、加密解密工具
        2、树状结构工具
        3、IP地址解析 （待优化）
        4、断言工具类
        5、日期工具类
        6、HttpUtil 请求工具类
        7、原生JDBC工具类
        8、集合翻页工具类（实现集合像SQL一样操作过滤的问题）
        9、ServletUtil 基本工具类
    - validator JSR303 校验配置
    - xss 过滤防御配置
    - 数据授权 [待完成]
        https://blog.csdn.net/cyy9487/article/details/124823962
        https://segmentfault.com/a/1190000042315187?sort=votes

- 系统功能
    我的账号
        - 更新账号
        - 密码修改
        - 退出登录
    RBAC权限管理
        - 用户管理 [查询，新增，更新，删除，分配角色]
        - 角色管理 [查询，新增，更新，删除，分配菜单，分配权限]
        - 菜单管理 [查询，新增，更新，删除]
        - 权限管理 [查询，新增，更新，删除]
    通用管理
        - 系统日志 [查询，删除]
        - 行政区域 [查询，更新]
        - 字典管理 [查询，新增，更新，删除]
        - 系统参数 [查询，新增，更新，删除]
    硬件信息监控
    Druid数据监控
