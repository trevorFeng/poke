package com.poke.dbservice.service;

import com.poke.common.bean.domain.mysql.Room;
import org.apache.ibatis.annotations.Param;

public interface RoomService {

    Room findByRoomId(Integer id);



}
