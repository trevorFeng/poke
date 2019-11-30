package com.poke.pokeBiz.service;

import com.poke.common.bean.bo.FriendInfo;

import java.util.List;

public interface FriendManagerService {

    List<FriendInfo> queryFriends();

    void applyFriend(Integer roomId);

    void passFriend(Integer passUserId);

    void removeFriend(Integer removeUserId);
}
