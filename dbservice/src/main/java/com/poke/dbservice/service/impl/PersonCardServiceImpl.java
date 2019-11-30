package com.poke.dbservice.service.impl;

import com.poke.common.bean.domain.mysql.PersonalCard;
import com.poke.common.bean.exception.BizException;
import com.poke.dbservice.dao.mysql.PersonalCardMapper;
import com.poke.dbservice.service.PersonalCardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class PersonCardServiceImpl implements PersonalCardService {

    @Resource
    private PersonalCardMapper personalCardMapper;

    @Override
    public Integer findCardNumByUserId(Integer userId) {
        Integer cardNumByUserId = personalCardMapper.findCardNumByUserId(userId);
        if (cardNumByUserId == null) {
            throw new BizException(-1 ,"玩家没有房卡记录");
        }
        return cardNumByUserId;
    }

    @Override
    public void updatePersonalCardNum(Integer userId, Integer card) {
        Integer num = personalCardMapper.updatePersonalCardNum(userId, card);
        if (Objects.equals(num ,0)) {
            throw new BizException(-1 ,"未发现更新房卡记录");
        }
    }

    @Override
    public void save(PersonalCard personalCard) {

    }
}
