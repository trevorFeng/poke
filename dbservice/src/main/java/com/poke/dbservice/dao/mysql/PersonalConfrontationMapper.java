package com.poke.dbservice.dao.mysql;


import com.poke.common.bean.domain.mysql.PersonalConfrontation;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalConfrontationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PersonalConfrontation record);

    int insertSelective(PersonalConfrontation record);

    PersonalConfrontation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PersonalConfrontation record);

    int updateByPrimaryKey(PersonalConfrontation record);
}