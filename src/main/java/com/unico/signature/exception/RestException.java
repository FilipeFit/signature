package com.unico.signature.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public abstract class RestException extends RuntimeException {

    private static final long serialVersionUID = -6635290010904547786L;

    public abstract HttpStatus getStatus();

    /**
     * If an exception has a response body, it must override this method.
     * 
     * @return response body code
     */
    public String getResponseBodyCode() {
        return null;
    }

    public Object getBody() {
        return null;
    }

    public String getDetail() {
        return null;
    }

}
