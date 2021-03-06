package com.poke.pokeMessage.core.event.niuniu;

import com.google.common.collect.Maps;
import com.poke.common.bean.bo.PaiXing;
import com.poke.common.bean.bo.SocketResult;
import com.poke.common.bean.enums.GameStatusEnum;
import com.poke.common.bean.enums.NiuNiuPaiXingEnum;
import com.poke.pokeMessage.bo.*;
import com.poke.pokeMessage.core.event.BaseEvent;
import com.poke.pokeMessage.core.event.Event;
import com.poke.pokeMessage.core.schedule.CountDownImpl;
import com.poke.pokeMessage.util.PokeUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 发一张牌
 */
@Service
public class FaPai1Event extends BaseEvent implements Event {


    @Override
    public void execute(RoomData roomData, Task task) {
        NiuniuData data = (NiuniuData) roomData;
        Integer roomId = data.getRoomId();
        Integer runingNum = data.getRuningNum();
        //计算得分,将用户的牌型放入paiXingMap,得分和总分放入scoreMap
        calcScore(data, runingNum);
        //改变状态
        data.setGameStatus(GameStatusEnum.FA_ONE_PAI.getCode());

        Map<Integer, List<String>> userPokeMap_5 = new HashMap<>(2 << 4);
        Map<Integer, List<String>> map = data.getPokesMap().get(runingNum);
        for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
            userPokeMap_5.put(entry.getKey(), entry.getValue());
        }
        SocketResult socketResult = new SocketResult(1008, userPokeMap_5);
        socketResult.setScoreMap(data.getRuningScoreMap().get(runingNum));


        Map<Integer, Integer> playerPaiXingMap = new HashMap<>();
        for (Map.Entry<Integer, PaiXing> entry : data.getPaiXingMap().get(runingNum).entrySet()) {
            playerPaiXingMap.put(entry.getKey(), entry.getValue().getPaixing());
        }
        socketResult.setPaiXing(playerPaiXingMap);
        socketResult.setGameStatus(GameStatusEnum.FA_ONE_PAI.getCode());

        socketService.broadcast(roomId, socketResult, data.getPlayers());
        //注册摊牌倒计时事件
        scheduleDispatch.addCountDown(new CountDownImpl(roomId, CountDownNum.TWENTY, CountDownFlag.TAN_PAI));
    }

    private void calcScore(NiuniuData data, Integer runingNum) {
        Set<Integer> paiXing = data.getPaiXing();
        Integer rule = data.getRule();
        Integer basePoint = data.getBasePoint();
        //庄家id
        Integer zhuangJiaUserId = data.getZhuangJiaMap().get(runingNum);
        //抢庄的map
        Map<Integer, Integer> qiangZhuangMap = data.getQiangZhuangMap().get(runingNum);
        //下注的map
        Map<Integer, Integer> xianJiaXiaZhuMap = data.getXiaZhuMap().get(runingNum);
        //每个玩家的牌
        Map<Integer, List<String>> pokesMap = data.getPokesMap().get(runingNum);
        //庄家的牌
        List<String> zhuangJiaPokes = pokesMap.get(zhuangJiaUserId);
        //庄家的牌型
        PaiXing zhuangJiaPaiXing = PokeUtil.isNiuNiu(zhuangJiaPokes, paiXing, rule);
        Integer zhuangJiaScore = 0;
        //初始化本局得分
        data.getRuningScoreMap().putIfAbsent(runingNum, new HashMap<>());
        Map<Integer, Integer> scoreMap = data.getRuningScoreMap().get(runingNum);

        data.getPaiXingMap().put(runingNum, new HashMap<>());
        Map<Integer, PaiXing> paiXingMap = data.getPaiXingMap().get(runingNum);
        paiXingMap.put(zhuangJiaUserId, zhuangJiaPaiXing);

        Map<Integer, Integer> totalScoreMap = data.getTotalScoreMap();
        //庄家的抢庄倍数
        Integer zhuangJiaQiangZhuang = qiangZhuangMap.get(zhuangJiaUserId) == null ? 1 : qiangZhuangMap.get(zhuangJiaUserId);
        for (Map.Entry<Integer, List<String>> entry : pokesMap.entrySet()) {
            Integer xianJiaUserId = entry.getKey();
            if (!Objects.equals(xianJiaUserId, zhuangJiaUserId)) {
                List<String> xianJiaPokes = entry.getValue();
                PaiXing xianJiaPaiXing = PokeUtil.isNiuNiu(xianJiaPokes, paiXing, rule);

                paiXingMap.put(xianJiaUserId, xianJiaPaiXing);
                //玩家的下注倍数
                Integer xianJiaQiangZhu = xianJiaXiaZhuMap.get(xianJiaUserId) == null ? 1 : xianJiaXiaZhuMap.get(xianJiaUserId);
                //基本分数
                Integer score = zhuangJiaQiangZhuang * xianJiaQiangZhu * basePoint;
                //闲家的总分
                Integer xianJiaTotalScore = totalScoreMap.get(xianJiaUserId) == null ? 0 : totalScoreMap.get(xianJiaUserId);
                //庄家大于闲家
                if (zhuangJiaPaiXing.getPaixing() > xianJiaPaiXing.getPaixing()) {
                    score = score * zhuangJiaPaiXing.getMultiple();
                    zhuangJiaScore += score;

                    scoreMap.put(xianJiaUserId, -score);
                    totalScoreMap.put(xianJiaUserId, xianJiaTotalScore - score);
                    //庄家小于闲家
                } else if (zhuangJiaPaiXing.getPaixing() < xianJiaPaiXing.getPaixing()) {
                    score = score * xianJiaPaiXing.getMultiple();
                    zhuangJiaScore -= score;

                    scoreMap.put(xianJiaUserId, score);
                    totalScoreMap.put(xianJiaUserId, xianJiaTotalScore + score);
                } else {
                    boolean zhuangJiaDa = true;
                    //炸弹牛，比炸弹大小(已经设置不可能出现两个五小牛)
                    if (Objects.equals(zhuangJiaPaiXing.getMultiple(), NiuNiuPaiXingEnum.NIU_15.getPaiXingCode())) {
                        if (!PokeUtil.niu_15_daXiao(zhuangJiaPokes, xianJiaPokes)) {
                            zhuangJiaDa = false;
                        }
                        //葫芦牛，比3张牌一样的大小
                    } else if (Objects.equals(zhuangJiaPaiXing.getPaixing(), NiuNiuPaiXingEnum.NIU_14.getPaiXingCode())) {
                        if (!PokeUtil.niu_14_daXiao(zhuangJiaPokes, xianJiaPokes)) {
                            zhuangJiaDa = false;
                        }
                        //同花牛，先比花色大小，再比牌值大小
                    } else if (Objects.equals(zhuangJiaPaiXing.getPaixing(), NiuNiuPaiXingEnum.NIU_13.getPaiXingCode())) {
                        if (!PokeUtil.niu_13_daXiao(zhuangJiaPokes, xianJiaPokes)) {
                            zhuangJiaDa = false;
                        }
                        //五花牛，比最大牌，再比花色 //顺子牛，比最大牌，再比花色//比最大牌，最后比花色
                    } else {
                        //倒叙排，比大小
                        Integer paiZhi = PokeUtil.biPaiZhi(zhuangJiaPokes, xianJiaPokes);
                        if (Objects.equals(paiZhi, 1)) {
                            zhuangJiaDa = true;
                        } else if (Objects.equals(-1, paiZhi)) {
                            zhuangJiaDa = false;
                        } else {
                            List<Integer> zhuangJiaNums = zhuangJiaPokes.stream().map(str -> PokeUtil.changePai(str.substring(1, 2))).collect(Collectors.toList());
                            Map<String, String> zhuangJiaMap = Maps.newHashMap();
                            for (String zhuang : zhuangJiaPokes) {
                                zhuangJiaMap.put(zhuang.substring(1, 2), zhuang.substring(0, 1));
                            }
                            List<Integer> xianJiaNums = xianJiaPokes.stream().map(str -> PokeUtil.changePai(str.substring(1, 2))).collect(Collectors.toList());
                            Map<String, String> xianJiaMap = Maps.newHashMap();
                            for (String xian : xianJiaPokes) {
                                xianJiaMap.put(xian.substring(1, 2), xian.substring(0, 1));
                            }
                            zhuangJiaNums.sort(Comparator.reverseOrder());
                            xianJiaNums.sort(Comparator.reverseOrder());
                            if (Integer.valueOf(zhuangJiaMap.get(zhuangJiaNums.get(0))) > Integer.valueOf(xianJiaMap.get(xianJiaNums.get(0)))) {
                                zhuangJiaDa = true;
                            } else {
                                zhuangJiaDa = false;
                            }
                        }
                    }
                    if (zhuangJiaDa) {
                        score = score * zhuangJiaPaiXing.getMultiple();
                        zhuangJiaScore += score;

                        scoreMap.put(xianJiaUserId, -score);
                        totalScoreMap.put(xianJiaUserId, xianJiaTotalScore - score);
                    } else {
                        score = score * xianJiaPaiXing.getMultiple();
                        zhuangJiaScore -= score;

                        scoreMap.put(xianJiaUserId, score);
                        totalScoreMap.put(xianJiaUserId, xianJiaTotalScore + score);
                    }
                }

            }
        }
        //设置庄家的分数
        Integer zhuangJiaTotalScore = totalScoreMap.get(zhuangJiaUserId) == null ? 0 : totalScoreMap.get(zhuangJiaUserId);
        scoreMap.put(zhuangJiaUserId, zhuangJiaScore);
        totalScoreMap.put(zhuangJiaUserId, zhuangJiaTotalScore + zhuangJiaScore);
    }
}
