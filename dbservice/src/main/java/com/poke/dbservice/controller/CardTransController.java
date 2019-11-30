package com.poke.dbservice.controller;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.CardTrans;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.dbservice.service.CardTrasnService;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class CardTransController {

    @Resource
    private CardTrasnService cardTrasnService;

    @RequestMapping(value = "/api/cardTrans/save" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> save(@RequestBody CardTrans cardTrans) {
        cardTrasnService.save(cardTrans);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.HANDLER_SUCCESS);
    }

    @RequestMapping(value = "/api/cardTrans/close/trans" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> closeTrans(@RequestParam("transNo") String transNo ,@RequestParam("turnInTime") Long turnInTime ,
                                         @RequestParam("turnInUserId") Integer turnInUserId ,@RequestParam("turnInUserName") String turnInUserName){
        cardTrasnService.closeTrans(transNo ,turnInTime ,turnInUserId ,turnInUserName);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.HANDLER_SUCCESS);
    }

    @RequestMapping(value = "/api/cardTrans/cardNum/transNo" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Integer> findCardNumByTransNo(@RequestParam String transNo) {
        Integer cardNumByTransNo = cardTrasnService.findCardNumByTransNo(transNo);
        return ResponseHelper.createInstance(cardNumByTransNo ,MessageCodeEnum.HANDLER_SUCCESS);
    }

    @RequestMapping(value = "/api/cardTrans/send/cardRecord" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<List<CardTrans>> findSendCardRecord(@RequestParam Integer userId) {
        return ResponseHelper.createInstance(cardTrasnService.findSendCardRecord(userId) ,MessageCodeEnum.HANDLER_SUCCESS);
    }

    @RequestMapping(value = "/api/cardTrans/receved/cardRecord" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<List<CardTrans>> findRecevedCardRecord(@RequestParam Integer turnInUserId) {
        return ResponseHelper.createInstance(cardTrasnService.findRecevedCardRecord(turnInUserId) ,MessageCodeEnum.HANDLER_SUCCESS);
    }
}
