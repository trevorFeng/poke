package com.poke.pokeBiz.service;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.domain.mysql.CardTrans;
import com.poke.common.bean.domain.mysql.User;

import java.util.List;

/**
 * @author trevor
 * @date 2019/3/8 17:05
 */
public interface CardTransService {

    JsonEntity<String> createCardPackage(Integer cardNum , User user);

    JsonEntity<Object> receiveCardPackage(String transNum , User user);

    JsonEntity<List<CardTrans>> findSendCardRecord(User user);

    JsonEntity<List<CardTrans>> findRecevedCardRecord(User user);
}
