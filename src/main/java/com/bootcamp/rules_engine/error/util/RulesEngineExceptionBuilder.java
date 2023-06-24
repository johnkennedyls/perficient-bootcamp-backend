package com.bootcamp.rules_engine.error.util;

import com.bootcamp.rules_engine.error.exception.DetailBuilder;
import com.bootcamp.rules_engine.error.exception.RulesEngineError;
import com.bootcamp.rules_engine.error.exception.RulesEngineErrorDetail;
import com.bootcamp.rules_engine.error.exception.RulesEngineException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.function.Supplier;

public class RulesEngineExceptionBuilder {
    public static Supplier<RulesEngineException> createRulesEngineException(String message, DetailBuilder... details) {
        return () -> new RulesEngineException(message, createRulesEngineError(message, HttpStatus.BAD_REQUEST, details));
    }

    public static Supplier<RulesEngineException> createRulesEngineException(String message, HttpStatus httpStatus,DetailBuilder... details) {
        return () -> new RulesEngineException(message, createRulesEngineError(message, httpStatus, details));
    }

    public static RulesEngineError createRulesEngineError(String message, HttpStatus httpStatus, DetailBuilder... details){
        return RulesEngineError.builder().status(httpStatus)
                .details(
                        Arrays.stream(details)
                                .map(RulesEngineExceptionBuilder::mapToRulesEngineErrorDetail)
                                .toList()
                ).build();
    }

    public static RulesEngineErrorDetail mapToRulesEngineErrorDetail(DetailBuilder detailBuilder) {
        return RulesEngineErrorDetail.builder()
                .errorCode(detailBuilder.getErrorCode().getCode())
                .errorMessage(detailBuilder.getErrorCode().getMessage().formatted(detailBuilder.getFields()))
                .build();

    }

}
