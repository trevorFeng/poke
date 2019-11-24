package com.poke.dbservice.dao.mysql;


import com.poke.common.bean.domain.mysql.RechargeRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);
}