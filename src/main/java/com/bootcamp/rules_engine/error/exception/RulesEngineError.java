package com.bootcamp.rules_engine.error.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
public class RulesEngineError {
    private HttpStatus status;
    private List<RulesEngineErrorDetail> details;
}
