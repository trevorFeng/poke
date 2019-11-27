package com.poke.user.service.impl;

import com.poke.common.bean.domain.mysql.User;
import com.poke.common.client.UserDbClient;
import com.poke.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDbClient userDbClient;

    @Override
    public User findByOpenid(String openid ,Integer userId) {
        return userDbClient.findByOpenid(openid).getData();
    }
}
