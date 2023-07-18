package com.daesung.cmn.server.token.properties;

import com.daesung.cmn.server.util.KeyStoreUtils;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "custom.token")
public class TokenConfig {

    // 현재 서버의 privateKey
    private String pathPrivateKey = "apikeys/private-key";

    // 현재 서버의 publicKey
    private String pathPublicKey = "apikeys/public-key";

    // 토큰 JWE 암호화에 사용되는 대칭키
    private String pathEncryptKey = "apikeys/encrypt-key";

    // XXX 서버간 토큰 유효기간(단위: 분)
    private long expirationTime = 525600; // 1년

    // 토큰 관련 암호화 키 init 처리 여부
    private boolean configSkip;

    // RSA
    private JWSVerifier jwsVerifier;
    private RSASSASigner rsassaSigner;

    // AES
    private SecretKey aesKey;
    private JWEEncrypter jweEncrypter;
    private JWEDecrypter jweDecrypter;

    // 각 서버의 publicKey
    /**
     * 플랫폼 퍼블릭 키는 플랫폼과 통신이 필요한 모든 서버가 가지고 있어야 하는 값이다. 해당 값에 대한 정의는 .yml 파일안에 다음과 같은
     * 형식으로 존재해야 한다.
     *
     * custom: token: path_platform_public_key: apikeys/platform-public-key
     */
    private String pathPlatformPublicKey = "apikeys/platform-public-key";

    private String pathAdminPublicKey;
    private String pathDaesungPublicKey;
    private String pathDggwIotPublicKey;
    private String pathDggwPublicKey;
    private String pathGooglePublicKey;
    private String pathPushPublicKey;

    // 각 서버의 privateKey
    private String pathAdminPrivateKey;
    private String pathDaesungPrivateKey;
    private String pathDggwIotPrivateKey;
    private String pathDggwPrivateKey;
    private String pathGooglePrivateKey;
    private String pathPushPrivateKey;

    // 각 서버의 토큰검증객체
    private JWSVerifier jwsVerifierByPlatform;
    private JWSVerifier jwsVerifierByAdmin;
    private JWSVerifier jwsVerifierByDaesung;
    private JWSVerifier jwsVerifierByDggwIot;
    private JWSVerifier jwsVerifierByDggw;
    private JWSVerifier jwsVerifierByGoogle;
    private JWSVerifier jwsVerifierByPush;

    private final KeyStoreUtils keyStoreUtils;

    @PostConstruct
    public void init(){
        log.info("init() called");

        try {
            if(configSkip) return;

            this.jwsVerifier = new RSASSAVerifier(keyStoreUtils.readPublicKey(this.getPathPublicKey()));
            this.rsassaSigner = new RSASSASigner(keyStoreUtils.readPrivateKey(this.getPathPrivateKey()));

            this.aesKey = keyStoreUtils.getAesEncryptKey(this.getPathEncryptKey());
            this.jweEncrypter = new DirectEncrypter(aesKey);
            this.jweDecrypter = new DirectDecrypter(aesKey);

            setJwsVerifierByPlatform();
            setJwsVerifierByAdmin();
            setJwsVerifierByDaesung();
            setJwsVerifierByDggwIot();
            setJwsVerifierByDggw();
            setJwsVerifierByGoogle();
            setJwsVerifierByPush();
        } catch (Exception e){
            log.error("", e);
        }
    }

    private void setJwsVerifierByPlatform() {
        log.info("setJwsVerifierByPlatform called");

        if (StringUtils.hasText(this.pathPlatformPublicKey)) {
            try {
                this.jwsVerifierByPlatform = new RSASSAVerifier(
                        keyStoreUtils.readPublicKey(this.pathPlatformPublicKey));
            } catch (Exception e) {
                log.info("setJwsVerifierByPlatform not found.");
            }
        }
    }

    private void setJwsVerifierByAdmin() {
        log.info("setJwsVerifierByAdmin called");

        if (StringUtils.hasText(this.pathAdminPublicKey)) {
            try {
                this.jwsVerifierByAdmin = new RSASSAVerifier(keyStoreUtils.readPublicKey(this.pathAdminPublicKey));
            } catch (Exception e) {
                log.info("setJwsVerifierByAdmin not found.");
            }
        }
    }

    private void setJwsVerifierByDaesung() {
        log.info("setJwsVerifierByDaesung called");

        if (StringUtils.hasText(this.pathDaesungPublicKey)) {
            try {
                this.jwsVerifierByDaesung = new RSASSAVerifier(keyStoreUtils.readPublicKey(this.pathDaesungPublicKey));
            } catch (Exception e) {
            }
        }
    }

    private void setJwsVerifierByDggwIot() {
        log.info("setJwsVerifierByDggwIot called");

        if (StringUtils.hasText(this.pathDggwIotPublicKey)) {
            try {
                this.jwsVerifierByDggwIot = new RSASSAVerifier(keyStoreUtils.readPublicKey(this.pathDggwIotPublicKey));
            } catch (Exception e) {
                log.info("setJwsVerifierByDggwIot not found.");
            }
        }
    }

    private void setJwsVerifierByDggw() {
        log.info("setJwsVerifierByDggw called");

        if (StringUtils.hasText(this.pathDggwPublicKey)) {
            try {
                this.jwsVerifierByDggw = new RSASSAVerifier(keyStoreUtils.readPublicKey(this.pathDggwPublicKey));
            } catch (Exception e) {
                log.info("setJwsVerifierByDggw not found.");
            }
        }
    }

    private void setJwsVerifierByGoogle() {
        log.info("setJwsVerifierByGoogle called");

        if (StringUtils.hasText(this.pathGooglePublicKey)) {
            try {
                this.jwsVerifierByGoogle = new RSASSAVerifier(keyStoreUtils.readPublicKey(this.pathGooglePublicKey));
            } catch (Exception e) {
                log.info("setJwsVerifierByGoogle not found.");
            }
        }
    }

    private void setJwsVerifierByPush() {
        log.info("setJwsVerifierByPush called");

        if (StringUtils.hasText(this.pathPushPublicKey)) {
            try {
                this.jwsVerifierByPush = new RSASSAVerifier(keyStoreUtils.readPublicKey(this.pathPushPublicKey));
            } catch (Exception e) {
                log.info("setJwsVerifierByPush not found.");
            }
        }
    }

}
