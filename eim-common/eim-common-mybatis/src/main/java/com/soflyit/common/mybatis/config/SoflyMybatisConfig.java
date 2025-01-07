package com.soflyit.common.mybatis.config;

import com.soflyit.common.mybatis.interceptor.AutoFillInterceptor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * mybastis 配置<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-01 10:19
 */
@Configuration
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class SoflyMybatisConfig {

    private List<SqlSessionFactory> sqlSessionFactoryList;

    @PostConstruct
    private void addInterceptor() {
        if (CollectionUtils.isNotEmpty(sqlSessionFactoryList)) {
            AutoFillInterceptor autoFillInterceptor = new AutoFillInterceptor();
            for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
                sqlSessionFactory.getConfiguration().addInterceptor(autoFillInterceptor);
            }
        }
    }

    @Autowired
    public void setSqlSessionFactoryList(List<SqlSessionFactory> sqlSessionFactoryList) {
        this.sqlSessionFactoryList = sqlSessionFactoryList;
    }


}
