package com.poke.dbservice.dao.mysql;

import com.poke.common.bean.domain.mysql.CardTrans;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author trevor
 * @date 2019/3/8 17:23
 */
@Repository
public interface CardTransMapper {


    void insert(@Param("cardTrans") CardTrans cardTrans);

    /**
     * 根据交易号查询交易房卡的数量
     * @param transNo
     * @return
     */
    Integer findCardNumByTransNo(@Param("transNo") String transNo);

    /**
     * 关闭交易,将版本号置为1
     * @param transNo
     * @param turnInTime
     * @param turnInUserId
     * @return
     */
    Long closeTrans(@Param("transNo") String transNo, @Param("turnInTime") Long turnInTime
            , @Param("turnInUserId") Long turnInUserId, @Param("turnInUserName") String turnInUserName);

    /**
     * 查询发出的房卡
     * @param userId
     * @return
     */
    List<CardTrans> findSendCardRecord(@Param("userId") Long userId);

    /**
     * 查询收到的房卡
     * @param turnInUserId
     * @return
     */
    List<CardTrans> findRecevedCardRecord(@Param("turnInUserId") Long turnInUserId);
}
