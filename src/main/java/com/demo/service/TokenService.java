package com.demo.service;

import com.demo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${test.jwt.expiration}")
    private String expiration;

    @Value("${test.jwt.secret}")
    private String secret;

    public String generateToken(Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        Date date = new Date();

        long exp = Long.parseLong(expiration);
        Date expiration = new Date(date.getTime() + exp);

        return Jwts.builder()
                .setIssuer("Demo")
                .setSubject(user.getId().toString())
                .setIssuedAt(date)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims body = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.parseLong(body.getSubject());
    }
}
