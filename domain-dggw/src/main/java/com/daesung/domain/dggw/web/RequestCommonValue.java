package com.daesung.domain.dggw.web;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class RequestCommonValue {

    //공통
    private String serviceKey; //setCommonValue
    private String dongCode;   //setCommonValue
    private String hoCode;     //setCommonValue
    private String resultCode;
    private String resultMsg;

    //조회시 공통
    private String numOfRows;
    private String pageNo;
    private String doubleDataFlag;
    private String totalCount;
    private String viewPeriod;
}
