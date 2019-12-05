package com.poke.pokeMessage.core.event;

import com.poke.pokeMessage.core.GameCore;
import com.poke.pokeMessage.core.TaskQueue;
import com.poke.pokeMessage.core.schedule.ScheduleDispatch;
import com.poke.pokeMessage.service.SocketService;
import com.trevor.common.dao.mongo.PlayerResultMapper;
import com.trevor.common.dao.mysql.RoomMapper;
import com.trevor.common.service.RedisService;
import com.trevor.common.service.UserService;
import com.trevor.message.core.TaskQueue;
import com.trevor.message.core.schedule.ScheduleDispatch;
import com.trevor.message.service.SocketService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BaseEvent {

    @Resource
    protected GameCore gameCore;

    @Resource
    protected SocketService socketService;

    @Resource
    protected TaskQueue taskQueue;

    @Resource
    protected ScheduleDispatch scheduleDispatch;

    @Resource
    protected PlayerResultMapper playerResultMapper;

    @Resource
    protected RoomMapper roomMapper;

    @Resource
    protected UserService userService;

//    @Resource
//    protected UserFeignResult userFeignResult;

//    @Resource
//    protected PlayerResultFeignResult playerResultFeignResult;
//
//    @Resource
//    protected RoomFeignResult roomFeignResult;

}
