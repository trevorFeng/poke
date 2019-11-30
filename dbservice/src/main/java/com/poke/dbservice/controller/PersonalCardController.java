package com.poke.dbservice.controller;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.PersonalCard;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.dbservice.service.PersonalCardService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class PersonalCardController {

    @Resource
    private PersonalCardService personalCardService;

    @RequestMapping(value = "/api/personalCard/save" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> save(@RequestBody PersonalCard personalCard){
        personalCardService.save(personalCard);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.HANDLER_SUCCESS);

    }

    @RequestMapping(value = "/api/personalCard/queryCardNum/{userId}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Integer> findCardNumByUserId(@PathVariable Integer userId) {
        Integer cardNumByUserId = personalCardService.findCardNumByUserId(userId);
        return ResponseHelper.createInstance(cardNumByUserId ,MessageCodeEnum.HANDLER_SUCCESS);
    }

    @RequestMapping(value = "/api/personalCard/update/card" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> updatePersonalCardNum(Integer userId, Integer card){
        personalCardService.updatePersonalCardNum(userId ,card);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.HANDLER_SUCCESS);
    }
}
