package com.unico.signature.client;


import com.unico.signature.config.ApplicationConfig;
import com.unico.signature.domain.Envelope;
import com.unico.signature.domain.TokenResponse;
import com.unico.signature.domain.UnicoTokenResponse;
import com.unico.signature.exception.UnicoException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;

@Slf4j
@Service
@AllArgsConstructor
public class UnicoClient {

    private static final Logger LOGGER = Logger.getLogger(UnicoClient.class.getName());
    private final RestTemplateBuilder rest;
    private final ApplicationConfig applicationConfig;
    private static final TokenResponse TOKEN = TokenResponse.builder().creationDate(LocalDateTime.now()).expiresIn(0)
            .build();

    public void createEnvelope(Envelope envelopeRequest) throws Exception {
        LOGGER.info("Creating envelope");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + getAccessToken());
        LOGGER.info("Envelope request " + envelopeRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(envelopeRequest, httpHeaders);
        LOGGER.info("body    ------ " + httpEntity.getBody());
        try {
            rest.build()
                    .exchange(applicationConfig.getUnicoRequestEnvelopeURL(),
                            HttpMethod.POST, httpEntity, Void.class)
                    .getBody();

            log.info("UNICO - envelope created successfully!");
        } catch (HttpStatusCodeException e) {
            log.error("UNICO - An error occurred while trying to create the envelope. (status code {}, body {})",
                    e.getRawStatusCode(), e.getResponseBodyAsString(), e);
            throw new UnicoException();
        }

    }

    private void generateUnicoToken() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        String requestToken = generateServiceToken();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type","urn:ietf:params:oauth:grant-type:jwt-bearer");
        map.add("assertion",requestToken);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<UnicoTokenResponse> token = rest.build()
                .postForEntity(applicationConfig.getUnicoRequestTokenURL(), entity, UnicoTokenResponse.class);

        TOKEN.setExpiresIn(token.getBody().getExpiresIn());
        TOKEN.setAccessToken(token.getBody().getAccessToken());
        TOKEN.setCreationDate(LocalDateTime.now());
    }

    private String generateServiceToken() throws Exception {

        PrivateKey privateKey = getPrivateKey();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

        Instant currentDate = Instant.now();
        String jwtToken = Jwts.builder()
                .setIssuer(applicationConfig.getTokenIss())
                .setAudience(applicationConfig.getTokenAud())
                .setExpiration(Date.from(currentDate.plus(1l, ChronoUnit.HOURS)))
                .setIssuedAt(Date.from(currentDate))
                .signWith(privateKey, signatureAlgorithm)
                .claim("scope", "*")
                .compact();
        return jwtToken;
    }

    private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String rsaPrivateKey = applicationConfig.getTokenKey()
                .trim()
                .replaceAll("[\\n\\t ]", "");

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    private String getAccessToken() throws Exception {
        if (isTokenInvalid()) {
            generateUnicoToken();
        }
        return TOKEN.getAccessToken();
    }

    private boolean isTokenInvalid() {
        LocalDateTime expirationDate = TOKEN.getCreationDate().plusSeconds(TOKEN.getExpiresIn());
        return expirationDate.isBefore(LocalDateTime.now());
    }

}
