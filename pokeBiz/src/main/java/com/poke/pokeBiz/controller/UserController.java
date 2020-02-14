package com.poke.pokeBiz.controller;


import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.LoginUser;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.common.client.PersonalCardDbClient;
import com.poke.common.core.UserContextHolder;
import com.poke.pokeBiz.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 一句话描述该类作用:【】
 *
 * @author: trevor
 * @create: 2019-03-14 0:56
 **/
@Api(value = "登出和获取登录用户" ,description = "登出和获取登录用户")
@RestController
public class UserController {

    @Resource
    private PersonalCardDbClient personalCardDbClient;

    @Resource
    private RedisService redisService;

    @ApiOperation("获取登录用户")
    @RequestMapping(value = "/api/login/user", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<LoginUser> getLoginUser() {
        User user = UserContextHolder.currentUser();
        Integer cardNum = personalCardDbClient.findCardNumByUserId(user.getId()).getData();

        LoginUser loginUser = new LoginUser();
        loginUser.setId(user.getId());
        loginUser.setAppName(user.getAppName());
        loginUser.setAppPictureUrl(user.getAppPictureUrl());
        loginUser.setFriendManageFlag(user.getFriendManageFlag());
        loginUser.setCardNum(cardNum);

        return ResponseHelper.createInstance(loginUser , MessageCodeEnum.HANDLER_SUCCESS);
    }

    @ApiOperation("退出登录")
    @RequestMapping(value = "/api/login/out", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> loginOut(@RequestParam String token) {
        //删除token
        redisService.delete("acess_token:" + token);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.LOGIN_OUT_SUCCESS);
    }
}
