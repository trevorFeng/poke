package com.poke.pokeMessage.core.event.niuniu;

import com.poke.common.bean.bo.SocketResult;
import com.poke.common.bean.enums.GameStatusEnum;
import com.poke.pokeMessage.bo.NiuniuData;
import com.poke.pokeMessage.bo.RoomData;
import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.core.event.BaseEvent;
import com.poke.pokeMessage.core.event.Event;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class TanPaiEvent extends BaseEvent implements Event {

    @Override
    public void execute(RoomData roomData, Task task) {
        NiuniuData data = (NiuniuData) roomData;
        String gameStatus = data.getGameStatus();
        Integer playerId = task.getPlayId();
        Integer roomId = task.getRoomId();
        //状态信息
        if (!Objects.equals(gameStatus, GameStatusEnum.TAN_PAI_COUNT_DOWN_START.getCode())) {
            socketService.sendToUserMessage(playerId, new SocketResult(-501), roomId);
            return;
        }
        Integer runingNum = data.getRuningNum();
        Set<Integer> readyPlayers = data.getReadyPlayMap().get(runingNum);
        if (!readyPlayers.contains(playerId)) {
            socketService.sendToUserMessage(playerId, new SocketResult(-503), roomId);
            return;
        }
        data.getTanPaiMap().putIfAbsent(runingNum, new HashSet<>());
        data.getTanPaiMap().get(runingNum).add(playerId);

        //广播摊牌的消息
        Set<Integer> players = data.getPlayers();
        SocketResult socketResult = new SocketResult();
        socketResult.setHead(1014);
        socketResult.setUserId(playerId);
        socketService.broadcast(roomId, socketResult, players);

        Integer readyPlayerSize = readyPlayers.size();
        Integer tanPaiSize = data.getTanPaiMap().get(runingNum).size();

        if (Objects.equals(readyPlayerSize, tanPaiSize)) {
            //删除摊牌倒计时监听器
            scheduleDispatch.removeCountDown(roomId);
            //添加继续或者停止事件
            taskQueue.addTask(roomId, Task.getStopOrContinue(roomId));
        }
    }
}
