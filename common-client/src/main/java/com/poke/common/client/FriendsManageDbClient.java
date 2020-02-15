package com.poke.common.client;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.FriendsManage;
import com.poke.common.bean.enums.MessageCodeEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("db-service")
public interface FriendsManageDbClient {

    @RequestMapping(value = "/api/friendsManage/find/userId" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<List<FriendsManage>> findByUserId(@RequestParam(value = "userId") Integer userId);

    @RequestMapping(value = "/api/friendsManage/save" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Object> save(@RequestBody FriendsManage friendsManage);

    @RequestMapping(value = "/api/friendsManage/update" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Object> update(@RequestBody FriendsManage friendsManage);


    @RequestMapping(value = "/api/friendsManage/count/userId/{userId}/manageId/{manageId}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Boolean> countFriendByUserIdAndManageId(@PathVariable("userId") Integer userId, @PathVariable("manageId") Integer manageId);


}
