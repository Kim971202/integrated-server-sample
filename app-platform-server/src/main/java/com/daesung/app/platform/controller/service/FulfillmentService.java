package com.daesung.app.platform.controller.service;

import com.daesung.domain.dggw.constant.DgWebFunctionId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class FulfillmentService {

    private final String URI_PAD = "/api/pad";
    private final String URI_DG = "/api/dg/";

    public FunctionInfo getFunctionInfo(String functionId){
        log.info("getFunctionInfo called");
        log.info("getFunctionInfo functionId:{}", functionId);

        DgWebFunctionId dgWebInfo = getWebDgInfo(functionId);

        return FunctionInfo.builder()
                .method(HttpMethod.resolve(dgWebInfo.getMethod()))
                .uri(URI_DG + dgWebInfo.getUri())
                .functionId(functionId)
                .build();
    }

    public DgWebFunctionId getWebDgInfo(String functionId){
        log.info("getWebDgInfo called");

        return Arrays.stream(DgWebFunctionId.values())
                .filter(func -> func.getId().equals(functionId))
                .findFirst()
                .orElse(null);
    }

}
