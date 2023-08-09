package com.daesung.app.platform.controller.rest;

import com.daesung.app.platform.controller.service.*;
import com.daesung.cmn.server.constant.Publics;
import com.daesung.cmn.server.util.JSON;
import com.daesung.cmn.server.util.ServerUtils;
import com.daesung.domain.inte.token.TokenMaterial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@Slf4j
@RestController
public class FulfillmentController {

    @Value("5")
    private long TIME_OUT;

    @Value("app-platform-server") //출발지서버ID
    private String serverId;

    private final ServerUtils serverUtils;
    private final FulfillmentService fulfillmentService;
    private final CreateRequestTokenService createRequestTokenService;
    private final ApiCallResult setApiCallResult;

    public FulfillmentController(ServerUtils serverUtils,
                                 FulfillmentService fulfillmentService,
                                 CreateRequestTokenService createRequestTokenService,
                                 ApiCallResult setApiCallResult){
        this.serverUtils = serverUtils;
        this.fulfillmentService = fulfillmentService;
        this.createRequestTokenService = createRequestTokenService;
        this.setApiCallResult = setApiCallResult;
    }

    @PostMapping(path = "/v1/api/app/fulfillment")
    public DeferredResult<ResponseEntity<?>> fulfillment(@RequestBody(required = false) String data, HttpServletRequest request){

        HttpMethod method = HttpMethod.resolve(request.getMethod());
        log.info("method: ", method);
        log.info("method: " + method);

        // 용도 확인 필요
        UrlPathHelper urlPathHelper = new UrlPathHelper();

        // result 선언
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>(Duration.ofSeconds(TIME_OUT).toMillis());

        TokenMaterial accessToken = (TokenMaterial) request.getAttribute(Publics.KEY_API_ACCESS_TOKEN);

        TokenMaterial tokenMaterial = createRequestTokenService.material(serverId, accessToken);
        log.info("create platform token:{}", JSON.toJson(tokenMaterial));

        String token = createRequestTokenService.jwt(tokenMaterial);
        log.info("token1(JWT): " + token);
        token = createRequestTokenService.jwe(token);
        log.info("token2(JWE): " + token);

        String did = tokenMaterial.getHeader().getDid();
        String dgCode = serverUtils.getDgCode(tokenMaterial.getPayload().getHid());
        String dbDong = "0101";
        String baseUrl = null;

        // TODO: 목적지(DID)에 따른 분시점 필요
        baseUrl = "http://localhost:7773";

        // 요청정보조회
        FunctionInfo  functionInfo = fulfillmentService.getFunctionInfo(tokenMaterial.getPayload().getFid());
        log.info("functionInfo:{}", functionInfo);

        UriSpec<RequestBodySpec> uriSpec = WebClientUtils.getSslClient(baseUrl, MediaType.APPLICATION_JSON_VALUE, functionInfo.getMethod(), token);

        String uri = functionInfo.getUri();

        Mono<ResponseEntity<String>> apiRequest = uriSpec
                .uri(uri)
                .bodyValue(data)
                .retrieve()
                .toEntity(String.class)
                ;
        apiRequest.subscribe(clientResult -> {
            setApiCallResult.set(ApiCallResult.Params.builder()
                    .token(accessToken)
                    .responseEntity(clientResult)
                    .deferredResult(result)
                    .build());
        });

        return result;
    }

}
