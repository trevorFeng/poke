package com.poke.pokeMessage.core.event;

import com.poke.pokeMessage.bo.RoomData;
import com.poke.pokeMessage.bo.Task;

public interface Event {

    void execute(RoomData roomData, Task task);
}
