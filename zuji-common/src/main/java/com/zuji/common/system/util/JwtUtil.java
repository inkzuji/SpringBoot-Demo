package com.zuji.common.system.util;

import com.auth0.jwt.exceptions.JWTDecodeException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;


public class JwtUtil {

    // Token过期时间120分钟（用户登录过期时间是此时间的两倍，以token在reids缓存时间为准）
    public static final long EXPIRE_TIME = 7200;

    /**
     * JWT 加密解密密钥
     */
    private static final String JWT_SECRET = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";

    /**
     * JWT的唯一标识
     */
    private static final String JWT_ID = "tokenId";


    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token) {
        return null != verifyJWT(token);
    }

    /**
     * 生成JWT, 无过期时间
     *
     * @param claims 需要加密的参数对象
     * @return String
     */
    public static String createJWT(Map<String, Object> claims) {
        return createJWT(claims, null);
    }

    /**
     * 生成JWT，带过期时间
     *
     * @param claims 需要加密的参数对象
     * @param time   过期时间
     * @return String
     */
    public static String createJWT(Map<String, Object> claims, Long time) {
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();

        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();

        //下面就是在为payload添加各种标准声明和私有声明了
        // 这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(JWT_ID)
                // iat: jwt的签发时间
                .setIssuedAt(new Date())
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(algorithm, secretKey);
        if (time != null && time >= 0) {
            long expMillis = nowMillis + time;
            Date exp = new Date(expMillis);

            // 设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 验证token
     *
     * @param token 加密串
     * @return Claims
     */
    public static Claims verifyJWT(String token) {
        // 签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key = generalKey();
        Claims claims;
        try {
            // 得到DefaultJwtParser
            claims = Jwts.parser()
                    // 设置签名的秘钥
                    .setSigningKey(key)
                    // 设置需要解析的jwt
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;

    }

    /**
     * 由字符串生成加密key
     *
     * @return SecretKey
     */
    private static SecretKey generalKey() {
        byte[] encodedKey = Base64.decodeBase64(JWT_SECRET);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }


    /**
     * 获取用户userId
     */
    public static String getUserId(String token) {
        try {
            Claims claims = verifyJWT(token);
            return claims.get("userId", String.class);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取用户昵称
     *
     * @return token中包含的用户名
     */
    public static String getEnv(String token) {
        try {
            Claims claims = verifyJWT(token);
            return claims.get("env", String.class);
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
