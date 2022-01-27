package com.accountify.demo.controller;

import com.accountify.demo.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = { "/","/login" })
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/auth")
    public ResponseEntity<String> authenticate(@RequestParam(name = "email") String email,
                                               @RequestParam(name = "password") String password) {
        UsernamePasswordAuthenticationToken loginData = new UsernamePasswordAuthenticationToken(email, password);
        String token = "";
        try {
            Authentication authentication = authenticationManager.authenticate(loginData);
            token = tokenService.generateToken(authentication);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @GetMapping
    public String login() {
        return "login";
    }
}
