package com.daesung.app.dggw.controller.rest;

import com.daesung.app.dggw.controller.service.CommonService;
import com.daesung.app.dggw.service.DgWebApiService;
import com.daesung.app.dggw.web.WebClientUtils;
import com.daesung.cmn.server.util.JSON;
import com.daesung.cmn.server.util.ServerUtils;
import com.daesung.domain.dggw.constant.DgWebFunctionId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

/**
 * 단지Open API 호출 담당 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class DgApiController {

    // Timeout Value
    @Value("5")
    private long TIME_OUT;

    @Value("http://localhost:3000")
    private String apiServerUrl;

    private final CommonService apiService;
    private final ServerUtils serverUtils;
    private final DgWebApiService dgWebApiService;

    @RequestMapping(path = "/api/dg/{uri1}")
    public DeferredResult<ResponseEntity<?>> execute(@RequestBody String data, @PathVariable String uri1, HttpServletRequest request){

        //FunctionID 검즘
        String functionId = uri1;

        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>(Duration.ofSeconds(TIME_OUT).toMillis());


        if(!DgWebFunctionId.valueOf(functionId).getId().equals(uri1)){
            log.info("Function ID does not match");
        }

        //요청값 세팅
        String homeId = "0000.0101.0101";
        String dong = String.valueOf(Integer.parseInt(serverUtils.getDgDong(homeId)));
        String ho = String.valueOf(Integer.parseInt(serverUtils.getDgHo(homeId)));
        log.info("homeId:{} dong:{} ho:{}", homeId, dong, ho);

        String serviceKey = dgWebApiService.getServiceKey(dong, ho);
        log.info("ServiceKey: " + serviceKey);

        Object requestValue = apiService.setCommonValue(JSON.fromJson(data, DgWebFunctionId.valueOf(functionId).getRequestClass()), serviceKey, dong, ho);
        log.info("requestValue:{}", JSON.toJson(requestValue, true));

        //webClient 생성
        HttpMethod method = HttpMethod.resolve(request.getMethod());
        UriSpec<RequestBodySpec> spec = null;
        Mono<ResponseEntity<String>> apiRequest = null;

        //요청 Url
        String uri = DgWebFunctionId.valueOf(functionId).getRoute() + "/" + DgWebFunctionId.valueOf(functionId).getUri();
        log.info("request url:{}", apiServerUrl + uri);

        if(HttpMethod.GET == method){
            spec = WebClientUtils.getQueryClient(apiServerUrl, method);
            //최종 호출 Url
            uri += "?" + WebClientUtils.getRequestString(requestValue.getClass(), requestValue);
            log.info("url:{}", apiServerUrl + uri);

            apiRequest = spec
                    .uri(uri)
                    .retrieve()
                    .toEntity(String.class);
        } else {
            spec = WebClientUtils.getSslClient(apiServerUrl, MediaType.APPLICATION_FORM_URLENCODED_VALUE, method);
            apiRequest = spec
                    .uri(uri)
                    .body(BodyInserters.fromFormData(WebClientUtils.getRequestFormData(requestValue.getClass(), requestValue)))
                    .retrieve()
                    .toEntity(String.class);
        }

        apiRequest.subscribe(clientResult -> {
            log.info("responseValue:{}", JSON.toJson(clientResult.getBody(), true));
            result.setResult(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(clientResult.getBody()));
        });

        return result;
    }

}
