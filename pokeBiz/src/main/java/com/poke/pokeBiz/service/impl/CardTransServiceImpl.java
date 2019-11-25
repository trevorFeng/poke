package com.poke.pokeBiz.service.impl;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.CardTrans;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.common.client.CardTransDbClient;
import com.poke.common.client.PersonalCardDbClient;
import com.poke.pokeBiz.service.CardTransService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CardTransServiceImpl implements CardTransService {

    @Resource
    private CardTransDbClient cardTransDbClient;

    @Resource
    private PersonalCardDbClient personalCardDbClient;

    /**
     * 生成房卡包
     * @param cardNum
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonEntity<String> createCardPackage(Integer cardNum , User user) {
        //判断玩家房卡数量是否大于交易的房卡数
        Integer cardNumByUserId = personalCardDbClient.findCardNumByUserId(user.getId()).getData();
        if (cardNumByUserId < cardNum) {
            return ResponseHelper.withErrorInstance(MessageCodeEnum.USER_ROOMCARD_NOT_ENOUGH);
        }
        //生成房卡交易，插入数据库
        Long time = System.currentTimeMillis();
        CardTrans cardTrans = new CardTrans();
        cardTrans.setCardNum(cardNum);
        cardTrans.setTurnOutUserName(user.getAppName());
        cardTrans.setTurnOutUserId(user.getId());
        cardTrans.setTurnOutTime(time);
        cardTrans.setTransNum(UUID.randomUUID().toString() + time);
        cardTrans.setVersion((byte)0);
        cardTransDbClient.insert(cardTrans);
        //减去玩家拥有的房卡数量
        personalCardMapper.updatePersonalCardNum(user.getId() ,cardNumByUserId - cardNum);
        return ResponseHelper.createInstance(cardTrans.getTransNum() ,MessageCodeEnum.CREATE_SUCCESS);
    }


    /**
     * 领取房卡包
     * @param transNum
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonEntity<Object> receiveCardPackage(String transNum , User user) {
        //将交易关闭
        Long termNum = cardTransMapper.closeTrans(transNum ,System.currentTimeMillis() , user.getId() , user.getAppName());
        if (!Objects.equals(BizKeys.ONE_UPDATE ,termNum)) {
            return ResponseHelper.withErrorInstance(MessageCodeEnum.TRANS_CLOSE);
        }
        //更新玩家房卡
        Integer cardNum = cardTransMapper.findCardNumByTransNo(transNum);
        Integer cardNumByUserId = personalCardMapper.findCardNumByUserId(user.getId());
        personalCardMapper.updatePersonalCardNum(user.getId() ,cardNumByUserId + cardNum);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.RECEIVE_SUCCESS);
    }

    /**
     * 查询发出的房卡
     * @return
     */
    public JsonEntity<List<CardTrans>> findSendCardRecord(User user) {
        List<CardTrans> cardTrans = this.cardTransMapper.findSendCardRecord(user.getId());
        return ResponseHelper.createInstance(cardTrans ,MessageCodeEnum.QUERY_SUCCESS);
    }

    /**
     * 查询收到的房卡
     * @return
     */
    public JsonEntity<List<CardTrans>> findRecevedCardRecord(User user) {
        List<CardTrans> recevedCardRecord = this.cardTransMapper.findRecevedCardRecord(user.getId());
        return ResponseHelper.createInstance(recevedCardRecord ,MessageCodeEnum.QUERY_SUCCESS);
    }
}
