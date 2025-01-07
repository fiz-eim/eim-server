package com.soflyit.system;

import com.soflyit.common.security.annotation.EnableCustomConfig;
import com.soflyit.common.security.annotation.EnableIndustryFeignClients;
import com.soflyit.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.soflyit.common.core.utils.AppUtils.initAppPath;

/**
 * 系统模块
 *
 * @author soflyit
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableIndustryFeignClients
@SpringBootApplication
public class FizSystemApplication {
    public static void main(String[] args) {
        try {
            initAppPath(FizSystemApplication.class);
            SpringApplication.run(FizSystemApplication.class, args);
            System.out.println("(♥◠‿◠)ﾉﾞ  系统模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
