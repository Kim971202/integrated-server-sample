package com.daesung.domain.jpa.dggw.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name="gw_event_subscribe_info")
public class SubscribeInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id; // ID

    @Column(name = "AUTH_ID")
    private String authId; // 인증ID(=기기ID)

    @Column(name = "AREA_CODE")
    private String areaCode; // 단지코드

    @Column(name = "AREA_DONG")
    private String areaDong; // 동

    @Column(name = "AREA_HO")
    private String areaHo; // 호

    @Column(name = "RTN_TOKEN")
    private String rtnToken; // 리턴토큰

    @Column(name = "RTN_URL")
    private String rtnUrl; // 리턴URL

    @Column(name = "CREATE_AT")
    private LocalDateTime creatAt; // 생성일시

    @Column(name = "UPDATE_AT")
    private LocalDateTime updaateAt; // 수정일시

    @Builder

    public SubscribeInfoEntity(
            Long id, String authId, String areaCode, String areaDong, String areaHo,
            String rtnToken, String rtnUrl, LocalDateTime creatAt, LocalDateTime updaateAt) {
        this.id = id;
        this.authId = authId;
        this.areaCode = areaCode;
        this.areaDong = areaDong;
        this.areaHo = areaHo;
        this.rtnToken = rtnToken;
        this.rtnUrl = rtnUrl;
        this.creatAt = creatAt;
        this.updaateAt = updaateAt;
    }

    public void updateInfo(String rtnUrl, String rtnToken, LocalDateTime updaateAt){
        this.rtnUrl = rtnUrl;
        this.rtnToken = rtnToken;
        this.updaateAt = updaateAt;
    }

}
