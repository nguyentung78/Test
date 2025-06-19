package com.ra.demo.security.jwt;

import com.ra.demo.security.UserPrinciple;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    @Value("${EXPIRED}")
    private Long EXPIRED;

    private Logger logger = LoggerFactory.getLogger(JwtProvider.class); // Sửa logger để khớp với class hiện tại

    public String generateToken(UserPrinciple userPrinciple) {
        // Tạo thời gian sống của token
        Date dateExpiration = new Date(new Date().getTime() + EXPIRED);
        return Jwts.builder()
                .setSubject(userPrinciple.getUsername())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setExpiration(dateExpiration)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | IllegalArgumentException exception) {
            logger.error("Invalid token: {}", exception.getMessage());
        }
        return false;
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}