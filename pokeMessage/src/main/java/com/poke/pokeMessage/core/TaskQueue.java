package com.poke.pokeMessage.core;

import com.google.common.collect.Maps;
import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.core.thread.TaskThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@Order(2)
@Slf4j
public class TaskQueue implements ApplicationRunner {

    /**
     * 每個房間的任務隊列
     */
    public static Map<Integer, ConcurrentLinkedQueue<Task>> taskQueueMap = Maps.newConcurrentMap();

    /**
     * executor1管的房间id
     */
    public static List<Integer> executor1ToRoomIds = new CopyOnWriteArrayList<>();

    /**
     * executor2管的房间id
     */
    public static List<Integer> executor2ToRoomIds = new CopyOnWriteArrayList<>();

    private static Executor executor1 = Executors.newFixedThreadPool(1);

    private static Executor executor2 = Executors.newFixedThreadPool(1);

    /**
     * 添加队列
     *
     * @param roomId
     */
    public void addQueue(Integer roomId) {
        taskQueueMap.put(roomId, new ConcurrentLinkedQueue<>());
        int executor1ToRoomIdsSize = executor1ToRoomIds.size();
        int executor2ToRoomIdsSize = executor2ToRoomIds.size();
        if (executor1ToRoomIdsSize > executor2ToRoomIdsSize) {
            executor2ToRoomIds.add(roomId);
        } else if (executor1ToRoomIdsSize < executor2ToRoomIdsSize) {
            executor1ToRoomIds.add(roomId);
        } else {
            executor2ToRoomIds.add(roomId);
        }
    }

    /**
     * 删除队列
     *
     * @param roomId
     * @return
     */
    public Boolean deleteQueue(Integer roomId) {
        if (!taskQueueMap.get(roomId).isEmpty()) {
            return Boolean.FALSE;
        }
        taskQueueMap.remove(roomId);
        if (executor1ToRoomIds.contains(roomId)) {
            executor1ToRoomIds.remove(roomId);
        } else if (executor2ToRoomIds.contains(roomId)) {
            executor2ToRoomIds.remove(roomId);
        }
        return Boolean.TRUE;
    }

    /**
     * 添加任务
     *
     * @param roomId
     * @param task
     */
    public void addTask(Integer roomId, Task task) {
        ConcurrentLinkedQueue<Task> taskQueue = taskQueueMap.get(roomId);
        taskQueue.offer(task);

    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true) {
            Iterator<ConcurrentLinkedQueue<Task>> iterator = taskQueueMap.values().iterator();
            while (iterator.hasNext()) {
                ConcurrentLinkedQueue<Task> taskQueue = iterator.next();
                Task poll = taskQueue.poll();
                if (poll != null) {
                    System.out.print(poll.toString());
                    if (executor1ToRoomIds.contains(poll.getRoomId())) {
                        executor1.execute(new TaskThread(poll));
                    }
                    if (executor2ToRoomIds.contains(poll.getRoomId())) {
                        executor2.execute(new TaskThread(poll));
                    }
                }
            }
        }
    }
}
