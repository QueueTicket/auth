package com.qticket.auth.config;

import com.qticket.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    USER_EMAIL_CONFLICT(HttpStatus.CONFLICT,"EMAIL CONFLICT","Email conflict."),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND,"NOT FOUND","Not found."),
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL SERVER ERROR","Internal server error.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return this.httpStatus;
    }
}