package com.poke.dbservice.dao.mysql;


import com.poke.common.bean.domain.mysql.PersonalCard;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PersonalCard record);

    int insertSelective(PersonalCard record);

    PersonalCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PersonalCard record);

    int updateByPrimaryKey(PersonalCard record);

    /**
     * 根据玩家id更新房卡数量
     * @param userId
     * @param card
     */
    Integer updatePersonalCardNum(@Param("userId") Integer userId, @Param("card") Integer card);

    /**
     * 根据玩家查询玩家拥有的房卡数量
     * @param userId
     * @return
     */
    Integer findCardNumByUserId(@Param("userId") Integer userId);

}