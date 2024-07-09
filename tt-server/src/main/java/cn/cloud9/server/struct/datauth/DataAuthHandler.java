package cn.cloud9.server.struct.datauth;

import cn.cloud9.server.struct.datauth.config.DataAuthConfigDTO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author OnCloud9
 * @description 数据授权处理器接口实现
 * @project tt-server
 * @date 2022年12月20日 下午 07:28
 */
@Slf4j
@Component("dataAuthHandler")
public class DataAuthHandler implements DataPermissionHandler {

    /**
     * 获取Sql语句，重新进行条件构造
     * @param where 查询条件表达式对象
     * @param mappedStatementId 映射的语句方法 cn.cloud9.server.struct.log.SystemLogDAO.selectPage
     * @return 查询条件表达式对象
     */
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        log.info("数据授权拦截器执行：where表达式：{}, sql映射id: {}", where, mappedStatementId);
        final DataAuth dataAuth = DataAuthContextHolder.get();
        if (!dataAuth.isActive() || StringUtils.isBlank(dataAuth.getStatementId()) || !mappedStatementId.equals(dataAuth.getStatementId())) return where;

        return expressionRebuild(where, dataAuth);
    }

    /**
     * 重构条件
     * @param where
     * @param dataAuth
     * @return
     */
    private Expression expressionRebuild(Expression where, DataAuth dataAuth) {
        AtomicReference<Expression> whereAtomic = new AtomicReference<>(where);
        final List<DataAuthConfigDTO> configList = dataAuth.getConfigList();
        if (CollectionUtils.isEmpty(configList)) return whereAtomic.get();

        final boolean isNull = Objects.isNull(whereAtomic.get());
        if (isNull) {
            final EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(new LongValue(1L));
            equalsTo.setRightExpression(new LongValue(1L));
            whereAtomic.set(equalsTo);
        }

        for (DataAuthConfigDTO config : configList) {
            final String tableName = config.getTableName();
            final String fieldName = config.getFieldName();
            final String authType = config.getAuthType();
            final List<String> authValue = JSON.parseArray(config.getAuthValue(), String.class);

            if ("date".equals(authType)) {
                /* 日期使用between */
                final Between between = new Between();
                between.setLeftExpression(new Column(tableName + "." + fieldName));
                between.setBetweenExpressionStart(new StringValue(authValue.get(0)));
                between.setBetweenExpressionEnd(new StringValue(authValue.get(1)));
                between.setNot(false);
                whereAtomic.set(new AndExpression(whereAtomic.get(), between));
            } else {
                final InExpression inExpression = new InExpression();
                /* 1、设置IN表达式左边的字段 */
                inExpression.setLeftExpression(new Column(tableName + "." + fieldName));
                whereAtomic.set(new AndExpression(whereAtomic.get(), inExpression));
            }
        }
        return whereAtomic.get();
    }

    /**
     * 构建过滤条件
     *
     * @param where      条件对象
     * @param conditions 条件列表
     * @return net.sf.jsqlparser.expression.Expression
     * @author Guo Shuai
     * @date 2022/7/25
     * @since 1.0
     **/
    public static Expression dataScopeFilter(Expression where, Map<String, Object> conditions) {
        //定义条件
        AtomicReference<Expression> whereAtomic = new AtomicReference<>(where);
        //循环构造条件
        conditions.forEach((key, value) -> {
            //判断value的类型（集合特殊处理）
            if (value instanceof Collection) {
                Collection<?> collection = (Collection<?>) value;
                InExpression expression = new InExpression();
                expression.setLeftExpression(new Column(key));
                //获取条件
                ItemsList itemsList = new ExpressionList(collection.stream().map(String::valueOf).map(StringValue::new).collect(Collectors.toList()));
                expression.setRightItemsList(itemsList);
                //拼接条件
                whereAtomic.set(new AndExpression(whereAtomic.get(), expression));
            } else {
                whereAtomic.set(new AndExpression(whereAtomic.get(), new EqualsTo(new Column(key), new StringValue(String.valueOf(value)))));
            }
        });

        return whereAtomic.get();
    }
}
