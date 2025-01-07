package com.soflyit.chattask.dx;

import com.soflyit.common.core.utils.AppUtils;
import com.soflyit.common.security.annotation.EnableCustomConfig;
import com.soflyit.common.security.annotation.EnableIndustryFeignClients;
import com.soflyit.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCustomConfig
@EnableCustomSwagger2
@EnableIndustryFeignClients
@SpringBootApplication(scanBasePackages = {"com.soflyit"})
public class EimDmsApplication {

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        try {
            AppUtils.initAppPath(EimDmsApplication.class);
            SpringApplication.run(EimDmsApplication.class, args);
            System.out.println("(♥◠‿◠)ﾉﾞ  文档管理系统启动成功   ლ(´ڡ`ლ)ﾞ  \n");
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
