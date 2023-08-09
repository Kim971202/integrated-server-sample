package com.daesung.domain.jpa.dggw.repository;

import com.daesung.domain.jpa.dggw.entity.SubscribeInfoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SubscribeInfoRepository extends CrudRepository<SubscribeInfoEntity, String> {

    public SubscribeInfoEntity findByAuthId(String authId);
    public SubscribeInfoEntity findByAreaCodeAndAreaDongAndAreaHo(String areaCode, String areaDong, String areaHo);

    @Transactional
    public void deleteByAuthId(String authId);
    @Transactional
    public void deleteByAreaCodeAndAreaDongAndAreaHo(String areaCode, String areaDong, String areaHo);
}
