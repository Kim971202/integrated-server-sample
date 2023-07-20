package com.daesung.domain.dggw.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@JsonInclude(Include.NON_EMPTY)
public class ResponseValue<T> {

    private Header header;
    private String resultCode;
    private String resultMessage;
    private T data;
}
