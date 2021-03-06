package com.poke.pokeMessage.service;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mongo.NiuniuRoomParam;
import com.poke.common.bean.domain.mysql.CardConsumRecord;
import com.poke.common.bean.domain.mysql.Room;
import com.poke.common.bean.enums.ConsumCardEnum;
import com.poke.common.bean.enums.GameStatusEnum;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.common.client.CardConsumDbClient;
import com.poke.common.client.PersonalCardDbClient;
import com.poke.common.client.RoomDbClient;
import com.poke.common.client.RoomParamDbClient;
import com.poke.pokeMessage.bo.NiuniuData;
import com.poke.pokeMessage.core.GameCore;
import com.poke.pokeMessage.core.TaskQueue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Objects;

@Service
public class CreateRoomService {

    @Resource
    private GameCore gameCore;

    @Resource
    private TaskQueue taskQueue;

    @Resource
    private RoomDbClient roomDbClient;

    @Resource
    private CardConsumDbClient cardConsumDbClient;

    @Resource
    private RoomParamDbClient roomParamDbClient;

    @Resource
    private PersonalCardDbClient personalCardDbClient;

    @Transactional(rollbackFor = Exception.class)
    public JsonEntity<Integer> createRoom(NiuniuRoomParam niuniuRoomParam, Integer userId) {
        //判断玩家拥有的房卡数量是否超过消耗的房卡数
        Integer cardNumByUserId = personalCardDbClient.findCardNumByUserId(userId).getData();
        Integer consumCardNum;
        if (Objects.equals(niuniuRoomParam.getConsumCardNum(), ConsumCardEnum.GAME_NUM_12_CARD_3.getCode())) {
            consumCardNum = ConsumCardEnum.GAME_NUM_12_CARD_3.getConsumCardNum();
            if (cardNumByUserId < ConsumCardEnum.GAME_NUM_12_CARD_3.getConsumCardNum()) {
                return ResponseHelper.withErrorInstance(MessageCodeEnum.USER_ROOMCARD_NOT_ENOUGH);
            }
        } else {
            consumCardNum = ConsumCardEnum.GAME_NUM_24_CARD_6.getConsumCardNum();
            if (cardNumByUserId < ConsumCardEnum.GAME_NUM_24_CARD_6.getConsumCardNum()) {
                return ResponseHelper.withErrorInstance(MessageCodeEnum.USER_ROOMCARD_NOT_ENOUGH);
            }
        }
        //生成房间，将房间信息存入数据库
        Integer totalNum;
        if (Objects.equals(consumCardNum, ConsumCardEnum.GAME_NUM_12_CARD_3.getConsumCardNum())) {
            totalNum = 12;
        } else {
            totalNum = 24;
        }
        Long currentTime = System.currentTimeMillis();
        Room room = new Room();
        room.setRoomType(niuniuRoomParam.getRoomType().byteValue());
        room.setRoomAuth(userId);
        room.setEntryTime(currentTime);
        room.setRuningNum(0);
        room.setTotalNum(totalNum);
        room.setStatus((byte)0);
        Integer roomId = roomDbClient.save(room).getData();

        //插入mongoDB
        niuniuRoomParam.setRoomId(roomId);
        roomParamDbClient.save(niuniuRoomParam);

        //存入gameCore
        NiuniuData data = new NiuniuData();
        data.setRoomId(room.getId());
        data.setRoomType(niuniuRoomParam.getRoomType());
        data.setRobZhuangType(niuniuRoomParam.getRobZhuangType());
        data.setBasePoint(niuniuRoomParam.getBasePoint());
        data.setRule(niuniuRoomParam.getRule());
        data.setXiazhu(niuniuRoomParam.getXiazhu());
        data.setSpecial(niuniuRoomParam.getSpecial());
        data.setPaiXing(niuniuRoomParam.getPaiXing() == null ? new HashSet<>() : niuniuRoomParam.getPaiXing());
        data.setTotalNum(totalNum.toString());
        data.setRuningNum(1);
        data.setGameStatus(GameStatusEnum.READY.getCode());
        gameCore.putRoomData(data ,room.getId());
        taskQueue.addQueue(room.getId());

        //生成房卡消费记录
        CardConsumRecord cardConsumRecord = new CardConsumRecord();
        cardConsumRecord.setRoomId(room.getId());
        cardConsumRecord.setRoomAuth(userId);
        cardConsumRecord.setConsumNum(consumCardNum);
        cardConsumDbClient.save(cardConsumRecord);

        //更新玩家的房卡数量信息
        personalCardDbClient.updatePersonalCardNum(userId, cardNumByUserId - consumCardNum);
        return ResponseHelper.createInstance(roomId, MessageCodeEnum.CREATE_SUCCESS);
    }
}
