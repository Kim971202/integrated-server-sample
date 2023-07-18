package com.daesung.domain.inte.token;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * API 토큰 생성시 사용.
 *
 */
@Builder
@Getter
@Setter @ToString
public class TokenMaterial {

    private Header header;
    private Payload payload;

    @Builder
    @Getter @Setter @ToString
    public static class Header {
        //출발지서버ID
        private String sid;
        //목적지서버ID
        private String did;
        //트랜잭션ID
        private String tid;
        //JWT or JWE
        private String contentType;
    }

    @Builder
    @Getter @Setter
    @ToString
    public static class Payload {
        //홈아이디
        private String hid;
        //월패드인증ID
        private String iot;
        //function ID
        private String fid;

    }
}
