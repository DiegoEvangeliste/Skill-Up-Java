package com.alkemy.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN("ADMIN", 1L),
    USER("USER", 2L);

    private final String name;
    private final Long id;
}