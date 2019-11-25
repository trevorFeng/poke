package com.poke.common.client;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.domain.mysql.CardTrans;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient("db-service")
public interface CardTransDbClient {

    @RequestMapping(value = "/api/cardTrans/save" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Integer> insert(@RequestBody CardTrans cardTrans);

}
