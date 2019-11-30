package com.poke.dbservice.dao.mysql;


import com.poke.common.bean.domain.mysql.CardTrans;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardTransMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CardTrans record);

    int insertSelective(CardTrans record);

    CardTrans selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CardTrans record);

    int updateByPrimaryKey(CardTrans record);

    /**
     * 关闭交易,将版本号置为1
     * @param transNo
     * @param turnInTime
     * @param turnInUserId
     * @return
     */
    Integer closeTrans(@Param("transNo") String transNo, @Param("turnInTime") Long turnInTime
            , @Param("turnInUserId") Integer turnInUserId, @Param("turnInUserName") String turnInUserName);

    /**
     * 根据交易号查询交易房卡的数量
     * @param transNo
     * @return
     */
    Integer findCardNumByTransNo(@Param("transNo") String transNo);

    /**
     * 查询发出的房卡
     * @param userId
     * @return
     */
    List<CardTrans> findSendCardRecord(@Param("userId") Integer userId);

    /**
     * 查询收到的房卡
     * @param turnInUserId
     * @return
     */
    List<CardTrans> findRecevedCardRecord(@Param("turnInUserId") Integer turnInUserId);


}