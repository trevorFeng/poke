package com.poke.dbservice.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.exception.BizException;
import com.poke.dbservice.dao.mysql.UserMapper;
import com.poke.dbservice.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User findByOpenid(String openid ,Integer userId) {
        if (StringUtils.isEmpty(openid) || userId == null) {
            throw new BizException(500 ,"openid或userId不能为空");
        }
        User user = userMapper.findByOpenidAndUserId(openid, userId);
        if (user == null) {
            throw new BizException(500 ,"没有该用户");
        }
        return user;
    }
}
