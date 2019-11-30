package com.poke.pokeMessage.core.schedule;

public interface CountDownListener {

    /**
     * 事件
     */
    void onCountDown();

    String getRoomId();

}
