package com.soflyit.auth;

import com.soflyit.common.security.annotation.EnableIndustryFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import static com.soflyit.common.core.utils.AppUtils.initAppPath;

/**
 * 认证授权中心
 *
 * @author soflyit
 */
@EnableIndustryFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FizAuthApplication {
    public static void main(String[] args) {
        initAppPath(FizAuthApplication.class);
        SpringApplication.run(FizAuthApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  认证授权中心启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
