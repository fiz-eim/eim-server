package com.soflyit.system.service.impl;

import com.soflyit.auth.domain.vo.AppSecretData;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.sign.Base64;
import com.soflyit.common.core.utils.uuid.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static com.soflyit.auth.common.AuthConstants.*;

/**
 * 密钥服务
 *
 * @author Toney
 */
@Slf4j
@Service
public class SecretService {


    public AppSecretData generateSecret(Integer secretType) {
        if (SIGN_ALGORITHM_MD5.equals(secretType)) {

            return generateMD5Secret();
        } else if (SIGN_ALGORITHM_RSA.equals(secretType)) {

            return generateRSASecret();
        }
        throw new BaseException("生成密钥失败，密钥类型无法识别；密钥类型：" + secretType);
    }


    public AppSecretData generateRSASecret() {
        return generateRSASecret(SIGNATURE_RSA_DEFAULT_KEY_SIZE);
    }


    public AppSecretData generateRSASecret(int length) {
        AppSecretData appSecretData = new AppSecretData();

        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(SIGNATURE_ALGORITHM_RSA);
            keyPairGen.initialize(length, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            String publicKeyString = Base64.encode(publicKey.getEncoded());

            String privateKeyString = Base64.encode(privateKey.getEncoded());

            appSecretData.setSecretType(SIGN_ALGORITHM_RSA);
            appSecretData.setPublicSecret(publicKeyString);
            appSecretData.setPrivateSecret(privateKeyString);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        }

        return appSecretData;
    }

    public AppSecretData generateMD5Secret() {
        AppSecretData appSecretData = new AppSecretData();
        String md5Secret = UUID.randomUUID().toString();
        appSecretData.setSecretType(SIGN_ALGORITHM_MD5);
        appSecretData.setPublicSecret(md5Secret);
        return appSecretData;
    }
}
