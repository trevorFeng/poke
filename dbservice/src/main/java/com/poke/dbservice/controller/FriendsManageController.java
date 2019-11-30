package com.poke.dbservice.controller;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.FriendsManage;
import com.poke.common.bean.domain.mysql.PersonalCard;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.dbservice.service.FriendsManageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class FriendsManageController {

    @Resource
    private FriendsManageService friendsManageService;

    @RequestMapping(value = "/api/friendsManage/find/userId" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<List<FriendsManage>> findByUserId(@RequestParam Integer userId){
        return ResponseHelper.createInstance(friendsManageService.findByUserId(userId) ,MessageCodeEnum.HANDLER_SUCCESS);

    }

    @RequestMapping(value = "/api/friendsManage/save" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> save(@RequestBody FriendsManage friendsManage){
        friendsManageService.save(friendsManage);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.HANDLER_SUCCESS);

    }

    @RequestMapping(value = "/api/friendsManage/update" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> update(@RequestBody FriendsManage friendsManage){
        friendsManageService.update(friendsManage);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.HANDLER_SUCCESS);
    }

}
