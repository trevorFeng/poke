package com.poke.user.service;

import com.poke.common.bean.domain.mysql.User;

public interface UserService {

    User findByOpenid(String openid ,Integer userId);
}
