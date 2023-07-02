package com.bootcamp.rules_engine.error.exception;

import com.bootcamp.rules_engine.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ColumnNameException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;

    public ColumnNameException(String columnName) {
        super(createErrorMessage(columnName));
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = ErrorCode.ERR_COLUMN_NAME.getCode();
        this.errorMessage = createErrorMessage(columnName);
    }

    private static String createErrorMessage(String columnName) {
        return "Column name '" + columnName + "' cannot contain spaces.";
    }
}
