package com.poke.dbservice.dao.mysql;


import com.poke.common.bean.domain.mysql.CardConsumRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface CardConsumRecordMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(CardConsumRecord record);

    int insertSelective(CardConsumRecord record);

    CardConsumRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CardConsumRecord record);

    int updateByPrimaryKey(CardConsumRecord record);
}