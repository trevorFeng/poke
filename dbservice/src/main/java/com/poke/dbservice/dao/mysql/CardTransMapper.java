package com.poke.dbservice.dao.mysql;


import com.poke.common.bean.domain.mysql.CardTrans;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTransMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CardTrans record);

    int insertSelective(CardTrans record);

    CardTrans selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CardTrans record);

    int updateByPrimaryKey(CardTrans record);
}