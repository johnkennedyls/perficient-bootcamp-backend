package com.bootcamp.rules_engine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ERR_COLUMN_NAME("ERR_COLUMN_NAME", "Error: Column %s contains spaces"),
    ERR_404("ERR_404", "%s with %s: %s not found."),
    ERR_500("ERR_500", "Oops, we ran into an error."),
    ERR_400("ERR_400", "Field %s %s."),
    ERR_403("ERR_403", "Not authorized."),
    ERR_DUPLICATED("ERR_DUPLICATED", "Resource %s with field %s: %s, already exists."),
    ERR_501("ERR_501","Login failed");



    private final String code;
    private final String message;
}
