package com.poke.dbservice.service.impl;

import com.poke.common.bean.domain.mysql.Room;
import com.poke.common.bean.exception.BizException;
import com.poke.dbservice.dao.mysql.RoomMapper;
import com.poke.dbservice.service.RoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoomServiceImpl implements RoomService {

    @Resource
    private RoomMapper roomMapper;

    @Override
    public Room findByRoomId(Integer id) {
        Room room = roomMapper.selectByPrimaryKey(id);
        if (room == null) {
            throw new BizException(-1 ,"没有房间记录");
        }
        return room;
    }

    @Override
    public Integer save(Room room) {
        Integer roomId = roomMapper.insert(room);
        return roomId;
    }

}
