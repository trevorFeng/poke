package com.poke.pokeMessage.service;

import com.poke.common.bean.bo.SocketResult;
import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.core.GameCore;
import com.poke.pokeMessage.core.TaskQueue;
import com.poke.pokeMessage.socket.socketImpl.NiuniuSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author trevor
 * @date 06/27/19 18:01
 */
@Slf4j
@Service
public class SocketService {

    public static ConcurrentHashMap<Integer, NiuniuSocket> sockets = new ConcurrentHashMap<>(2 << 11);

    @Resource
    private TaskQueue taskQueue;

    @Resource
    private GameCore gameCore;

    @PreDestroy
    public void destory() {
        Iterator<NiuniuSocket> iterator = sockets.values().iterator();
        while (iterator.hasNext()) {
            NiuniuSocket socket = iterator.next();
            socket.stop();
        }
    }

    /**
     * fixedRate设置的上一个任务的开始时间到下一个任务开始时间的间隔
     * fixedDelay是设定上一个任务结束后多久执行下一个任务，也就是fixedDelay只关心上一任务的结束时间和下一任务的开始时间
     */
    @Scheduled(initialDelay = 1000 * 3, fixedDelay = 300)
    public void checkRoom() {
        Iterator<NiuniuSocket> iterator = sockets.values().iterator();
        while (iterator.hasNext()) {
            NiuniuSocket socket = iterator.next();
            socket.flush();
        }
    }

    /**
     * 房间广播
     *
     * @param roomId
     * @param res
     */
    public void broadcast(Integer roomId, SocketResult res, Set<Integer> playerIds) {
        for (Integer playId : playerIds) {
            sendToUserMessage(playId, res, roomId);
        }
    }

    /**
     * 给玩家发消息
     *
     * @param playId
     * @param res
     */
    public void sendToUserMessage(Integer playId, SocketResult res, Integer roomId) {
        NiuniuSocket socket = sockets.get(playId);
        if (socket != null && socket.session != null && socket.session.isOpen()) {
            socket.sendMessage(res);
        } else {
            if (socket != null) {
                leave(roomId, socket);
            }
        }
    }

    /**
     * 用户离开
     *
     * @param roomId
     * @param socket
     */
    public void leave(Integer roomId, NiuniuSocket socket) {
        if (sockets.containsKey(socket.userId)) {
            NiuniuSocket s = sockets.get(socket.userId);
            s.close(socket.session);
            sockets.remove(socket.userId);
            //删除消息通道
            gameCore.removeMessageQueue(socket.userId);

            Task task = Task.getLeave(roomId, socket.userId);
            taskQueue.addTask(roomId, task);
        }
    }

    /**
     * 用户加入
     *
     * @param socket
     */
    public void join(NiuniuSocket socket) {
        if (sockets.containsKey(socket.userId)) {
            NiuniuSocket s = sockets.get(socket.userId);
            sockets.remove(socket.userId);
            s.directSendMessage(new SocketResult(500), s.session);
            s.close(s.session);
        }
        sockets.put(socket.userId, socket);
    }


    /**
     * 停止房间
     * @param players
     * @param roomId
     */
    public void stopRoom(Set<Integer> players ,Integer roomId){
        gameCore.removeRoomData(roomId);
        log.info("停止房间：" + roomId);
        for (Integer playerId : players) {
            NiuniuSocket socket = sockets.get(playerId);
            if (socket != null) {
                socket.stop();
            }
            sockets.remove(playerId);
        }
    }
}
