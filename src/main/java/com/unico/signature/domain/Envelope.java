package com.unico.signature.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Envelope {

    private List<Map<String, Integer>> envelopeFlow;
    private Boolean isFrame;
    private List<Document> documents;
}
