package com.poke.dbservice.controller;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.Room;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.dbservice.service.RoomService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class RoomController {

    @Resource
    private RoomService roomService;

    @RequestMapping(value = "/api/room/query/{id}" ,method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Room> findByRoomId(@PathVariable Integer id){
        return ResponseHelper.createInstance(roomService.findByRoomId(id) , MessageCodeEnum.HANDLER_SUCCESS);
    }


    @RequestMapping(value = "/api/room/save" ,method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Integer> save(@RequestBody Room room) {
        return ResponseHelper.createInstance(roomService.save(room) , MessageCodeEnum.HANDLER_SUCCESS);
    }

}
