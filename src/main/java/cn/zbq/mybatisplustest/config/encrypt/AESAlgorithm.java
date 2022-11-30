package cn.zbq.mybatisplustest.config.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * AES/ECB/PKCS5Padding
 * 参考：https://github.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-core/src/main/java/com/baomidou/mybatisplus/core/toolkit/AES.java
 * <br/>
 * <p>
 * MySQL中加密 SELECT TO_BASE64(AES_ENCRYPT('明文',FROM_BASE64('密钥')));
 * MySQL中解密 SELECT AES_DECRYPT(FROM_BASE64('密文'),FROM_BASE64('密钥'));
 * </p>
 *
 * @author zbq
 * @since 2022/11/20
 */
public class AESAlgorithm {
    /**
     * 算法 AES/ECB/PKCS5Padding
     */
    private static final String ALGORITHM = "AES";

    /**
     * 加密
     *
     * @param data 待加密内容
     * @param key  密钥
     * @return 密文
     */
    public static String encrypt(String data, String key) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        try {
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @param key  密钥
     * @return /
     */
    public static String decrypt(String data, String key) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        try {
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES密钥生成
     *
     * @return 密钥
     * @throws NoSuchAlgorithmException /
     */
    public static String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128);
        return new String(Base64.getEncoder().encode(keyGenerator.generateKey().getEncoded()));
    }
}
