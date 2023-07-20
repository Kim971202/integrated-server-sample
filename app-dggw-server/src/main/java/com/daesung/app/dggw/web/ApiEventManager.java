package com.daesung.app.dggw.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class ApiEventManager {

    private final ConcurrentHashMap<String, DeferredResult<ResponseEntity<?>>> webSocketRequests = new ConcurrentHashMap<>();

    public void setOnMethod(String eventId, DeferredResult<ResponseEntity<?>> result) {

        log.info("setOnMethod called");
    }

    public int getWebSocketRequestSize() {

        log.info("getWebSocketRequestSize called");

        return webSocketRequests.size();
    }

    public void setEvent(String id, DeferredResult<ResponseEntity<?>> result) {
        log.info("setEvent called");

        webSocketRequests.put(id, result);
        log.debug("요청이벤트 정보 eventId:{} count:{}", id, getWebSocketRequestSize());
    }

    public DeferredResult<ResponseEntity<?>> getEvent(String id) {

        log.info("getEvent called");


        try {
            return webSocketRequests.get(id);
        } catch(Exception e) {}
        return null;
    }

    public void removeEvent(String id) {

        log.info("removeEvent called");

        try {
            webSocketRequests.remove(id);
        } catch( Exception e) {}

        log.debug("현재 요청이벤트 count:{}", getWebSocketRequestSize());
    }

    public boolean containsKey(String id) {
        log.info("containsKey called");

        try {
            return webSocketRequests.containsKey(id);
        } catch(Exception e) {}
        return false;
    }

}
