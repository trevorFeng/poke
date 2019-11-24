package com.poke.user.service.impl;

import com.poke.common.bean.domain.mysql.User;
import com.poke.common.client.UserClient;
import com.poke.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserClient userClient;

    @Override
    public User findByOpenid(String openid) {
        return userClient.findByOpenid(openid).getData();
    }
}
