package com.poke.pokeMessage.core.event.niuniu;

import com.poke.common.bean.bo.SocketResult;
import com.poke.common.bean.enums.GameStatusEnum;
import com.poke.pokeMessage.bo.NiuniuData;
import com.poke.pokeMessage.bo.RoomData;
import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.core.event.BaseEvent;
import com.poke.pokeMessage.core.event.Event;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

@Service
public class XiaZhuEvent extends BaseEvent implements Event {

    @Override
    public void execute(RoomData roomData, Task task) {
        NiuniuData data = (NiuniuData) roomData;
        String gameStatus = data.getGameStatus();
        Integer playerId = task.getPlayId();
        Integer roomId = task.getRoomId();
        Integer runingNum = data.getRuningNum();
        Set<Integer> readyPlayers = data.getReadyPlayMap().get(runingNum);
        Integer zhuangJiaId = data.getZhuangJiaMap().get(runingNum);
        Set<Integer> players = data.getPlayers();
        //校验状态
        if (!Objects.equals(gameStatus, GameStatusEnum.XIA_ZHU_COUNT_DOWN_START.getCode())) {
            socketService.sendToUserMessage(playerId, new SocketResult(-501), roomId);
            return;
        }
        //校验是否是准备的玩家
        if (!readyPlayers.contains(playerId)) {
            socketService.sendToUserMessage(playerId, new SocketResult(-504), roomId);
            return;
        }
        //该玩家是否是闲家
        if (Objects.equals(zhuangJiaId, playerId)) {
            socketService.sendToUserMessage(playerId, new SocketResult(-505), roomId);
            return;
        }
        data.getXiaZhuMap().putIfAbsent(runingNum, new HashMap<>());
        data.getXiaZhuMap().get(runingNum).put(playerId, task.getXiaZhuBeiShu());
        //广播下注的消息
        socketService.broadcast(roomId,
                new SocketResult(1011, playerId, task.getXiaZhuBeiShu(), Boolean.TRUE)
                , players);

        Integer readyPlayerSize = readyPlayers.size();
        Integer xiaZhuSize = data.getXiaZhuMap().get(runingNum).size();
        if (Objects.equals(readyPlayerSize - 1, xiaZhuSize)) {
            //删除下注倒计时监听器
            scheduleDispatch.removeCountDown(roomId);
            //添加发一张牌事件
            taskQueue.addTask(roomId, Task.getNiuniuFaPai1(roomId));
        }
    }
}
