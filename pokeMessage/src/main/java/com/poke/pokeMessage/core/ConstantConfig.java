package com.poke.pokeMessage.core;

import com.poke.pokeMessage.core.schedule.CountDownImpl;
import com.poke.pokeMessage.core.thread.TaskThread;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ConstantConfig {

    @Resource
    public void setTaskQueue(TaskQueue taskQueue) {
        CountDownImpl.taskQueue = taskQueue;
    }

    @Resource
    public void setGameCore(GameCore gameCore){
        TaskThread.gameCore = gameCore;
    }
}
