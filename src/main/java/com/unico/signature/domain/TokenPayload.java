package com.unico.signature.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenPayload {

    private String iss;
    private String aud;
    private String scope;
    private Long exp;
    private Long iat;
}
