package com.bootcamp.rules_engine.error;

import com.bootcamp.rules_engine.enums.ErrorCode;
import com.bootcamp.rules_engine.error.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

import static com.bootcamp.rules_engine.error.util.RulesEngineExceptionBuilder.createRulesEngineError;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RulesEngineException.class)
    public ResponseEntity<RulesEngineError> handleAccountSystemException(RulesEngineException icesiException){
        return ResponseEntity.status(icesiException.getError().getStatus()).body(icesiException.getError());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RulesEngineError> handleRuntimeException(RuntimeException runtimeException){
        var error = createRulesEngineError(runtimeException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, new DetailBuilder(ErrorCode.ERR_500));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RulesEngineError> handleBadCredentialsException(BadCredentialsException exception){
        var error = createRulesEngineError(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, new DetailBuilder(ErrorCode.ERR_501));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RulesEngineError> handleValidationExceptions( MethodArgumentNotValidException ex) {
        var errorBuilder = RulesEngineError.builder().status(HttpStatus.BAD_REQUEST);
        var details = ex.getBindingResult().getAllErrors().stream().map(this::mapBindingResultToError).toList();
        var error = errorBuilder.details(details).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(ColumnNameException.class)
    public ResponseEntity<RulesEngineErrorDetail> handleColumnNameException(ColumnNameException ex) {
        RulesEngineErrorDetail errorDetail = new RulesEngineErrorDetail(ex.getMessage(), "ERR_COLUMN_NAME");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RulesEngineErrorDetail> handleException(Exception ex) {
        RulesEngineErrorDetail errorDetail = new RulesEngineErrorDetail("Internal Server Error", "ERR_500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
    }

    @ExceptionHandler(InvalidTableDataException.class)
    public ResponseEntity<RulesEngineErrorDetail> handleColumnNameException(InvalidTableDataException ex) {
        RulesEngineErrorDetail errorDetail = new RulesEngineErrorDetail(ex.getMessage(), "ERR_INVALID_TABLE_DATA");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }


    private RulesEngineErrorDetail mapBindingResultToError(ObjectError objectError){
        var message = ErrorCode.ERR_400.getMessage().formatted(((FieldError) objectError).getField(), objectError.getDefaultMessage());
        return RulesEngineErrorDetail.builder()
                .errorCode(ErrorCode.ERR_400.getCode())
                .errorMessage(message).build();
    }
}
