package com.poke.dbservice.service;

import com.poke.common.bean.domain.mysql.User;

public interface UserService {

    User findByOpenid(String openid);
}
