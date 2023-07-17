package com.daesung.domain.dggw.web.data;

import com.daesung.domain.dggw.web.RequestCommonValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Getter @Setter
@ToString
@JsonInclude(Include.NON_EMPTY)
public class NoticeDetail extends RequestCommonValue {
    //공지번호
    private String idx;

    private DATA data;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class DATA {
        private String idx;
        private String notiType;
        private String notiTitle;
        private String startDate;
        private String notiOwner;
        private String notiContent;
        private String filePath;
        private String fileName;
    }
}