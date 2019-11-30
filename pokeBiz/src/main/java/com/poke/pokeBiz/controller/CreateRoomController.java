package com.poke.pokeBiz.controller;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mongo.NiuniuRoomParam;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.pokeBiz.service.CreateRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author trevor
 * @date 2019/3/8 16:51
 */
@Api(value = "创建房间" ,description = "创建房间接口")
@RestController
@Validated
public class CreateRoomController {

    @Resource
    private CreateRoomService createRoomService;

    /**
     * 创建一个房间
     * @param niuniuRoomParam 房间参数
     * @return 房间的唯一id
     */
    @ApiOperation("创建一个房间")
    @RequestMapping(value = "/api/room/create", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Integer> createRoom(@RequestBody NiuniuRoomParam niuniuRoomParam){
        return ResponseHelper.createInstance(createRoomService.createRoom(niuniuRoomParam) , MessageCodeEnum.HANDLER_SUCCESS);
    }
}
