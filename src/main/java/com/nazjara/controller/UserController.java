package com.nazjara.controller;

import com.nazjara.model.User;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

//    @Secured("ROLE_developer")
    @PreAuthorize("hasRole('poweruser') or #id == #jwt.subject")
    @PostAuthorize("returnObject.id == #jwt.subject")
    @GetMapping("/{id}")
    public User get(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return User.builder()
                .id(jwt.getSubject())
                .firstName(jwt.getClaimAsString("given_name"))
                .lastName(jwt.getClaimAsString("family_name"))
                .email(jwt.getClaimAsString("email"))
                .build();
    }
}
