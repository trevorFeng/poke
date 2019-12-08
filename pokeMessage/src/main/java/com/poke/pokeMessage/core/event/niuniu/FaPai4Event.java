package com.poke.pokeMessage.core.event.niuniu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.poke.common.bean.bo.PaiXing;
import com.poke.common.bean.enums.GameStatusEnum;
import com.poke.common.util.PokeUtil;
import com.poke.common.util.RandomUtils;
import com.poke.pokeMessage.bo.CountDownNum;
import com.poke.pokeMessage.bo.NiuniuData;
import com.poke.pokeMessage.bo.RoomData;
import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.core.event.BaseEvent;
import com.poke.pokeMessage.core.event.Event;
import com.poke.pokeMessage.core.schedule.CountDownImpl;
import com.trevor.common.bo.PaiXing;
import com.trevor.common.bo.SocketResult;
import com.trevor.common.enums.GameStatusEnum;
import com.trevor.common.util.PokeUtil;
import com.trevor.common.util.RandomUtils;
import com.trevor.message.bo.*;
import com.trevor.message.core.event.BaseEvent;
import com.trevor.message.core.event.Event;
import com.trevor.message.core.schedule.CountDownImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FaPai4Event extends BaseEvent implements Event {

    @Override
    public void execute(RoomData roomData, Task task) {
        NiuniuData data = (NiuniuData) roomData;
        String runingNum = data.getRuningNum();
        Integer roomId = data.getRoomId();
        Map<String, List<String>> userAlreadyPokesMap = data.getPokesMap().get(runingNum);
        //判断下这个任务是否已经执行过
        if (userAlreadyPokesMap != null && !userAlreadyPokesMap.isEmpty()) {
            return;
        }
        //生成牌
        Set<Integer> readyPlayerUserIds = data.getReadyPlayMap().get(runingNum);
        Set<Integer> paiXing = data.getPaiXing();
        List<List<String>> pokesList = getPokesList(paiXing, readyPlayerUserIds.size());
        //设置每个人的牌
        data.getPokesMap().put(runingNum, setPlayersPoke(pokesList, readyPlayerUserIds));
        //改变房间状态
        data.setGameStatus(GameStatusEnum.FA_FOUR_PAI.getCode());
        //发牌并发送房间状态
        Set<Integer> players = data.getPlayers();
        faPai(data.getPokesMap().get(runingNum), readyPlayerUserIds, players, roomId);
        //抢庄倒计时添加到任务队列
        scheduleDispatch.addCountDown(new CountDownImpl(data.getRoomId(), CountDownNum.TWENTY, CountDownFlag.NIUNIU_QIANG_ZHUANG));
    }

    private void faPai(Map<String, List<String>> pokesMap, Set<Integer> readyPlayerUserIds, Set<Integer> players, Integer roomId) {
        //给每个人发牌
        for (String playerId : players) {
            if (readyPlayerUserIds.contains(playerId)) {
                List<String> userPokeList_4 = pokesMap.get(playerId).subList(0, 4);
                SocketResult soc = new SocketResult(1004, userPokeList_4);
                soc.setGameStatus(GameStatusEnum.FA_FOUR_PAI.getCode());
                socketService.sendToUserMessage(playerId, soc, roomId);
            } else {
                SocketResult soc = new SocketResult(1004);
                soc.setGameStatus(GameStatusEnum.FA_FOUR_PAI.getCode());
                socketService.sendToUserMessage(playerId, soc, roomId);
            }
        }
    }

    private Map<Integer, List<String>> setPlayersPoke(List<List<String>> pokesList, Set<Integer> readyPlayerUserIds) {
        int num = 0;
        Map<Integer, List<String>> pokeMap = Maps.newHashMap();
        for (Integer playerId : readyPlayerUserIds) {
            List<String> pokes = pokesList.get(num);
            pokeMap.put(playerId, pokes);
            num++;
        }
        return pokeMap;
    }

    private List<List<String>> getPokesList(Set<Integer> paiXing, Integer readyPlayerUserSize) {
        List<String> rootPokes = PokeUtil.generatePoke5();
        //生成牌在rootPokes的索引
        List<List<Integer>> lists;
        //生成牌
        List<List<String>> pokesList = Lists.newArrayList();
        //判断每个集合是否有两个五小牛，有的话重新生成
        Boolean twoWuXiaoNiu = true;
        while (twoWuXiaoNiu) {
            lists = RandomUtils.getSplitListByMax(rootPokes.size(), readyPlayerUserSize * 5);
            //生成牌
            pokesList = Lists.newArrayList();
            for (List<Integer> integers : lists) {
                List<String> stringList = Lists.newArrayList();
                integers.forEach(index -> {
                    stringList.add(rootPokes.get(index));
                });
                pokesList.add(stringList);
            }
            int niu_16_nums = 0;
            for (List<String> pokes : pokesList) {
                PaiXing niu_16 = PokeUtil.isNiu_16(pokes, paiXing);
                if (niu_16 != null) {
                    niu_16_nums++;
                }
            }
            if (niu_16_nums < 2) {
                twoWuXiaoNiu = false;
            }
        }

        return pokesList;
    }
}
