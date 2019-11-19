package com.poke.dbservice.service;


import com.poke.common.bean.domain.mysql.CardConsumRecord;
import com.poke.dbservice.dao.mysql.CardConsumRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author trevor
 * @date 2019/3/8 16:23
 */
@Service
public class CardConsumRecordService {

    @Resource
    private CardConsumRecordMapper cardConsumRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long insertOne(CardConsumRecord cardConsumRecord){
        return cardConsumRecordMapper.insert(cardConsumRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoomRecordIds(List<Long> roomIds){
        cardConsumRecordMapper.deleteByRoomIds(roomIds);
    }


}
