package com.poke.pokeMessage.core.event.niuniu;

import com.poke.common.bean.bo.SocketResult;
import com.poke.pokeMessage.bo.NiuniuData;
import com.poke.pokeMessage.bo.RoomData;
import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.core.event.BaseEvent;
import com.poke.pokeMessage.core.event.Event;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DisConnectionEvent extends BaseEvent implements Event {

    @Override
    public void execute(RoomData roomData, Task task) {
        NiuniuData data = (NiuniuData) roomData;
        Set<String> realPlayers = data.getRealPlayers();
        String playerId = task.getPlayId();
        Set<String> players = data.getPlayers();
        //如果是真正的玩家则广播消息断开连接的消息
        if (realPlayers.contains(playerId)) {
            data.getDisConnections().add(playerId);
            SocketResult res = new SocketResult(1001, playerId);
            socketService.broadcast(task.getRoomId(), res, players);
        }
    }
}
