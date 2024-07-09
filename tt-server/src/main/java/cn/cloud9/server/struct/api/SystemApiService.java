package cn.cloud9.server.struct.api;

import cn.cloud9.server.struct.util.ListPageUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.dozermapper.core.util.ReflectionUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月03日 下午 08:28
 */
@Slf4j
@Service("systemApiService")
public class SystemApiService {

    @Resource
    private WebApplicationContext webApplicationContext;

    private static final List<SystemApiDTO> apiList = new ArrayList<>();

    private static SystemApiDTO queryCondition;

    @PostConstruct
    private void init() {
        if (CollectionUtils.isNotEmpty(apiList)) return;
        final RequestMappingHandlerMapping mapping = webApplicationContext.getBean(RequestMappingHandlerMapping.class);
        final Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.keySet().forEach(info -> {
            final SystemApiDTO dto = new SystemApiDTO();

            final HandlerMethod handlerMethod = map.get(info);

            /* Controller与方法的信息 */
            final Method method = handlerMethod.getMethod();
            dto.setHandlerMethodName(method.getName());
            final Class<?> beanType = handlerMethod.getBeanType();
            dto.setHandlerPackage(beanType.getPackage().getName());
            dto.setHandlerController(beanType.getSimpleName());
            final String params = Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.joining(","));
            dto.setHandlerMethodParams(params);
            final String returnType = method.getReturnType().getName();
            dto.setHandlerMethodReturn(returnType);

            /* 接口地址 */
            final PatternsRequestCondition patternsCondition = info.getPatternsCondition();
            final Set<String> patterns = patternsCondition.getPatterns();
            dto.setApiPath(patterns.toString());

            /* 请求MimeType条件 */
            final ConsumesRequestCondition consumesCondition = info.getConsumesCondition();
            final Set<MediaType> consumableMediaTypes = consumesCondition.getConsumableMediaTypes();
            final String cmtString = consumableMediaTypes.stream().map(MimeType::getType).collect(Collectors.toList()).toString();
            dto.setConsumeMimeType(cmtString);

            /* 响应MimeType条件 */
            final ProducesRequestCondition producesCondition = info.getProducesCondition();
            final Set<MediaTypeExpression> producesConditionExpressions = producesCondition.getExpressions();
            final String rceString = producesConditionExpressions.stream().map(rce -> rce.getMediaType().getType()).collect(Collectors.toList()).toString();
            dto.setProduceMimeType(rceString);

            /* 请求头信息要求 */
            final HeadersRequestCondition headersCondition = info.getHeadersCondition();
            final Set<NameValueExpression<String>> headersConditionExpressions = headersCondition.getExpressions();
            final String hceString = headersConditionExpressions.stream().map(hce -> "name: " + hce.getName() + ", value: " + hce.getValue()).collect(Collectors.toList()).toString();
            dto.setRequireHeaders(hceString);

            /* 请求参数要求 */
            final ParamsRequestCondition paramsCondition = info.getParamsCondition();
            final Set<NameValueExpression<String>> paramsConditionExpressions = paramsCondition.getExpressions();
            final String pceString = paramsConditionExpressions.stream().map(pce -> "name: " + pce.getName() + ", value: " + pce.getValue()).collect(Collectors.toList()).toString();
            dto.setRequireParams(pceString);

            /* 请求方式要求 */
            final RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            final Set<RequestMethod> methods = methodsCondition.getMethods();
            dto.setMethodType(methods.toString());

            apiList.add(dto);
        });
    }

    public IPage<SystemApiDTO> getSystemApiPage(SystemApiDTO queryCondition) {
        SystemApiService.queryCondition = queryCondition;
        final List<SystemApiDTO> filtered = apiList.stream().filter(this::queryWrapper).collect(Collectors.toList());

        return ListPageUtil.returnPage(queryCondition.getPage(), filtered);
    }

    private boolean queryWrapper(SystemApiDTO row) {
        List<Boolean> resList = new ArrayList<>();
        resList.add(apiPathLike(row));
        resList.add(handlerMethodNameLike(row));
        resList.add(handlerControllerLike(row));
        resList.add(handlerPackageLike(row));
        return resList.stream().allMatch(b -> b);
    }

    /**
     * 接口地址模糊查询
     * @param row
     * @return
     */
    private boolean apiPathLike(SystemApiDTO row) {
        final String apiPath = row.getApiPath();
        final boolean isEmptyApiPath = StringUtils.isBlank(queryCondition.getApiPath());
        if (isEmptyApiPath) return true;
        return StringUtils.isNotBlank(apiPath) && apiPath.contains(queryCondition.getApiPath());
    }

    /**
     * 处理器方法模糊查询
     * @param row
     * @return
     */
    private boolean handlerMethodNameLike(SystemApiDTO row) {
        final String handlerMethodName = row.getHandlerMethodName();
        final boolean isEmptyHmn = StringUtils.isBlank(queryCondition.getHandlerMethodName());
        if (isEmptyHmn) return true;
        return StringUtils.isNotBlank(handlerMethodName) && handlerMethodName.contains(queryCondition.getHandlerMethodName());
    }

    /**
     * 处理器所在类模糊查询
     * @param row
     * @return
     */
    private boolean handlerControllerLike(SystemApiDTO row) {
        final String handlerController = row.getHandlerController();
        final boolean isEmptyHc = StringUtils.isBlank(queryCondition.getHandlerController());
        if (isEmptyHc) return true;
        return StringUtils.isNotBlank(handlerController) && handlerController.contains(queryCondition.getHandlerController());
    }

    /**
     * 处理器所在包模糊查询
     * @param row
     * @return
     */
    private boolean handlerPackageLike(SystemApiDTO row) {
        final String handlerPackage = row.getHandlerPackage();
        final boolean isEmptyHc = StringUtils.isBlank(queryCondition.getHandlerPackage());
        if (isEmptyHc) return true;
        return StringUtils.isNotBlank(handlerPackage) && handlerPackage.contains(queryCondition.getHandlerPackage());
    }
}
