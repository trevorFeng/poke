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

    @RequestMapping(value = "/api/user/query/userId/{userId}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<User> findByUserId(@PathVariable Integer userId){
        return ResponseHelper.createInstance(userService.findUserById(userId) , MessageCodeEnum.HANDLER_SUCCESS);
    }

    @RequestMapping(value = "/api/user/query/phoneNum/{phoneNum}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<User> findByPhoneNum(@PathVariable(value = "phoneNum") String phoneNum) {
        return ResponseHelper.createInstance(userService.findPhoneNum(phoneNum) , MessageCodeEnum.HANDLER_SUCCESS);
    }

    @RequestMapping(value = "/api/user/save" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> saveUser(@RequestBody User user){
        userService.saveUser(user);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.HANDLER_SUCCESS);
    }

    @RequestMapping(value = "/api/user/isExist/opnenId/{openid}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Boolean> isExistByOpnenId(@PathVariable String openid) {
        return ResponseHelper.createInstance(userService.isExistByOpnenId(openid) , MessageCodeEnum.HANDLER_SUCCESS);
    }

    @RequestMapping(value = "/api/user/update/user" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> updateUser(@RequestBody User user){
        userService.updateUser(user);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.HANDLER_SUCCESS);
    }

    @RequestMapping(value = "/api/user/isExist/phoneNum/{phoneNum}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Boolean> isExistByPhoneNum(@PathVariable String phoneNum) {
        return ResponseHelper.createInstance(userService.isExistByPhoneNum(phoneNum) , MessageCodeEnum.HANDLER_SUCCESS);
    }


}
