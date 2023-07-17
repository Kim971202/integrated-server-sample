package com.daesung.domain.dggw.web.data;

import com.daesung.domain.dggw.web.RequestCommonValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
@JsonInclude(Include.NON_EMPTY)
public class NoticeList extends RequestCommonValue {
    //전체데이터(ALL), 전체공지(1), 세대 개별공지(2)
    private String notiType;

    private DATA data;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder @Getter @Setter
    @JsonInclude(Include.NON_EMPTY)
    public static class DATA {
        private String dongCode;
        private String hoCode;
        private String notiType;
        private List<ITEMS> items;

        @NoArgsConstructor @AllArgsConstructor
        @Builder @Getter @Setter
        @JsonInclude(Include.NON_EMPTY)
        public static class ITEMS {
            private String notiType;
            private String idx;
            private String notiTitle;
            //yyyyMMdd
            private String startDate;
            //Y/N
            private String newFlag;
        }
    }
}
