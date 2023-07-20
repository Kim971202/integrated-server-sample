package com.daesung.app.dggw.websocket.event;

import com.daesung.app.dggw.web.ApiEventManager;
import com.daesung.app.dggw.websocket.SocketManager;
import com.daesung.domain.dggw.websocket.ResponseValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class ApiEventListner {

    @Value("5")
    private long TIME_OUT;

    private final SocketManager socketManager;
    private final ApiEventManager apiEventManager;
    @EventListener
    @Async
    public void onSampleEvent(SampleEvent event){
        log.info("onSampleEvent called");

        WebSocketSession session = event.getSession();
        ResponseValue<?> responseValue = event.getResponseValue();
        String transactionId = responseValue.getHeader().getTransactionID();

        String authId = socketManager.findSocketConnectInfoBySessionId(session.getId());

        String eventId = authId + transactionId;

        DeferredResult<ResponseEntity<?>> result = apiEventManager.getEvent(eventId);

        responseValue.setHeader(null);

        if(result != null){
            result.setResult(ResponseEntity.ok(responseValue));
            apiEventManager.removeEvent(eventId);
        } else {
            log.info("result is NULL");
        }

    }

}
