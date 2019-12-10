package com.poke.pokeMessage.core.event;

import com.poke.common.client.PlayerResultDbClient;
import com.poke.common.client.RoomDbClient;
import com.poke.common.client.UserDbClient;
import com.poke.pokeMessage.core.GameCore;
import com.poke.pokeMessage.core.TaskQueue;
import com.poke.pokeMessage.core.schedule.ScheduleDispatch;
import com.poke.pokeMessage.service.SocketService;
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
    protected PlayerResultDbClient playerResultDbClient;

    @Resource
    protected RoomDbClient roomDbClient;

    @Resource
    protected UserDbClient userDbClient;

}
