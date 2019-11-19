package com.poke.dbservice.dao.mysql;


import com.poke.common.bean.domain.mysql.CardConsumRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author trevor
 * @date 2019/3/8 16:23
 */
@Repository
public interface CardConsumRecordMapper {


    Long insert(@Param("cardConsumRecord") CardConsumRecord cardConsumRecord);

    void deleteByRoomIds(@Param("roomRecordIds") List<Long> roomIds);
}
