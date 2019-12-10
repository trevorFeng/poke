package com.poke.pokeMessage.socket;

import com.poke.common.client.FriendsManageDbClient;
import com.poke.common.client.RoomDbClient;
import com.poke.common.client.UserDbClient;
import com.poke.pokeMessage.core.GameCore;
import com.poke.pokeMessage.core.TaskQueue;
import com.poke.pokeMessage.socket.service.NiuniuService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author trevor
 * @date 06/27/19 18:05
 */
@Component
public class BaseServer {

    protected static GameCore gameCore;

    protected static UserDbClient userDbClient;

    protected static TaskQueue taskQueue;

    protected static NiuniuService niuniuService;

    protected static RoomDbClient roomDbClient;

    protected static FriendsManageDbClient friendsManageDbClient;

    @Resource
    public void setUserService(GameCore gameCore) {
        BaseServer.gameCore = gameCore;
    }

    @Resource
    public void setUserService(UserDbClient userDbClient) {
        BaseServer.userDbClient = userDbClient;
    }

    @Resource
    public void setTaskQueue(TaskQueue taskQueue) {
        BaseServer.taskQueue = taskQueue;
    }

    @Resource
    public void setNiuniuService(NiuniuService niuniuService) {
        BaseServer.niuniuService = niuniuService;
    }

    @Resource
    public void setRoomService(RoomDbClient roomDbClient) {
        BaseServer.roomDbClient = roomDbClient;
    }

    @Resource
    public void setFriendManageMapper(FriendsManageDbClient friendsManageDbClient) {
        BaseServer.friendsManageDbClient = friendsManageDbClient;
    }

}
