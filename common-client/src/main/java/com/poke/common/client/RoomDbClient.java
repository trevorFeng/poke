package com.poke.common.client;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.Room;
import com.poke.common.bean.enums.MessageCodeEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("db-service")
public interface RoomDbClient {

    @RequestMapping(value = "/api/room/query/{id}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Room> findByRoomId(@PathVariable Integer id);

    @RequestMapping(value = "/api/room/save" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonEntity<Integer> save(@RequestBody Room room);

    JsonEntity<Object> updateStatus();

}
