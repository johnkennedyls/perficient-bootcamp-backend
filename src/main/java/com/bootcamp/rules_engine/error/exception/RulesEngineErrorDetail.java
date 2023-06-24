package com.bootcamp.rules_engine.error.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class RulesEngineErrorDetail {
    private String errorCode;
    private String errorMessage;
}
