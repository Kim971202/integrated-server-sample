package com.daesung.domain.dggw.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_EMPTY)
@NoArgsConstructor @AllArgsConstructor
@Builder @Getter @Setter @ToString
public class HandleTextMessage {
    private Header header;
    private Object data;
    private String resultCode;
    private String resultMessage;
}
