package com.eggzin.cliente_service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class JwtUtils {

    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";
    public static final String SECRET_KEY = "@2Qw_i@-RiTkfJ6tgE6FjNE7xRgYVpL7";
    public static final long EXPIRE_DAYS = 1;
    public static final long EXPIRE_HOURS = 0;
    public static final long EXPIRE_MINUTES = 30;

    private JwtUtils() {}

    //Alterado de 'Key' para 'SecretKey' para atender à nova versão do JWT
    private static SecretKey generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private static Date toExpireDate(Date start){
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static JwtToken createToken(String username) {
        Date issuedAt = new Date();
        Date limit = toExpireDate(issuedAt);

        //Expressão alterada para atender à nova versão do JWT
        String token = Jwts.builder()
                .header()
                    .type("JWT")
                    .and()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(limit)
                .signWith(generateKey())
                .compact();

        return new JwtToken(token);
    }

    private static Claims getClaimsFromToken(String token) {
        try{
            //Expressão alterada para atender à nova versão do JWT
            return Jwts.parser()
                    .verifyWith(generateKey()).build()
                    .parseSignedClaims(refactorToken(token)).getPayload();
        } catch (JwtException e) {
            log.error(String.format("Token inválido: %s", e.getMessage()));
        }
        return null;
    }

    public static String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public static boolean isTokenValid(String token) {
        try{
            //Expressão alterada para atender à nova versão do JWT
            Jwts.parser()
                    .verifyWith(generateKey()).build()
                    .parseSignedClaims(refactorToken(token));
            return true;
        } catch (JwtException e) {
            log.error(String.format("Token inválido: %s", e.getMessage()));
        }
        return false;
    }

    private static String refactorToken(String token) {
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }

}
