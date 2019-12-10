package com.poke.pokeMessage.controller;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.domain.mongo.NiuniuRoomParam;
import com.poke.pokeMessage.service.CreateRoomService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class CreateRoomController {

    @Resource
    private CreateRoomService createRoomService;

    @RequestMapping(value = "/api/message/create/room/userId/{userId}", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Integer> createRoom(@PathVariable("userId") Integer userId, @RequestBody NiuniuRoomParam niuniuRoomParam) {
        return createRoomService.createRoom(niuniuRoomParam, userId);
    }
}
