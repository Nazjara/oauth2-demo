package com.nazjara.controller;

import com.nazjara.model.Token;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

    @GetMapping
    public Token getToken(@AuthenticationPrincipal Jwt jwt) {
        System.out.println("Received request for a token...");

        return Token.builder()
                .id(jwt.getClaimAsString("sid"))
                .scope(jwt.getClaimAsString("scope"))
                .expirationDate(jwt.getClaimAsInstant("exp"))
                .build();
    }
}
