package com.daesung.domain.dggw.constant;

import com.daesung.domain.dggw.web.data.EnergyUseTarget;
import com.daesung.domain.dggw.web.data.NoticeDetail;
import com.daesung.domain.dggw.web.data.NoticeList;
import com.daesung.domain.dggw.web.data.UserList;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 단지서버(공용부)와 단지GW서버 간 API 목록(http rest 방식)
 */
@Getter
@AllArgsConstructor
public enum DgWebFunctionId {
    //공용부
    getNoticeList("getNoticeList","공지사항목록조회","GET","/notice","getNoticeList", NoticeList.class)
    ,getNoticeDetail("getNoticeDetail","공지사항상세보기","GET","/notice","getNoticeDetail", NoticeDetail.class)
    ,postEnergyUseTargetSet("postEnergyUseTargetSet","에너지목표사용량설정","POST","/ems","postEnergyUseTargetSet", EnergyUseTarget.class)
    ,getUserList("getUserList", "사용자목록조회", "GET", "/user", "getUserList", UserList.class)
    ;

    private String id;
    private String desc;
    private String method;
    private String route;
    private String uri;
    private Class<?> requestClass;
}
