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
public class UserList extends RequestCommonValue {

    private DATA data;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    @JsonInclude(Include.NON_EMPTY)
    public static class DATA {
        private String dongCode;
        private String hoCode;
        private List<ITEMS> items;

        @NoArgsConstructor @AllArgsConstructor
        @Builder @Getter @Setter
        @JsonInclude(Include.NON_EMPTY)
        public static class ITEMS {
            private String userIdx;
            private String userName;
            private String userAge;
            private String userNickName;
            //yyyyMMdd
            private String insertDtime;
            //Default N
            private char exitFlag;

        }

    }
}
