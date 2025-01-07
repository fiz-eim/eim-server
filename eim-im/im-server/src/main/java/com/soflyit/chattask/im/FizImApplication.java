package com.soflyit.chattask.im;

import com.soflyit.chattask.lib.netty.server.ChatWebSocketServer;
import com.soflyit.common.core.utils.AppUtils;
import com.soflyit.common.security.annotation.EnableCustomConfig;
import com.soflyit.common.security.annotation.EnableIndustryFeignClients;
import com.soflyit.common.swagger.annotation.EnableCustomSwagger2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@EnableCustomConfig
@EnableCustomSwagger2
@EnableIndustryFeignClients
@SpringBootApplication(scanBasePackages = {"com.soflyit"})
@Slf4j
public class FizImApplication {

    public static void main(String[] args) {
        AppUtils.initAppPath(FizImApplication.class);
        loadFont();
        ConfigurableApplicationContext cxt = SpringApplication.run(FizImApplication.class, args);
        startWebsocketServer(cxt);

        System.out.println("(♥◠‿◠)ﾉﾞ  聊天系统启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }

    private static void loadFont() {

        try {
            String configDir = System.getProperty("soflyit-app.log-config.dir");
            String fontFilePath = configDir + "font/SourceHanSans-VF.ttf";
            File fontFile = new File(fontFilePath);
            if (fontFile.exists() && fontFile.isFile()) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            } else {
                log.warn("字体加载失败，请检查字体文件路径：{}", fontFilePath);
            }
        } catch (FontFormatException e) {
            log.warn("字体加载失败，" + e.getMessage(), e);
        } catch (IOException e) {
            log.warn("字体加载失败，" + e.getMessage(), e);
        }
    }


    private static void startWebsocketServer(ConfigurableApplicationContext cxt) {
        ChatWebSocketServer server = cxt.getBean(ChatWebSocketServer.class);
        cxt.getBean("SoflyExecutor", ThreadPoolTaskExecutor.class).execute(server);
    }

}
