package com.poke.admin.controller;

import com.poke.admin.service.RechargeRecordService;
import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.RechargeCard;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

public class AddRoomCardController {

    @Resource
    private RechargeRecordService rechargeRecordService;

    @ApiOperation("为玩家充值房卡")
    @RequestMapping(value = "/api/recharge/card", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> rechargeCard(@RequestBody RechargeCard rechargeCard){
        return rechargeRecordService.rechargeCard(rechargeCard);
    }
}
