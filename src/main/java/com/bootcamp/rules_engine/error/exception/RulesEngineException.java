package com.bootcamp.rules_engine.error.exception;

import lombok.Getter;

@Getter
public class RulesEngineException extends RuntimeException{
    private final RulesEngineError error;
    public RulesEngineException(String message, RulesEngineError error){
        super(message);
        this.error = error;
    }
}
