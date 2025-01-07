package com.soflyit.common.mybatis.interceptor;

import com.baomidou.mybatisplus.core.MybatisParameterHandler;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.util.Properties;

/**
 * 自动填充拦截器<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-01 09:49
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AutoFillInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object paramterObject = args[1];
        MybatisParameterHandler mybatisParameterHandler = new MybatisParameterHandler(mappedStatement, paramterObject, null);
        mybatisParameterHandler.processParameter(paramterObject);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
