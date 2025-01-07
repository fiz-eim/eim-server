package com.soflyit.auth.service;

import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.sign.Base64;
import com.soflyit.system.api.domain.SysAuthApp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static com.soflyit.auth.common.AuthConstants.*;

/**
 * 签名验证服务
 */
@Service
@Slf4j
public class BspSignService {


    public Boolean validateSignature(SysAuthApp app, String nonceStr, String signature) {

        if (app == null) {
            log.error("签名验证失败：应用信息不能为空");
            return Boolean.FALSE;
        } else if (StringUtils.isEmpty(nonceStr)) {
            log.error("签名验证失败：随机字符串不能为空");
            return Boolean.FALSE;
        } else if (StringUtils.isEmpty(signature)) {
            log.error("签名验证失败：签名字符串不能为空");
            return Boolean.FALSE;
        }
        if (app.getSecretType() == SIGN_ALGORITHM_MD5) {
            String sign = sign(app.getSecretPub(), nonceStr, SIGN_ALGORITHM_MAP.get(app.getSecretType()));
            if (!StringUtils.equals(signature, sign)) {
                log.error("签名验证失败：签名不一致。原签名：{}， 签名：{}", signature, sign);
                return Boolean.FALSE;
            }
        } else if (app.getSecretType() == SIGN_ALGORITHM_RSA) {
            String sign = sign(app.getSecretPrivate(), nonceStr, SIGNATURE_ALGORITHM_RSA);
            log.warn("ras sign:{}, \nsrc sign:", sign, signature);
            return verifySignByPublicKey(nonceStr, signature, app.getSecretPub());
        }

        return Boolean.TRUE;
    }


    public String sign(String secret, String nonceStr, String secretType) {

        if (secretType == SIGNATURE_ALGORITHM_MD5) {
            return doMd5Sign(secret, nonceStr);
        } else if (secretType == SIGNATURE_ALGORITHM_RSA) {
            return doRSASign(secret, nonceStr);
        } else {
            throw new BaseException("无法失败签名算法:" + secretType);
        }
    }

    private String doRSASign(String secret, String nonceStr) {
        return signByPrivateKey(nonceStr, secret);
    }

    private PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes = Base64.decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(SIGNATURE_ALGORITHM_RSA);
        return keyFactory.generatePublic(keySpec);
    }


    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes = Base64.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(SIGNATURE_ALGORITHM_RSA);
        return keyFactory.generatePrivate(keySpec);
    }


    public static String signByPrivateKey(String content, String privateKey) {
        try {
            PrivateKey priKey = getPrivateKey(privateKey);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM_SHA1_RSA);
            signature.initSign(priKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception e) {
            log.warn("sign error, content: {}, priKey: {}", content, privateKey);
            log.error("sign error, message is {}", e.getMessage());
        }
        return null;
    }



    public boolean verifySignByPublicKey(String content, String sign, String publicKey) {
        try {
            PublicKey pubKey = getPublicKey(publicKey);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM_SHA1_RSA);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.decode(sign.replace(" ", "")));
        } catch (Exception e) {
            log.warn("sign error, content: {}, sign: {}, pubKey: {}", content, sign, publicKey);
            log.error("sign error", e);
        }
        return false;
    }

    private String doMd5Sign(String secret, String nonceStr) {
        try {
            MessageDigest md = MessageDigest.getInstance(SIGNATURE_ALGORITHM_MD5);
            md.update(nonceStr.getBytes(StandardCharsets.UTF_8));
            if (StringUtils.isNotEmpty(secret)) {
                md.update(secret.getBytes(StandardCharsets.UTF_8));
            }
            byte[] digestBytes = md.digest();
            return Hex.encodeHexString(digestBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
