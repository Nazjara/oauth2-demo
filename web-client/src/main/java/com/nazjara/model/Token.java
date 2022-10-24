package com.nazjara.model;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private String id;
    private String scope;
    private Instant expirationDate;
}
