package com.poke.common.client;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.domain.mongo.NiuniuRoomParam;
import com.poke.common.bean.domain.mysql.FriendsManage;
import com.poke.common.bean.domain.mysql.Room;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("db-service")
public interface RoomParamDbClient {

    @RequestMapping(value = "/api/room/niuniuParam/save" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Object> save(@RequestBody NiuniuRoomParam niuniuRoomParam);


}
