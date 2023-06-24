package com.bootcamp.rules_engine.error.exception;

import com.bootcamp.rules_engine.enums.ErrorCode;
import lombok.Getter;

@Getter
public class DetailBuilder {
    private final ErrorCode errorCode;
    private final Object[] fields;

    public DetailBuilder(ErrorCode errorCode, Object... fields){
        this.errorCode = errorCode;
        this.fields = fields;
    }
}
