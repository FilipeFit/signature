package com.unico.signature.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unico.signature.domain.Document;
import com.unico.signature.domain.Envelope;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EnvelopeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createEnvelopeTest() throws Exception {

        Document document = Document.builder()
                .emitterUserName("test")
                .fileBase64("test")
                .emitterUserName("test")
                .build();

        Envelope envelope = Envelope.builder()
                .documents(Collections.singletonList(document))
                .isFrame(false)
                .build();

        mockMvc.perform(post("/envelopes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(envelope)))
                .andExpect(status().isCreated());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
