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
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class QiangZhuangEvent extends BaseEvent implements Event {

    @Override
    public void execute(RoomData roomData, Task task) {
        NiuniuData data = (NiuniuData) roomData;
        //当前局数
        Integer runingNum = data.getRuningNum();
        //当前的房间状态
        String gameStatus = data.getGameStatus();
        Integer roomId = data.getRoomId();
        Integer playerId = task.getPlayId();
        Set<Integer> players = data.getPlayers();
        //验证状态
        if (!Objects.equals(gameStatus, GameStatusEnum.QIANG_ZHUANG_COUNT_DOWN_START.getCode())) {
            socketService.sendToUserMessage(playerId, new SocketResult(-501), roomId);
            return;
        }
        //校验是否是准备的玩家
        Set<Integer> readyPlayers = data.getReadyPlayMap().get(runingNum);
        if (!readyPlayers.contains(playerId)) {
            socketService.sendToUserMessage(playerId, new SocketResult(-503), roomId);
            return;
        }

        //放入data
        Integer multiple = task.getQiangZhuangBeiShu() <= 0 ? 0 : task.getQiangZhuangBeiShu();
        data.getQiangZhuangMap().putIfAbsent(runingNum, new HashMap<>());
        Map<Integer, Integer> qiangZhuangMap = data.getQiangZhuangMap().get(runingNum);
        qiangZhuangMap.putIfAbsent(playerId, multiple);


        //广播抢庄的消息
        socketService.broadcast(roomId, new SocketResult(1010, playerId, multiple), players);

        Integer readyPlayerSize = readyPlayers.size();
        Integer qiangZhuangSize = data.getQiangZhuangMap().get(runingNum).size();

        //全部已经抢庄
        if (Objects.equals(readyPlayerSize, qiangZhuangSize)) {
            //删除抢庄倒计时监听器
            scheduleDispatch.removeCountDown(roomId);
            //添加选择庄家事件
            Task selectZhuangJia = Task.getNiuniuSelectZhuangJia(roomId);
            taskQueue.addTask(roomId, selectZhuangJia);
        }
    }

}
