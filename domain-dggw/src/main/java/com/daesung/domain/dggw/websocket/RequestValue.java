package com.daesung.domain.dggw.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Builder @Getter @Setter @ToString
public class RequestValue<T> {

    private Header header;
    private T data;
}
