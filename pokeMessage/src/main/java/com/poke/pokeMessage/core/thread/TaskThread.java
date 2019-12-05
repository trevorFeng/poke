package com.poke.pokeMessage.core.thread;


import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.core.GameCore;

public class TaskThread implements Runnable {

    public Task task;

    public static GameCore gameCore;

    public TaskThread(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        gameCore.execut(task);
    }
}
