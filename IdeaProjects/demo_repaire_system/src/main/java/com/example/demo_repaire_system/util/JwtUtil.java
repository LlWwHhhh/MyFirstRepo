package com.example.demo_repaire_system.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

//创建
@Component
public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);//加密
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;//有效时间

    public static String generateToken(String studentAccount){
        return Jwts.builder()
                .setSubject(studentAccount)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith( SECRET_KEY)
                .compact();
    }

    //解密
    public static Claims validateTokenAndGetClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
