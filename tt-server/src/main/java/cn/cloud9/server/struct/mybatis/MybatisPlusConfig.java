package cn.cloud9.server.struct.mybatis;

import cn.cloud9.server.struct.datauth.DataAuthHandler;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 06:42
 */
@Configuration
public class MybatisPlusConfig {

    @Resource
    private DataPermissionHandler dataAuthHandler;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        /* 1、追加翻页拦截器 */
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        /* 2、追加数据授权拦截器 */
        interceptor.addInnerInterceptor(new DataPermissionInterceptor(dataAuthHandler));
        return interceptor;
    }
}
