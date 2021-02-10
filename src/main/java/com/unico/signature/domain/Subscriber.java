package com.unico.signature.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subscriber {
    private String subscriberName;
    private String subscriberCPF;
    private String subscriberEmail;
    private Integer subscriberOrder;
    private String authCode;
}