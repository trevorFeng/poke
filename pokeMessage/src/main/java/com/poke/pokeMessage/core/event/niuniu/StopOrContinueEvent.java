package com.poke.pokeMessage.core.event.niuniu;

import com.google.common.collect.Lists;
import com.poke.common.bean.bo.PaiXing;
import com.poke.common.bean.bo.SocketResult;
import com.poke.common.bean.domain.mongo.PlayerResult;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.enums.GameStatusEnum;
import com.poke.pokeMessage.bo.NiuniuData;
import com.poke.pokeMessage.bo.RoomData;
import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.core.event.BaseEvent;
import com.poke.pokeMessage.core.event.Event;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StopOrContinueEvent extends BaseEvent implements Event {

    @Override
    public void execute(RoomData roomData, Task task) {
        NiuniuData data = (NiuniuData) roomData;
        Integer roomId = data.getRoomId();
        Set<Integer> players = data.getPlayers();

        data.setGameStatus(GameStatusEnum.JIE_SUAN.getCode());
        SocketResult soc = new SocketResult(1012);
        soc.setGameStatus(GameStatusEnum.JIE_SUAN.getCode());

        socketService.broadcast(roomId, soc, players);

        //保存结果
        List<PlayerResult> playerResults = generatePlayerResults(roomId, data);
        playerResultDbClient.saveList(playerResults);

        Integer runingNum = Integer.valueOf(data.getRuningNum());
        Integer totalNum = Integer.valueOf(data.getTotalNum());
        Boolean isOver = Objects.equals(runingNum, totalNum);

        //结束
        if (isOver) {
            //roomDbClient.updateStatus(Long.valueOf(roomId), 2, runingNum);
            SocketResult socketResult = new SocketResult(1013);
            socketResult.setGameStatus(GameStatusEnum.STOP.getCode());
            socketResult.setTanPaiPlayerUserIds(data.getTanPaiMap().get(runingNum));

            socketService.broadcast(roomId, socketResult, data.getPlayers());
            socketService.stopRoom(players ,roomId);
        } else {
            Integer next = runingNum + 1;

            //roomDbClient.updateRuningNum(Long.valueOf(roomId), runingNum);

            data.setGameStatus(GameStatusEnum.READY.getCode());
            data.setRuningNum(next);

            SocketResult socketResult = new SocketResult();
            socketResult.setHead(1016);
            socketResult.setTanPaiPlayerUserIds(data.getTanPaiMap().get(runingNum));
            socketResult.setRuningAndTotal(next + "/" + totalNum);
            socketService.broadcast(roomId, socketResult, data.getPlayers());
        }
    }

    private List<PlayerResult> generatePlayerResults(Integer roomId, NiuniuData data) {
        Long entryDatetime = System.currentTimeMillis();
        Integer runingNum = data.getRuningNum();
        Map<Integer, Integer> scoreMap = data.getRuningScoreMap().get(runingNum);
        Set<Integer> readyPlayerSet = data.getReadyPlayMap().get(runingNum);
        List<Integer> readyPlayerList = Lists.newArrayList();
        for (Integer playerId : readyPlayerSet) {
            readyPlayerList.add(playerId);
        }
        List<User> users = userDbClient.findByUserIdList(readyPlayerList).getData();
        Integer zhuangJiaId = data.getZhuangJiaMap().get(runingNum);
        Map<Integer, Integer> totalScoreMap = data.getTotalScoreMap();
        Map<Integer, List<String>> pokesMap = data.getPokesMap().get(runingNum);
        Map<Integer, PaiXing> paiXingMap = data.getPaiXingMap().get(runingNum);
        List<PlayerResult> playerResults = new ArrayList<>();
        for (User user : users) {
            PlayerResult playerResult = new PlayerResult();
            Integer userId = user.getId();
            String userIdStr = String.valueOf(user.getId());
            //玩家id
            playerResult.setUserId(userId);
            //房间id
            playerResult.setRoomId(Long.valueOf(roomId));
            //第几局
            playerResult.setGameNum(Integer.valueOf(runingNum));
            //本局得分情况
            playerResult.setScore(scoreMap.get(userIdStr));
            //是否是庄家
            if (Objects.equals(zhuangJiaId, userIdStr)) {
                playerResult.setIsZhuangJia(Boolean.TRUE);
            } else {
                playerResult.setIsZhuangJia(Boolean.FALSE);
            }
            //设置总分
            playerResult.setTotalScore(totalScoreMap.get(userIdStr));
            //设置牌
            playerResult.setPokes(pokesMap.get(userIdStr));
            //设置牌型
            PaiXing paiXing = paiXingMap.get(userIdStr);
            playerResult.setPaiXing(paiXing.getPaixing());
            //设置倍数
            playerResult.setPaiXing(paiXing.getMultiple());
            //设置时间
            playerResult.setEntryTime(entryDatetime);
            playerResults.add(playerResult);
        }
        return playerResults;
    }

}
