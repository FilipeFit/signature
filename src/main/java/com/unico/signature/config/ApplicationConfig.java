package com.unico.signature.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfig {

    @Value("${unico.token-payload.iss}")
    private String tokenIss;

    @Value("${unico.token-payload.aud}")
    private String tokenAud;

    @Value("${unico.token-url}")
    private String unicoRequestTokenURL;

    @Value("${unico.envelope-url}")
    private String unicoRequestEnvelopeURL;

    @Value("${unico.token-payload.key}")
    private String tokenKey;

}
