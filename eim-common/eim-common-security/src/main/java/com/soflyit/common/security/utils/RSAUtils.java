package com.soflyit.common.security.utils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密解密
 *
 * @author soflyit
 * @date 2020/8/21 9:36
 */
public class RSAUtils {


    private final static Map<Integer, String> keyMap = new HashMap<>();

    private final static String ALGORITHM = "RSA";

    private final static String CIPHER_ALGORITHM = "RSA";

    private final static String CHARSET = "utf-8";



    private static final int KEY_SIZE = 1024;


    private static final int MAX_BLOCK = KEY_SIZE / 8;

    public static void main(String[] args) throws Exception {

        genKeyPair();

        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
    }


    public static void genKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);

        keyPairGen.initialize(KEY_SIZE, new SecureRandom());

        KeyPair keyPair = keyPairGen.generateKeyPair();

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));

        String privateKeyString = new String(Base64.getEncoder().encode((privateKey.getEncoded())));


        keyMap.put(0, publicKeyString);

        keyMap.put(1, privateKeyString);
    }



    public static String encrypt(String data) throws Exception {
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgGEg5060WH4y90HA/gb12bovHtltCvpILJrFYWVDV71BglhsIrb7rd9IC3ev959dmesnHmtO8ALjsKKh4E+f2jrYDrBvucabbK+DUOUTyBn+4vWFdh1MsDcCBpv8QSLEwqUmO/uvPY+y+4LyUuFi0tZU7kAdFkHZa+5PZbTNOx7ZrObfaJaWD0ScqJ/cKtjWYyo1VSXiUZu1PW0M60Ck3X+oxmzBLobUpH4U/aJhsmkkphIfLY3ZmAn/ECZKo8YotYlbk0sKrJY2+ISlrTvG6ievu0T+G4I4ANEA5VrWMnI4IQQSLNd23AuDNJPeBBK1rDR12Ce2T0PAt2NGQdjUCwIDAQAB";
        return encrypt(publicKey, data);
    }


    public static String encrypt(String publicKey, String data) throws Exception {

        byte[] decoded = Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(decoded));

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] datas = data.getBytes(CHARSET);
        return new String(Base64.getEncoder().encode(cipher.doFinal(datas)));
    }


    public static String decrypt(String data) throws Exception {
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCAYSDnTrRYfjL3QcD+BvXZui8e2W0K+kgsmsVhZUNXvUGCWGwitvut30gLd6/3n12Z6ycea07wAuOwoqHgT5/aOtgOsG+5xptsr4NQ5RPIGf7i9YV2HUywNwIGm/xBIsTCpSY7+689j7L7gvJS4WLS1lTuQB0WQdlr7k9ltM07Htms5t9olpYPRJyon9wq2NZjKjVVJeJRm7U9bQzrQKTdf6jGbMEuhtSkfhT9omGyaSSmEh8tjdmYCf8QJkqjxii1iVuTSwqsljb4hKWtO8bqJ6+7RP4bgjgA0QDlWtYycjghBBIs13bcC4M0k94EErWsNHXYJ7ZPQ8C3Y0ZB2NQLAgMBAAECggEAfN99ROD+dTFT28P+uFkG3KDIYsZ01X+7on9tTJ5WEcsKJIJjtKSuTGLqThU+eatvPV9JbsJMRxNYbCDVOj3ZLEmejTM7icXQurhCD/ay9imrDxJongpD5Qeslcc2aBkssHQ5q5DV1RiorRKjKNEI8jH+7Lw6TsMAfIaSfkiOdFjZJgdcTNCjTeAFZ77q1tORgEoggegMOspxMpXilDtvZav63gM4+sEXwCZ9+eqsiVoRNoep0qEK4udyY313EMFgLxVxZhsTIpNQ9iW6XFu+jOkiIM3BNy/EvQfHkFOqbB8CiR1aa8lN1M6AOYZoROK2UrcgZMfNLakSYnV6IMiWsQKBgQDWLJj6uFQX6LJ0ryBLj1sH3DPmSUEez4RRKr3tZBB+q9RpmuIM4tJg5luDMjTjsRrwfLiWgoQdqpH33hfQ9X/IEs66RELpDTA7vd0lEphiIMyQ45zUEDOVJ1lW5SrKacrVqfrajQ0SCrQraWRijBRa7eclMEUT7HyAHFaH57oqcwKBgQCZc1CcWY+ucpHvJJNGxcTYpM01BPnd2E0t+ZrHL7bX2Adw6SaoJM6MnK+TDMqfX3aZ58RYwOeucUjcSDqRQMjJBcEEdmXBqSOZzl8aJIXdhnTtJGaWxCVbdMBQGM7e8ommAOOv0JOZRtQLNPL0suP2F34n2+DHdmtBqvDvbtnSCQKBgQDPcTDXHvRsl/lJZVpddUORcGpgcbF+WzbouwJszU/gsSOzCc5K9uMX8ebPQzDRHPhf2B2dwlqetVhegSIGRdL3gafz6Cv6uMhwBO0OfF1WJjidWajMCck8fXld3ee9WAybOOF9D8OKJw3tQk512QHaNLV+Yqt1qcfI+imhxwOdGwKBgC7tTvehhAkrTGm/27JHBN3j+EWAkhE/9spajPThKJ91l0D2bDX7m5bwy9jCjqySeyix8h/aTn/QcSG8jPyivUSMl+yPcKr62MJOlFLgXHfUiKmE+kKdFXeUisNz8r/HmETF/Ntlg93aBTqwJKSkmIZHxPLXkTQuiLp6VlhCpCZRAoGAOVBaIAX5smVFE/mchTSg1Orkc2tTLUt1JhTv2UqwBozPUVdyWVkH1CpiKqgTaA/LtLsZpztfPe8E2W9/G0DaanRiEyCnU9R3JvgryG6tIbPZUo9l6geBBfDFmVcN0O3idQ/1TgklUkYppj46mGMCqYRon2Ybzw7M+iqoswNOO8o=";
        return decrypt(privateKey, data);
    }


    public static String decrypt(String privateKey, String data) throws Exception {

        byte[] datas = Base64.getDecoder().decode(data.getBytes(CHARSET));

        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decoded));

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        return new String(cipher.doFinal(datas));
    }

}
