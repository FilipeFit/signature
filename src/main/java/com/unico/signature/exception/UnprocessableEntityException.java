package com.unico.signature.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class UnprocessableEntityException extends RestException {

    private static final long serialVersionUID = 3980240545669347957L;

    private String responseBodyCode;
    private String detail;

    public UnprocessableEntityException(String responseBodyCode) {
        this.responseBodyCode = responseBodyCode;
    }

    public UnprocessableEntityException(String responseBodyCode, String detail) {
        this.detail = detail;
        this.responseBodyCode = responseBodyCode;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

    @Override
    public String getResponseBodyCode() {
        return responseBodyCode;
    }

    @Override
    public String getDetail() {
        return detail;
    }
}
