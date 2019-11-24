package com.poke.dbservice.dao.mysql;


import com.poke.common.bean.domain.mysql.PersonalCard;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PersonalCard record);

    int insertSelective(PersonalCard record);

    PersonalCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PersonalCard record);

    int updateByPrimaryKey(PersonalCard record);
}