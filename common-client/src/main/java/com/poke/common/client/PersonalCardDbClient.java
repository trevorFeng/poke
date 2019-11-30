package com.poke.common.client;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.domain.mysql.PersonalCard;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("db-service")
public interface PersonalCardDbClient {

    @RequestMapping(value = "/api/personalCard/queryCardNum/{userId}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Integer> findCardNumByUserId(@PathVariable  Integer userId);

    @RequestMapping(value = "/api/personalCard/save" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Integer> save(@RequestBody PersonalCard personalCard);

    @RequestMapping(value = "/api/personalCard/update/card" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Object> updatePersonalCardNum(Integer userId, Integer card);


}
