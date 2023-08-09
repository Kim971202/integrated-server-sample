package com.daesung.app.platform.controller.service;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpMethod;

@Builder @Getter @ToString
public class FunctionInfo {

    private HttpMethod method;
    private String uri;
    private String functionId;
}
