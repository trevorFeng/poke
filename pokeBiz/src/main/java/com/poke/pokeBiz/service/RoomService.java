package com.poke.pokeBiz.service;

import com.poke.common.bean.domain.mysql.Room;
import com.poke.common.client.RoomDbClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author trevor
 * @date 2019/3/7 15:57
 */
@Service
public class RoomService {

    @Resource
    private RoomDbClient roomDbClient;

    public Room findOneById(Integer id) {
        return roomDbClient.findByRoomId(id).getData();
    }

}