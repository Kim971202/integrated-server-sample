package com.daesung.app.platform.controller.service;

import com.daesung.cmn.server.constant.TokenContentType;
import com.daesung.cmn.server.util.ApiTokenUtils;
import com.daesung.domain.inte.token.TokenMaterial;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateRequestTokenService {

    private final ApiTokenUtils apiTokenUtils;

    public TokenMaterial material(String serverId, TokenMaterial recvAccessToken){
        log.info("material called");

        TokenMaterial tokenMaterial = TokenMaterial.builder()
                .header(TokenMaterial.Header.builder()
                        .sid(serverId)
                        .did(recvAccessToken.getHeader().getDid())
                        .tid(recvAccessToken.getHeader().getTid())
                        .contentType(TokenContentType.JWT.name())
                        .build())
                .payload(TokenMaterial.Payload.builder()
                        .hid(recvAccessToken.getPayload().getHid())
                        .iot(recvAccessToken.getPayload().getIot())
                        .fid(recvAccessToken.getPayload().getFid())
                        .build())
                .build();

        return tokenMaterial;
    }

    public String jwt(TokenMaterial tokenMaterial){
        log.info("jwt called");
        return apiTokenUtils.createJWT(tokenMaterial);
    }

    public String jwe(String jwtToken){
        log.info("jwe called");
        return apiTokenUtils.createJWE(jwtToken);
    }

}
