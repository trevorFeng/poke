package com.poke.dbservice.dao.mysql;


import com.poke.common.bean.domain.mysql.RechargeRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 一句话描述该类作用:【】
 *
 * @author: trevor
 * @create: 2019-03-08 0:14
 **/
@Repository
public interface RechargeRecordMapper {

    /**
     * 插入一条记录
     * @param rechargeRecord
     */
    void insert(@Param("rechargeRecord") RechargeRecord rechargeRecord);
}
