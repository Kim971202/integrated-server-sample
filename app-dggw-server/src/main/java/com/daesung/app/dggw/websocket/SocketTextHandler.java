package com.daesung.app.dggw.websocket;

import com.daesung.app.dggw.websocket.event.SampleEvent;
import com.daesung.cmn.server.util.JSON;
import com.daesung.cmn.server.util.ServerUtils;
import com.daesung.domain.dggw.constant.DgWebFunctionId;
import com.daesung.domain.dggw.websocket.HandleTextMessage;
import com.daesung.domain.dggw.websocket.RequestValue;
import com.daesung.domain.dggw.websocket.ResponseValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class SocketTextHandler extends TextWebSocketHandler {

    @Value("0001")
    private String serverDgCode; // 0001

    private final ApplicationEventPublisher eventPublisher;

    private final SocketManager socketManager;
    private final ServerUtils serverUtils;

    public SocketTextHandler(ApplicationEventPublisher eventPublisher, SocketManager socketManager, ServerUtils serverUtils) {
        this.eventPublisher = eventPublisher;
        this.socketManager = socketManager;
        this.serverUtils = serverUtils;
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        try{
            HandleTextMessage handleTextMessage = JSON.fromJson(message.getPayload(), HandleTextMessage.class);

            log.info("wallpad >> gw:{}", JSON.toJson(handleTextMessage, true));

            String functionId = handleTextMessage.getHeader().getFunctionID();
            log.info("functionId: " + functionId);

            // TODO: FunctionID로 분기점 생성

            // 요청값
            RequestValue<?> requestValue = null;
            // 응답값
            ResponseValue<?> responseValue = null;

            // TODO: 받은 FunctionID에 따라 요청/응답으로 분기
            requestValue = JSON.fromJson(message.getPayload(), RequestValue.class);
            responseValue = JSON.fromJson(message.getPayload(), ResponseValue.class);

            // TODO: Test용 requestValue
            if(DgWebFunctionId.getNoticeList.getId().equals(functionId)){

            }

            log.info("requestValue: " + JSON.fromJson(message.getPayload(), RequestValue.class));
            log.info("responseValue: " + JSON.fromJson(message.getPayload(), ResponseValue.class));

            session.sendMessage(new TextMessage(JSON.toJson(handleTextMessage)));
//            session.sendMessage(new PingMessage());
            eventPublisher.publishEvent(SampleEvent.builder()
                    .source(this)
                    .session(session)
                    .responseValue(responseValue)
                    .build());
        }catch(Exception error){
            log.error("", error);
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("afterConnectionEstablished");
        System.out.println("session: " + session);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.info("handlePongMessage called");
    }



}
