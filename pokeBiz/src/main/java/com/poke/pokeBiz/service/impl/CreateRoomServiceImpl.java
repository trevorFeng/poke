package com.poke.pokeBiz.service.impl;

import com.poke.common.bean.domain.mongo.NiuniuRoomParam;
import com.poke.common.client.MessageClient;
import com.poke.common.core.UserContextHolder;
import com.poke.pokeBiz.service.CreateRoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CreateRoomServiceImpl implements CreateRoomService {

    @Resource
    private MessageClient messageClient;

    @Override
    public Integer createRoom(NiuniuRoomParam niuniuRoomParam) {
        return messageClient.createRoom(niuniuRoomParam , UserContextHolder.currentUser().getId()).getData();
    }
}
