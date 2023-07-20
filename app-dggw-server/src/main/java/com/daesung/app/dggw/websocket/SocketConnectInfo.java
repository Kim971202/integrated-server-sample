package com.daesung.app.dggw.websocket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;

/**
 * 월패드 정보
 *
 */
@Builder
@Getter
@Setter
public class SocketConnectInfo {

    //인증ID(=기기ID와 동일한 값)
    private String authId;
    //기기ID
    private String deviceId;
    //월패드웹소켓세션
    @JsonIgnore
    private WebSocketSession session;
    //웹소켓세션만료시간
    private Date expiredDate;

    //단지 동
    private String dgDong;
    //단지 호
    private String dgHo;
}
