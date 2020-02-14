package com.poke.pokeMessage.core.event.niuniu;

import com.google.common.collect.Maps;
import com.poke.common.bean.bo.SocketResult;
import com.poke.common.bean.enums.GameStatusEnum;
import com.poke.pokeMessage.bo.NiuniuData;
import com.poke.pokeMessage.bo.RoomData;
import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.core.event.BaseEvent;
import com.poke.pokeMessage.core.event.Event;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 默认下注事件
 */
@Service
public class DefaultXiaZhuEvent extends BaseEvent implements Event {


    @Override
    public void execute(RoomData roomData, Task task) {
        NiuniuData data = (NiuniuData) roomData;
        Integer rungingNum = data.getRuningNum();
        Integer roomId = data.getRoomId();
        Set<Integer> players = data.getPlayers();
        Set<Integer> readyPlayers = data.getReadyPlayMap().get(rungingNum);
        //已经下注的玩家
        data.getXiaZhuMap().putIfAbsent(rungingNum ,new HashMap<>());
        Collection<Integer> xiaZhuPlayers = data.getXiaZhuMap().get(rungingNum).values();
        Integer zhuangJiaPlayerId = data.getZhuangJiaMap().get(rungingNum);
        Map<Integer, Integer> map = Maps.newHashMap();
        for (Integer s : readyPlayers) {
            if (!Objects.equals(s, zhuangJiaPlayerId) && !xiaZhuPlayers.contains(s)) {
                map.put(s, 1);
            }
        }
        if (!map.isEmpty()) {
            data.setGameStatus(GameStatusEnum.DEFAULT_XIA_ZHU.getCode());
            SocketResult soc = new SocketResult();
            soc.setHead(1020);
            soc.setXianJiaXiaZhuMap(map);
            soc.setGameStatus(GameStatusEnum.DEFAULT_XIA_ZHU.getCode());
            socketService.broadcast(roomId, soc, players);
            taskQueue.addTask(roomId, Task.getNiuniuFaPai1(roomId));
        }
    }
}
