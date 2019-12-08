package com.poke.pokeMessage.socket.service;

import com.poke.pokeMessage.bo.SocketMessage;
import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.core.TaskQueue;
import com.poke.pokeMessage.socket.socketImpl.NiuniuSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class NiuniuService {

    @Resource
    private TaskQueue taskQueue;


    /**
     * 处理准备的消息
     *
     * @param roomId
     */
    public void dealReadyMessage(Integer roomId, NiuniuSocket socket) {
        Task task = Task.getNiuniuReady(roomId, socket.userId);
        taskQueue.addTask(roomId, task);
    }

    /**
     * 处理抢庄的消息
     *
     * @param roomId
     */
    public void dealQiangZhuangMessage(Integer roomId, NiuniuSocket socket, SocketMessage socketMessage) {
        Task task = Task.getNiuniuQiangZhuang(roomId, socket.userId, socketMessage.getQiangZhuangMultiple());
        taskQueue.addTask(roomId, task);
    }

    /**
     * 处理闲家下注的消息
     *
     * @param roomId
     */
    public void dealXiaZhuMessage(Integer roomId, NiuniuSocket socket, SocketMessage socketMessage) {
        Task task = Task.getNiuniuXiaZhu(roomId, socket.userId, socketMessage.getXianJiaMultiple());
        taskQueue.addTask(roomId, task);
    }

    /**
     * 处理摊牌的消息
     *
     * @param roomId
     */
    public void dealTanPaiMessage(Integer roomId, NiuniuSocket socket) {
        Task task = Task.getNiuniuTanPai(roomId, socket.userId);
        taskQueue.addTask(roomId, task);
    }
}
