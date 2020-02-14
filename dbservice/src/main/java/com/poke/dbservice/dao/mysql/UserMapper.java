package com.poke.dbservice.dao.mysql;


import com.poke.common.bean.domain.mysql.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    Long countByPhone(String phoneNum);

    Long countByOpenid(String openid);

    User findByOpenidAndUserId(String openid ,Integer userId);

    User findByPhoneNum(String phoneNum);

    /**
     * 根据id集合查询用户
     * @param ids
     * @return
     */
    List<User> findUsersByIds(@Param("ids") List<Integer> ids);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}