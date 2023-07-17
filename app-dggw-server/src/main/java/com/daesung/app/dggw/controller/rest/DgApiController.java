package com.daesung.app.dggw.controller.rest;

import com.daesung.app.dggw.controller.service.CommonService;
import com.daesung.app.dggw.web.WebClientUtils;
import com.daesung.cmn.server.util.JSON;
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

    @Value("http://1.226.248.99:3000")
    private String apiServerUrl;

    private final CommonService apiService;

    @RequestMapping(path = "/api/dg/{uri1}")
    public DeferredResult<ResponseEntity<?>> execute(@RequestBody String data, @PathVariable String uri1, HttpServletRequest request){

        //FunctionID 검즘
        String functionId = uri1;

        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>(Duration.ofSeconds(TIME_OUT).toMillis());


        if(!DgWebFunctionId.valueOf(functionId).getId().equals(uri1)){
            log.info("Function ID does not match");
        }

        //요청값 세팅
        String homeId = "0001";
        String dong = "101";
        String ho = "101";

        //Servicekey TODO: Redis DB에서 획득하여야 함
        String serviceKey = "84b9dffe-a16c-4179-a4b9-8db426146630";

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
            uri += "?" + WebClientUtils.getRequestString(requestValue.getClass(), requestValue);

            apiRequest = spec
                    .uri(uri)
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
