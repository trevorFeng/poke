package com.poke.dbservice.service;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.domain.mysql.CardTrans;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CardTrasnService {

    void save(CardTrans cardTrans);

    void closeTrans(String transNo , Long turnInTime ,Integer turnInUserId ,String turnInUserName);

    Integer findCardNumByTransNo(String transNo);

    List<CardTrans> findSendCardRecord(Integer userId);

    List<CardTrans> findRecevedCardRecord(Integer turnInUserId);



}
