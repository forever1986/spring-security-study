package com.demo.lesson12.utils;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.encrypt.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
@Component
@ConfigurationProperties("rsa")
public class JwtUtil {

    //有效期为
    private final Long JWT_TTL = 60 * 60 * 1000L; // 60 * 60 *1000  一个小时
    //设置秘钥密码
    @Setter
    private String key;
    //设置密钥名称
    @Setter
    private String jks;
    //密钥对
    private KeyPair keyPair;

    public String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 创建token
     */
    public String createToken(String subject) {
        return getJwtBuilder(subject, null, getUUID());// 设置过期时间
    }

    /**
     * 创建token
     */
    public String createToken(String subject, Long ttlMillis) {
        return getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
    }

    /**
     * 创建token
     */
    public String createToken(String id, String subject, Long ttlMillis) {
        return getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
    }

    /**
     * 解析token
     */
    public String parseJWT(String token) throws Exception {
        KeyPair tempKeyPair =  keyPair();
        return Jwts.parser()
                .setSigningKey(tempKeyPair.getPublic())
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    /**
     * 生成token
     */
    private String getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
        KeyPair tempKeyPair =  keyPair();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setId(uuid)            //唯一的ID
                .setSubject(subject)    // 主题  可以是JSON数据
                .setIssuer("spring-security-study")       // 签发者
                .setIssuedAt(now)       // 签发时间
                .signWith(signatureAlgorithm, tempKeyPair.getPrivate())    //使用RS256非对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate)
                .compact();
    }

    /**
     * 密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        if(keyPair!=null)
            return keyPair;
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource(jks), key.toCharArray());
        keyPair = factory.getKeyPair("demo", key.toCharArray());
        return keyPair;
    }

}
