package com.daesung.app.dggw.websocket;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 실제 서버와 연결된 웹소켓 세션을 관리
 *
 */
@Slf4j
@Component
@Getter
public class SocketManager {

    //<인증ID(=기기ID), 웹소켓정보>
    private final ConcurrentHashMap<String, SocketConnectInfo> webSocketConnectInfo = new ConcurrentHashMap<>();

    //socket동시처리
    private final ConcurrentHashMap<String, ConcurrentWebSocketSessionDecorator> webSocketDecorators = new ConcurrentHashMap<>();

    public ConcurrentWebSocketSessionDecorator createWebSocketDecorator(WebSocketSession session) {
        log.info("createWebSocketDecorator called");

        return new ConcurrentWebSocketSessionDecorator(session, 1000 * 5, 1024 * 64);
    }
    public void putWebSocketDecorator(String sessionId, ConcurrentWebSocketSessionDecorator decorator) {
        log.info("putWebSocketDecorator called");
        webSocketDecorators.put(sessionId, decorator);
    }
    public ConcurrentWebSocketSessionDecorator getWebSocketDecorator(String sessionId) {
        log.info("getWebSocketDecorator called");
        return webSocketDecorators.get(sessionId);
    }
    public void sendMessage(WebSocketSession session, TextMessage message) {

        log.info("sendMessage(WebSocketSession session, TextMessage message) called");

        try {
            ConcurrentWebSocketSessionDecorator decorator = getWebSocketDecorator(session.getId());

            if( decorator == null ) {
                if( session.isOpen() ) {
                    session.sendMessage(message);
                }
            } else {
                if( decorator.isOpen() ) {
                    decorator.sendMessage(message);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
    public void sendMessage(String sessionId, TextMessage message) {

        log.info("sendMessage(String sessionId, TextMessage message) called");


        try {
            ConcurrentWebSocketSessionDecorator decorator = getWebSocketDecorator(sessionId);

            if( decorator != null && decorator.isOpen() ) {
                decorator.sendMessage(message);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
    public void sendPing(WebSocketSession session, PingMessage message) {

        log.info("sendPing(WebSocketSession session, PingMessage message) called");


        try {
            ConcurrentWebSocketSessionDecorator decorator = getWebSocketDecorator(session.getId());

            if( decorator == null ) {
                if( session.isOpen() ) {
                    session.sendMessage(message);
                }
            } else {
                if( decorator.isOpen() ) {
                    decorator.sendMessage(message);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
    public int getWebSocketDecoratorsSize() {
        log.info("getWebSocketDecoratorsSize called");

        return webSocketDecorators.size();
    }


    public int getSocketConnectInfoSize() {
        log.info("getSocketConnectInfoSize called");

        return webSocketConnectInfo.size();
    }

    /**
     * 월패드 정보 갱신
     *
     * @param id 홈id 0000.0000.0000
     * @param info 월패드정보
     */
    public void updateSocketConnectInfo(String id, SocketConnectInfo info) {

        log.info("updateSocketConnectInfo called");


        String sessionId = info.getSession().getId();
        removeSocketConnectInfoBySessionId(sessionId);
        showSocketConnectInfoSize();

        setSocketConnectInfo(id, info);
    }

    /**
     * 월패드 정보 추가
     *
     * @param id 인증ID(=기기ID)
     * @param info 월패드정보
     */
    public void setSocketConnectInfo(String id, SocketConnectInfo info) {

        log.info("setSocketConnectInfo called");


        webSocketConnectInfo.put(id, info);

        log.debug("연결된 월패드 소켓연결정보 authId:{} session id:{} count:{}",id, info.getSession().getId(), getSocketConnectInfoSize());
    }

    /**
     * 월패드 정보 삭제
     *
     * @param id 웹소켓세션ID
     */
    public void removeSocketConnectInfoBySessionId(String id) {

        log.info("removeSocketConnectInfoBySessionId called");


        try {
            String authId = findSocketConnectInfoBySessionId(id);

            if( authId != null ) {
                log.info("웹소켓연결정보삭제:{}", authId);
                webSocketConnectInfo.remove(authId);
                webSocketDecorators.remove(id);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 인증ID(=기기ID) 값으로 월패드 정보 찾기
     *
     * 단지월패드의 경우 단지코드+동+호 값으로 구성됨
     *
     * @param id 인증ID(=기기ID)
     * @return
     */
    public SocketConnectInfo findSocketConnectById(String id) {

        log.info("findSocketConnectById called");


        try {
            return webSocketConnectInfo.get(id);
        } catch(NullPointerException e) {//값이 없는 경우 skip
        } catch(Exception e) {
            log.error("", e);
        }

        return null;
    }

    /**
     * 웺소켓 세션 아이디로 월패드 정보 찾기
     *
     * @param sessionId 웹소켓세션ID
     * @return
     */
    public String findSocketConnectInfoBySessionId(String sessionId) {

        log.info("findSocketConnectInfoBySessionId called");


        if( getSocketConnectInfoSize() < 1 ) return null;

        return webSocketConnectInfo.entrySet().stream()
                .filter(entry  -> sessionId.equals(entry.getValue().getSession().getId()))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public void showSocketConnectInfoSize() {

        log.info("showSocketConnectInfoSize called");


        log.debug("현재 연결된 웹 소켓 count:{}", getSocketConnectInfoSize());
        log.debug("현재 연결된 웹 소켓 데코레이터 count:{}", getWebSocketDecoratorsSize());
    }

    /**
     * 현재 연결되어 있는 websocket을 대상으로 ping 전송.
     */
    public void sendPing() {

        log.info("sendPing called");


        webSocketConnectInfo.entrySet().stream()
                .forEach(entry -> {
                    try {
                        sendPing(entry.getValue().getSession(), new PingMessage());
                    } catch (Exception e) {
                        log.error("", e);
                    }
                });
    }
}
