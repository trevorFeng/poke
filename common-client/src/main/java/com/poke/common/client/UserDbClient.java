package com.poke.common.client;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.domain.mysql.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("db-service")
public interface UserDbClient {

    @RequestMapping(value = "/api/user/query/openid/userid" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<User> findByOpenidAndUserId(@RequestParam(value = "openid") String openid ,@RequestParam(value = "userId") Integer userId);

    @RequestMapping(value = "/api/user/query/userIds" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<List<User>> findByUserIdList(@RequestParam(value = "userIds") List<Integer> userIds);

    @RequestMapping(value = "/api/user/query/userId/{userId}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<User> findByUserId(@PathVariable(value = "userId") Integer userId);

    @RequestMapping(value = "/api/user/query/phoneNum/{phoneNum}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<User> findByPhoneNum(@PathVariable(value = "phoneNum") String phoneNum);

    @RequestMapping(value = "/api/user/save" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Object> save(@RequestBody User user);

    @RequestMapping(value = "/api/user/isExist/opnenId/{openid}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Boolean> isExistByOpnenId(@PathVariable(value = "openid") String openid);

    @RequestMapping(value = "/api/user/isExist/phoneNum/{phoneNum}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Boolean> isExistByPhoneNum(@PathVariable(value = "phoneNum") String phoneNum);

    @RequestMapping(value = "/api/user/update/user" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Object> updateUser(@RequestBody User user);
}
