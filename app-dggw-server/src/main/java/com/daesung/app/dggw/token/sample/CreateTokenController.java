package com.daesung.app.dggw.token.sample;

import com.daesung.cmn.server.constant.TokenContentType;
import com.daesung.cmn.server.util.ApiTokenUtils;
import com.daesung.cmn.server.util.JSON;
import com.daesung.cmn.server.util.ServerUtils;
import com.daesung.domain.inte.token.TokenMaterial;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CreateTokenController {

    @Value("sample-token-server-id")
    private String serverId;

    private final ApiTokenUtils apiTokenUtils;
    private final ServerUtils serverUtils;

    @GetMapping("/token/jwt")
    public String createJWS(){

        TokenMaterial tokenMaterial = TokenMaterial.builder()
                .header(TokenMaterial.Header.builder()
                        .sid(serverId)
                        .did("app-platform-server")
                        .tid(serverUtils.getTransactionId())
                        .contentType(TokenContentType.JWT.name())
                        .build())
                .payload(TokenMaterial.Payload.builder()
                        .hid(serverUtils.getHomeId("0001", "101", "101"))
                        .iot("Wallpad Auth ID")
                        .fid("ThisisFunctionID")
                        .build())
                .build();

        log.info("create platform token:" + JSON.toJson(tokenMaterial, true));
        //플랫폼 JWT 토큰 생성
        String token = apiTokenUtils.createJWT(tokenMaterial);

        //token = apiTokenUtils.decryptJWE(token);
        return token;
    }

}
