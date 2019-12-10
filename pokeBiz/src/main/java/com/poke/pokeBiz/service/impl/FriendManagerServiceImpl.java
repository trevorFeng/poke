package com.poke.pokeBiz.service.impl;

import com.google.common.collect.Lists;
import com.poke.common.bean.bo.FriendInfo;
import com.poke.common.bean.domain.mysql.FriendsManage;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.client.FriendsManageDbClient;
import com.poke.common.client.UserDbClient;
import com.poke.common.core.UserContextHolder;
import com.poke.pokeBiz.service.FriendManagerService;
import com.poke.pokeBiz.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 一句话描述该类作用:【】
 *
 * @author: trevor
 * @create: 2019-03-03 23:56
 **/
@Slf4j
@Service
public class FriendManagerServiceImpl implements FriendManagerService {

    @Resource
    private FriendsManageDbClient friendsManageDbClient;

    @Resource
    private UserDbClient userDbClient;

    @Resource
    private RoomService roomService;

    /**
     * 查询好友（申请通过和未通过的）
     * @return
     */
    @Override
    public List<FriendInfo> queryFriends() {
        User user = UserContextHolder.currentUser();
        List<FriendsManage> list = friendsManageDbClient.findByUserId(user.getId()).getData();
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> ids = list.stream().map(friendsManage -> friendsManage.getManageFriendId()).collect(Collectors.toList());
        Map<Integer ,Byte> map = list.stream().collect(Collectors.toMap(FriendsManage::getManageFriendId ,FriendsManage::getAllowFlag));
        List<User> users = userDbClient.findByUserIdList(ids).getData();
        List<FriendInfo> friendInfos = Lists.newArrayList();
        users.forEach(user1 -> {
            FriendInfo friendInfo = new FriendInfo();
            friendInfo.setUserId(user1.getId());
            friendInfo.setAppName(user1.getAppName());
            friendInfo.setPictureUrl(user1.getAppPictureUrl());
            friendInfo.setAllowFlag(map.get(user1.getId()));
            friendInfos.add(friendInfo);
        });
        return friendInfos;
    }

    /**
     * 申请好友
     * @param roomId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyFriend(Integer roomId) {
        User user = UserContextHolder.currentUser();
        Integer roomAuthId = roomService.findOneById(roomId).getRoomAuth();
        FriendsManage friendsManage = new FriendsManage();
        friendsManage.setUserId(roomAuthId);
        friendsManage.setManageFriendId(user.getId());
        friendsManage.setAllowFlag((byte) 0);
        friendsManageDbClient.save(friendsManage);
    }

    /**
     * 通过好友申请
     * @param passUserId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void passFriend(Integer passUserId) {
        User user = UserContextHolder.currentUser();
        FriendsManage friendsManage = new FriendsManage();
        friendsManage.setUserId(user.getId());
        friendsManage.setManageFriendId(passUserId);
        friendsManage.setAllowFlag((byte)1);
        friendsManageDbClient.update(friendsManage);
    }

    /**
     * 提出好友
     * @param removeUserId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeFriend(Integer removeUserId) {
        User user = UserContextHolder.currentUser();
        FriendsManage friendsManage = new FriendsManage();
        friendsManage.setUserId(user.getId());
        friendsManage.setManageFriendId(removeUserId);
        friendsManage.setAllowFlag((byte)0);
        friendsManageDbClient.update(friendsManage);
    }
}
