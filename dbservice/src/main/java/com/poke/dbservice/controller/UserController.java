package com.poke.dbservice.controller;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.dbservice.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/api/user/query/openid/userid" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<User> findByOpenidAndUserId(@RequestParam String openid ,@RequestParam Integer userId){
        return ResponseHelper.createInstance(userService.findByOpenidAndUserId(openid ,userId) , MessageCodeEnum.HANDLER_SUCCESS);
    }

    @RequestMapping(value = "/api/user/query/userIds" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public JsonEntity<List<User>> findByUserIdList(@RequestParam List<Integer> userIds){
        return ResponseHelper.createInstance(userService.findUsersByIds(userIds) , MessageCodeEnum.HANDLER_SUCCESS);
    }


}
