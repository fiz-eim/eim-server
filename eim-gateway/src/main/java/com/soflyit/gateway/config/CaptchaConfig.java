package com.soflyit.gateway.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;

/**
 * 验证码配置
 *
 * @author soflyit
 */
@Configuration
public class CaptchaConfig {
    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();

        properties.setProperty(KAPTCHA_BORDER, "yes");

        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");

        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");

        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");

        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "38");

        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCode");

        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");

        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");

        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();

        properties.setProperty(KAPTCHA_BORDER, "yes");

        properties.setProperty(KAPTCHA_BORDER_COLOR, "105,179,90");

        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");

        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");

        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");

        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");

        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");

        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, "com.soflyit.gateway.config.KaptchaTextCreator");

        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");

        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");

        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");

        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");

        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");

        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
