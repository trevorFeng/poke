package com.poke.dbservice.dao.mysql;


import com.poke.common.bean.domain.mysql.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    User findByOpenidAndUserId(String openid ,Integer userId);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}