package com.daesung.cmn.server.util;

import com.daesung.cmn.server.constant.Publics;
import com.daesung.cmn.server.constant.TokenContentType;
import com.daesung.cmn.server.constant.TokenHeaderCustomParam;
import com.daesung.cmn.server.constant.TokenPayload;
import com.daesung.cmn.server.token.properties.TokenConfig;
import com.daesung.domain.inte.token.TokenMaterial;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiTokenUtils {

    @Value("false")
    private boolean isTokenSkip;

    @Value("SeverId")
    private String serverId;

    private final TokenConfig tokenConfig;
    private final ServerUtils serverUtils;

    public TokenContentType getTokenType(String token){
        log.info("getTokenType called");

        int count = StringUtils.countOccurrencesOf(token, ".");

        if( count == 4){
            return TokenContentType.JWE;
        }
        return TokenContentType.JWT;
    }

    public TokenMaterial verify(String token){
        log.info("verify called");

        try{
            if(TokenContentType.JWE == getTokenType(token)){
                token = decryptJWE(token);
                log.info("JWE 토큰 복호화 결과:{}", token);
            }

            log.info("isTokenSkip: {}", isTokenSkip);
            if(isTokenSkip){
                return getTokenMaterial(SignedJWT.parse(token));
            } else if(verifyJWT(token)){
                return getTokenMaterial(SignedJWT.parse(token));
            }
        }catch (Exception e){
            log.error("", e);
        }
        return null;
    }

    /**
     * JWE. 토큰 복호화 처리
     *
     * @param token
     * @param encKeyPath
     * @return 복호화 성공시 JWT 토큰 string. 그외 null
     */
    public String decryptJWE(String token){
        log.info("decryptJWE called");

        try{
            JWEObject jweObject = JWEObject.parse(token);
            jweObject.decrypt(tokenConfig.getJweDecrypter());

            log.info("decryptJWE success.");
            return jweObject.getPayload().toSignedJWT().serialize();
        } catch (Exception e){
            log.error("", e);
        }
        return null;
    }

    public TokenMaterial getTokenMaterial(SignedJWT signedJWT) throws Exception {
        log.info("getTokenMaterial called");

        return TokenMaterial.builder()
                .header(TokenMaterial.Header.builder()
                        .sid(signedJWT.getHeader().getCustomParam(TokenHeaderCustomParam.SID.getKey()).toString())
                        .did(signedJWT.getHeader().getCustomParam(TokenHeaderCustomParam.DID.getKey()).toString())
                        .tid(signedJWT.getHeader().getCustomParam(TokenHeaderCustomParam.TID.getKey()).toString())
                        .contentType(signedJWT.getHeader().getContentType())
                        .build())
                .payload(TokenMaterial.Payload.builder()
                        .hid(signedJWT.getJWTClaimsSet().getStringClaim(TokenPayload.HID.getKey()))
                        .iot(signedJWT.getJWTClaimsSet().getStringClaim(TokenPayload.IOT.getKey()))
                        .fid(signedJWT.getJWTClaimsSet().getStringClaim(TokenPayload.FID.getKey()))
                        .build())
                .build();
    }

    /**
     * JWT. 토큰 sign 검증 + 유효기간 체크 결과
     *
     * @param token
     * @return
     */
    public boolean verifyJWT(String token){
        log.info("verifyJWT called");

        try{
            SignedJWT signedJWT = SignedJWT.parse(token);

            if( verifySignedJWT(signedJWT, tokenConfig.getJwsVerifierByPlatform()) && verifyExpiredJWT(signedJWT)){
                return true;
            }
        } catch(Exception e){
            log.error("", e);
        }
        return false;
    }

    /**
     * JWT. 토큰 sign 검증
     *
     * @param signedJWT
     * @param jwsVerifier
     * @return
     */
    public boolean verifySignedJWT(SignedJWT signedJWT, JWSVerifier jwsVerifier){
        log.info("verifySignedJWT called");

        try {
            if( signedJWT.verify(jwsVerifier) ) {
                return true;
            }
        } catch(Exception e) {
            log.error("", e);
        }

        log.info("verifySignedJWT false.");
        return false;
    }

    /**
     * JWT. 토큰 유효기간 검증
     * @param token
     * @param pubKeyPath
     * @return 정상 true, 그외 false
     */
    public boolean verifyExpiredJWT(SignedJWT signedJWT) {
        log.info("verifyExpiredJWT called");

        try {
            Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();
            Date now = new Date();

            if( !now.after(exp) ) {
                return true;
            }
        } catch(Exception e) {
            log.error("", e);
        }

        log.info("verifyExpiredJWT false.");
        return false;
    }

    public void tokenValidation(TokenMaterial token) {
        log.info("tokenValidation called");

        //토큰 header, payload 필수값 체크
        System.out.println("token.getHeader().getSid(): " + token.getHeader().getSid());
        System.out.println("token.getHeader().getDid(): " + token.getHeader().getDid());
        System.out.println("token.getHeader().getTid(): " + token.getHeader().getTid());
        System.out.println("token.getHeader().getContentType(): " + token.getHeader().getContentType());

        System.out.println("token.getPayload().getHid(): " + token.getPayload().getHid());
        System.out.println("token.getPayload().getIot(): " + token.getPayload().getIot());
        System.out.println("token.getPayload().getFid()): " + token.getPayload().getFid());

        if( token.getHeader() == null
                || !StringUtils.hasText(token.getHeader().getSid())
                || !StringUtils.hasText(token.getHeader().getDid())
                || !StringUtils.hasText(token.getHeader().getTid())
                || !StringUtils.hasText(token.getHeader().getContentType())
                || token.getPayload() == null
                || !StringUtils.hasText(token.getPayload().getHid())
                || !StringUtils.hasText(token.getPayload().getIot())
                || !StringUtils.hasText(token.getPayload().getFid())) {
        }

        //플랫폼을 제외한 나머지 서버대상.
        if( !Publics.APP_PLATFORM_SERVER.equals(serverId)
                && !serverId.equals(token.getHeader().getDid()) ) {log.info("플랫폼을 제외한 나머지 서버대상.");}
    }

    public String createJWT(TokenMaterial material) {
        log.info("createJWT(TokenMaterial material) called");

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .customParam(TokenHeaderCustomParam.SID.getKey(), material.getHeader().getSid()) //출발지서버ID
                .customParam(TokenHeaderCustomParam.DID.getKey(), material.getHeader().getDid()) //목적지서버ID
                .customParam(TokenHeaderCustomParam.TID.getKey(), material.getHeader().getTid()) //트랜잭션ID
                .contentType(material.getHeader().getContentType())
                .build();

        LocalDateTime timeStamp = serverUtils.getTimeAsiaSeoulNow();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issueTime(new Date())
                .expirationTime(Date.from(timeStamp.plusMinutes(tokenConfig.getExpirationTime()).atZone(ZoneId.systemDefault()).toInstant()))
                .claim(TokenPayload.HID.getKey(), material.getPayload().getHid()) //홈아이디
                .claim(TokenPayload.IOT.getKey(), material.getPayload().getIot()) //월패드인증ID
                .claim(TokenPayload.FID.getKey(), material.getPayload().getFid()) //function ID
                .build();

        return createJWT(header, claimsSet);
    }

    private String createJWT(JWSHeader header, JWTClaimsSet claimsSet) {
        log.info("createJWT(JWSHeader header, JWTClaimsSet claimsSet) called");

        System.out.println(header);
        System.out.println(claimsSet);

        try {
            SignedJWT signedJWT = new SignedJWT(header,claimsSet);
            signedJWT.sign(tokenConfig.getRsassaSigner());

            return signedJWT.serialize();

        } catch(Exception e) {
            log.error("", e);
        }

        return null;
    }

}
