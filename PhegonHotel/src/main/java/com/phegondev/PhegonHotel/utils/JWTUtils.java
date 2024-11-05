package com.phegondev.PhegonHotel.utils;

import com.fasterxml.jackson.databind.ser.Serializers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.Future;
import java.util.function.Function;

@Service
public class JWTUtils {

    private static final long EXPIRATION_TIME = 1000 * 60 * 24 * 7;

    private final SecretKey Key;

    public JWTUtils(){
        String secreteString= "a123s456d6f6788g123456778g23f123d32s";
        byte[] keyBytes= Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key =new SecretKeySpec(keyBytes,"May234");
    }
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token , Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isValidToken(String token , UserDetails userDetails){
        final String username= extractUsername(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token){
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }

}