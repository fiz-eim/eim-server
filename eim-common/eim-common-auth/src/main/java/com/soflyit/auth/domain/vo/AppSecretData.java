package com.soflyit.auth.domain.vo;

import lombok.Data;

/**
 * 密钥数据
 *
 * @author Toney
 */
@Data
public class AppSecretData {

    private Integer secretType;

    private String publicSecret;

    private String privateSecret;

}
