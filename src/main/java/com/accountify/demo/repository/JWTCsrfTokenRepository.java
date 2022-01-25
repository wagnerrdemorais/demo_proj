package com.accountify.demo.repository;

import com.accountify.demo.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

public class JWTCsrfTokenRepository implements CsrfTokenRepository {

    private static final Logger log = LoggerFactory.getLogger(JWTCsrfTokenRepository.class);

    @Value("${test.jwt.secret}")
    private String secret;

    @Value("${test.jwt.expiration}")
    private String expiration;

    Date date = new Date();

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        User userPrincipal = (User) request.getUserPrincipal();

        String id = UUID.randomUUID().toString().replace("-", "");

        long exp = Long.parseLong(expiration);
        Date expiration = new Date(date.getTime() + exp);

        String token;
        token = Jwts.builder()
                .setIssuer("Accountify_demo")
                .setSubject(id)
                .setIssuedAt(date)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", token);
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        //TODO
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        return null; //TODO
    }
}