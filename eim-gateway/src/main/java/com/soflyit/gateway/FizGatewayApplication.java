package com.soflyit.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import static com.soflyit.common.core.utils.AppUtils.initAppPath;

/**
 * 网关启动程序
 *
 * @author soflyit
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FizGatewayApplication {

    public static void main(String[] args) {

        initAppPath(FizGatewayApplication.class);
        SpringApplication.run(FizGatewayApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  网关启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }

}
