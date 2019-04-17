package com.cikers.wechat.mall.modules.app.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;

public class JWTUtil {

    // 过期时间5分钟
    private static final long EXPIRE_TIME = 5000000*60*1000;
    private static final String secret = "cikers";
    /**
     * 校验token是否正确
     * @return 是否正确
     */
    public static boolean verify(HttpServletRequest request) {
        try {
            String token=request.getHeader("token");
            String open_id = JWT.decode(token).getClaim("open_id").asString();
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("open_id", open_id)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getCurrentUsername(HttpServletRequest request) {
        try {
            String token=request.getHeader("token");
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getCurrentUserOpenId(HttpServletRequest request) {
        try {
            String token=request.getHeader("token");
            if (StringTools.isNullOrEmpty(token)){
                return null;
            }
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("open_id").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
    public static String getCurrentUserOpenIdByToken(String token) {
        try {
            //String token=request.getHeader("token");
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("open_id").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
    /**
     * 生成签名,5min后过期
     * @param open_id 用户名
     * @return 加密的token
     */
    public static String sign(String open_id) {
        try {
            Calendar calendar = Calendar.getInstance();
            Date date = new Date(System.currentTimeMillis());
            calendar.setTime(date);
//        calendar.add(Calendar.WEEK_OF_YEAR, -1);
            calendar.add(Calendar.YEAR, 10);
//            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withClaim("open_id", open_id)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


    public static void main( String args[] ) throws UnsupportedEncodingException {
//        String s ="pages/category/category?category=上新频道&articleNumbers=[\"AS201ATZ1\",\"BB9082\",\"BB9426\",\"BB9465\",\"BC0297\",\"BC0311\",\"AU244ATZ1\",\"AU104ATZ1\",\"AV3038-600\",\"AO0571-600\",\"AQ9288-600\",\"AO3269-600\",\"AH7362-077\",\"SC3310-610\",\"SP2160-060\",\"SP2120-610\",\"SP2163-060\",\"SC3888-100\",\"894192-640\",\"AO8997-077\"]&isFilterSize=false&jsonPath=&multipleData=%5B%7B%22title%22%3A%22%22%2C%22isFilterSize%22%3Afalse%2C%22jsonPath%22%3A%22%22%2C%22menuId%22%3A%22%22%2C%22articleNumbers%22%3A%5B%5D%7D%2C%7B%22title%22%3A%22%22%2C%22isFilterSize%22%3Afalse%2C%22jsonPath%22%3A%22%22%2C%22menuId%22%3A%22%22%2C%22articleNumbers%22%3A%5B%5D%7D%5D&isMultiple=false";
        String s = "%E9%A3%9E%E9%B1%BC%E5%8D%87%E7%8F%AD%E9%A9%AC%E6%AD%A3%E5%93%81%E8%B6%B3%E7%90%83%E8%A3%85%E5%A4%87";
                s= URLDecoder.decode(s,"utf8");

        System.out.println(s);
    }
}
