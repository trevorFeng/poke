package com.poke.pokeMessage.bo;

import lombok.Data;

@Data
public class RoomData {

    /**
     * 房间的id
     */
    protected Integer roomId;

    /**
     * 房间类型 1为13人牛牛，2为10人牛牛，3为6人牛牛 ，4为金花
     */
    protected Integer roomType;

}
