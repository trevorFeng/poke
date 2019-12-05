package com.poke.pokeMessage.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.poke.pokeMessage.bo.RoomData;
import com.poke.pokeMessage.bo.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 游戏数据
 */
@Service
public class GameCore {

    @Resource
    private NiuniuCore niuniuCore;

    /**
     * 全部房间的游戏数据，key-roomId，value-房间数据
     */
    private static Map<Integer ,RoomData> roomDtaMap = Maps.newConcurrentMap();

    /**
     * 每个玩家的sockket消息队列，key-userId，value-socket消息
     */
    private static Map<Integer ,ConcurrentLinkedQueue<String>> messageMap = Maps.newConcurrentMap();

    /**
     * 每次取得消息数量
     */
    private static Integer messageNumber = 5;

    public void putRoomData(RoomData roomData, Integer roomId) {
        roomDtaMap.put(roomId ,roomData);
    }

    public void removeRoomData(Integer roomId) {
        roomDtaMap.remove(roomId);
    }

    public void putMessageQueue(ConcurrentLinkedQueue<String> concurrentLinkedQueue ,Integer userId) {
        messageMap.put(userId ,concurrentLinkedQueue);
    }

    public void removeMessageQueue(Integer userId) {
        messageMap.remove(userId);
    }

    public void addSocketMessage(Integer userId ,String message){
        messageMap.get(userId).offer(message);
    }

    /**
     * 得到前5个消息，没有就返回
     */
    public List<String> getFiveMessage(Integer userId){
        List<String> messages = Lists.newArrayList();
        ConcurrentLinkedQueue<String> messageQueue = messageMap.get(userId);
        int i = 1;
        while (i <= messageNumber) {
            String poll = messageQueue.poll();
            if (poll != null) {
                messages.add(poll);
            }else {
                break;
            }
        }
        return messages;
    }

    public void execut(Task task) {
        RoomData roomData = roomDtaMap.get(task.getRoomId());
        if (roomData != null) {
            Integer roomType = roomData.getRoomType();
            if (Objects.equals(roomType, 1)) {
                niuniuCore.executNiuniu(task, roomData);
            } else if (Objects.equals(roomType, 2)) {

            }
        }
    }


}
