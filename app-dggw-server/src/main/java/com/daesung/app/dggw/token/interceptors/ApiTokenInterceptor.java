package com.daesung.app.dggw.token.interceptors;

import com.daesung.cmn.server.constant.Publics;
import com.daesung.cmn.server.util.ApiTokenUtils;
import com.daesung.cmn.server.util.JSON;
import com.daesung.domain.inte.token.TokenMaterial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ApiTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private ApiTokenUtils apiTokenUtils;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(DispatcherType.REQUEST.equals(request.getDispatcherType())){

            //서버 간 토큰 정보 송/수신은 request header Authorization key 값을 사용
            String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

            if(accessToken == null){
                log.info("AccessToken is NULL");
            } else {
                accessToken = accessToken.replace("Bearer ", "");
                log.info("수신.accessToken:{}", accessToken);

                TokenMaterial token = apiTokenUtils.verify(accessToken);
                log.info("==== token info ====\n{}", JSON.toJson(token, true));

                //sign 검증 실패시 or 유효기간 지난경우
                if( token == null){
                    log.info("sign 검증 실패시 or 유효기간 지난경우");
                } else {
                    //토큰 header, payload 필수값 체크
                    apiTokenUtils.tokenValidation(token);
                }
                //검증 된 토큰 정보를 셋팅.
                request.setAttribute(Publics.KEY_API_ACCESS_TOKEN, token);

            }

        }
        System.out.println("true Returned");
        return true;
    }
}
