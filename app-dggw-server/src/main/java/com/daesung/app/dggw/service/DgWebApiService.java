package com.daesung.app.dggw.service;

import com.daesung.cmn.server.util.ServerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DgWebApiService {

    @Value("5")
    private long TIME_OUT;

    private final RedisTemplate<String, String> redisTemplate;
    private final ServerUtils serverUtils;

    public String getServiceKey(String dong, String ho){
        log.info("getServiceKey called");

        try {
            try {
                //단지 redis 서버를 통해 accessToken 정보 조회
                ValueOperations<String, String> vop = redisTemplate.opsForValue();

                String key = serverUtils.getRedisKey(dong, ho);
                String result = vop.get(key);

                return result;
            } catch (Exception e){
                log.error("", e);
            }
        }catch (Exception e){
            log.error("", e);
        }
        return null;
    }
}
