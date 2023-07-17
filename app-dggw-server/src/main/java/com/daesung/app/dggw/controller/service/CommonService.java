package com.daesung.app.dggw.controller.service;

import com.daesung.domain.dggw.web.RequestCommonValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CommonService {

    public String toCamelCase(String s) {

        log.info("toCamelCase called");

        Matcher m = Pattern.compile("[_|-](\\w)").matcher(s);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        return m.appendTail(sb).toString();
    }

    public Object setCommonValue(Object object, String serviceKey, String dong, String ho) {
        log.info("setCommonValue called");

        RequestCommonValue commonValue = (RequestCommonValue)object;

        commonValue.setServiceKey(serviceKey);
        commonValue.setDongCode(dong);
        commonValue.setHoCode(ho);
        commonValue.setDoubleDataFlag("N");

        return object;
    }
}