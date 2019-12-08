package com.poke.pokeMessage.core.schedule;

import com.poke.pokeMessage.bo.CountDownNum;
import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.core.TaskQueue;
import lombok.Data;

@Data
public class CountDownImpl implements CountDownListener {

    public static TaskQueue taskQueue;

    /**
     * 房间id
     */
    private Integer roomId;

    /**
     * 哪个阶段的倒计时
     */
    private String countDownFlag;

    /**
     * 默认为5
     */
    private Integer time = 5;

    public CountDownImpl(Integer roomId, Integer time, String countDownFlag) {
        this.roomId = roomId;
        this.time = time;
        this.countDownFlag = countDownFlag;
    }

    @Override
    public void onCountDown() {
        if (time > 0) {
            Task task = Task.getNiuniuCountDown(time, roomId, countDownFlag , CountDownNum.TWENTY);
            taskQueue.addTask(roomId, task);
            time--;
        }
    }

    @Override
    public Integer getRoomId() {
        return roomId;
    }


}
