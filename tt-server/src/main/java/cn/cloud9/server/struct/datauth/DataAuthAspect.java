package cn.cloud9.server.struct.datauth;

import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.authority.user.UserContextHolder;
import cn.cloud9.server.struct.datauth.config.DataAuthConfigDTO;
import cn.cloud9.server.struct.datauth.config.DataAuthConfigMapper;
import cn.cloud9.server.struct.datauth.map.DataAuthMapDTO;
import cn.cloud9.server.struct.datauth.map.DataAuthMapMapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description 数据授权AOP切点
 * @project tt-server
 * @date 2022年12月20日 下午 07:43
 */
@Slf4j
@Aspect
@Component
public class DataAuthAspect {

    @Resource
    private DataAuthConfigMapper dataAuthConfigMapper;
    @Resource
    private DataAuthMapMapper dataAuthMapMapper;

    @Pointcut("@annotation(cn.cloud9.server.struct.datauth.DataAuthActive)")
    public void dataAuthPointCut() {}

    /**
     * 方法执行前预处理日志
     * @param joinPoint 切点方法对象
     */
    @Before("dataAuthPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        final DataAuthActive daa = getAnnotation(joinPoint);
        final UserContext userCtx = UserContextHolder.getUserContext();

        /* 无授权注解，没有登录用户，用户是管理员，数据权限未开启的情况不执行授权 */
        final boolean noAuth = Objects.isNull(daa);
        final boolean noUser = Objects.isNull(userCtx);
        if (noUser || noAuth || userCtx.isAdmin() || !daa.ident().isActive()) return;

        /* 获取注解标记的标识枚举 */
        final DataAuthIdent dataAuthIdent = daa.ident();

        /* 确保授权新的信息 */
        DataAuthContextHolder.remove();
        final DataAuth dataAuth = DataAuthContextHolder.get();
        dataAuth.setActive(dataAuthIdent.isActive());
        dataAuth.setStatementId(daa.sqlStatementId());
        /* 根据用户id, 业务标识key，获取当前业务授权的配置 */
        final Wrapper<DataAuthMapDTO> queryWrapper = Wrappers.<DataAuthMapDTO>lambdaQuery()
                .eq(DataAuthMapDTO::getUserId, userCtx.getUserId())
                .eq(DataAuthMapDTO::getIdentKey, dataAuthIdent.getIdentKey());
        final List<DataAuthMapDTO> mapList = dataAuthMapMapper.selectList(queryWrapper);
        log.info("当前用户 {}, 业务标识 {}, 授权关联集合 {}", userCtx.getUserName(), dataAuthIdent.getIdentName(), mapList);
        /* 没有配置项时不需要执行 */
        if (CollectionUtils.isEmpty(mapList)) return;

        /* 获取到业务授权的关联记录之后，逐个查询配置项，放入这个用户提供的授权值 */
        List<DataAuthConfigDTO> configList = new ArrayList<>(mapList.size());
        for (DataAuthMapDTO authMap : mapList) {
            final DataAuthConfigDTO config = dataAuthConfigMapper.selectById(authMap.getConfigId());

            /* 这里要解决授权值转换的问题 */
            config.setAuthValue(authMap.getAuthValue());
            configList.add(config);
        }
        /* 授权对象放入配置集合 */
        dataAuth.setConfigList(configList);

        /* 放置授权信息 */
        DataAuthContextHolder.set(dataAuth);
    }

    /**
     * 执行后的处理
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "dataAuthPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        DataAuthContextHolder.remove();
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "dataAuthPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        log.error("授权方法异常：{}", e.getMessage());
        DataAuthContextHolder.remove();
    }


    /**
     * 从切点方法中获取 @DataAuthActive 注解对象
     * @param joinPoint 切点方法对象
     * @return 注解对象
     */
    public DataAuthActive getAnnotation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return method.getAnnotation(DataAuthActive.class);
    }

}
