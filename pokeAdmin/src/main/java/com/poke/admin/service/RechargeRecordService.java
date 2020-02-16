package com.poke.admin.service;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.RechargeCard;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.RechargeRecord;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.common.client.PersonalCardDbClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author trevor
 * @date 03/21/19 18:24
 */
@Service
@Slf4j
public class RechargeRecordService {

    @Resource
    private PersonalCardDbClient personalCardDbClient;

    /**
     * 为玩家充值
     * @param rechargeCard
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonEntity<Object> rechargeCard(RechargeCard rechargeCard) {
        Integer userId = rechargeCard.getUserId();
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUserId(userId);
        rechargeRecord.setRechargeCard(rechargeCard.getCardNum());
        rechargeRecord.setUnitPrice(rechargeCard.getUnitPrice());
        rechargeRecord.setTotalPrice(rechargeCard.getTotalPrice());
        rechargeRecord.setTime(System.currentTimeMillis());
        //rechargeRecordMapper.insertOne(rechargeRecord);

        Integer hasCardNum = personalCardDbClient.findCardNumByUserId(userId).getData();
        if (hasCardNum == null) {
            hasCardNum = 0;
        }
        personalCardDbClient.updatePersonalCardNum(userId ,hasCardNum + rechargeCard.getCardNum());

        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.HANDLER_SUCCESS);
    }
}
