package com.daesung.app.platform.controller.service;

import com.daesung.domain.inte.token.TokenMaterial;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@Service
public class ApiCallResult {

    @Builder
    public static class Params {
        public TokenMaterial token;
        public ResponseEntity<String> responseEntity;
        public DeferredResult<ResponseEntity<?>> deferredResult;
    }

    public void set(ApiCallResult.Params params){
        log.info("set called");

        log.info("platform >> app tid:{} responseValue:{}", params.token.getHeader().getTid(), params.responseEntity.getBody());
        params.deferredResult.setResult(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(params.responseEntity.getBody()));
    }

}
