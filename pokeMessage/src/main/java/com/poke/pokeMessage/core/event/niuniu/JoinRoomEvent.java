package com.poke.pokeMessage.core.event.niuniu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.poke.common.bean.bo.PaiXing;
import com.poke.common.bean.bo.Player;
import com.poke.common.bean.bo.SocketResult;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.enums.GameStatusEnum;
import com.poke.common.bean.enums.RoomTypeEnum;
import com.poke.common.bean.enums.SpecialEnum;
import com.poke.pokeMessage.bo.NiuniuData;
import com.poke.pokeMessage.bo.RoomData;
import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.core.event.BaseEvent;
import com.poke.pokeMessage.core.event.Event;
import com.poke.pokeMessage.socket.socketImpl.NiuniuSocket;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class JoinRoomEvent extends BaseEvent implements Event {

    @Override
    public void execute(RoomData roomData, Task task) {
        NiuniuData data = (NiuniuData) roomData;
        NiuniuSocket socket = (NiuniuSocket) task.getSocket();
        //当前局数
        Integer runingNum = data.getRuningNum();
        //房主是否开启好友管理功能
        Boolean isFriendManage = task.getIsFriendManage();
        //加入的玩家是否是房主的好友
        Boolean roomAuthFriendAllow = task.getRoomAuthFriendAllow();
        Set<Integer> special = data.getSpecial();

        User joinUser = task.getJoinUser();
        Integer realPlayersSize = data.getRealPlayers().size();
        Integer roomType = data.getRoomType();

        //房间规则检查
        SocketResult soc = checkRoom(isFriendManage, special, roomAuthFriendAllow
                , joinUser, realPlayersSize, roomType);
        if (soc.getHead() != null) {
            socket.directSendMessage(soc, socket.session);
            socket.close(socket.session);
            return;
        }

        Integer playerId = task.getPlayId();
        Integer roomId = task.getRoomId();
        Set<Integer> players = data.getPlayers();
        Map<Integer, Integer> totalScoreMap = data.getTotalScoreMap();

        //加入定时刷消息的socketMap
        socketService.join(socket);
        //删除自己的消息队列
        gameCore.removeMessageQueue(playerId);

        //不是吃瓜群众则加入到真正的玩家集合中并且删除自己的掉线状态
        Set<Integer> disConnections = data.getDisConnections();
        if (!soc.getIsChiGuaPeople()) {
            //加入到真正的玩家（打牌的人）中
            data.getRealPlayers().add(playerId);
            //删除自己掉线状态
            disConnections.remove(playerId);
            //设置新人的总分
            setNewPlayerTotalScore(soc ,totalScoreMap
                    ,data.getRuningScoreMap().get(runingNum) ,data.getGameStatus() ,playerId);
            //soc.setTotalScore(totalScoreMap.get(playerId) == null ? 0 : totalScoreMap.get(playerId));
            //给别的玩家发1000的新人加入消息
            sendToOtherPlayers(soc ,roomId ,players);
            //给自己发2002的消息
            sendToNewPlayer(soc ,data ,disConnections ,totalScoreMap ,task);
            //加入玩家（吃瓜群众和玩家）
            data.getPlayers().add(playerId);
        //是吃瓜群众只给新人发
        }else {
            sendToNewPlayer(soc ,data ,disConnections ,totalScoreMap ,task);
        }
    }

    private void setNewPlayerTotalScore(SocketResult soc ,Map<Integer, Integer> totalScoreMap ,
                                        Map<Integer ,Integer> runingScoreMap ,String gameStatus ,Integer playerId){
        if (runingScoreMap == null) {
            runingScoreMap = new HashMap<>();
        }
        Integer totalScore = totalScoreMap.get(playerId) == null ? 0 : totalScoreMap.get(playerId);
        if (Objects.equals(gameStatus, GameStatusEnum.TAN_PAI_COUNT_DOWN_START.getCode()) ||
                Objects.equals(gameStatus, GameStatusEnum.FA_ONE_PAI.getCode())) {
            Integer runingScore = runingScoreMap.get(playerId) == null ? 0 : runingScoreMap.get(playerId);
            soc.setTotalScore(totalScore - runingScore);
        }else {
            soc.setTotalScore(totalScore);
        }
    }

    /**
     * 给别人发消息
     * @param soc
     * @param roomId
     * @param players
     */
    private void sendToOtherPlayers(SocketResult soc ,Integer roomId ,Set<Integer> players){
        //给别的玩家发1000的新人加入消息
        soc.setHead(1000);
        socketService.broadcast(roomId, soc, players);
    }

    /**
     * 给新加入的玩家发消息
     * @param soc
     * @param data
     * @param disConnections
     * @param totalScoreMap
     * @param task
     */
    private void sendToNewPlayer(SocketResult soc ,NiuniuData data ,Set<Integer> disConnections ,
                              Map<Integer, Integer> totalScoreMap ,Task task){
        soc.setHead(2002);
        //设置房间正在运行的局数
        soc.setRuningAndTotal(Integer.valueOf(data.getRuningNum()) + "/" + Integer.valueOf(data.getTotalNum()));
        //设置掉线的玩家
        soc.setDisConnectionPlayerIds(disConnections);
        //设置真正的玩家
        soc.setPlayers(getRealRoomPlayerCount(data.getRealPlayers(), data.getGuanZhongs(),
                totalScoreMap ,data.getRuningScoreMap().get(data.getRuningNum()) ,data.getGameStatus()));
        //设置房间状态信息
        welcome(data, task, soc);
        socketService.sendToUserMessage(task.getPlayId(), soc, task.getRoomId());
    }

    /**
     * 检查房间
     * @param isFriendManage
     * @param special
     * @param roomAuthFriendAllow
     * @param joinUser
     * @param realPlayersSize
     * @param roomType
     * @return
     */
    private SocketResult checkRoom(Boolean isFriendManage, Set<Integer> special, Boolean roomAuthFriendAllow
            , User joinUser, Integer realPlayersSize, Integer roomType) {
        if (isFriendManage) {
            //配置仅限好友
            if (special.contains(SpecialEnum.JUST_FRIENDS.getCode())) {
                //不是房主的好友
                if (!roomAuthFriendAllow) {
                    return new SocketResult(508);
                    //是房主的好友
                } else {
                    return dealCanSee(joinUser, special, realPlayersSize, roomType);
                }
            }
            //未配置仅限好友
            else {
                return dealCanSee(joinUser, special, realPlayersSize, roomType);
            }
            // 未开通
        } else {
            return dealCanSee(joinUser, special, realPlayersSize, roomType);
        }
    }

    /**
     * 处理是否可以观战
     *
     * @throws IOException
     */
    private SocketResult dealCanSee(User user, Set<Integer> special, Integer realPlayersSize, Integer roomType) {
        SocketResult socketResult = new SocketResult();
        socketResult.setUserId(user.getId());
        socketResult.setName(user.getAppName());
        socketResult.setPictureUrl(user.getAppPictureUrl());
        Boolean bo = realPlayersSize < RoomTypeEnum.getRoomNumByType(roomType);
        //允许观战
        if (special != null && special.contains(SpecialEnum.CAN_SEE.getCode())) {
            if (bo) {
                socketResult.setIsChiGuaPeople(Boolean.FALSE);
            } else {
                socketResult.setIsChiGuaPeople(Boolean.TRUE);
            }
            return socketResult;
            //不允许观战
        } else {
            if (bo) {
                socketResult.setIsChiGuaPeople(Boolean.FALSE);
                return socketResult;
            } else {
                return new SocketResult(509);
            }

        }
    }

    /**
     * 得到房间里真正的玩家
     *
     * @return
     */
    private List<Player> getRealRoomPlayerCount(Set<Integer> realUserIds, Set<Integer> guanZhongUserIds,
                                                Map<Integer, Integer> totalScoreMap , Map<Integer ,Integer> runingScoreMap ,
                                                String gameStatus) {
        List<Integer> userIds = Lists.newArrayList();
        for (Integer s : realUserIds) {
            userIds.add(s);
        }
        List<User> users = userDbClient.findByUserIdList(userIds).getData();

        List<Player> players = Lists.newArrayList();
        for (User user : users) {
            Player player = new Player();
            player.setUserId(user.getId());
            player.setName(user.getAppName());
            player.setPictureUrl(user.getAppPictureUrl());
            if (guanZhongUserIds.contains(String.valueOf(user.getId()))) {
                player.setIsGuanZhong(Boolean.TRUE);
            }
            players.add(player);
            setPlayerTotalScore(player ,totalScoreMap ,runingScoreMap ,gameStatus);
            //player.setTotalScore(totalScoreMap.get(user.getId().toString()) == null ? 0 : totalScoreMap.get(user.getId().toString()));
        }
        return players;
    }

    private void setPlayerTotalScore(Player player ,Map<Integer, Integer> totalScoreMap ,
                                        Map<Integer ,Integer> runingScoreMap ,String gameStatus){
        String playerId = player.getUserId().toString();
        if (runingScoreMap == null) {
            runingScoreMap = new HashMap<>();
        }
        Integer totalScore = totalScoreMap.get(playerId) == null ? 0 : totalScoreMap.get(playerId);
        if (Objects.equals(gameStatus, GameStatusEnum.TAN_PAI_COUNT_DOWN_START.getCode()) ||
                Objects.equals(gameStatus, GameStatusEnum.FA_ONE_PAI.getCode())) {
            Integer runingScore = runingScoreMap.get(playerId) == null ? 0 : runingScoreMap.get(playerId);
            player.setTotalScore(totalScore - runingScore);
        }else {
            player.setTotalScore(totalScore);
        }
    }

    /**
     * 欢迎玩家加入，发送房间状态信息
     */
    private void welcome(NiuniuData data, Task task, SocketResult socketResult) {
        String gameStatus = data.getGameStatus();
        Integer runingNum = data.getRuningNum();
        Integer userId = task.getPlayId();
        Set<Integer> readyPlayers = data.getReadyPlayMap().get(runingNum);
        socketResult.setGameStatus(gameStatus);
        //设置准备的玩家
        if (Objects.equals(gameStatus, GameStatusEnum.READY.getCode()) ||
                Objects.equals(gameStatus, GameStatusEnum.READY_COUNT_DOWN_START.getCode()) ||
                Objects.equals(gameStatus, GameStatusEnum.READY_COUNT_DOWN_END.getCode())) {
            socketResult.setReadyPlayerIds(readyPlayers);
        }
        //设置玩家先发的4张牌
        else if (Objects.equals(gameStatus, GameStatusEnum.FA_FOUR_PAI.getCode())) {
            socketResult.setReadyPlayerIds(readyPlayers);
            List<String> pokes_4 = data.getPokesMap().get(runingNum).get(userId).subList(0, 4);
            socketResult.setUserPokeList_4(pokes_4);
        }
        //设置抢庄的玩家
        else if (Objects.equals(gameStatus, GameStatusEnum.QIANG_ZHUANG_COUNT_DOWN_START.getCode()) ||
                Objects.equals(gameStatus, GameStatusEnum.QIANG_ZHUANG_COUNT_DOWN_END.getCode())) {
            socketResult.setReadyPlayerIds(readyPlayers);
            List<String> pokes_4 = data.getPokesMap().get(runingNum).get(userId).subList(0, 4);
            socketResult.setUserPokeList_4(pokes_4);
            socketResult.setQiangZhuangMap(data.getQiangZhuangMap().get(runingNum));
        }
        //设置庄家
        else if (Objects.equals(gameStatus, GameStatusEnum.QIANG_ZHUANG_ZHUAN_QUAN.getCode())) {
            socketResult.setReadyPlayerIds(readyPlayers);
            List<String> pokes_4 = data.getPokesMap().get(runingNum).get(userId).subList(0, 4);
            socketResult.setUserPokeList_4(pokes_4);
            socketResult.setZhuangJiaUserId(data.getZhuangJiaMap().get(runingNum));
        }
        //设置玩家下注信息
        else if (Objects.equals(gameStatus, GameStatusEnum.XIA_ZHU_COUNT_DOWN_START.getCode()) ||
                Objects.equals(gameStatus, GameStatusEnum.XIA_ZHU_COUNT_DOWN_END.getCode()) ||
                Objects.equals(gameStatus, GameStatusEnum.DEFAULT_XIA_ZHU.getCode())) {
            socketResult.setReadyPlayerIds(readyPlayers);
            List<String> pokes_4 = data.getPokesMap().get(runingNum).get(userId).subList(0, 4);
            socketResult.setUserPokeList_4(pokes_4);
            socketResult.setZhuangJiaUserId(data.getZhuangJiaMap().get(runingNum));
            socketResult.setXianJiaXiaZhuMap(data.getXiaZhuMap().get(runingNum));
        }
        //设置最后一张牌
        else if (Objects.equals(gameStatus, GameStatusEnum.FA_ONE_PAI.getCode())) {
            socketResult.setReadyPlayerIds(readyPlayers);
            setLastPoke(data, runingNum, socketResult);
        }
        //设置摊牌的玩家,玩家不会收到JIE_SUAN("13" ,"本局结算")的消息
        else if (Objects.equals(gameStatus, GameStatusEnum.TAN_PAI_COUNT_DOWN_START.getCode()) ||
                Objects.equals(gameStatus, GameStatusEnum.TAN_PAI_COUNT_DOWN_END.getCode())) {
            socketResult.setReadyPlayerIds(readyPlayers);
            setLastPoke(data, runingNum, socketResult);
            socketResult.setTanPaiPlayerUserIds(data.getTanPaiMap().get(runingNum));
        }


    }

    private void setLastPoke(NiuniuData data, Integer runingNum, SocketResult socketResult) {
        socketResult.setUserPokeMap_5(data.getPokesMap().get(runingNum));
        socketResult.setZhuangJiaUserId(data.getZhuangJiaMap().get(runingNum));
        socketResult.setXianJiaXiaZhuMap(data.getXiaZhuMap().get(runingNum));
        socketResult.setScoreMap(data.getRuningScoreMap().get(runingNum));
        Map<Integer, Integer> map = Maps.newHashMap();
        for (Map.Entry<Integer, PaiXing> entry : data.getPaiXingMap().get(runingNum).entrySet()) {
            map.put(entry.getKey(), entry.getValue().getPaixing());
        }
        socketResult.setPaiXing(map);
    }


}
