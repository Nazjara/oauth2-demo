package com.nazjara.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class User {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
}
