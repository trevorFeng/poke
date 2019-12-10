package com.poke.dbservice.service;

import com.poke.common.bean.domain.mysql.User;

import java.util.List;

public interface UserService {

    User findByOpenidAndUserId(String openid ,Integer userId);

    List<User> findUsersByIds(List<Integer> userIds);

    User findUserById(Integer userId);
}
