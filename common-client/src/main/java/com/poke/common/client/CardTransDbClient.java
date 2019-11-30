package com.poke.common.client;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.CardTrans;
import com.poke.common.bean.enums.MessageCodeEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("db-service")
public interface CardTransDbClient {

    @RequestMapping(value = "/api/cardTrans/save" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Object> save(@RequestBody CardTrans cardTrans);

    @RequestMapping(value = "/api/cardTrans/close/trans" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Object> closeTrans(@RequestParam("transNo") String transNo ,@RequestParam("turnInTime") Long turnInTime ,
                                         @RequestParam("turnInUserId") Integer turnInUserId ,@RequestParam("turnInUserName") String turnInUserName);

    @RequestMapping(value = "/api/cardTrans/cardNum/transNo" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Integer> findCardNumByTransNo(@RequestParam String transNo);

    @RequestMapping(value = "/api/cardTrans/send/cardRecord" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<List<CardTrans>> findSendCardRecord(@RequestParam Integer userId);

    @RequestMapping(value = "/api/cardTrans/receved/cardRecord" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<List<CardTrans>> findRecevedCardRecord(@RequestParam Integer turnInUserId);

}
