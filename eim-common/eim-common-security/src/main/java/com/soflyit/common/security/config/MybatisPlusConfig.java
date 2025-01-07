package com.soflyit.common.security.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

/**
 * 解决 mybatisplus不识别databaseid问题<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-02 11:41
 */
public class MybatisPlusConfig {

    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.put("Oracle", "oracle");
        properties.put("MySQL", "mysql");
        properties.put("SQL Server", "sqlServer");
        properties.put("PostgreSQL", "pg");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}
