package com.daesung.domain.dggw.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Builder @Getter @Setter @ToString
public class Header {

    private String protocolVer;
    private String messageFlag;
    private String payloadType;
    private String functionID;
    private String sourceID;
    private String destinationID;
    private String transactionID;
    private String accessToken;
}
