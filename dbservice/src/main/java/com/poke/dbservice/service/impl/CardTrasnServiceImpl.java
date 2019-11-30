package com.poke.dbservice.service.impl;

import com.poke.common.bean.bo.BizKeys;
import com.poke.common.bean.domain.mysql.CardTrans;
import com.poke.common.bean.exception.BizException;
import com.poke.dbservice.dao.mysql.CardTransMapper;
import com.poke.dbservice.service.CardTrasnService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class CardTrasnServiceImpl implements CardTrasnService {

    @Resource
    private CardTransMapper cardTransMapper;

    @Override
    public void save(CardTrans cardTrans) {
        cardTransMapper.insert(cardTrans);
    }

    @Override
    public void closeTrans(String transNo, Long turnInTime, Integer turnInUserId, String turnInUserName) {
        Integer termNum = cardTransMapper.closeTrans(transNo, turnInTime, turnInUserId, turnInUserName);
        if (!Objects.equals(BizKeys.ONE_UPDATE ,termNum)) {
            throw new BizException(-1 ,"领取房卡失败或者房卡已经被领取");
        }
    }

    @Override
    public Integer findCardNumByTransNo(String transNo) {
        Integer cardNumByTransNo = cardTransMapper.findCardNumByTransNo(transNo);
        if (cardNumByTransNo == null) {
            throw new BizException(-1 ,"未发现房卡交易记录");
        }
        return cardNumByTransNo;
    }

    @Override
    public List<CardTrans> findSendCardRecord(Integer userId) {
        return cardTransMapper.findSendCardRecord(userId);
    }

    @Override
    public List<CardTrans> findRecevedCardRecord(Integer turnInUserId) {
        return cardTransMapper.findRecevedCardRecord(turnInUserId);
    }
}
