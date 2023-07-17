package com.daesung.cmn.server.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
public class ServerUtils {

    /**
     * 트랜잭션 ID
     *
     * @return
     */
    public String getTransactionId() {
        log.info("getTransactionId called");

        return getTransactionIdBaseUUID();
    }

    /**
     * TransactionID 생성 UUID 리턴
     *
     * @return a432e21a-54df-4e43-8ef9-99cd274dced8
     */
    private String getTransactionIdBaseUUID() {
        log.info("getTransactionIdBaseUUID called");
        // Random UUID
        return UUID.randomUUID().toString();
    }

    public String getDate(String pattern) {
        log.info("getDate called");

        return getTimeAsiaSeoulNow().format(DateTimeFormatter.ofPattern(pattern));
    }

    public String changeDateFormat(String date, String from, String to) {
        log.info("changeDateFormat called");

        try {
            return new SimpleDateFormat(to).format(new SimpleDateFormat(from).parse(date));
        } catch (Exception e) {
            log.error("", e);
        }
        return date;
    }

    /**
     * 홈아이디(단지코드, 동, 호)
     *
     * input : "0000","103","301" output : "0000.0103.0301"
     *
     * @param dgCode
     * @param dgDong
     * @param dgHo
     * @return
     */
    public String getHomeId(String dgCode, String dgDong, String dgHo) {
        log.info("getHomeId called");

        return String.format("%04d.%04d.%04d", Integer.parseInt(StringUtils.hasText(dgCode) ? dgCode : "0000"),
                Integer.parseInt(StringUtils.hasText(dgDong) ? dgDong : "0000"),
                Integer.parseInt(StringUtils.hasText(dgHo) ? dgHo : "0000"));
    }

    /**
     * input : "0000.0103.0301" output : "0000"
     *
     * @param homeId
     * @return
     */
    public String getDgCode(String homeId) {
        log.info("getDgCode called");

        return homeId.split("\\.")[0];
    }

    /**
     * 단지코드, 동, 호 정보를 4자리 규격에 맞도록 변환 처리
     *
     * @param value
     * @return
     */
    public String getTransCodeFormat(String value) {
        log.info("getTransCodeFormat called");

        return String.format("%04d", Integer.parseInt(StringUtils.hasText(value) ? value : "0000"));
    }

    /**
     * 단지GW서버 ext id 값을 2자리 규격에 맞도록 변환 처리
     *
     * @param value
     * @return
     */
    public String getTransExtIdFormat(String value) {
        log.info("getTransExtIdFormat called");

        return String.format("%02d", Integer.parseInt(StringUtils.hasText(value) ? value : "01"));
    }

    /**
     * input : "0000.0103.0301" output : "0103"
     *
     * @param homeId
     * @return
     */
    public String getDgDong(String homeId) {
        log.info("getDgDong called");

        return homeId.split("\\.")[1];
    }

    /**
     * input : "0000.0103.0301" output : "0301"
     *
     * @param homeId
     * @return
     */
    public String getDgHo(String homeId) {
        log.info("getDgHo called");

        return homeId.split("\\.")[2];
    }

    public String getDongFrom(String sourceId) {
        log.info("getDongFrom called");

        if (sourceId == null)
            return "";

        return sourceId.substring(4, 8);
    }

    public String getHoFrom(String sourceId) {
        log.info("getHoFrom called");

        if (sourceId == null)
            return "";

        return sourceId.substring(8, 12);
    }

    public String getRedisKey(String dgDong, String dgHo) {
        log.info("getRedisKey(String dgDong, String dgHo) called");

        return getRedisKey("0000", dgDong, dgHo, "00", "01");
    }

    /**
     * accessToken 조회를 위한 key 값 리턴.
     *
     * @param dgCode 단지코드4
     * @param dgDong 동4
     * @param dgHo   호4
     * @param ext    확장2
     * @param id     기기ID2
     * @return
     */
    public String getRedisKey(String dgCode, String dgDong, String dgHo, String ext, String id) {
        log.info("getRedisKey(String dgCode, String dgDong, String dgHo, String ext, String id) called");

        return "0000" + getTransCodeFormat(dgDong) + getTransCodeFormat(dgHo) + ext + id;
    }

    public LocalDateTime getTimeAsiaSeoulNow() {
        log.info("getTimeAsiaSeoulNow called");

        return getTimeNow("Asia/Seoul");
    }

    public LocalDateTime getTimeNow(String zoneId) {
        log.info("getTimeNow called");

        return LocalDateTime.now(ZoneId.of(zoneId));
    }

    public static void main(String[] args) {
        log.info("main called");
    }
}
