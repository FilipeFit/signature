package com.unico.signature.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    private String emitterUserUUID;
    private String emitterUserName;
    private String documentType;
    private String fileBase64;
    private String unitUUID;
    private List<Subscriber> subscribers;
}
