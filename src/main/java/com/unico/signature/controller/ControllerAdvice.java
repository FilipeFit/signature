package com.unico.signature.controller;

import com.unico.signature.exception.RestException;
import com.unico.signature.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {
    private final MessageSource messageSource;

    @ExceptionHandler(RestException.class)
    public ResponseEntity<Object> handleRestException(RestException restException) {
        log.error(restException.getMessage(), restException);
        if (restException.getResponseBodyCode() != null) {
            ErrorResponse body = ErrorResponse.builder()
                    .code(restException.getResponseBodyCode())
                    .message(getMessage(restException.getResponseBodyCode()))
                    .detail(restException.getDetail())
                    .build();
            if (restException.getStatus().equals(HttpStatus.BAD_REQUEST)) {
                return ResponseEntity.status(restException.getStatus()).body(Arrays.asList(body));
            }
            if (restException.getStatus().equals(HttpStatus.PRECONDITION_FAILED)) {
                return ResponseEntity.status(restException.getStatus()).body(Arrays.asList(body));
            }
            return ResponseEntity.status(restException.getStatus()).body(body);
        }
        return ResponseEntity.status(restException.getStatus()).build();
    }

    private ErrorResponse createError(ObjectError error) {
        String field = "";
        if (error instanceof FieldError) {
            field = ((FieldError) error).getField();
        }
        return ErrorResponse.builder()
                .code(error.getDefaultMessage())
                .message(getMessage(error.getDefaultMessage(), field))
                .build();
    }

    private String getMessage(String code, Object... args) {
        return this.messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
