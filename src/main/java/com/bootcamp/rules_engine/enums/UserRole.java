package com.bootcamp.rules_engine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    ADMIN("ADMIN"),
    CONSULTANT("CONSULTANT"),
    RESEARCHER("RESEARCHER"),
    EDITOR("EDITOR");
    private final String role;
}
