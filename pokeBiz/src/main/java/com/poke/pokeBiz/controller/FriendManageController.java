package com.poke.pokeBiz.controller;

import com.poke.common.bean.bo.FriendInfo;
import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.pokeBiz.service.FriendManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 一句话描述该类作用:【好友管理】
 *
 * @author: trevor
 * @create: 2019-03-03 23:05
 **/
@Api(value = "好友管理" ,description = "好友管理相关接口")
@RestController
public class FriendManageController {

    @Resource
    private FriendManagerService friendManagerService;

    /**
     * 查询好友（申请通过和未通过的）
     * @return
     */
    @ApiOperation(value = "查询好友（申请通过和未通过的）")
    @RequestMapping(value = "/api/friend/manager/query", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<List<FriendInfo>> queryFriends(){
        List<FriendInfo> friendInfos = friendManagerService.queryFriends();
        return ResponseHelper.createInstance(friendInfos , MessageCodeEnum.QUERY_SUCCESS);
    }

    /**
     * 申请成为房主的好友
     * @return
     */
    @ApiOperation(value = "申请成为房主的好友")
    @RequestMapping(value = "/api/friend/manager/query/{roomId}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> applyFriend(@PathVariable("roomId") Integer roomId){
        friendManagerService.applyFriend(roomId);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.HANDLER_SUCCESS);
    }

    /**
     * 踢出好友
     * @return
     */
    @ApiOperation(value = "踢出好友")
    @RequestMapping(value = "/api/friend/manager/remove/{removeUserId}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> removeFriend(@PathVariable("removeUserId") Integer removeUserId){
        friendManagerService.removeFriend(removeUserId);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.HANDLER_SUCCESS);
    }

    /**
     * 通过好友申请
     * @return
     */
    @ApiOperation(value = "通过好友申请")
    @RequestMapping(value = "/api/friend/manager/pass/{passUserId}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> passFriend(@PathVariable("passUserId") Integer passUserId){
        friendManagerService.passFriend(passUserId);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.HANDLER_SUCCESS);
    }


}
