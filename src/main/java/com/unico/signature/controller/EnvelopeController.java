package com.unico.signature.controller;

import com.unico.signature.client.UnicoClient;
import com.unico.signature.domain.Envelope;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/envelopes")
@RequiredArgsConstructor
public class EnvelopeController {

    private final UnicoClient unicoClientService;

    @PostMapping
    public ResponseEntity<Void> disputeInvoice(@RequestBody Envelope envelope) throws Exception {
        unicoClientService.createEnvelope(envelope);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
