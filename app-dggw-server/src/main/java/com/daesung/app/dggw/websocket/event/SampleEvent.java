package com.daesung.app.dggw.websocket.event;

import com.daesung.domain.dggw.websocket.ResponseValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketSession;

/**
 * 월패드에서 API에 대한 응답처리 할때.
 *
 */
@Builder
@Getter
@AllArgsConstructor
@ToString
public class SampleEvent {

    @NonNull
    private Object source;
    @NonNull
    private WebSocketSession session;
    @NonNull
    private ResponseValue<?> responseValue;
}
