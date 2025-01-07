package com.soflyit.auth.config;

import com.soflyit.auth.common.BspAuthConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 类说明
 *
 * @author Toney
 * @date 2023-06-26
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthConfig {

    private String encodePwdRule = BspAuthConstant.LOGIN_PWD_ENCODE_RULE_NONE;
}
